package cl.sse.tongji.edu.android_end.model;


import java.io.Serializable;

public class User implements Serializable {
    public static final int TYPE_STUDENT = 1;
    public static final int TYPE_TEACHER = 2;

    Integer id;
    String username;
    String password;
    String e_mail;
    String create_time;
    String token;
    String type;

    public String getUsername() {
        return username;
    }

    public Integer getStudentId() {
        return id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public String getE_mail() {
        return e_mail;
    }

    public String getToken() {
        return token;
    }

    public void setType(Integer itype) {
        switch (itype) {
            case TYPE_STUDENT:
                type = "student";
                break;
            case TYPE_TEACHER:
                type = "teacher";
                break;
        }
    }
}
