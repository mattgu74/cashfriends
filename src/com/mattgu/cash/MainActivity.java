package com.mattgu.cash;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.protocol.BasicHttpContext;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.ApacheClient;
import retrofit.client.Client;
import retrofit.client.Response;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mattgu.cash.api.Api;
import com.mattgu.cash.models.User;
import com.mattgu.cash.models.UserTransactions;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final Button btnReload = (Button) findViewById(R.id.button_reload);
		final Button btnPay = (Button) findViewById(R.id.button_pay);
		final Button btnMyAccount = (Button) findViewById(R.id.button_my_account);
		
		Client client = new ApacheClient() {
		    final CookieStore cookieStore = new BasicCookieStore();
		    @Override
		    protected HttpResponse execute(HttpClient client, HttpUriRequest request) throws IOException {
		        // BasicHttpContext is not thread safe 
		        // CookieStore is thread safe
		        BasicHttpContext httpContext = new BasicHttpContext();
		        httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		        return client.execute(request, httpContext);
		    }
		};
		
		RestAdapter restAdapter = new RestAdapter.Builder()
		    .setEndpoint("http://192.168.1.30:5000")
		    .setClient(client)
		    .build();
		final Api service = restAdapter.create(Api.class);
		service.login("root", "root", new Callback<User>() {
			
			@Override
			public void success(User user, Response arg1) {
				Log.i("coucou", ""+user);
				service.getTransactions(new Callback<UserTransactions>() {

					@Override
					public void failure(RetrofitError arg0) {
						Log.e("coucou", ""+arg0);
					}

					@Override
					public void success(UserTransactions trans, Response arg1) {
						Log.i("coucou", ""+trans);
					}
				});
			}
			
			@Override
			public void failure(RetrofitError arg0) {
				Log.e("coucou", ""+arg0);
			}
		});
		
		
		View.OnClickListener handler = new View.OnClickListener(){
		
			public void onClick(View v) {
				Intent intentMain = new Intent(MainActivity.this , 
                        LoginActivity.class);
				if(v==btnReload) {
					intentMain.putExtra("action", 1);
				} else if(v==btnPay) {
					intentMain.putExtra("action", 2);
				} else {
					intentMain.putExtra("action", 3);
				}
				MainActivity.this.startActivity(intentMain);
			}
		};
		
		btnReload.setOnClickListener(handler);
		btnPay.setOnClickListener(handler);
		btnMyAccount.setOnClickListener(handler);
	}

}
