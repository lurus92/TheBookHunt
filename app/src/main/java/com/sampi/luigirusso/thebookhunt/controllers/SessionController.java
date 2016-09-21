package com.sampi.luigirusso.thebookhunt.controllers;

import com.sampi.luigirusso.thebookhunt.entities.User;

import java.util.NoSuchElementException;

/**
 * Created by luigirusso on 08/03/16.
 * This class provide basic
 */
public class SessionController {

    private User currentUser;

    public SessionController(){
        currentUser = null;
    }

    public SessionController(User user){
        currentUser = user;
    }
    /**
     * The login without arguments looks if an user is already stored in the device
     * @throws Exception is thrown if no user is found
     */
    public void retriveStoredUser() throws NoSuchElementException{
        /* TODO: add if to manage reading from file */
        throw new NoSuchElementException();
    }

    /**
     * The login method allow to log in a specific user
     * @param username username of the user
     * @param password password of the user
     */
    public void startLogin(String username, String password){
        /* TODO: check password, database connection */
        currentUser = new User(0000,username);
        if (username=="prof") currentUser.setExample();
        /* TODO: retrive important data from user */
        /* TODO: save result to file */
    }


    public void startLogin(){        //Login
        /* TODO: get remote data from user user */
    }

    public void register(){}  /* TODO: implement */

    public User getCurrentUser() {
        return currentUser;
    }
}
