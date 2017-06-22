package hotelsmembership.com.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import javax.inject.Inject;

import hotelsmembership.com.Applications.Initializer;
import hotelsmembership.com.Interfaces.SmsListener;
import hotelsmembership.com.Model.BasicResponse;
import hotelsmembership.com.Model.Membership;
import hotelsmembership.com.Model.VerifyOTPPayload;
import hotelsmembership.com.Model.Vouchers.Voucher;
import hotelsmembership.com.R;
import hotelsmembership.com.Receiver.SmsBroadcastReceiver;
import hotelsmembership.com.Retrofit.Client.RestClient;
import hotelsmembership.com.Retrofit.Services.ApiInterface;
import hotelsmembership.com.Utils.OTPFinder;
import hotelsmembership.com.databinding.FragmentRedeemBinding;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RedeemFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RedeemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RedeemFragment extends BottomSheetDialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_MEMBERSHIP = "membership";
    private static final String ARG_VOUCHER = "voucher";
    final int GET_MY_PERMISSION = 3370;
    @Inject
    Retrofit mRetrofit;
    // TODO: Rename and change types of parameters
    FragmentRedeemBinding fragmentRedeemBinding;
    private OnFragmentInteractionListener mListener;
    private VerifyOTPPayload verifyPayload;
    Voucher voucher;
    Membership membership;
    private ProgressDialog progressBar;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public RedeemFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static RedeemFragment newInstance( Membership membership, Voucher voucher) {
        RedeemFragment fragment = new RedeemFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_MEMBERSHIP, membership);
        args.putParcelable(ARG_VOUCHER, voucher);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            membership = getArguments().getParcelable(ARG_MEMBERSHIP);
            voucher = getArguments().getParcelable(ARG_VOUCHER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentRedeemBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_redeem, container, false);
        ((Initializer) getActivity().getApplication()).getNetComponent().inject(this);
        verifyPayload = new VerifyOTPPayload(voucher.getVoucherNumber());
        fragmentRedeemBinding.setData(verifyPayload);
        fragmentRedeemBinding.setVoucher(voucher);
        fragmentRedeemBinding.verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentRedeemBinding.getData().getOtp() != null) {
                    progressBar = new ProgressDialog(getContext());
                    progressBar.setMessage("Verifying OTP...");
                    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressBar.setIndeterminate(true);
                    progressBar.show();
                    redeemVoucher();
                }
            }
        });

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.RECEIVE_SMS)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.RECEIVE_SMS},
                                GET_MY_PERMISSION);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }});
                builder.create().show();

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.RECEIVE_SMS},
                        GET_MY_PERMISSION);

            }
        }
        else{
            bindSMSListener();
        }
        return fragmentRedeemBinding.getRoot();
    }
    void redeemVoucher(){
        if (mRetrofit == null){
            mRetrofit = RestClient.getClient();
        }
        ApiInterface apiInterface = mRetrofit.create(ApiInterface.class);
        apiInterface.redeemVoucher(fragmentRedeemBinding.getData(),membership.getHotel().getHotelId(), membership.getAuthToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BasicResponse>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(BasicResponse basicResponse) {
                        if (basicResponse.getStatusCode() == 200 && basicResponse.getContent() != null ){

                            Toast.makeText(getContext(),basicResponse.getContent(),Toast.LENGTH_SHORT).show();
                            if (mListener != null) {
                                mListener.onRedemption(true,voucher.getVoucherNumber());
                            }
                        }
                        else {
                            Toast.makeText(getContext(),"Error " + basicResponse.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if (progressBar != null){progressBar.dismiss();}
                        Toast.makeText(getContext(),throwable.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        progressBar.dismiss();
                    }
                });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        compositeDisposable.clear();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onRedemption(Boolean success,String voucherNo);
    }
    void bindSMSListener(){
        SmsBroadcastReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                Log.d("Text",messageText);
                fragmentRedeemBinding.redeemOtp.setText(new OTPFinder().getOTPFrom(messageText));
                fragmentRedeemBinding.verifyBtn.performClick();
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GET_MY_PERMISSION && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            bindSMSListener();
        }
    }
}
