package com.lolipop.reader.network;

import com.lolipop.reader.model.BaseListModel;
import com.lolipop.reader.model.BaseModel;
import com.lolipop.reader.model.Book;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author FengZhongChan
 * @date 2022/7/6 17:17
 */
public interface NetService {
    @GET("/app/open/api/category/getCategoryEnd")
    Observable<BaseModel<BaseListModel<Book>>> getCategoryEnd(
            @Query("pageNum") int pageNum,
            @Query("pageSize") int pageSize
    );
}
