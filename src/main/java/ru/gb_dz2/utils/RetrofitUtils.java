package ru.gb_dz2.utils;

import lombok.experimental.UtilityClass;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.time.Duration;

@UtilityClass
public class RetrofitUtils {
    static HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new Logger());

    public static Retrofit getRetrofit() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(Duration.ofMinutes(1l))
                .addInterceptor(logging.setLevel(HttpLoggingInterceptor.Level.BASIC))
                .build();
        return new Retrofit.Builder()
                .client(client)
                .baseUrl("http://80.78.248.82:8189/market/api/v1/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }
}
