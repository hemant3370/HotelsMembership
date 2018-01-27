package loyaltywallet.com.Activities;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import loyaltywallet.com.Applications.Initializer;
import loyaltywallet.com.Fragments.MembershipsFragment;
import loyaltywallet.com.Fragments.RoomReservation;
import loyaltywallet.com.Model.Membership;
import loyaltywallet.com.Model.Vouchers.Voucher;
import loyaltywallet.com.R;
import loyaltywallet.com.databinding.MembershipDetailBinding;
import retrofit2.Retrofit;

public class CardActivity extends AppCompatActivity implements RoomReservation.OnFragmentInteractionListener {

    private static final String ARG_PARAM_MEMBERSHIP = "membership";
    private static final String ARG_VOUCHERS = "vouchers";
    private static final String ARG_CARD = "cardNumber";

    Membership membership;
    String cardNumber;
    List<Voucher> vouchers;
    private MembershipsFragment.OnListFragmentInteractionListener mListener;
    MembershipDetailBinding membershipDetailBinding;
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
        }
        else{
            membership = ((Initializer) getApplication()).getCardContext().getMembership();
            cardNumber = membership.getCardNumber();
            vouchers = ((Initializer) getApplication()).getCardContext().getVouchers();
        }
        setup();
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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
