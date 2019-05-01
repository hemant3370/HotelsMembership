package loyaltywallet.com.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import loyaltywallet.com.Applications.Initializer;
import loyaltywallet.com.Model.BasicResponse;
import loyaltywallet.com.Model.Membership;
import loyaltywallet.com.Model.VerifyOTPPayload;
import loyaltywallet.com.Model.Vouchers.Voucher;
import loyaltywallet.com.R;
import loyaltywallet.com.Retrofit.Client.RestClient;
import loyaltywallet.com.Retrofit.Services.ApiInterface;
import loyaltywallet.com.databinding.FragmentRedeemBinding;
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
    

}
