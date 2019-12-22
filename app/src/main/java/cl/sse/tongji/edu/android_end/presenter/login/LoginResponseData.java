package cl.sse.tongji.edu.android_end.presenter.login;

import cl.sse.tongji.edu.android_end.model.User;

public class LoginResponseData {
    String code;
    Boolean flag;
    String message;
    String token;
    User obj;

    public void setUserType(String type){
        if(type == "student"){
            obj.setType(User.TYPE_STUDENT);
        }
    }

    public User getUser(){
        return obj;
    }
}
