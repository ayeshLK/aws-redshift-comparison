package io.sample.redshift.jdbc;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class JdbcUserRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall callCreateTempTable;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

    }

    @PostConstruct
    public void init() {
        this.callCreateTempTable = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("create_temp_user_data");
    }

    @Override
    @Transactional
    public List<User> listUsers() {
        this.callCreateTempTable.execute();
        return jdbcTemplate.query("SELECT * FROM temp_user_data LIMIT 100", (rs, rowNumber) -> new User(
                rs.getLong("userid"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("phone")
        ));
    }
}
