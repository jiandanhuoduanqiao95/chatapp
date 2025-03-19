package com.example.chatapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.databinding.ActivitySignUpBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import org.json.JSONObject;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    private static final String BASE_URL = "http://10.0.2.2:5000/";
    
    private ActivitySignUpBinding binding;
    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
//        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        setListeners();
        
        // 初始化OkHttpClient
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build();
    }

    private void setListeners() {
        binding.textSignIn.setOnClickListener(v -> {
            onBackPressed();
        });
        
        // 设置注册按钮点击事件
        binding.buttonSignUp.setOnClickListener(v -> performSignUp());
    }
    
    private void performSignUp() {
        // 获取输入的注册信息
        String telephone = binding.inputMobile.getText().toString().trim();
        String password = binding.inputPassword.getText().toString().trim();
        String nickname = binding.inputName.getText().toString().trim();
        
        // 基本输入验证
        if (telephone.isEmpty()) {
            showToast("请输入手机号");
            return;
        }
        if (password.isEmpty()) {
            showToast("请输入密码");
            return;
        }
        if (nickname.isEmpty()) {
            showToast("请输入昵称");
            return;
        }
        
        loading(true);
        
        // 构建请求体
        FormBody formBody = new FormBody.Builder()
                .add("telephone", telephone)
                .add("password", password)
                .add("nickname", nickname)
                .build();
                
        // 构建请求
        Request request = new Request.Builder()
                .url(BASE_URL + "auth/register")
                .post(formBody)
                .build();
                
        // 发送异步请求
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    loading(false);
                    showToast("网络请求失败: " + e.getMessage());
                });
            }
            
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String responseBody = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseBody);
                    int code = jsonObject.getInt("code");
                    String message = jsonObject.optString("message", "");
                    
                    runOnUiThread(() -> {
                        loading(false);
                        if (code == 200) {
                            showToast("注册成功");
                            // 注册成功后跳转到登录页面
                            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            showToast("注册失败: " + message);
                        }
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        loading(false);
                        showToast("数据解析错误");
                    });
                }
            }
        });
    }
    
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    
    private void loading(boolean isLoading) {
        if (isLoading) {
            binding.buttonSignUp.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.buttonSignUp.setVisibility(View.VISIBLE);
        }
    }
}