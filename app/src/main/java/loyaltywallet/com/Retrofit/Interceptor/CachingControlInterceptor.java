package loyaltywallet.com.Retrofit.Interceptor;

import java.io.IOException;

import loyaltywallet.com.Applications.Initializer;
import loyaltywallet.com.Utils.ConnectivityUtil;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hemantsingh on 18/06/17.
 */

public class CachingControlInterceptor  implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        // Add Cache Control only for GET methods
        if (request.method().equals("GET")) {
            if (new ConnectivityUtil(Initializer.getAppContext()).connected()) {
                // 1 day
                request = request.newBuilder()
                        .header("Cache-Control", "only-if-cached")
                        .build();
            } else {
                // 4 weeks stale
                request = request.newBuilder()
                        .header("Cache-Control", "public, max-stale=2419200")
                        .build();
            }
        }

        Response originalResponse = chain.proceed(request);
        return originalResponse.newBuilder()
                .header("Cache-Control", "max-age=600")
                .build();
    }
}
