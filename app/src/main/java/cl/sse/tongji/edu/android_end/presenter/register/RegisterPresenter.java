package cl.sse.tongji.edu.android_end.presenter.register;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cl.sse.tongji.edu.android_end.RegisterActivity;
import cl.sse.tongji.edu.android_end.common.HttpTrust.TrustAllCerts;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterPresenter {
    RegisterActivity activity;
    RegisterResponse response_message;


    public RegisterPresenter(RegisterActivity iactivity) {
        activity = iactivity;
    }

    public void sendReigsterRequest(final String username, final String password, final String email) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    builder.sslSocketFactory(TrustAllCerts.createSSLSocketFactory(), new TrustAllCerts());
                    builder.hostnameVerifier(new TrustAllCerts.TrustAllHostnameVerifier());
                    OkHttpClient client = builder.build();

                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    Map map = new HashMap();
                    map.put("username", username);
                    map.put("password", password);
                    map.put("type", "student");
                    map.put("email", email);
                    Gson gson = new Gson();
                    String json_str = gson.toJson(map);

                    RequestBody body = RequestBody.Companion.create(JSON, json_str);
                    Request request = new Request.Builder()
                            .url("https://118.25.153.97/auth/register")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    Log.d("LoginPresenter", "succeed");
                    String responseData = response.body().string();
                    Log.d("LoginPresenter", responseData);
                    response_message = gson.fromJson(responseData, RegisterResponse.class);

                    Message message = new Message();
                    message.obj = response_message;
                    handler.sendMessage(message);


                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("aaa", "bbb");
                } catch (IllegalStateException e) {
                    response_message.flag = Boolean.FALSE;
                    response_message.message = "Server is off line";
                    Message message = new Message();
                    message.obj = response_message;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (response_message.flag) {
                Toast.makeText(activity, "注册成功", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(activity, "失败：" + response_message.message, Toast.LENGTH_LONG).show();
            }
        }
    };
}
