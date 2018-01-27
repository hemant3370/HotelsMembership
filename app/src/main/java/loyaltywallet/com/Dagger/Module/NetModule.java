package loyaltywallet.com.Dagger.Module;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import loyaltywallet.com.Constants;
import loyaltywallet.com.Retrofit.Interceptor.CachingControlInterceptor;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Hemant on 6/20/2016.
 */
@Module
public class NetModule {
    private String BASE_URL = Constants.MyUrl.BASE_URL;
    private Context context;
    public NetModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit()
    {
        long SIZE_OF_CACHE = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(new File(context.getCacheDir(), "http"), SIZE_OF_CACHE);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
                                      @Override
                                      public Response intercept(Chain chain) throws IOException {
                                          Request original = chain.request();

                                          Request request = original.newBuilder()
                                                  .header("Content-Type", "application/json")
                                                  .method(original.method(), original.body())
                                                  .build();

                                          return chain.proceed(request);
                                      }
                                  })
                .addNetworkInterceptor(new CachingControlInterceptor())
                .cache(cache);
//                .addInterceptor(logging);
//        .addNetworkInterceptor(new CachingControlInterceptor())
//                .cache(cache);
                OkHttpClient client = httpClient.build();
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:sssZ").create();
        Retrofit retrofit;
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();

        return retrofit;

    }




}
