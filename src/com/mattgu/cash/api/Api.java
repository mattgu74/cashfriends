package com.mattgu.cash.api;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

import com.mattgu.cash.models.User;

public interface Api {
	@FormUrlEncoded
	@POST("/api/user/login")
	User login(@Field("login") String login, @Field("password") String password);

	@FormUrlEncoded
	@POST("/api/user/login")
	void login(@Field("login") String login, @Field("password") String password, 
			Callback<User> callback);
}
