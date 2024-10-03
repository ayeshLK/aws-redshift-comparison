package io.sample.redshift.jdbc;

import java.util.List;

public interface UserRepository {

    List<User> listUsers();
}
