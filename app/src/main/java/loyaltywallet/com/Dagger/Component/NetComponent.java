package loyaltywallet.com.Dagger.Component;


import javax.inject.Singleton;

import dagger.Component;
import loyaltywallet.com.Activities.MainActivity;
import loyaltywallet.com.Activities.VouchersActivity;
import loyaltywallet.com.Dagger.Module.AppModule;
import loyaltywallet.com.Dagger.Module.NetModule;
import loyaltywallet.com.Fragments.AddMembership;
import loyaltywallet.com.Fragments.RedeemFragment;


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
