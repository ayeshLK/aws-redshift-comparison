package io.sample.redshift.dataapi;

import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.redshiftdata.RedshiftDataClient;
import software.amazon.awssdk.services.redshiftdata.model.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class RedshiftDataUserRepository implements UserRepository {
    private final RedshiftDataClient dataClient;

    @Value("${redshift.clusterId}")
    private String clusterId;
    @Value("${redshift.database.name}")
    private String databaseName;
    @Value("${redshift.database.user}")
    private String databaseUser;

    @Autowired
    public RedshiftDataUserRepository(RedshiftDataClient dataClient) {
        this.dataClient = dataClient;
    }

    @PreDestroy
    public void destroy() {
        this.dataClient.close();
    }

    @Override
    public List<User> listUsers() {
        BatchExecuteStatementRequest batchExecRequest = BatchExecuteStatementRequest.builder()
                .clusterIdentifier(clusterId)
                .database(databaseName)
                .dbUser(databaseUser)
                .sqls("CALL create_temp_user_data();", "SELECT * FROM temp_user_data LIMIT 100;")
                .build();
        BatchExecuteStatementResponse batchExecResp = this.dataClient.batchExecuteStatement(batchExecRequest);

        DescribeStatementRequest describeStmReq = DescribeStatementRequest.builder().id(batchExecResp.id()).build();
        boolean isCompleted = false;
        DescribeStatementResponse describeStmResp = null;
        while (!isCompleted) {
            describeStmResp = this.dataClient.describeStatement(describeStmReq);

            if (StatusString.FINISHED.equals(describeStmResp.status())) {
                isCompleted = true;
            } else if (StatusString.FAILED.equals(describeStmResp.status())) {
                throw new RuntimeException("Failed to retrieve results for the batch-execute, hence aborting");
            }
        }

        Optional<SubStatementData> queryWithResultSet = describeStmResp.subStatements().stream()
                .filter(SubStatementData::hasResultSet)
                .findFirst();
        if (queryWithResultSet.isEmpty()) {
            return Collections.emptyList();
        }

        GetStatementResultRequest getStmReq = GetStatementResultRequest.builder()
                .id(queryWithResultSet.get().id()).build();
        GetStatementResultResponse results = this.dataClient.getStatementResult(getStmReq);
        return results.records().stream().map(this::constructUserDetails).toList();
    }

    private User constructUserDetails(List<Field> row) {
        return new User(
                row.get(0).longValue(), row.get(1).stringValue(), row.get(2).stringValue(), row.get(3).stringValue());
    }
}
