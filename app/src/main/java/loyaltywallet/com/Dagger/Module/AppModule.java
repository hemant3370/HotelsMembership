package loyaltywallet.com.Dagger.Module;

import android.app.Application;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

/**
 * Created by HemantSingh on 6/20/2016.
 */
@Module
public class AppModule {

    private Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }


}
