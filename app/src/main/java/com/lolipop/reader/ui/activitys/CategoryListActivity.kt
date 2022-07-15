package com.lolipop.reader.ui.activitys

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.lolipop.reader.R
import com.lolipop.reader.databinding.ActivityCategoryListBinding
import com.lolipop.reader.extensions.viewBinding
import com.lolipop.reader.ui.viewmodel.CategoryListModel

/**
 * @author FengZhongChan
 * @date 2022/7/7 14:41
 */
class CategoryListActivity : AppCompatActivity() {
    private val binding: ActivityCategoryListBinding by viewBinding()
    private val viewModel: CategoryListModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_list)
        initView()
        initData()
    }

    private fun initView() {

    }

    private fun initData() {
        viewModel.getCategoryEnd()
    }
}