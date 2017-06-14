package hotelsmembership.com.Dagger.Component;


import javax.inject.Singleton;

import dagger.Component;
import hotelsmembership.com.Activities.MainActivity;
import hotelsmembership.com.Activities.VouchersActivity;
import hotelsmembership.com.Dagger.Module.AppModule;
import hotelsmembership.com.Dagger.Module.NetModule;
import hotelsmembership.com.Fragments.AddMembership;
import hotelsmembership.com.Fragments.RedeemFragment;


/**
 * Created by Hemant on 6/20/2016.
 */
@Singleton
@Component(modules = {NetModule.class, AppModule.class})
public interface NetComponent {
    void inject(MainActivity activity);
    void inject(VouchersActivity activity);
    void inject(AddMembership fragment);
    void inject(RedeemFragment fragment);
}
