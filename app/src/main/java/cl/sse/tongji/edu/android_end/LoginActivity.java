package cl.sse.tongji.edu.android_end;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;

import cl.sse.tongji.edu.android_end.common.HttpTrust.TrustAllCerts;
import cl.sse.tongji.edu.android_end.model.User;
import cl.sse.tongji.edu.android_end.presenter.login.LoginPresenter;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    ImageButton btn_login;
    ImageButton btn_register;
    EditText login_id;
    EditText login_passwd;
    LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d("LoginActivity", "create");
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        login_id = findViewById(R.id.login_id);
        login_passwd = findViewById(R.id.login_passwd);

        btn_register.setOnClickListener(new BtnRegister());
        btn_login.setOnClickListener(new BtnLogin());
        showHttpResult("test");

        loadLoginInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("LoginActivity", "Destroy");
        saveLoginInfo();
    }

    private void testHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String url = "https://118.25.153.97/event_lib/test";
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.sslSocketFactory(TrustAllCerts.createSSLSocketFactory(), new TrustAllCerts());
                builder.hostnameVerifier(new TrustAllCerts.TrustAllHostnameVerifier());
                OkHttpClient client = builder.build();

                Request request = new Request.Builder().url(url).build();
                Call call = client.newCall(request);
                try {
                    Response response = call.execute();
                    if (response.code() == 200) {
                        String tempResult = response.body().string();
                        showHttpResult(tempResult);
                    } else {
                        showHttpResult("响应状态码错误，无法获取数据");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showHttpResult(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, s, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadLoginInfo() {
        SharedPreferences pref = getSharedPreferences("login_data", MODE_PRIVATE);
        String id = pref.getString("username","");
        String passwd = pref.getString("password","");
        login_id.setText(id);
        login_passwd.setText(passwd);
    }

    private void saveLoginInfo() {
        Log.d("LoginActivity", "save login data");
        SharedPreferences.Editor editor = getSharedPreferences("login_data", MODE_PRIVATE).edit();
        String id = login_id.getText().toString();
        String passwd = login_passwd.getText().toString();
        editor.putString("username", id);
        editor.putString("password", passwd);
        editor.apply();
    }

    public class BtnRegister implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            Log.d("LoginActivity", "register");
            startActivity(intent);
        }
    }

    public class BtnLogin implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String id = login_id.getText().toString();
            String password = login_passwd.getText().toString();
            String type = "student";

            saveLoginInfo();

            presenter = new LoginPresenter(LoginActivity.this);
            presenter.SendLoginRequest(id, password, "student");
        }
    }

    //登陆成功跳转到首页
    public void jumpToHome(User user){
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    //展示错误信息
    public void showErrorMessage(final String type, final String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle(type)
                        .setMessage(msg)
                        .create().show();
            }
        });
    }
}
