package com.kursach.ckursach;

public class Consts {
    static Consts instance;
   String userName;
    public static final String USERS_DIR ="/users";
    private Consts(){

    }
    static Consts getInstance(){
        if(instance == null){
            instance = new Consts();
        }
        return instance;
    }
    void setUserName(String userName){
        this.userName = userName;
    }
    String getUserName(){
        return userName;
    }
}
