package com.caesar.ken.coralfits.Models;

public class User {
    public String myemail, userid, firebaseToken;

    public User(String myemail, String userid, String firebaseToken) {
        this.myemail = myemail;
        this.userid = userid;
        this.firebaseToken = firebaseToken;
    }

    public String getMyemail() {
        return myemail;
    }

    public void setMyemail(String myemail) {
        this.myemail = myemail;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getToken() {
        return firebaseToken;
    }

    public void setToken(String token) {
        this.firebaseToken = firebaseToken;
    }
}
