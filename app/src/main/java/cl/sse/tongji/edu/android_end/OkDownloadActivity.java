package cl.sse.tongji.edu.android_end;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.liulishuo.filedownloader.DownloadTask;

import java.io.File;

public class OkDownloadActivity extends AppCompatActivity {

    private Button btn_ok_start;
    private Button btn_ok_pause;
    private Button btn_ok_cancel;

    private DownloadTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_download);

        btn_ok_start = findViewById(R.id.start_okdownload);
        btn_ok_start.setOnClickListener(new BtnOkStartListener());
    }

    class BtnOkStartListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            String filename = "single-test";
            String url = "https://cdn.llscdn.com/yy/files/xs8qmxn8-lls-LLS-5.8-800-20171207-111607.apk";
            File parentFile;

        }
    }
}
