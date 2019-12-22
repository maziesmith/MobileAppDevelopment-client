package cl.sse.tongji.edu.android_end;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.io.File;
import java.io.InputStream;

import cl.sse.tongji.edu.android_end.model.User;
import cl.sse.tongji.edu.android_end.presenter.DownloadListener;
import cl.sse.tongji.edu.android_end.presenter.DownloadTask;

public class DownloadService extends Service {

    private DownloadTask downloadTask;
    private String downloadUrl;
    private static final String NOTIFICATION_CHANNEL_ID = "cl_download";
    private static final String channelName = "My Download Service";

    private DownloadListener listener = new DownloadListener() {


        @Override
        public void onProgress(int progress) {
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            NotificationManager manager = getNotificationManager();
            manager.createNotificationChannel(chan);
            manager.notify(1, getNotification("Downloading...", progress));
        }

        @Override
        public void onSucceed() {
            downloadTask = null;
            //创建通知
            stopForeground(true);

            //安卓新特性，api26以上新建通知必须用manager将channel加入
            //安卓书本更新也太慢了
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            NotificationManager manager = getNotificationManager();
            manager.createNotificationChannel(chan);
            manager.notify(1, getNotification("Download Success", -1));
            Toast.makeText(DownloadService.this, "Download Success", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailed() {
            downloadTask = null;

            stopForeground(true);
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            NotificationManager manager = getNotificationManager();
            manager.createNotificationChannel(chan);
            manager.notify(1, getNotification("Download Failde", -1));
            Toast.makeText(DownloadService.this, "Downloaded Failed", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onPause() {
            downloadTask = null;
            Toast.makeText(DownloadService.this, "Download Paused", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCanceled() {
            downloadTask = null;
            stopForeground(true);
            Toast.makeText(DownloadService.this, "Download Canceled", Toast.LENGTH_LONG).show();
        }
    };

    public DownloadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private DownloadBinder mBinder = new DownloadBinder();

    class DownloadBinder extends Binder {
        private User user;

        public void startDownload(String url) {
            if (downloadTask == null) {
                downloadUrl = url;
                downloadTask = new DownloadTask(listener, user);
                downloadTask.execute(downloadUrl);
                startForeground(1, getNotification("Downloading...", 0));
                Toast.makeText(DownloadService.this, "Dowloading...", Toast.LENGTH_LONG).show();
            }
        }

        public void setUser(User iuser) {
            user = iuser;
        }

        public void pauseDownload() {
            if (downloadTask != null) {
                downloadTask.pause();
            }
        }

        public void cancelDownload() {
            if (downloadTask != null) {
                downloadTask.cancel();
            }
            if (downloadUrl != null) {
                String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                String directory = Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                File file = new File(directory + fileName);
                if (file.exists()) {
                    file.delete();
                }
                getNotificationManager().cancel(1);
                stopForeground(true);
                Toast.makeText(DownloadService.this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private Notification getNotification(String title, int progress) {
        Intent intent = new Intent(this, DownloadActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.download);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentIntent(pi);
        builder.setContentTitle(title);
        if (progress >= 0) {
            builder.setContentTitle(progress + "%");
            builder.setProgress(100, progress, false);
        }

        return builder.build();
    }
}
