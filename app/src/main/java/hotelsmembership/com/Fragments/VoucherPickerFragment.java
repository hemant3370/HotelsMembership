package hotelsmembership.com.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import hotelsmembership.com.Adapter.VoucherPickerAdapter;
import hotelsmembership.com.Interfaces.VoucherPicker;
import hotelsmembership.com.Model.Vouchers.Voucher;
import hotelsmembership.com.R;

public class VoucherPickerFragment extends BottomSheetDialogFragment  {

    // TODO: Customize parameter argument names
// TODO: Customize parameter argument names
    private static final String ARG_Vouchers = "vouchers";
    // TODO: Customize parameters
    private List<Voucher> vouchers;
    private VoucherPicker mListener;
    public VoucherPickerAdapter voucherPickerAdapter;
    public void setmListener(VoucherPicker mListener) {
        this.mListener = mListener;
    }

    public static VoucherPickerFragment newInstance(List<Voucher> vouchers) {
        VoucherPickerFragment fragment = new VoucherPickerFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_Vouchers, (ArrayList<? extends Parcelable>) vouchers);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            vouchers = getArguments().getParcelableArrayList(ARG_Vouchers);
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_voucher_list_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.voucher_recy);
        Button noneButton = (Button) view.findViewById(R.id.none_button);
        noneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onVoucherPicked(null);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        voucherPickerAdapter = new VoucherPickerAdapter(vouchers, mListener);
        recyclerView.setAdapter(voucherPickerAdapter);
    }
    void resetData(){
        voucherPickerAdapter.notifyDataSetChanged();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

}
