package com.lolipop.reader.ui.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import com.lolipop.reader.R;
import com.lolipop.reader.model.BookModel;

public class BookListAdapter extends BaseQuickAdapter<BookModel, BaseViewHolder> {
    public BookListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder viewHolder, BookModel bookModel) {
        viewHolder.setText(R.id.tv_name, bookModel.getName());
    }
}
