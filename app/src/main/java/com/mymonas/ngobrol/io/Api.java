package com.mymonas.ngobrol.io;

import com.mymonas.ngobrol.io.model.BaseCallback;
import com.mymonas.ngobrol.io.model.CategoryCallback;
import com.mymonas.ngobrol.io.model.PostCallback;
import com.mymonas.ngobrol.io.model.ThreadCallback;
import com.mymonas.ngobrol.io.model.UserLoginCallback;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by Huteri on 10/16/2014.
 */
public interface Api {

    @GET("/thread.php?action=get_all_threads")
    void getThreads(@Query("categoryId") String categoryId, Callback<ThreadCallback> callback);

    @GET("/post.php?action=get_posts")
    void getPosts(@Query("threadId") int threadId, @Query("limit") int limit, @Query("p") int page, Callback<PostCallback> callback);

    @GET("/category.php?action=get_categories")
    void getCategories(Callback<CategoryCallback> callback);

    @FormUrlEncoded
    @POST("/user.php?action=register")
    void regUser(@Field("username") String username, @Field("password") String password, Callback<BaseCallback> callback);

    @FormUrlEncoded
    @POST("/user.php?action=login")
    void logUser(@Field("username") String username, @Field("password") String password, @Field("androidName") String androidName, @Field("androidId") String androidId, Callback<UserLoginCallback> callback);

    @FormUrlEncoded
    @POST("/user.php?action=logout")
    void logoutUser(@Field("userId") int userId, @Field("api") String api, Callback<BaseCallback> callback);

    @FormUrlEncoded
    @POST("/post.php?action=submit_post")
    void submitPost(@Query("threadId") int threadId, @Field("userId") int userId, @Field("api") String api,@Field("androidId") String androidId, @Field("text") String text, Callback<BaseCallback> callback);

}
