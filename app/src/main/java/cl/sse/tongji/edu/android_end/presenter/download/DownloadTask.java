package cl.sse.tongji.edu.android_end.presenter.download;

import android.os.AsyncTask;
import android.os.Environment;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import cl.sse.tongji.edu.android_end.common.HttpTrust.TrustAllCerts;
import cl.sse.tongji.edu.android_end.model.User;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadTask extends AsyncTask<String, Integer, Integer> {
    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILD = 1;
    public static final int TYPE_PAUSED = 2;
    public static final int TYPE_CANCELED = 3;

    private DownloadListener listener;
    private boolean isCanceled = false;
    private boolean isPause = false;
    private int lastProgress;
    private User user;

    public DownloadTask(DownloadListener ilistener, User iuser) {
        listener = ilistener;
        user = iuser;
    }

    @Override
    protected Integer doInBackground(String... strings) {
        InputStream is = null;
        RandomAccessFile savedFile = null;
        File file = null;
        try {
            long downloadLength = 0;
            String downloadUrl = strings[0];
            String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            String directory = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            file = new File(directory + fileName);
            if (file.exists()) {
                downloadLength = file.length();
            }
            long contentLength = getContentLength(downloadUrl);
            if (contentLength == 0) {
                return TYPE_FAILD;
            } else if (contentLength == downloadLength) {
                return TYPE_SUCCESS;
            }


            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(TrustAllCerts.createSSLSocketFactory(), new TrustAllCerts());
            builder.hostnameVerifier(new TrustAllCerts.TrustAllHostnameVerifier());
            OkHttpClient client = builder.build();

            String token = user.getToken();
            Request request = new Request.Builder()
                    .addHeader("Authorization", token)
                    .addHeader("RANGE", "bytes=" + downloadLength + '-')
                    .url(downloadUrl)
                    .build();
            Response response = client.newCall(request).execute();

            // response 永远为真，与书本代码结构略有偏差
            is = response.body().byteStream();
            savedFile = new RandomAccessFile(file, "rw");
            savedFile.seek(downloadLength);
            byte[] bytes = new byte[1024];
            int total = 0;
            int len;
            while ((len = is.read(bytes)) != -1) {
                if (isCanceled) {
                    return TYPE_CANCELED;
                } else if (isPause) {
                    return TYPE_PAUSED;
                } else {
                    total += len;
                    savedFile.write(bytes, 0, len);
                    int progress = (int) ((total + downloadLength) * 100 / contentLength);
                    publishProgress(progress);
                }
            }
            response.body().close();
            return TYPE_SUCCESS;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (savedFile != null) {
                    savedFile.close();
                }
                if (isCanceled && file != null) {
                    file.delete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return TYPE_FAILD;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if (progress > lastProgress) {
            listener.onProgress(progress);
            lastProgress = progress;
        }
    }

    @Override
    protected void onPostExecute(Integer integer) {
        switch (integer) {
            case TYPE_SUCCESS:
                listener.onSucceed();
                break;
            case TYPE_FAILD:
                listener.onFailed();
                break;
            case TYPE_CANCELED:
                listener.onCanceled();
                break;
            case TYPE_PAUSED:
                listener.onPause();
                break;
        }
    }

    public void pause() {
        isPause = true;
    }

    public void cancel() {
        isCanceled = true;
    }

    private long getContentLength(String url) throws IOException {
        //要使用专门的client
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.sslSocketFactory(TrustAllCerts.createSSLSocketFactory(), new TrustAllCerts());
        builder.hostnameVerifier(new TrustAllCerts.TrustAllHostnameVerifier());
        OkHttpClient client = builder.build();

        //TODO 添加token
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().contentLength();
        }
        return 0;
    }
}
