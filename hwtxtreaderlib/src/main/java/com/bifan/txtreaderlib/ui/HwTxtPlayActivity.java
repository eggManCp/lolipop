package com.bifan.txtreaderlib.ui;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bifan.txtreaderlib.BroadcastReceiver.ScreenStatusReceiver;
import com.bifan.txtreaderlib.R;
import com.bifan.txtreaderlib.bean.TxtChar;
import com.bifan.txtreaderlib.bean.TxtMsg;
import com.bifan.txtreaderlib.interfaces.ICenterAreaClickListener;
import com.bifan.txtreaderlib.interfaces.IChapter;
import com.bifan.txtreaderlib.interfaces.ILoadListener;
import com.bifan.txtreaderlib.interfaces.ISliderListener;
import com.bifan.txtreaderlib.interfaces.ITextSelectListener;
import com.bifan.txtreaderlib.main.TxtConfig;
import com.bifan.txtreaderlib.main.TxtReaderView;
import com.bifan.txtreaderlib.utils.ELogger;
import com.bifan.txtreaderlib.utils.FileProvider;

import java.io.File;

/**
 * Created by bifan-wei
 * on 2017/12/8.
 */

public class HwTxtPlayActivity extends AppCompatActivity {
    protected Handler mHandler;
    protected boolean FileExist = false;
    private ScreenStatusReceiver receiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewLayout());
        FileExist = getIntentData();
        init();
        registerBroadcast();
        loadFile();
        registerListener();
    }

    protected int getContentViewLayout() {
        return R.layout.activity_hwtxtpaly;
    }

    protected boolean getIntentData() {
        // Get the intent that started this activity
        Uri uri = getIntent().getData();
        if (uri != null) {
            ELogger.log("getIntentData", "" + uri);
        } else {
            ELogger.log("getIntentData", "uri is null");
        }
        if (uri != null) {
            try {
                String path = FileProvider.getFileAbsolutePath(this, uri);
                if (!TextUtils.isEmpty(path)) {
                    if (path.contains("/storage/")) {
                        path = path.substring(path.indexOf("/storage/"));
                    }
                    ELogger.log("getIntentData", "path:" + path);
                    File file = new File(path);
                    if (file.exists()) {
                        FilePath = path;
                        FileName = file.getName();
                        return true;
                    } else {
                        toast("文件不存在");
                        return false;
                    }
                }
                return false;
            } catch (Exception e) {
                toast("文件出错了");
            }
        }

        FilePath = getIntent().getStringExtra("FilePath");
        FileName = getIntent().getStringExtra("FileName");
        ContentStr = getIntent().getStringExtra("ContentStr");
        if (ContentStr == null) {
            return FilePath != null && new File(FilePath).exists();
        } else {
            return true;
        }

    }

    private void registerBroadcast() {
        receiver = new ScreenStatusReceiver(mTxtReaderView);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(receiver, filter);
    }


    /**
     * @param context  上下文
     * @param FilePath 文本文件路径
     */
    public static void loadTxtFile(Context context, String FilePath) {
        loadTxtFile(context, FilePath, null);
    }

    /**
     * @param context 上下文
     * @param str     文本文内容
     */
    public static void loadStr(Context context, String str) {
        loadTxtStr(context, str, null);
    }

    /**
     * @param context  上下文
     * @param str      文本显示内容
     * @param FileName 显示的书籍或者文件名称
     */
    public static void loadTxtStr(Context context, String str, String FileName) {
        Intent intent = new Intent();
        intent.putExtra("ContentStr", str);
        intent.putExtra("FileName", FileName);
        intent.setClass(context, HwTxtPlayActivity.class);
        context.startActivity(intent);
    }

    /**
     * @param context  上下文
     * @param FilePath 文本文件路径
     * @param FileName 显示的书籍或者文件名称
     */
    public static void loadTxtFile(Context context, String FilePath, String FileName) {
        Intent intent = new Intent();
        intent.putExtra("FilePath", FilePath);
        intent.putExtra("FileName", FileName);
        intent.setClass(context, HwTxtPlayActivity.class);
        context.startActivity(intent);
    }

    protected View mTopDecoration, mBottomDecoration;
    protected View mChapterMsgView;
    protected TextView mChapterMsgName;
    protected TextView mChapterMsgProgress;
    protected TextView mChapterNameText;
    protected TextView mChapterMenuText;
    protected TextView mProgressText;
    protected TextView mSettingText;
    protected TextView mSelectedText;
    protected TxtReaderView mTxtReaderView;
    protected View mTopMenu;
    protected View mBottomMenu;
    protected View mCoverView;
    protected View ClipboardView;
    protected String CurrentSelectedText;
    protected BatteryView mBattery;

    protected ChapterList mChapterListPop;
    protected MenuHolder mMenuHolder = new MenuHolder();

    protected void init() {
        mHandler = new Handler();
        mChapterMsgView = findViewById(R.id.activity_hwTxtPlay_chapter_msg);
        mChapterMsgName = findViewById(R.id.chapter_name);
        mChapterMsgProgress = findViewById(R.id.chapter_progress);
        mTopDecoration = findViewById(R.id.activity_hwTxtPlay_top);
        mBottomDecoration = findViewById(R.id.activity_hwTxtPlay_bottom);
        mTxtReaderView = findViewById(R.id.activity_hwTxtPlay_readerView);
        mChapterNameText = findViewById(R.id.activity_hwTxtPlay_chapterName);
        mChapterMenuText = findViewById(R.id.activity_hwTxtPlay_chapter_menuText);
        mProgressText = findViewById(R.id.activity_hwTxtPlay_progress_text);
        mSettingText = findViewById(R.id.activity_hwTxtPlay_setting_text);
        mTopMenu = findViewById(R.id.activity_hwTxtPlay_menu_top);
        mBottomMenu = findViewById(R.id.activity_hwTxtPlay_menu_bottom);
        mCoverView = findViewById(R.id.activity_hwTxtPlay_cover);
        ClipboardView = findViewById(R.id.activity_hwTxtPlay_ClipBoar);
        mSelectedText = findViewById(R.id.activity_hwTxtPlay_selected_text);
        mBattery = findViewById(R.id.battery);

        mMenuHolder.mTitle = findViewById(R.id.txtReader_menu_title);
        mMenuHolder.mPreChapter = findViewById(R.id.txtReadr_menu_chapter_pre);
        mMenuHolder.mNextChapter = findViewById(R.id.txtReadr_menu_chapter_next);
        mMenuHolder.mSeekBar = findViewById(R.id.txtReadr_menu_seekbar);
        mMenuHolder.mTextSizeDel = findViewById(R.id.txtRead_menu_textsize_del);
        mMenuHolder.mTextSize = findViewById(R.id.txtRead_menu_textSize);
        mMenuHolder.mTextSizeAdd = findViewById(R.id.txtRead_menu_textSize_add);
        mMenuHolder.mBoldSelectedLayout = findViewById(R.id.txtRead_menu_textSetting1_bold);
        mMenuHolder.mCoverSelectedLayout = findViewById(R.id.txtRead_menu_textSetting2_cover);
        mMenuHolder.mShearSelectedLayout = findViewById(R.id.txtRead_menu_textSetting2_shear);
        mMenuHolder.mTranslateSelectedLayout = findViewById(R.id.txtRead_menu_textSetting2_translate);

        mMenuHolder.mStyle1 = findViewById(R.id.txtReader_menu_style1);
        mMenuHolder.mStyle2 = findViewById(R.id.txtReader_menu_style2);
        mMenuHolder.mStyle3 = findViewById(R.id.txtRead_menu_style3);
        mMenuHolder.mStyle4 = findViewById(R.id.txtReader_menu_style4);
        mMenuHolder.mStyle5 = findViewById(R.id.txtReader_menu_style5);
    }

    private final int[] StyleTextColors = new int[]{
            Color.parseColor("#4a453a"),
            Color.parseColor("#505550"),
            Color.parseColor("#453e33"),
            Color.parseColor("#8f8e88"),
            Color.parseColor("#27576c")
    };

    protected String ContentStr = null;
    protected String FilePath = null;
    protected String FileName = null;

    protected void loadFile() {
        TxtConfig.savePageSwitchDuration(this, 400);
        if (ContentStr == null) {
            if (TextUtils.isEmpty(FilePath) || !(new File(FilePath).exists())) {
                toast("文件不存在");
                return;
            }

        }
        mHandler.postDelayed(() -> {
            //延迟加载避免闪一下的情况出现
            if (ContentStr == null) {
                loadOurFile();
            } else {
                loadStr();
            }
        }, 300);


    }

    /**
     *
     */
    protected void loadOurFile() {
        mTxtReaderView.loadTxtFile(FilePath, new ILoadListener() {
            @Override
            public void onSuccess() {
                if (!hasExisted) {
                    onLoadDataSuccess();
                }
            }

            @Override
            public void onFail(final TxtMsg txtMsg) {
                if (!hasExisted) {
                    runOnUiThread(() -> onLoadDataFail(txtMsg));
                }

            }

            @Override
            public void onMessage(String message) {
                //加载过程信息
            }
        });
    }

    /**
     * @param txtMsg txtMsg
     */
    protected void onLoadDataFail(TxtMsg txtMsg) {
        //加载失败信息
        toast(String.valueOf(txtMsg));
    }

    /**
     *
     */
    protected void onLoadDataSuccess() {
        if (TextUtils.isEmpty(FileName)) {//没有显示的名称，获取文件名显示
            FileName = mTxtReaderView.getTxtReaderContext().getFileMsg().FileName;
        }
        setBookName(FileName);
        initWhenLoadDone();
    }

    private void loadStr() {
        String testText = ContentStr;
        mTxtReaderView.loadText(testText, new ILoadListener() {
            @Override
            public void onSuccess() {
                setBookName("test with str");
                initWhenLoadDone();
            }

            @Override
            public void onFail(TxtMsg txtMsg) {
                //加载失败信息
                toast(txtMsg + "");
            }

            @Override
            public void onMessage(String message) {
                //加载过程信息
            }
        });
    }

    protected void initWhenLoadDone() {
        if (mTxtReaderView.getTxtReaderContext().getFileMsg() != null) {
            FileName = mTxtReaderView.getTxtReaderContext().getFileMsg().FileName;
        }
        mMenuHolder.mTextSize.setText(String.valueOf(mTxtReaderView.getTextSize()));
        mTopDecoration.setBackgroundColor(mTxtReaderView.getBackgroundColor());
        mBottomDecoration.setBackgroundColor(mTxtReaderView.getBackgroundColor());
        //mTxtReaderView.setLeftSlider(new MuiLeftSlider());//修改左滑动条
        //mTxtReaderView.setRightSlider(new MuiRightSlider());//修改右滑动条
        //字体初始化
        onTextSettingUi(mTxtReaderView.getTxtReaderContext().getTxtConfig().Bold);
        //翻页初始化
        onPageSwitchSettingUi(mTxtReaderView.getTxtReaderContext().getTxtConfig().Page_Switch_Mode);
        //保存的翻页模式
        int pageSwitchMode = mTxtReaderView.getTxtReaderContext().getTxtConfig().Page_Switch_Mode;
        if (pageSwitchMode == TxtConfig.PAGE_SWITCH_MODE_SERIAL) {
            mTxtReaderView.setPageSwitchByTranslate();
        } else if (pageSwitchMode == TxtConfig.PAGE_SWITCH_MODE_COVER) {
            mTxtReaderView.setPageSwitchByCover();
        } else if (pageSwitchMode == TxtConfig.PAGE_SWITCH_MODE_SHEAR) {
            mTxtReaderView.setPageSwitchByShear();
        }
        //章节初始化
        if (mTxtReaderView.getChapters() != null && mTxtReaderView.getChapters().size() > 0) {
            WindowManager m = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics metrics = new DisplayMetrics();
            m.getDefaultDisplay().getMetrics(metrics);
            int ViewHeight = metrics.heightPixels - mTopDecoration.getHeight();
            mChapterListPop = new ChapterList(this, ViewHeight, mTxtReaderView.getChapters(), mTxtReaderView.getTxtReaderContext().getParagraphData().getCharNum());
            mChapterListPop.setOnDismissListener(this::hideNavigationBarStatusBar);
            mChapterListPop.getListView().setOnItemClickListener((adapterView, view, i, l) -> {
                IChapter chapter = (IChapter) mChapterListPop.getAdapter().getItem(i);
                mChapterListPop.dismiss();
                mTxtReaderView.loadFromProgress(chapter.getStartParagraphIndex(), 0);
            });
        } else {
            Gone(mChapterMenuText);
        }
    }

    protected void registerListener() {
        mSettingText.setOnClickListener(view -> Show(mTopMenu, mBottomMenu, mCoverView));
        setMenuListener();
        setSeekBarListener();
        setCenterClickListener();
        setPageChangeListener();
        setOnTextSelectListener();
        setStyleChangeListener();
        setExtraListener();
    }

    private void setExtraListener() {
        mMenuHolder.mPreChapter.setOnClickListener(new ChapterChangeClickListener(true));
        mMenuHolder.mNextChapter.setOnClickListener(new ChapterChangeClickListener(false));
        mMenuHolder.mTextSizeAdd.setOnClickListener(new TextChangeClickListener(true));
        mMenuHolder.mTextSizeDel.setOnClickListener(new TextChangeClickListener(false));
        mMenuHolder.mBoldSelectedLayout.setOnClickListener(new TextSettingClickListener());
        mMenuHolder.mTranslateSelectedLayout.setOnClickListener(new SwitchSettingClickListener(TxtConfig.PAGE_SWITCH_MODE_SERIAL));
        mMenuHolder.mCoverSelectedLayout.setOnClickListener(new SwitchSettingClickListener(TxtConfig.PAGE_SWITCH_MODE_COVER));
        mMenuHolder.mShearSelectedLayout.setOnClickListener(new SwitchSettingClickListener(TxtConfig.PAGE_SWITCH_MODE_SHEAR));
        mBattery.registerBattery(this);
    }

    protected void setStyleChangeListener() {
        mMenuHolder.mStyle1.setOnClickListener(new StyleChangeClickListener(ContextCompat.getColor(this, R.color.hwTxtReader_styleColor1), StyleTextColors[0]));
        mMenuHolder.mStyle2.setOnClickListener(new StyleChangeClickListener(ContextCompat.getColor(this, R.color.hwTxtReader_styleColor2), StyleTextColors[1]));
        mMenuHolder.mStyle3.setOnClickListener(new StyleChangeClickListener(ContextCompat.getColor(this, R.color.hwTxtReader_styleColor3), StyleTextColors[2]));
        mMenuHolder.mStyle4.setOnClickListener(new StyleChangeClickListener(ContextCompat.getColor(this, R.color.hwTxtReader_styleColor4), StyleTextColors[3]));
        mMenuHolder.mStyle5.setOnClickListener(new StyleChangeClickListener(ContextCompat.getColor(this, R.color.hwTxtReader_styleColor5), StyleTextColors[4]));
    }

    protected void setOnTextSelectListener() {
        mTxtReaderView.setOnTextSelectListener(new ITextSelectListener() {
            @Override
            public void onTextChanging(TxtChar firstSelectedChar, TxtChar lastSelectedChar) {
                //firstSelectedChar.Top
                //  firstSelectedChar.Bottom
                // 这里可以根据 firstSelectedChar与lastSelectedChar的top与bottom的位置
                //计算显示你要显示的弹窗位置，如果需要的话
            }

            @Override
            public void onTextChanging(String selectText) {
                onCurrentSelectedText(selectText);
            }

            @Override
            public void onTextSelected(String selectText) {
                onCurrentSelectedText(selectText);
            }
        });

        mTxtReaderView.setOnSliderListener(new ISliderListener() {
            @Override
            public void onShowSlider(TxtChar txtChar) {
                //TxtChar 为当前长按选中的字符
                // 这里可以根据 txtChar的top与bottom的位置
                //计算显示你要显示的弹窗位置，如果需要的话
            }

            @Override
            public void onShowSlider(String currentSelectedText) {
                onCurrentSelectedText(currentSelectedText);
                Show(ClipboardView);
            }

            @Override
            public void onReleaseSlider() {
                Gone(ClipboardView);
            }
        });

    }

    protected void setPageChangeListener() {
        mTxtReaderView.setPageChangeListener(progress -> {
            int p = (int) (progress * 1000);
            mProgressText.setText(((float) p / 10) + "%");
            mMenuHolder.mSeekBar.setProgress((int) (progress * 100));
            IChapter currentChapter = mTxtReaderView.getCurrentChapter();
            if (currentChapter != null) {
                mChapterNameText.setText((currentChapter.getTitle() + "").trim());
            } else {
                mChapterNameText.setText("无章节");
            }
        });
    }

    protected void setCenterClickListener() {
        mTxtReaderView.setOnCenterAreaClickListener(new ICenterAreaClickListener() {
            @Override
            public boolean onCenterClick(float widthPercentInView) {
                mSettingText.performClick();
                return true;
            }

            @Override
            public boolean onOutSideCenterClick(float widthPercentInView) {
                if (mBottomMenu.getVisibility() == View.VISIBLE) {
                    mSettingText.performClick();
                    return true;
                }
                return false;
            }
        });
    }

    protected void setMenuListener() {
        mTopMenu.setOnTouchListener((view, motionEvent) -> true);
        mBottomMenu.setOnTouchListener((view, motionEvent) -> true);
        mCoverView.setOnTouchListener((view, motionEvent) -> {
            Gone(mTopMenu, mBottomMenu, mCoverView, mChapterMsgView);
            return true;
        });
        View.OnClickListener chapterMsgShowClick = v -> {
            if (mChapterListPop != null) {
                if (!mChapterListPop.isShowing()) {
                    mChapterListPop.showAsDropDown(mTopDecoration);
                    mHandler.postDelayed(() -> {
                        IChapter currentChapter = mTxtReaderView.getCurrentChapter();
                        if (currentChapter != null) {
                            mChapterListPop.setCurrentIndex(currentChapter.getIndex());
                            mChapterListPop.notifyDataSetChanged();
                        }
                    }, 300);
                } else {
                    mChapterListPop.dismiss();

                }
            }
        };
        mChapterNameText.setOnClickListener(chapterMsgShowClick);
        mChapterMenuText.setOnClickListener(chapterMsgShowClick);
        mTopMenu.setOnClickListener(view -> {
            if (mChapterListPop.isShowing()) {
                mChapterListPop.dismiss();
            }
        });
    }

    protected void setSeekBarListener() {
        mMenuHolder.mSeekBar.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                mTxtReaderView.loadFromProgress(mMenuHolder.mSeekBar.getProgress());
                Gone(mChapterMsgView);
            }
            return false;
        });
        mMenuHolder.mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
                if (fromUser) {
                    mHandler.post(() -> onShowChapterMsg(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Gone(mChapterMsgView);
            }
        });

    }


    private void onShowChapterMsg(int progress) {
        if (mTxtReaderView != null && mChapterListPop != null) {
            IChapter chapter = mTxtReaderView.getChapterFromProgress(progress);
            if (chapter != null) {
                float p = (float) chapter.getStartIndex() / (float) mChapterListPop.getAllCharNum();
                if (p > 1) {
                    p = 1;
                }
                Show(mChapterMsgView);
                mChapterMsgName.setText(chapter.getTitle());
                mChapterMsgProgress.setText((int) (p * 100) + "%");
            }
        }
    }

    private void onCurrentSelectedText(String SelectedText) {
        String selectTextShow = String.format(getString(R.string.select_char_num), (SelectedText + "").length());
        mSelectedText.setText(selectTextShow);
        CurrentSelectedText = SelectedText;
    }

    private void onTextSettingUi(Boolean isBold) {
        int rs = isBold ? R.drawable.ic_bold_selected : R.drawable.ic_bold_normal;
        mMenuHolder.mBoldSelectedLayout.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), rs, null));
    }

    private void onPageSwitchSettingUi(int pageSwitchMode) {
        if (pageSwitchMode == TxtConfig.PAGE_SWITCH_MODE_SERIAL) {
            mMenuHolder.mTranslateSelectedLayout.setBackgroundResource(R.drawable.shape_menu_textsetting_selected);
            mMenuHolder.mCoverSelectedLayout.setBackgroundResource(R.drawable.shape_menu_textsetting_unselected);
            mMenuHolder.mShearSelectedLayout.setBackgroundResource(R.drawable.shape_menu_textsetting_unselected);
        } else if (pageSwitchMode == TxtConfig.PAGE_SWITCH_MODE_COVER) {
            mMenuHolder.mTranslateSelectedLayout.setBackgroundResource(R.drawable.shape_menu_textsetting_unselected);
            mMenuHolder.mCoverSelectedLayout.setBackgroundResource(R.drawable.shape_menu_textsetting_selected);
            mMenuHolder.mShearSelectedLayout.setBackgroundResource(R.drawable.shape_menu_textsetting_unselected);
        } else if (pageSwitchMode == TxtConfig.PAGE_SWITCH_MODE_SHEAR) {
            mMenuHolder.mTranslateSelectedLayout.setBackgroundResource(R.drawable.shape_menu_textsetting_unselected);
            mMenuHolder.mCoverSelectedLayout.setBackgroundResource(R.drawable.shape_menu_textsetting_unselected);
            mMenuHolder.mShearSelectedLayout.setBackgroundResource(R.drawable.shape_menu_textsetting_selected);
        }
    }

    private class TextSettingClickListener implements View.OnClickListener {

        public TextSettingClickListener() {
        }

        @Override
        public void onClick(View view) {
            if (FileExist) {
                Boolean Bold = mTxtReaderView.getTxtReaderContext().getTxtConfig().Bold;
                mTxtReaderView.setTextBold(!Bold);
                onTextSettingUi(!Bold);
            }
        }
    }

    private class SwitchSettingClickListener implements View.OnClickListener {
        private final int pageSwitchMode;

        public SwitchSettingClickListener(int pageSwitchMode) {
            this.pageSwitchMode = pageSwitchMode;
        }

        @Override
        public void onClick(View view) {
            if (FileExist) {
                if (pageSwitchMode == TxtConfig.PAGE_SWITCH_MODE_COVER) {
                    mTxtReaderView.setPageSwitchByCover();
                } else if (pageSwitchMode == TxtConfig.PAGE_SWITCH_MODE_SERIAL) {
                    mTxtReaderView.setPageSwitchByTranslate();
                }
                if (pageSwitchMode == TxtConfig.PAGE_SWITCH_MODE_SHEAR) {
                    mTxtReaderView.setPageSwitchByShear();
                }
                onPageSwitchSettingUi(pageSwitchMode);
            }
        }
    }


    private class ChapterChangeClickListener implements View.OnClickListener {
        private final Boolean Pre;

        public ChapterChangeClickListener(Boolean pre) {
            Pre = pre;
        }

        @Override
        public void onClick(View view) {
            if (Pre) {
                mTxtReaderView.jumpToPreChapter();
            } else {
                mTxtReaderView.jumpToNextChapter();
            }
        }
    }

    private class TextChangeClickListener implements View.OnClickListener {
        private final Boolean Add;

        public TextChangeClickListener(Boolean pre) {
            Add = pre;
        }

        @Override
        public void onClick(View view) {
            if (FileExist) {
                int textSize = mTxtReaderView.getTextSize();
                if (Add) {
                    if (textSize + 2 <= TxtConfig.MAX_TEXT_SIZE) {
                        mTxtReaderView.setTextSize(textSize + 2);
                        mMenuHolder.mTextSize.setText(textSize + 2 + "");
                    }
                } else {
                    if (textSize - 2 >= TxtConfig.MIN_TEXT_SIZE) {
                        mTxtReaderView.setTextSize(textSize - 2);
                        mMenuHolder.mTextSize.setText(textSize - 2 + "");
                    }
                }
            }
        }
    }

    private class StyleChangeClickListener implements View.OnClickListener {
        private final int BgColor;
        private final int TextColor;

        public StyleChangeClickListener(int bgColor, int textColor) {
            BgColor = bgColor;
            TextColor = textColor;
        }

        @Override
        public void onClick(View view) {
            if (FileExist) {
                mTxtReaderView.setStyle(BgColor, TextColor);
                mTopDecoration.setBackgroundColor(BgColor);
                mBottomDecoration.setBackgroundColor(BgColor);
            }
        }
    }

    protected void setBookName(String name) {
        mMenuHolder.mTitle.setText(name + "");
    }

    protected void Show(View... views) {
        for (View v : views) {
            v.setVisibility(View.VISIBLE);
        }
    }

    protected void Gone(View... views) {
        for (View v : views) {
            v.setVisibility(View.GONE);
        }
    }


    private Toast t;

    protected void toast(final String msg) {
        if (t != null) {
            t.cancel();
        }
        t = Toast.makeText(HwTxtPlayActivity.this, msg, Toast.LENGTH_SHORT);
        t.show();
    }

    protected static class MenuHolder {
        public TextView mTitle;
        public TextView mPreChapter;
        public TextView mNextChapter;
        public SeekBar mSeekBar;
        public View mTextSizeDel;
        public View mTextSizeAdd;
        public TextView mTextSize;
        public View mBoldSelectedLayout;
        public View mCoverSelectedLayout;
        public View mShearSelectedLayout;
        public View mTranslateSelectedLayout;
        public View mStyle1;
        public View mStyle2;
        public View mStyle3;
        public View mStyle4;
        public View mStyle5;
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavigationBarStatusBar();
    }

    private void hideNavigationBarStatusBar() {
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBattery.releaseReceiver();
        unregisterReceiver(receiver);
        exist();
    }

    public void BackClick(View view) {
        finish();
    }

    public void onCopyText(View view) {
        if (!TextUtils.isEmpty(CurrentSelectedText)) {
            toast("已经复制到粘贴板");
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setText(CurrentSelectedText + "");
        }
        onCurrentSelectedText("");
        mTxtReaderView.releaseSelectedState();
        Gone(ClipboardView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        exist();
    }

    protected boolean hasExisted = false;

    protected void exist() {
        if (!hasExisted) {
            ContentStr = null;
            hasExisted = true;
            if (mHandler != null) {
                mHandler.removeCallbacksAndMessages(null);
                mHandler = null;
            }
            if (mTxtReaderView != null) {
                mTxtReaderView.saveCurrentProgress();
                mTxtReaderView.onDestroy();
            }
            if (mTxtReaderView != null) {
                mTxtReaderView.getTxtReaderContext().Clear();
                mTxtReaderView = null;
            }
            if (mChapterListPop != null) {
                if (mChapterListPop.isShowing()) {
                    mChapterListPop.dismiss();
                }
                mChapterListPop.onDestroy();
                mChapterListPop = null;
            }
            mMenuHolder = null;
        }
    }
}
