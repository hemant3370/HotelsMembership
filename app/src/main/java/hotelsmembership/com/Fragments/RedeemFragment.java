package hotelsmembership.com.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import javax.inject.Inject;

import hotelsmembership.com.Applications.Initializer;
import hotelsmembership.com.Model.BasicResponse;
import hotelsmembership.com.Model.Membership;
import hotelsmembership.com.Model.VerifyOTPPayload;
import hotelsmembership.com.R;
import hotelsmembership.com.Retrofit.Client.RestClient;
import hotelsmembership.com.Retrofit.Loader.RetrofitLoader;
import hotelsmembership.com.Retrofit.Services.ApiInterface;
import hotelsmembership.com.databinding.FragmentRedeemBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
    private static final String ARG_PARAM1 = "voucherNo";
    private static final String ARG_MEMBERSHIP = "membership";
    @Inject
    Retrofit mRetrofit;
    // TODO: Rename and change types of parameters
    private String voucherNo;
    FragmentRedeemBinding fragmentRedeemBinding;
    private OnFragmentInteractionListener mListener;
    private VerifyOTPPayload verifyPayload;
    Membership membership;
    private ProgressDialog progressBar;

    public RedeemFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static RedeemFragment newInstance(String param1, Membership membership) {
        RedeemFragment fragment = new RedeemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putParcelable(ARG_MEMBERSHIP, membership);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            voucherNo = getArguments().getString(ARG_PARAM1);
            membership = getArguments().getParcelable(ARG_MEMBERSHIP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentRedeemBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_redeem, container, false);
        ((Initializer) getActivity().getApplication()).getNetComponent().inject(this);
        verifyPayload = new VerifyOTPPayload(voucherNo);
        fragmentRedeemBinding.setData(verifyPayload);
        fragmentRedeemBinding.verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar=new ProgressDialog(getContext());
                progressBar.setMessage("Verifying OTP...");
                progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressBar.setIndeterminate(true);
                progressBar.show();
                redeemVoucher();
            }
        });
        return fragmentRedeemBinding.getRoot();
    }
    void redeemVoucher(){
        if (mRetrofit == null){
            mRetrofit = RestClient.getClient();
        }
        ApiInterface apiInterface = mRetrofit.create(ApiInterface.class);
        Call<BasicResponse> call = apiInterface.redeemVoucher(fragmentRedeemBinding.getData(),membership.getHotel().getHotelId(), membership.getAuthToken());
        RetrofitLoader.load(getContext(), getActivity().getLoaderManager(), fragmentRedeemBinding.getData().hashCode(), call, new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, final Response<BasicResponse> response) {
                progressBar.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().getContent() != null ){

                    Toast.makeText(getContext(),response.body().getContent(),Toast.LENGTH_SHORT).show();
                    if (mListener != null) {

                    }
                }
                else {
                    Toast.makeText(getContext(),"Error " + response.body().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                if (progressBar != null){progressBar.dismiss();}
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
        void onFragmentInteraction(Uri uri);
    }
}
