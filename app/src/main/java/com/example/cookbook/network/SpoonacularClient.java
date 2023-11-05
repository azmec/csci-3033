package com.example.cookbook.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Singleton client connected to the spoonacular web API.
 *
 * @author {Carlos Aldana Lira}
 */
public class SpoonacularClient {
	private static final String BASE_URL = "https://api.spoonacular.com/";
	private static SpoonacularClient instance;
	private final SpoonacularService api;

	/**
	 * Default, parameterless constructor.
	 */
	private SpoonacularClient() {
		HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
		loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

		OkHttpClient client = new OkHttpClient.Builder()
			.addInterceptor(loggingInterceptor)
			.build();
		Retrofit retrofit = new Retrofit.Builder()
			.baseUrl(BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.client(client)
			.addCallAdapterFactory(RxJava3CallAdapterFactory.create())
			.build();

		api = retrofit.create(SpoonacularService.class);
	}

	/**
	 * Return the single instance of the client.
	 *
	 * @return The single client instance.
	 */
	public static synchronized SpoonacularClient getInstance() {
		if (instance == null)
			instance = new SpoonacularClient();

		return instance;
	}

	/**
	 * Return a reference to the API.
	 *
	 * @see SpoonacularService
	 * @return A reference to the API.
	 */
	public SpoonacularService getApi() {
		return api;
	}
}
