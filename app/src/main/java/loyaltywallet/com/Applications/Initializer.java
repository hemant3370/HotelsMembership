package loyaltywallet.com.Applications;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

import com.crashlytics.android.Crashlytics;
import loyaltywallet.com.Dagger.Component.DaggerNetComponent;
import loyaltywallet.com.Dagger.Component.NetComponent;
import loyaltywallet.com.Dagger.Module.NetModule;
import loyaltywallet.com.Model.CardContext;
import io.fabric.sdk.android.Fabric;


/**
 * Created by Hemant Singh on 18/04/16.
 */
public class Initializer extends Application {

    private static Initializer sInstance;

    public DisplayMetrics metrics;
  ;
    public static Initializer getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static void saveToPreferences(Context context, String preferenceName, boolean preferenceValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String preferenceName, String defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getString(preferenceName, defaultValue);
    }

    public static boolean readFromPreferences(Context context, String preferenceName, boolean defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getBoolean(preferenceName, defaultValue);
    }
    public DisplayMetrics getMetrics() {
        metrics = new DisplayMetrics();

        metrics = getResources().getDisplayMetrics();
        return metrics;
    }
    CardContext cardContext;

    public CardContext getCardContext() {
        return cardContext;
    }

    public void setCardContext(CardContext cardContext) {
        this.cardContext = cardContext;
    }

    private NetComponent mNetComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        //Dagger
        mNetComponent = DaggerNetComponent.builder()
                .netModule(new NetModule(getApplicationContext()))
                .build();
                sInstance = this;

    }
    public NetComponent getNetComponent() {
        return mNetComponent;
    }
}
