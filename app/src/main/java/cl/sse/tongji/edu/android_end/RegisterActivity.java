package cl.sse.tongji.edu.android_end;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import cl.sse.tongji.edu.android_end.presenter.register.RegisterPresenter;

public class RegisterActivity extends AppCompatActivity {

    EditText register_id;
    EditText register_passwd;
    EditText resister_email;
    ImageButton register_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_id = findViewById(R.id.register_id);
        register_passwd = findViewById(R.id.register_passwd);
        resister_email = findViewById(R.id.register_email);
        register_btn = findViewById(R.id.register_btn);

        register_btn.setOnClickListener(new RegisterButtonListener());
    }

    class RegisterButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String id = register_id.getText().toString();
            String passwd = register_passwd.getText().toString();
            String email = resister_email.getText().toString();

            RegisterPresenter presenter = new RegisterPresenter(RegisterActivity.this);
            presenter.sendReigsterRequest(id, passwd, email);
        }
    }
}
