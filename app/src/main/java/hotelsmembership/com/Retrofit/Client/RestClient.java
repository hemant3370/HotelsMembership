package hotelsmembership.com.Retrofit.Client;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import hotelsmembership.com.Constants;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Hemant Singh on 17/06/16.
 */
public class RestClient {
    private static final String BASE_URL = Constants.MyUrl.BASE_URL;
    private static Retrofit retrofit = null;

// set your desired log level

    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
