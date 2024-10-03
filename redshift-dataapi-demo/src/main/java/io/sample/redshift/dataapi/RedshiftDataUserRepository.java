package io.sample.redshift.dataapi;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RedshiftDataUserRepository implements UserRepository {

    // todo: implement this method properly
    @Override
    public List<User> listUsers() {
        return List.of();
    }
}
