package com.mattgu.cash.api;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

import com.mattgu.cash.models.Transaction;
import com.mattgu.cash.models.User;
import com.mattgu.cash.models.UserTransactions;

public interface Api {
	@FormUrlEncoded
	@POST("/api/user/login")
	User login(@Field("login") String login, @Field("password") String password);

	@FormUrlEncoded
	@POST("/api/user/login")
	void login(@Field("login") String login, @Field("password") String password, 
			Callback<User> callback);
	
	@GET("/api/user/transactions")
	void getTransactions(Callback<UserTransactions> callback);

	@FormUrlEncoded
	@POST("/api/user/transactions")
	void postTransaction(@Field("dst_usr") String dst_usr, @Field("dst_email") String dst_email, @Field("message") String message, @Field("amount") int amount,
			Callback<Transaction> callback);
	
	@GET("/user")
	void user(Callback<User> callback);
}
