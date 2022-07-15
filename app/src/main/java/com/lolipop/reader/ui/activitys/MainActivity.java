package com.lolipop.reader.ui.activitys;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.PermissionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.daliy.txtreader.ui.HwTxtPlayActivity;
import com.daliy.txtreader.utils.FileProvider;
import com.lolipop.reader.R;
import com.lolipop.reader.model.BookModel;
import com.lolipop.reader.ui.adapter.BookListAdapter;
import com.lolipop.reader.ui.viewmodel.CategoryListModel;

public class MainActivity extends AppCompatActivity {
    protected RecyclerView rec;
    protected AppCompatImageView add;

    private BookListAdapter adapter;

    private CategoryListModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);

        initView();
    }

    private void initView() {
        rec = findViewById(R.id.rec);
        add = findViewById(R.id.iv_add);
        adapter = new BookListAdapter(R.layout.item_book);
        rec.setLayoutManager(new GridLayoutManager(this, 3));
        rec.setAdapter(adapter);
        rec.addItemDecoration(new RecyclerView.ItemDecoration() {

            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view);
                if (position % 3 == 2) {
                    outRect.right = 10;
                }
                outRect.left = 10;
                outRect.top = 5;
                outRect.bottom = 5;
            }
        });
        adapter.setOnItemClickListener((adapter, view, position) -> {
            HwTxtPlayActivity.loadTxtFile(this, ((BookModel) adapter.getData().get(position)).getPath());
        });

        adapter.setOnItemLongClickListener((adapter, view, position) -> {
            new AlertDialog.Builder(MainActivity.this)
                    .setCancelable(true)
                    .setMessage("这本看完了,删除哇?")
                    .setPositiveButton("确定", (dialog, which) -> {
                        FileUtils.delete(((BookModel) adapter.getData().get(position)).getPath());
                        adapter.getData().remove(position);
                        adapter.notifyItemRemoved(position);
                        dialog.dismiss();
                    })
                    .setNegativeButton("等哈", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .create()
                    .show();
            return false;
        });

        add.setOnClickListener(v -> {
            checkPermission();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 0x001) {//是否选择，没选择就不会继续
            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程
            String path = FileProvider.getFileAbsolutePath(this, uri);
            HwTxtPlayActivity.loadTxtFile(this, path);
        }
    }

    private void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/plain");//设置类型
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 0x001);
    }

    private void checkPermission() {
        PermissionUtils.permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        chooseFile();
                    }

                    @Override
                    public void onDenied() {

                    }
                }).request();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        super.onResume();
        getRecentBooks();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getRecentBooks() {
        List<BookModel> list = new ArrayList<>();
        List<File> files = FileUtils.listFilesInDir(getExternalCacheDir().getAbsolutePath());
        for (File file : files) {
            if (file.exists() && file.getName().toLowerCase().endsWith("txt")) {
                BookModel model = new BookModel();
                model.setName(file.getName());
                model.setPath(file.getAbsolutePath());
                model.setLastTime(file.lastModified());
                list.add(model);
            }
        }
        Comparator<BookModel> byTime =
                Comparator.comparingLong(BookModel::getLastTime);
        list.sort(byTime);
        Collections.reverse(list);
        adapter.setNewInstance(list);
    }
}
