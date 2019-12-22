package cl.sse.tongji.edu.android_end;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.VpnService;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import cl.sse.tongji.edu.android_end.model.User;

public class DownloadActivity extends AppCompatActivity {

    private DownloadService.DownloadBinder downloadBinder;

    private Button btn_start;
    private Button btn_pause;
    private Button btn_cancel;

    private User user;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            downloadBinder = (DownloadService.DownloadBinder) iBinder;
            downloadBinder.setUser(user);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        //绑定布局控件和监听
        btn_start = findViewById(R.id.start_download);
        btn_pause = findViewById(R.id.pause_download);
        btn_cancel = findViewById(R.id.cancel_download);

        user = (User) getIntent().getSerializableExtra("user");
        btn_start.setOnClickListener(new BtnStartListener());

        //启动服务
        Intent intent = new Intent(this, DownloadService.class);
        startForegroundService(intent);
        //绑定服务
        bindService(intent, connection, BIND_AUTO_CREATE);
        if (ContextCompat.checkSelfPermission(DownloadActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DownloadActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    public class BtnStartListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            String url = "https://118.25.153.97/file/jpeg/tim1.jpg";
            downloadBinder.startDownload(url);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[1]!=PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "No permission. can't run ", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
                default:
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
