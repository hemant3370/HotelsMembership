package hotelsmembership.com.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import hotelsmembership.com.Applications.Initializer;
import hotelsmembership.com.Fragments.RedeemFragment;
import hotelsmembership.com.Fragments.VoucherDetails;
import hotelsmembership.com.Model.BasicResponse;
import hotelsmembership.com.Model.Membership;
import hotelsmembership.com.Model.RedeemPayload;
import hotelsmembership.com.Model.Vouchers.Voucher;
import hotelsmembership.com.R;
import hotelsmembership.com.Retrofit.Client.RestClient;
import hotelsmembership.com.Retrofit.Loader.RetrofitLoader;
import hotelsmembership.com.Retrofit.Services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class VouchersActivity extends AppCompatActivity implements VoucherDetails.OnFragmentInteractionListener,
        RedeemFragment.OnFragmentInteractionListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    @Inject
    Retrofit mRetrofit;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private static final String ARG_VOUCHERS = "vouchers";
    private static final String ARG_CARD = "cardNumber";
    private static final String ARG_MEMBERSHIP = "membership";

    List<Voucher> vouchers ;
    String cardNumber;
    Membership membership;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vouchers);
        ((Initializer) getApplication()).getNetComponent().inject(this);
        cardNumber = getIntent().getStringExtra(ARG_CARD);
        membership = getIntent().getParcelableExtra(ARG_MEMBERSHIP);
        vouchers = getIntent().getParcelableArrayListExtra(ARG_VOUCHERS);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),vouchers,cardNumber,membership);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

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

                    Toast.makeText(VouchersActivity.this,"OTP Sent",Toast.LENGTH_SHORT).show();
                    RedeemFragment redeemFragment = RedeemFragment.newInstance(voucher.getVoucherNumber(), membership);
                    redeemFragment.show(getSupportFragmentManager(),redeemFragment.getTag());

                }
                else {
                    Toast.makeText(VouchersActivity.this,"Error " + response.body().getMessage(),Toast.LENGTH_SHORT).show();
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

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        List<Voucher> vouchers ;
        String cardNumber;
        Membership membership;
        public SectionsPagerAdapter(FragmentManager fm, List<Voucher> vouchers,
                String cardNumber,
                Membership membership) {
            super(fm);
            this.vouchers = vouchers;
            this.cardNumber = cardNumber;
            this.membership = membership;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return VoucherDetails.newInstance(vouchers.get(position),cardNumber,membership);
        }

        @Override
        public int getCount() {
            return vouchers.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Vouchers";
        }
    }
}
