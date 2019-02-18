package com.sunhill.app.repository;

import com.sunhill.app.model.User;
import com.sunhill.app.store.Store;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private Store store;

    public UserRepository(Store store) {
        this.store = store;
    }

    public User findOne(Integer userId){
        return store.getUsers().get(userId);
    }

    public void save(User user){
        store.getUsers().put(user.getId(), user);
    }

}
