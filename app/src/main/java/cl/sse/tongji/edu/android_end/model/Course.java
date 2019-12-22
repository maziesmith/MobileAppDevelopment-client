package cl.sse.tongji.edu.android_end.model;

import android.widget.TextView;

import java.util.List;

public class Course {
    public Integer id;
    public String name;
    public Integer teacher_id;
    public String teacher_name;
    public String description;
    public String image;
    public List<PDFModel> pdfs;

    public String getName() {
        return name;
    }

    public String getImgUrl(){
        return image;
    }

    public String getTeacher_name(){
        return teacher_name;
    }

    public String getDescription(){return description;}

    public Integer getId() {
        return id;
    }
}
