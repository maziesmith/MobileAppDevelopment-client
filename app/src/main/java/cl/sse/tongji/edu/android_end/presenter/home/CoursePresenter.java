package cl.sse.tongji.edu.android_end.presenter.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cl.sse.tongji.edu.android_end.HomeActivity;
import cl.sse.tongji.edu.android_end.model.Course;
import cl.sse.tongji.edu.android_end.common.HttpTrust.TrustAllCerts;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class CoursePresenter {

    private TakeResponseData takeResponseData;

    HomeActivity activity;
    Context context;

    public CoursePresenter(HomeActivity iactivity) {
        activity = iactivity;
    }

    public CoursePresenter(Context icontext) {
        context = icontext;
    }

    public void initCourse() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获得无安全检查的client
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.sslSocketFactory(TrustAllCerts.createSSLSocketFactory(), new TrustAllCerts());
                builder.hostnameVerifier(new TrustAllCerts.TrustAllHostnameVerifier());
                OkHttpClient client = builder.build();

                //构建请求
                String url = "https://118.25.153.97/course/";
                SharedPreferences pref = activity.getSharedPreferences("token", MODE_PRIVATE);
                String token = pref.getString("token", null);
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("Authorization", token)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String response_string = response.body().string();
                    Gson gson = new Gson();
                    List<Course> courses = gson.fromJson(response_string, new TypeToken<List<Course>>() {
                    }.getType());

                    //更新ui
                    Message message = new Message();
                    message.obj = courses;
                    show_all_course_handler.sendMessage(message);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void addToFavorite(final Course course, final String token) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //获得无安全检查的client
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    builder.sslSocketFactory(TrustAllCerts.createSSLSocketFactory(), new TrustAllCerts());
                    builder.hostnameVerifier(new TrustAllCerts.TrustAllHostnameVerifier());
                    OkHttpClient client = builder.build();

                    //构建请求
                    String url = "https://118.25.153.97/course/take";

                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    Map map = new HashMap();
                    map.put("course_id", course.getId());
                    Gson gson = new Gson();
                    String json_str = gson.toJson(map);
                    RequestBody body = RequestBody.Companion.create(JSON, json_str);

                    Request request = new Request.Builder()
                            .url(url)
                            .addHeader("Authorization", token)
                            .post(body)
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    takeResponseData = gson.fromJson(responseData, TakeResponseData.class);

                    Message message = new Message();
                    message.obj = takeResponseData;

                    take_course_handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void initFavorite(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获得无安全检查的client
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.sslSocketFactory(TrustAllCerts.createSSLSocketFactory(), new TrustAllCerts());
                builder.hostnameVerifier(new TrustAllCerts.TrustAllHostnameVerifier());
                OkHttpClient client = builder.build();

                //构建请求
                String url = "https://118.25.153.97/course/take";
                SharedPreferences pref = activity.getSharedPreferences("token", MODE_PRIVATE);
                String token = pref.getString("token", null);
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("Authorization", token)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    String response_string = response.body().string();
                    Gson gson = new Gson();
                    List<Course> courses = gson.fromJson(response_string, new TypeToken<List<Course>>() {
                    }.getType());

                    //更新ui
                    Message message = new Message();
                    message.obj = courses;
                    show_all_favorite_handler.sendMessage(message);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void initPDF() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获得无安全检查的client
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.sslSocketFactory(TrustAllCerts.createSSLSocketFactory(), new TrustAllCerts());
                builder.hostnameVerifier(new TrustAllCerts.TrustAllHostnameVerifier());
                OkHttpClient client = builder.build();

                //构建请求
                String url = "https://118.25.153.97/file/pdf";
                SharedPreferences pref = activity.getSharedPreferences("token", MODE_PRIVATE);
                String token = pref.getString("token", null);
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("Authorization", token)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    String response_string = response.body().string();
                    Gson gson = new Gson();
                    List<Course> courses = gson.fromJson(response_string, new TypeToken<List<Course>>() {
                    }.getType());

                    //更新ui
                    Message message = new Message();
                    message.obj = courses;
                    show_all_pdf_handler.sendMessage(message);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler show_all_course_handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            List<Course> courses = (List<Course>) msg.obj;
            activity.showAllCourse(courses);
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler take_course_handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            TakeResponseData data = (TakeResponseData) msg.obj;
            Toast.makeText(context, data.message, Toast.LENGTH_SHORT).show();
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler show_all_favorite_handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            List<Course> courses = (List<Course>) msg.obj;

            activity.showAllFavorite(courses);
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler show_all_pdf_handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            List<Course> courses = (List<Course>) msg.obj;
            activity.showAllPDFS(courses);
        }
    };

}
