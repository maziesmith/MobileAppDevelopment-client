package cl.sse.tongji.edu.android_end.presenter.login;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import javax.xml.parsers.FactoryConfigurationError;

import cl.sse.tongji.edu.android_end.LoginActivity;
import cl.sse.tongji.edu.android_end.model.HttpTrust.TrustAllCerts;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class LoginPresenter {
    private LoginActivity activity;
    private LoginResponseMessage response_message;

    public LoginPresenter(LoginActivity loginActivity) {
        activity = loginActivity;
    }


    public void SendLoginRequest(final String username, final String password, final String type) {
        Log.d("LoginPresenter", "SendLoginRequest");
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
                    map.put("type", type);
                    Gson gson = new Gson();
                    String json_str = gson.toJson(map);

                    RequestBody body = RequestBody.create(JSON, json_str);
                    Request request = new Request.Builder()
                            .url("https://118.25.153.97/auth/login")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    Log.d("LoginPresenter", "succeed");
                    String responseData = response.body().string();
                    Log.d("LoginPresenter", responseData);

                    response_message = gson.fromJson(responseData, LoginResponseMessage.class);
                    Message message = new Message();
                    message.obj = response_message;
                    handler.sendMessage(message);




                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("aaa", "bbb");
                } catch (IllegalStateException e){
                    response_message.flag= Boolean.FALSE;
                    response_message.message="Server is off line";
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
                SharedPreferences.Editor editor = activity.getSharedPreferences("token", MODE_PRIVATE).edit();
                editor.putString("token", response_message.token);
                editor.apply();
                activity.jumpToHome();
            } else {
                activity.showErrorMessage("登陆失败", response_message.message);
            }
        }
    };

}
