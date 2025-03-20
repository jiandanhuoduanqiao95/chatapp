package com.example.chatapp.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.chatapp.databinding.ActivitySignInBinding;
import android.content.Intent;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        client = new OkHttpClient();

        binding.buttonSignIn.setOnClickListener(v -> {
            if (validateInputs()) {
                loginUser();
            }
        });

        binding.textCreateNewAccount
                .setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));
    }

    private boolean validateInputs() {
        if (binding.inputMobile.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (binding.inputPassword.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void loginUser() {
        String telephone = binding.inputMobile.getText().toString().trim();
        String password = binding.inputPassword.getText().toString().trim();

        FormBody formBody = new FormBody.Builder()
                .add("telephone", telephone)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url("http://10.0.2.2:5000/auth/login")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(SignInActivity.this, "网络错误: " + e.getMessage(), Toast.LENGTH_SHORT)
                        .show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String responseBody = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseBody);
                    int code = jsonObject.getInt("code");
                    String message = jsonObject.optString("message", "");
                    runOnUiThread(() -> {
                        Toast.makeText(SignInActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        // 跳转到主界面
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> Toast.makeText(SignInActivity.this, "登录失败，请检查您的凭据", Toast.LENGTH_SHORT).show());
                }

            }
        });
    }
}