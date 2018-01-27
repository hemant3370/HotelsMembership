package loyaltywallet.com.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import loyaltywallet.com.Model.Membership;
import loyaltywallet.com.Model.Vouchers.Voucher;
import loyaltywallet.com.R;
import loyaltywallet.com.databinding.VoucherDetailsBinding;

public class VoucherDetails extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_MEMBERSHIP = "membership";

    // TODO: Rename and change types of parameters
    private Voucher voucher;
    String cardNumber;
    Membership membership;
    VoucherDetailsBinding voucherDetailsBinding;
    private OnFragmentInteractionListener mListener;

    public VoucherDetails() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static VoucherDetails newInstance(Voucher voucher, String cardNumber, Membership membership) {
        VoucherDetails fragment = new VoucherDetails();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, voucher);
        args.putString(ARG_PARAM2, cardNumber);
        args.putParcelable(ARG_MEMBERSHIP, membership);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            voucher = getArguments().getParcelable(ARG_PARAM1);
            cardNumber = getArguments().getString(ARG_PARAM2);
            membership = getArguments().getParcelable(ARG_MEMBERSHIP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        voucherDetailsBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_voucher_details, container, false);
        voucherDetailsBinding.setData(voucher);
        if (voucher.getStatus().equals("Redeemed")){
            voucherDetailsBinding.redeemBtn.setText("Redeemed");
            voucherDetailsBinding.redeemBtn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.redeemed));
        }
        voucherDetailsBinding.tncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String[] terms = new String[voucher.getVoucherCategory().getTermsAndConditions().size()];
                for (int i = 0; i < terms.length; i++){
                    terms[i] = (i+1) + ". " + voucher.getVoucherCategory().getTermsAndConditions().get(i).getTermDescription();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
                adapter.addAll(terms);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Terms and Conditions");
                builder.setAdapter(adapter, null);
                final AlertDialog alert = builder.create();
                alert.setCancelable(false);
                alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alert.hide();
                    }
                });
                alert.show();
            }
        });
        voucherDetailsBinding.redeemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (voucher.getStatus().equals("Redeemed")){
                    Toast.makeText(getContext(),"Already redeemed!",Toast.LENGTH_SHORT).show();
                }
                else if (mListener != null  ) {
                    mListener.onRedeemClick(voucher,cardNumber, membership);
                }
            }
        });
        return voucherDetailsBinding.getRoot();
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onRedeemClick(Voucher voucher, String cardNumber, Membership membership);
    }
}
