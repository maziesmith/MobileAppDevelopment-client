package cl.sse.tongji.edu.android_end;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.io.InputStream;

import cl.sse.tongji.edu.android_end.presenter.DownloadListener;
import cl.sse.tongji.edu.android_end.presenter.DownloadTask;

public class DownloadService extends Service {

    private DownloadTask downloadTask;
    private String downloadUrl;
    // TODO 实现接口
    private DownloadListener listener = new DownloadListener() {


        @Override
        public void onProgress(int progress) {

        }

        @Override
        public void onSucceed() {

        }

        @Override
        public void onFailed() {

        }

        @Override
        public void onPause() {

        }

        @Override
        public void onCanceled() {

        }
    };

    public DownloadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private DownloadBinder mBinder = new DownloadBinder();

    class DownloadBinder extends Binder{
        public void startDownload(String url){
            if(downloadTask==null){
                downloadTask = new DownloadTask(listener);
                downloadTask.execute(downloadUrl);
                startForeground(1, getNotification("Downloading...",0));
                Toast.makeText(DownloadService.this,"Dowloading...",Toast.LENGTH_LONG).show();
            }
        }

        public void pauseDownload(){

        }
    }

    private NotificationManager getNotificationManager(){
        return (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    }

    private Notification getNotification(String title, int progress){
        Intent intent = new Intent(this, DownloadActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this,0,intent,0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentIntent(pi);
        builder.setContentTitle(title);
        if (progress>=0){
            builder.setContentTitle(progress+"%");
            builder.setProgress(100, progress, false);
        }

        return builder.build();
    }
}
