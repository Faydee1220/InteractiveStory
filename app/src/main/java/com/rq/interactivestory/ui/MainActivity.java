package com.rq.interactivestory.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rq.interactivestory.R;

public class MainActivity extends AppCompatActivity {
    private EditText nameTextField;
    private Button startButton;

    // onCreate 類似 iOS 的 viewDidload
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameTextField = findViewById(R.id.nameTextField);
        startButton = findViewById(R.id.startButton);


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameTextField.getText().toString();
                // Anonymous inner class
//                Toast.makeText(MainActivity.this, name, Toast.LENGTH_SHORT).show();
                
                startStory(name);
            }
        });

    }

    // onResume 類似 iOS 的 viewWillAppear
    @Override
    protected void onResume() {
        super.onResume();
        nameTextField.setText("");
    }

    private void startStory(String name) {
        // 前往下一個畫面，目的地：StoryActivity.class
        // startActivity 類似 iOS 的 performSegue，目的地：segue.destinationViewController
        Intent intent = new Intent(this, StoryActivity.class);
        // 傳遞參數，Key-Value
        Resources resources = getResources();
        String key = resources.getString(R.string.key_name);
        intent.putExtra(key, name);
        startActivity(intent);

    }
}
