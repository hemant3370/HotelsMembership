package loyaltywallet.com.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import loyaltywallet.com.Applications.Initializer;
import loyaltywallet.com.Fragments.RedeemFragment;
import loyaltywallet.com.Fragments.VoucherDetails;
import loyaltywallet.com.Model.BasicResponse;
import loyaltywallet.com.Model.Membership;
import loyaltywallet.com.Model.RedeemPayload;
import loyaltywallet.com.Model.Vouchers.Voucher;
import loyaltywallet.com.R;
import loyaltywallet.com.Retrofit.Client.RestClient;
import loyaltywallet.com.Retrofit.Services.ApiInterface;
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
    final int GET_MY_PERMISSION = 3370;
    private static final String ARG_VOUCHERS = "vouchers";
    private static final String ARG_CARD = "cardNumber";
    private static final String ARG_MEMBERSHIP = "membership";
    private static final String ARG_INDEX = "index";
    private CompositeDisposable compositeDisposable =
            new CompositeDisposable();
    List<Voucher> vouchers;
    String cardNumber;
    Membership membership;
    @BindView(R.id.progressBar)
    ProgressBar progressDialog;
    RedeemFragment redeemFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vouchers);
        ButterKnife.bind(this);
        ((Initializer) getApplication()).getNetComponent().inject(this);
        cardNumber = getIntent().getStringExtra(ARG_CARD);
        membership = getIntent().getParcelableExtra(ARG_MEMBERSHIP);
        vouchers = getIntent().getParcelableArrayListExtra(ARG_VOUCHERS);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImageView imageView = (ImageView) toolbar.findViewById(R.id.toolbar_logo);
//        toolbar.addView(imageView,
//                new Toolbar.LayoutParams(150, 150, Gravity.END));
        Glide.with(this).load(membership.getHotel().getHotelLogoURL()).skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade().into(imageView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setSubtitle(cardNumber);
        getSupportActionBar().setTitle(membership.getHotel().getHotelName());
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),vouchers,cardNumber,membership);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setCurrentItem(getIntent().getIntExtra(ARG_INDEX,0));
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECEIVE_SMS)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.sms_permission);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            VouchersActivity.this.requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS,Manifest.permission.READ_SMS},
                                    GET_MY_PERMISSION);
                        }
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }});
                builder.create().show();

            } else {

                // No explanation needed, we can request the permission.

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    this.requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS,Manifest.permission.READ_SMS},
                            GET_MY_PERMISSION);
                }

            }
        }
    }

    @Override
    public void onRedeemClick(Voucher voucher, String cardNumber, Membership membership) {

        requestOTP(voucher,cardNumber, membership);
    }

    void requestOTP(final Voucher voucher, String cardNumber, final Membership membership) {
        progressDialog.setVisibility(View.VISIBLE);
        if (mRetrofit == null) {
            mRetrofit = RestClient.getClient();
        }
        ApiInterface apiInterface = mRetrofit.create(ApiInterface.class);
        apiInterface.sendOTP(new RedeemPayload(cardNumber, voucher.getVoucherNumber()), membership.getHotel().getHotelId(), membership.getAuthToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BasicResponse>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(BasicResponse basicResponse) {
                        if (basicResponse.getStatusCode() == 200) {
                            Toast.makeText(VouchersActivity.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                             redeemFragment = RedeemFragment.newInstance(membership, voucher);
                            redeemFragment.show(getSupportFragmentManager(), redeemFragment.getTag());
                            progressDialog.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(VouchersActivity.this, "Error " + basicResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Toast.makeText(VouchersActivity.this, "Error " + throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onRedemption(Boolean success, String voucherNo) {
        if(redeemFragment != null) redeemFragment.dismissAllowingStateLoss();
        if (success){
            for (Voucher voucher : vouchers){
                if (voucher.getVoucherNumber().equals(voucherNo)){
                    voucher.setStatus("R");
                }
            }
            for (Voucher voucher : Voucher.sortedVouchers(((Initializer) getApplication()).getCardContext().getVouchers())){
                if (voucher.getVoucherNumber().equals(voucherNo)){
                    voucher.setStatus("R");
                }
            }
            int index = mViewPager.getCurrentItem();
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),vouchers,cardNumber,membership);
            mViewPager.setAdapter(mSectionsPagerAdapter);
            mViewPager.setCurrentItem(index);
            AlertDialog.Builder builder = new AlertDialog.Builder(VouchersActivity.this,R.style.MyDialogTheme);
            builder.setTitle("Hey!!");
            builder.setMessage("Voucher Number " + voucherNo + " successfully redeemed.");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
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

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
