package cl.sse.tongji.edu.android_end.model;

import android.widget.TextView;

public class Course {
    Integer id;
    String name;
    Integer teacher_id;
    String description;
    String image;

    public String getName() {
        return name;
    }

    public String getImgUrl(){
        return image;
    }
}
