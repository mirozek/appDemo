package Michal.appdemo.user;

public interface UserService {
    User findUserByEmail(String email);
    void saveUser(User user);
}