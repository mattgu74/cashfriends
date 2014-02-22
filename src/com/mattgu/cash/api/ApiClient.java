package com.mattgu.cash.api;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.protocol.BasicHttpContext;

import retrofit.RestAdapter;
import retrofit.client.ApacheClient;
import retrofit.client.Client;

public class ApiClient
{
    private static ApiClient   _instance;
    private Client client;
    public Api service;
    
    private ApiClient()
    {
    	client = new ApacheClient() {
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
		    .setEndpoint("http://cash.mattgu.com")
		    .setClient(client)
		    .build();
		service = restAdapter.create(Api.class);
    }

    public static ApiClient getInstance()
    {
        if (_instance == null)
        {
            _instance = new ApiClient();
        }
        return _instance;
    }
    
    public static Api getService()
    {
        Object apiClient = ApiClient.getInstance();
        return ((ApiClient) apiClient)._getService();
    }
    
    public Api _getService()
    {
    	return service;
    }
}