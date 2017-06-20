package hotelsmembership.com.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import hotelsmembership.com.Adapter.VoucherGistAdapter;
import hotelsmembership.com.Fragments.MembershipsFragment;
import hotelsmembership.com.Interfaces.CardVoucherClickListener;
import hotelsmembership.com.Model.Membership;
import hotelsmembership.com.Model.Vouchers.Voucher;
import hotelsmembership.com.R;
import hotelsmembership.com.databinding.MembershipDetailBinding;
import retrofit2.Retrofit;

public class CardActivity extends AppCompatActivity implements CardVoucherClickListener {

    private static final String ARG_PARAM_MEMBERSHIP = "membership";
    private static final String ARG_VOUCHERS = "vouchers";
    private static final String ARG_CARD = "cardNumber";
    private static final String ARG_INDEX = "index";
    Membership membership;
    String cardNumber;
    List<Voucher> vouchers;
    private MembershipsFragment.OnListFragmentInteractionListener mListener;
    MembershipDetailBinding membershipDetailBinding;
    @BindView(R.id.voucher_recy)
    RecyclerView recyclerView;
    @Inject
    Retrofit mRetrofit;
    @BindView(R.id.progressBar)
    ProgressBar progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        membershipDetailBinding = DataBindingUtil.setContentView(this,R.layout.activity_card);
        ButterKnife.bind(this);
        if (getIntent().getStringExtra(ARG_CARD) != null) {
            membership = getIntent().getParcelableExtra(ARG_PARAM_MEMBERSHIP);
            cardNumber = getIntent().getStringExtra(ARG_CARD);
            vouchers = getIntent().getParcelableArrayListExtra(ARG_VOUCHERS);
            setup();
        }
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    void setup(){
        setTitle(membership.getHotel().getHotelName());
        if (membership.getCardType().equals("")) {
            membershipDetailBinding.setImageUrl(membership.getHotel().getCardsImageURLs().getGold());
        } else {
            membershipDetailBinding.setImageUrl(membership.getCardType().equals("G") ? membership.getHotel().getCardsImageURLs().getGold() : membership.getHotel().getCardsImageURLs().getSilver());
        }
        membershipDetailBinding.setData(membership);
        recyclerView.setAdapter(new VoucherGistAdapter(vouchers,this));
    }

    public void callForTableBooking(View view) {
        String[] tokens = membership.getHotel().getPhoneNumbers().getTableResevation().replace("|",",").split(",");
        if (tokens.length > 1) {
            chooseNumberToCall(tokens);
        }
        else {
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",membership.getHotel().getPhoneNumbers().getTableResevation() , null)));
        }
    }
    void chooseNumberToCall(final String[] numbers){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Call to Book");
        builder.setItems(numbers, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",numbers[item] , null)));
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void callForRoomBooking(View view) {
        String[] tokens = membership.getHotel().getPhoneNumbers().getRoomResevation().replace("|",",").split(",");
        if (tokens.length > 1) {
            chooseNumberToCall(tokens);
        } else {
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", membership.getHotel().getPhoneNumbers().getRoomResevation(), null)));
        }
    }

    @Override
    public void onVoucherGistClick(int item) {
        Intent voucherDetail = new Intent(CardActivity.this, VouchersActivity.class);
        voucherDetail.putExtra(ARG_CARD, cardNumber);
        voucherDetail.putExtra(ARG_PARAM_MEMBERSHIP, membership);
        voucherDetail.putParcelableArrayListExtra(ARG_VOUCHERS, (ArrayList<? extends Parcelable>) vouchers);
        voucherDetail.putExtra(ARG_INDEX, item);
        startActivity(voucherDetail);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
