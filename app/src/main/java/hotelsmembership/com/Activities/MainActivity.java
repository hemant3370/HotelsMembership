package hotelsmembership.com.Activities;

import android.arch.persistence.room.Room;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import hotelsmembership.com.Applications.Initializer;
import hotelsmembership.com.Fragments.AddMembership;
import hotelsmembership.com.Fragments.HomeFragment;
import hotelsmembership.com.Fragments.MembershipsFragment;
import hotelsmembership.com.Fragments.RedeemFragment;
import hotelsmembership.com.Fragments.VoucherDetails;
import hotelsmembership.com.Fragments.VoucherListDialogFragment;
import hotelsmembership.com.Model.AddCardPayload;
import hotelsmembership.com.Model.BasicResponse;
import hotelsmembership.com.Model.HotelsDatabase;
import hotelsmembership.com.Model.HotelsResponse;
import hotelsmembership.com.Model.Membership;
import hotelsmembership.com.Model.RedeemPayload;
import hotelsmembership.com.Model.Vouchers.Voucher;
import hotelsmembership.com.Model.Vouchers.VouchersResponse;
import hotelsmembership.com.R;
import hotelsmembership.com.Retrofit.Client.RestClient;
import hotelsmembership.com.Retrofit.Loader.RetrofitLoader;
import hotelsmembership.com.Retrofit.Services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,
AddMembership.OnFragmentInteractionListener, MembershipsFragment.OnListFragmentInteractionListener,
VoucherListDialogFragment.Listener, VoucherDetails.OnFragmentInteractionListener, RedeemFragment.OnFragmentInteractionListener{
    android.support.v4.app.FragmentTransaction fragmentTransaction;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;
    HotelsDatabase hotelsDatabase;
    @Inject
    Retrofit mRetrofit;
    @BindView(R.id.progressBar)
    ProgressBar progressDialog;
    @BindView(R.id.frame)
     FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ((Initializer) getApplication()).getNetComponent().inject(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        View header = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        invalidateOptionsMenu();
        setTitle("Home");

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, HomeFragment.newInstance("",""));
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.commit();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
       Call<HotelsResponse> call = apiInterface.getHotels();
       RetrofitLoader.load(MainActivity.this, this.getLoaderManager(), 1111, call, new Callback<HotelsResponse>() {
           @Override
           public void onResponse(Call<HotelsResponse> call, final Response<HotelsResponse> response) {
               if (response.code() == 200 ){
                   hotelsDatabase = Room.databaseBuilder(getApplicationContext(),
                           HotelsDatabase.class, "hotel-db").build();
                   new Thread(new Runnable() {
                       public void run() {
                           // a potentially  time consuming task
                           hotelsDatabase.daoAccess().insertMultipleListRecord(response.body().getContent());
                       }
                   }).start();
               }
               else {
                   Toast.makeText(MainActivity.this,"Error " + response.body().getMessage(),Toast.LENGTH_SHORT).show();
               }

           }

           @Override
           public void onFailure(Call<HotelsResponse> call, Throwable t) {

           }
       });
   }
   void getVouchers(final AddCardPayload payload, final Membership membership){
        progressDialog.setVisibility(View.VISIBLE);
        if (mRetrofit == null){
            mRetrofit = RestClient.getClient();
        }
        ApiInterface apiInterface = mRetrofit.create(ApiInterface.class);
        Call<VouchersResponse> call = apiInterface.getVouchers(payload,membership.getHotel().getHotelId());
        RetrofitLoader.load(MainActivity.this, this.getLoaderManager(), payload.hashCode(), call, new Callback<VouchersResponse>() {
            @Override
            public void onResponse(Call<VouchersResponse> call, final Response<VouchersResponse> response) {
                if (response.code() == 200 ){
                    if (response.body().getContent().size() > 0) {
                        VoucherListDialogFragment bottomSheetDialogFragment = VoucherListDialogFragment.newInstance(response.body().getContent(), payload.getCardNumber(), membership);
                        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                        progressDialog.setVisibility(View.INVISIBLE);
                    }
                    else{
                        progressDialog.setVisibility(View.INVISIBLE);
                        Toast.makeText(MainActivity.this, "No Voucher available to redeem",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, response.body().getMessage(),Toast.LENGTH_SHORT).show();
                    progressDialog.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onFailure(Call<VouchersResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (id == R.id.nav_home) {
            setTitle("Home");
            fragmentTransaction.replace(R.id.frame, HomeFragment.newInstance("",""));
        } else if (id == R.id.nav_mymembership) {
            setTitle("My Memberships");
            fragmentTransaction.replace(R.id.frame, MembershipsFragment.newInstance(1));
        } else if (id == R.id.nav_offers) {
            setTitle("Offers");
            fragmentTransaction.replace(R.id.frame, HomeFragment.newInstance("",""));
        } else if (id == R.id.nav_profile) {
            setTitle("My Profile");
            fragmentTransaction.replace(R.id.frame, HomeFragment.newInstance("",""));
        } else if (id == R.id.nav_contactus) {
            setTitle("Contact Us");
            fragmentTransaction.replace(R.id.frame, HomeFragment.newInstance("",""));
        }
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void onHomeItemClick(View view){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (view.getId()){
            case R.id.mymemberships:
                setTitle("My Memberships");
                fragmentTransaction.replace(R.id.frame, MembershipsFragment.newInstance(1)).addToBackStack(null);
                break;
            case R.id.addmembership:
                setTitle("Add Membership");
                fragmentTransaction.replace(R.id.frame, AddMembership.newInstance("","")).addToBackStack(null);
                break;
            case R.id.myprofile:
                setTitle("My Profile");
                fragmentTransaction.replace(R.id.frame, AddMembership.newInstance("","")).addToBackStack(null);
                break;
            case R.id.offers:
                setTitle("Offers");
                fragmentTransaction.replace(R.id.frame, AddMembership.newInstance("","")).addToBackStack(null);
                break;
            default:
                return;
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.commit();
    }

    @Override
    public void onMembershipAdded() {
        setTitle("Home");
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, HomeFragment.newInstance("",""));
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.commit();
    }

    @Override
    public void onListFragmentInteraction(Membership item) {
        getVouchers(new AddCardPayload(item.getCardNumber(),"31/05/2018", item.getPhoneNumber()),item);
    }


    @Override
    public void onRedeemClick(Voucher voucher, String cardNumber, Membership membership) {
        requestOTP(voucher,cardNumber, membership);
    }
    void requestOTP(final Voucher voucher, String cardNumber, final Membership membership){

        if (mRetrofit == null){
            mRetrofit = RestClient.getClient();
        }
        ApiInterface apiInterface = mRetrofit.create(ApiInterface.class);
        Call<BasicResponse> call = apiInterface.sendOTP(new RedeemPayload(cardNumber, voucher.getVoucherNumber()),membership.getHotel().getHotelId());
        RetrofitLoader.load(this, getLoaderManager(), voucher.hashCode() + cardNumber.hashCode(), call, new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, final Response<BasicResponse> response) {
                if (response.code() == 200 && response.body() != null && response.body().getContent() != null ){

                    Toast.makeText(MainActivity.this,"OTP Sent",Toast.LENGTH_SHORT).show();
                    RedeemFragment redeemFragment = RedeemFragment.newInstance(voucher.getVoucherNumber(), membership);
                    redeemFragment.show(getSupportFragmentManager(),redeemFragment.getTag());

                }
                else {
                    Toast.makeText(MainActivity.this,"Error " + response.body().getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
}
