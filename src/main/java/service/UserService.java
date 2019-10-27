package service;

import model.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class UserService {

    /* хранилище данных */
    private Map<Long, User> dataBase = Collections.synchronizedMap(new HashMap<>());
    /* счетчик id */
    private AtomicLong maxId = new AtomicLong(0);
    /* список авторизованных пользователей */
    private Map<Long, User> authMap = Collections.synchronizedMap(new HashMap<>());
    /* делаем UserService singleton */
    private static UserService instance;

    private UserService() {
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(dataBase.values());
    }

    public User getUserById(Long id) {
        return dataBase.get(id);
    }

    public boolean addUser(User user) {
        if (!isExistsThisUser(user)) {
            Long temp = maxId.incrementAndGet();
            user.setId(temp);
            dataBase.put(temp, user);
            return true;
        }
        return false;
    }

    public void deleteAllUser() {
        dataBase.clear();
    }

    private boolean isExistsThisUser(User user) {
        return dataBase.containsValue(user);
    }

    public List<User> getAllAuth() {
        return new ArrayList<>(authMap.values());
    }

    public boolean authUser(User user) {
        if (isExistsThisUser(user)) {
            Long temp = getIdOfUser(user);
            if (temp.equals(-1L)) {
                return false;
            }
            user.setId(temp);
            authMap.put(temp, user);
            return true;
        }
        return false;
    }

    public void logoutAllUsers() {
        authMap.clear();
    }

    public boolean isUserAuthById(Long id) {
        return authMap.containsKey(id);
    }

    private Long getIdOfUser(User user) {
        List<User> list = getAllUsers();
        Iterator<User> it = list.iterator();
        while (it.hasNext()) {
            User temp = it.next();
            if (user.equals(temp)) {
                return temp.getId();
            }
        }
        return -1L;
    }
}
