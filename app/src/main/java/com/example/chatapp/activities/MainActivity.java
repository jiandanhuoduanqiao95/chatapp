package com.example.chatapp.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.chatapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 设置欢迎信息
        binding.textViewWelcome.setText("欢迎来到聊天应用！");
    }
} 