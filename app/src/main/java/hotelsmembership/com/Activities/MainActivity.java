package hotelsmembership.com.Activities;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import hotelsmembership.com.Applications.Initializer;
import hotelsmembership.com.Fragments.AddMembership;
import hotelsmembership.com.Fragments.MembershipsFragment;
import hotelsmembership.com.Fragments.VoucherDetails;
import hotelsmembership.com.Fragments.VouchersFragment;
import hotelsmembership.com.Model.AddCardPayload;
import hotelsmembership.com.Model.AddMembershipResponse;
import hotelsmembership.com.Model.CardContext;
import hotelsmembership.com.Model.CardNumberPayload;
import hotelsmembership.com.Model.Hotel.Hotel;
import hotelsmembership.com.Model.HotelsDatabase;
import hotelsmembership.com.Model.HotelsResponse;
import hotelsmembership.com.Model.Membership;
import hotelsmembership.com.Model.OffersResponse;
import hotelsmembership.com.Model.VenuesResponse;
import hotelsmembership.com.Model.Vouchers.Voucher;
import hotelsmembership.com.Model.Vouchers.VouchersResponse;
import hotelsmembership.com.R;
import hotelsmembership.com.Retrofit.Client.RestClient;
import hotelsmembership.com.Retrofit.Services.ApiInterface;
import hotelsmembership.com.Utils.ConnectivityUtil;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function3;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,
AddMembership.OnFragmentInteractionListener, MembershipsFragment.OnListFragmentInteractionListener,
VouchersFragment.Listener{
    android.support.v4.app.FragmentTransaction fragmentTransaction;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
//    @BindView(R.id.nav_view) NavigationView navigationView;
    HotelsDatabase hotelsDatabase;
    @Inject
    Retrofit mRetrofit;
    @BindView(R.id.progressBar)
    ProgressBar progressDialog;
    @BindView(R.id.frame)
     FrameLayout frameLayout;

    private CompositeDisposable compositeDisposable =
            new CompositeDisposable();
    private static final String ARG_VOUCHERS = "vouchers";
    private static final String ARG_CARD = "cardNumber";
    private static final String ARG_MEMBERSHIP = "membership";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ((Initializer) getApplication()).getNetComponent().inject(this);

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//        View header = navigationView.getHeaderView(0);
//        navigationView.setNavigationItemSelectedListener(this);
        invalidateOptionsMenu();
        setTitle("My Memberships");
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, MembershipsFragment.newInstance(1));
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.commit();
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
        getHotels();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
   void getHotels(){

       if (mRetrofit == null){
           mRetrofit = RestClient.getClient();
       }
       ApiInterface apiInterface = mRetrofit.create(ApiInterface.class);
       apiInterface.getHotels()
                .subscribeOn(Schedulers.newThread())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(new Observer<HotelsResponse>() {
                   @Override
                   public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                   }

                   @Override
                   public void onNext(final HotelsResponse hotelsResponse) {
                       hotelsDatabase = Room.databaseBuilder(getApplicationContext(),
                               HotelsDatabase.class, "hotel-db").build();
                       new Thread(new Runnable() {
                           public void run() {
                               // a potentially  time consuming task
                                   hotelsDatabase.daoAccess().insertMultipleListRecord(hotelsResponse.getContent());
                           }
                       }).start();
                   }

                   @Override
                   public void onError(Throwable throwable) {
                       Toast.makeText(MainActivity.this, throwable.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                   }

                   @Override
                   public void onComplete() {

                   }
               });
   }
    public void addCard(final Hotel selectedHotel, AddCardPayload payload){
        progressDialog.setVisibility(View.VISIBLE);
        if (mRetrofit == null){
            mRetrofit = RestClient.getClient();
        }
        ApiInterface apiInterface = mRetrofit.create(ApiInterface.class);
        apiInterface.addMembership(payload, selectedHotel.getHotelId())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<AddMembershipResponse>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(final AddMembershipResponse addMembershipResponse) {
                        if (addMembershipResponse.getStatusCode() == 200 && addMembershipResponse.getContent() != null ){
                            new Thread(new Runnable() {
                                public void run() {
                                    // a potentially  time consuming task
                                    Membership membership = addMembershipResponse.getContent();
                                    membership.setHotel(selectedHotel);
                                    hotelsDatabase.daoAccess().insertOnlySingleRecord(membership);
                                }
                            }).start();
                            String url = "";
                            if (addMembershipResponse.getContent().getCardType().equals("")) {
                                url = selectedHotel.getCardsImageURLs().getGold();
                            } else {
                                url = addMembershipResponse.getContent().getCardType().equals("G") ?
                                        selectedHotel.getCardsImageURLs().getGold() :
                                        selectedHotel.getCardsImageURLs().getSilver();
                            }
                            Glide.with(MainActivity.this).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    progressDialog.setVisibility(View.INVISIBLE);
                                    onMembershipAdded();
                                    return true;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    progressDialog.setVisibility(View.INVISIBLE);
                                    onMembershipAdded();
                                    return true;
                                }
                            }).into(1080,1080);
                        }
                        else {
                            Toast.makeText(MainActivity.this,"Error " + addMembershipResponse.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        progressDialog.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),throwable.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    void getCardDetails(final AddCardPayload payload, final Membership membership){
        progressDialog.setVisibility(View.VISIBLE);
        if (mRetrofit == null){
            mRetrofit = RestClient.getClient();
        }
        ApiInterface apiInterface = mRetrofit.create(ApiInterface.class);

        Observable<VouchersResponse> vouchersResponseObservable =  apiInterface.getVouchers(payload,membership.getHotel().getHotelId(), membership.getAuthToken())
                 .subscribeOn(Schedulers.newThread())
                 .observeOn(AndroidSchedulers.mainThread());
       Observable<OffersResponse> offersResponseObservable =  apiInterface.getOffers(new CardNumberPayload(membership.getCardNumber()),membership.getHotel().getHotelId(), membership.getAuthToken())
               .subscribeOn(Schedulers.newThread())
               .observeOn(AndroidSchedulers.mainThread());
       Observable<VenuesResponse> venuesResponseObservable =  apiInterface.getVenues(new CardNumberPayload(membership.getCardNumber()),membership.getHotel().getHotelId(), membership.getAuthToken())
               .subscribeOn(Schedulers.newThread())
               .observeOn(AndroidSchedulers.mainThread());
       Observable<CardContext> combined = Observable.zip(vouchersResponseObservable, offersResponseObservable, venuesResponseObservable, new Function3<VouchersResponse, OffersResponse, VenuesResponse, CardContext>() {
           @Override
           public CardContext apply(VouchersResponse vouchersResponse, OffersResponse offersResponse, VenuesResponse venuesResponse) throws Exception {
               return new CardContext(membership,membership.getCardNumber(),vouchersResponse.getContent(),offersResponse.getContent(),venuesResponse.getContent());
           }
   });

       combined.subscribe(new Observer<CardContext>() {
                     @Override
                     public void onSubscribe(Disposable disposable) {
                         compositeDisposable.add(disposable);
                     }

           @Override
           public void onNext(CardContext value) {
               if (value != null) {
                   ((Initializer) getApplication()).setCardContext(value);
                   Intent i = new Intent(MainActivity.this, MembershipActivity.class);
                   startActivity(i);
               }
               else{
                   addCard(membership.getHotel(), new AddCardPayload(membership.getCardNumber(), membership.getCardExpiryDate(),
                           membership.getPhoneNumber()));
                   Toast.makeText(MainActivity.this, "Unable to load the Card. Please try again!",Toast.LENGTH_SHORT).show();
               }
           }

                     @Override
                     public void onError(Throwable throwable) {
                         Toast.makeText(MainActivity.this, throwable.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                         addCard(membership.getHotel(), new AddCardPayload(membership.getCardNumber(), membership.getCardExpiryDate(),
                                 membership.getPhoneNumber()));
                     }

                     @Override
                     public void onComplete() {
                         progressDialog.setVisibility(View.INVISIBLE);
                     }
                 });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //fab.setVisibility(View.VISIBLE);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (id == R.id.nav_home) {
            setTitle("Home");
            fragmentTransaction.replace(R.id.frame, MembershipsFragment.newInstance(1));
        } else if (id == R.id.nav_mymembership) {
            setTitle("My Memberships");
            fragmentTransaction.replace(R.id.frame, MembershipsFragment.newInstance(1));
        }
//        else if (id == R.id.nav_offers) {
//            setTitle("Offers");
//            fragmentTransaction.replace(R.id.frame, MembershipsFragment.newInstance(1));
//        } else if (id == R.id.nav_profile) {
//            setTitle("My Profile");
//            fragmentTransaction.replace(R.id.frame, MembershipsFragment.newInstance(1));
//        } else if (id == R.id.nav_contactus) {
//            setTitle("Contact Us");
//            fragmentTransaction.replace(R.id.frame, MembershipsFragment.newInstance(1));
//        }
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void onHomeItemClick(View view){

        switch (view.getId()){
            case R.id.mymemberships:
            case R.id.mymembership:
                setTitle("My Memberships");
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                        android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, MembershipsFragment.newInstance(1)).addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.addmembership:
                onAddClicked();
                break;
//            case R.id.myprofile:
//                setTitle("My Profile");
//                fragmentTransaction.replace(R.id.frame, MembershipsFragment.newInstance(1)).addToBackStack(null);
//                break;
//            case R.id.offers:
//                setTitle("Offers");
//                fragmentTransaction.replace(R.id.frame, MembershipsFragment.newInstance(1)).addToBackStack(null);
//                break;
            default:
                return;
        }

    }

    public void onMembershipAdded() {
        setTitle("My Memberships");
      //  fab.setVisibility(View.VISIBLE);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, MembershipsFragment.newInstance(1));
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.commit();
    }

    @Override
    public void onCardClicked(Membership item) {
        if(new ConnectivityUtil(this).connected()) {
            getCardDetails(new AddCardPayload(item.getCardNumber(), "31/05/2018", item.getPhoneNumber()), item);
        }
        else {
            Toast.makeText(MainActivity.this, "Please connect to Internet first.",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAddClicked() {
        if (new ConnectivityUtil(getApplicationContext()).connected()) {
            setTitle("Add Membership");
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                    android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, AddMembership.newInstance("", ""));
            fragmentTransaction.commit();
        }
        else{
            Toast.makeText(MainActivity.this, "Please connect to Internet first.",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCardFullScreenAction(Membership item) {
        Intent fullCard = new Intent(this, CardFullscreenActivity.class);
        fullCard.putExtra("membership", item);
        startActivity(fullCard);
    }

    @Override
    public void onVoucherClicked(Voucher voucher, String cardNumber, Membership membership) {
        getSupportFragmentManager().popBackStackImmediate();
        setTitle("Voucher Details");
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, VoucherDetails.newInstance(voucher,cardNumber, membership)).addToBackStack(null);
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
