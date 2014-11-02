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
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

/**
 * Created by Huteri on 10/16/2014.
 */
public interface Api {

    // thread

    @GET("/thread.php?action=get_all_threads")
    void getThreads(@Query("categoryId") String categoryId, @Query("sortPopular") int sortPopular, Callback<ThreadCallback> callback);

    @FormUrlEncoded
    @POST("/thread.php?action=submit_thread")
    void submitThread(@Field("userId") int userId, @Field("api") String api, @Field("androidId") String androidId, @Field("categoryId") int categoryId, @Field("title") String title, @Field("text") String text, Callback<BaseCallback> callback);

    // post

    @GET("/post.php?action=get_posts")
    void getPosts(@Query("threadId") int threadId, @Query("limit") int limit, @Query("p") int page, Callback<PostCallback> callback);

    @FormUrlEncoded
    @POST("/post.php?action=submit_post")
    void submitPost(@Query("threadId") int threadId, @Field("userId") int userId, @Field("api") String api,@Field("androidId") String androidId, @Field("text") String text, Callback<BaseCallback> callback);

    // category

    @GET("/category.php?action=get_categories")
    void getCategories(Callback<CategoryCallback> callback);

    // user

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
    @POST("/user.php?action=edit_profile")
    void editProfile(@Field("userId") int userId, @Field("api") String api, @Field("androidId") String androidId, @Field("fullName") String fullName, @Field("email") String email, @Field("aboutMe") String aboutMe, Callback<BaseCallback> callback);

    @Multipart
    @POST("/user.php?action=upload_profile")
    void uploadPic(@Query("api") String api, @Query("userId") int userId, @Part("image") TypedFile avatar, Callback<BaseCallback> callback);
  }
