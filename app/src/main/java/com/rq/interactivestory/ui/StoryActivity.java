package com.rq.interactivestory.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rq.interactivestory.R;
import com.rq.interactivestory.model.Page;
import com.rq.interactivestory.model.Story;

import java.util.Stack;

public class StoryActivity extends AppCompatActivity {

    public static final String TAG = StoryActivity.class.getSimpleName();
    private String name;
    private Story story;

    private ImageView storyImageView;
    private TextView storyTextView;
    private Button choice1Button;
    private Button choice2Button;

    private Stack<Integer> pageStack = new Stack<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        // 設定 UI
        storyImageView = findViewById(R.id.storyImageView);
        storyTextView = findViewById(R.id.storyTextView);
        choice1Button = findViewById(R.id.choice1Button);
        choice2Button = findViewById(R.id.choice2Button);

        // 取得前一頁傳遞的參數
        Intent intent = getIntent();
        name = intent.getStringExtra(getString(R.string.key_name));
        if (name == null || name.isEmpty()) {
            name = "Friend";
        }
        Log.d(TAG, name);

        story = new Story();
        loadPage(0);
    }

    private void loadPage(int pageNumber) {
        pageStack.push(pageNumber);

        final Page page = story.getPage(pageNumber);

        // 設定圖片
        Drawable image = ContextCompat.getDrawable(this, page.getImageId());
        storyImageView.setImageDrawable(image);

        String pageText = (String) getText(page.getTextId());
        // Add name if placeholder included. Won't add if not
        // 將字串中的特定參數替換
        // %1s ，%1 代表用第一個參數 name 替換，s 代表 simple string
        pageText = String.format(pageText, name);
        storyTextView.setText(pageText);

        if (page.isFinalPage()) {
            choice1Button.setVisibility(View.INVISIBLE);
            choice2Button.setText(R.string.play_again_button_text);
            choice2Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 回到前一頁
                    // 可重新輸入姓名
//                    finish();

                    // 回到故事第一頁
                    loadPage(0);
                }
            });
        }
        else {
            loadButtons(page);
        }



    }

    private void loadButtons(final Page page) {
        choice1Button.setVisibility(View.VISIBLE);
        choice1Button.setText(page.getChoice1().getTextId());
        choice1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nextPage = page.getChoice1().getNextPage();
                loadPage(nextPage);
            }
        });

        choice2Button.setVisibility(View.VISIBLE);
        choice2Button.setText(page.getChoice2().getTextId());
        choice2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nextPage = page.getChoice2().getNextPage();
                loadPage(nextPage);
            }
        });
    }

    // Android 左下方的返回按鈕事件
    @Override
    public void onBackPressed() {
        pageStack.pop();
        if (pageStack.isEmpty()) {
            super.onBackPressed(); // 執行 super class 中的預設行為
            // 因為在 AndroidManifest.xml 中有設定 android:parentActivityName=".ui.MainActivity"
            // 會回到輸入姓名頁
        }
        else {
            loadPage(pageStack.pop());
        }
        // 舉例：
        // page 0 -> page 1 -> 按下返回 -> 第一個 pop 回到 page 0，第二個 pop 移除 page 0，最後 loadPage 的 push 回到 page 0
    }
}
