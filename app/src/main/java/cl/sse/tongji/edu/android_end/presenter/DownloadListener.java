package cl.sse.tongji.edu.android_end.presenter;

// 这难道是要重头实现一个监听器？？？我去，为毛不要用已经写好的呢，我引用能引用到啊

public interface DownloadListener {
    void onProgress(int progress);

    void onSucceed();

    void onFailed();

    void onPause();

    void onCanceled();
}
