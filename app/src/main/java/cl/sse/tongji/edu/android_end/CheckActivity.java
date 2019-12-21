package cl.sse.tongji.edu.android_end;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import cl.sse.tongji.edu.android_end.presenter.check.CheckAdapter;
import cl.sse.tongji.edu.android_end.presenter.check.CheckStudent;

public class CheckActivity extends AppCompatActivity {

    RecyclerView check_recycler;
    List<CheckStudent> students;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_t);

        check_recycler = findViewById(R.id.check_recycler);
        students = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            CheckStudent student = new CheckStudent("aaa");
            students.add(student);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        check_recycler.setLayoutManager(layoutManager);
        CheckAdapter adapter = new CheckAdapter(students);
        check_recycler.setAdapter(adapter);
    }
}
