package cl.sse.tongji.edu.android_end;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

import cl.sse.tongji.edu.android_end.model.Course;
import cl.sse.tongji.edu.android_end.presenter.home.CoursePresenter;
import cl.sse.tongji.edu.android_end.presenter.home.fragment.CourseFrament;
import cl.sse.tongji.edu.android_end.presenter.home.fragment.Test3Fragment;
import cl.sse.tongji.edu.android_end.presenter.home.fragment.TestFragment;
import cl.sse.tongji.edu.android_end.presenter.home.viewholder.CourseAdapter;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigation;
    private LinearLayout home_root;
    private RecyclerView course_recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawer = findViewById(R.id.drawer_home);
        toolbar = findViewById(R.id.toolbar);
        navigation = findViewById(R.id.nav_view);
        course_recycler = findViewById(R.id.course_gallery);


        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }

//        navigation.setCheckedItem(R.id.menu_course);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                String title = menuItem.getTitle().toString();
                Log.d("HomeActivity",title);

                switch (title){
                    case "course":
                        turnToCoursePage();
                        break;
                    case "check":
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    private void turnToCoursePage(){
        // 切换到课程页
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        CourseFrament frament = new CourseFrament();
        transaction.replace(R.id.home_frag, frament);
        transaction.commit();

        //初始化课程页面
        CoursePresenter presenter = new CoursePresenter(HomeActivity.this);
        presenter.init();
    }

    public void showAllCourse(List<Course> courses){
        course_recycler = findViewById(R.id.course_gallery);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        course_recycler.setLayoutManager(layoutManager);
        CourseAdapter adapter = new CourseAdapter(courses);
        course_recycler.setAdapter(adapter);
    }
}
