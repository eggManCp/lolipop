package com.lolipop.reader.ui.activitys;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.daliy.txtreader.main.LoadListenerAdapter;
import com.daliy.txtreader.main.TxtReaderView;
import com.lolipop.reader.R;


/**
 * created by ： bifan-wei
 */
public class MoreDisplayActivity extends AppCompatActivity {
    private final int[] backgroundColors = new int[]{
            Color.parseColor("#ccebcc"),
            Color.parseColor("#d4c7a5"),
            Color.parseColor("#393330"),
            Color.parseColor("#00141f"),
    };
    private final int[] textColors = new int[]{
            Color.parseColor("#505550"),
            Color.parseColor("#453e33"),
            Color.parseColor("#8f8e88"),
            Color.parseColor("#27576c")
    };

    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        setContentView(R.layout.activity_display_more);
        setReaderView(findViewById(R.id.txtReaderView_g_1), backgroundColors[0], textColors[0], 20);
        setReaderView(findViewById(R.id.txtReaderView_g_2), backgroundColors[1], textColors[1], 200);
        setReaderView(findViewById(R.id.txtReaderView_g_3), backgroundColors[2], textColors[2], 400);
        setReaderView(findViewById(R.id.txtReaderView_g_4), backgroundColors[3], textColors[3], 600);
    }

    private void setReaderView(final TxtReaderView readerView, final int background, final int textColor, long delatLoadTime) {
        handler.postDelayed(() -> {
            String path = getIntent().getStringExtra("filePath");
            readerView.loadTxtFile(path, new LoadListenerAdapter() {
                @Override
                public void onSuccess() {
                    readerView.setStyle(background, textColor);
                }
            });
        }, delatLoadTime);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
