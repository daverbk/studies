package springboot.cruddemo.dao;

import springboot.cruddemo.entity.User;

public interface UserDao {
    User findByUserName(String userName);
}
