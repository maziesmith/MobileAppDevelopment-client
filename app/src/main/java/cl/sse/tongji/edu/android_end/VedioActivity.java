package cl.sse.tongji.edu.android_end;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bumptech.glide.Glide;

import java.io.IOException;

import cn.jzvd.JZDataSource;
import cn.jzvd.JzvdStd;

public class VedioActivity extends AppCompatActivity {

    JzvdStd player;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vedio);

        player = findViewById(R.id.player);
        player.setUp(url,"network");
        url = "https://118.25.153.97/file/vedio/1/1.mp4";
        JZDataSource jzDataSource = null;
        try {
            jzDataSource = new JZDataSource(getAssets().openFd("1.mp4"));
            jzDataSource.title = "饺子快长大";
            player.setUp(jzDataSource,JzvdStd.SCREEN_NORMAL );
        } catch (IOException e) {
            e.printStackTrace();
        }

        Glide.with(this).load("https://http.cat/200").into(player.thumbImageView);

    }

    @Override
    protected void onPause() {
        super.onPause();
        JzvdStd.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (JzvdStd.backPress()) {
            return;
        }
        super.onBackPressed();
    }


}
