package com.jwt.example.services;
import com.jwt.example.models.User;
import java.util.List;
public interface UserService {
    boolean saveData(User data);

    List<User> fetchAllUser();

    void deleteAllUser();
}
