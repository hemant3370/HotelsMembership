package hotelsmembership.com.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hotelsmembership.com.Adapter.VoucherPickerAdapter;
import hotelsmembership.com.Applications.Initializer;
import hotelsmembership.com.Interfaces.VoucherPicker;
import hotelsmembership.com.R;

public class VoucherPickerFragment extends BottomSheetDialogFragment  {

    // TODO: Customize parameter argument names
// TODO: Customize parameter argument names
    private static final String ARG_TYPE = "column-count";
    // TODO: Customize parameters
    private String mType;
    private VoucherPicker mListener;

    public void setmListener(VoucherPicker mListener) {
        this.mListener = mListener;
    }

    public static VoucherPickerFragment newInstance(String type) {
        VoucherPickerFragment fragment = new VoucherPickerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_voucher_list_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(new VoucherPickerAdapter(((Initializer) getActivity().getApplication()).getCardContext().getVouchers(), mListener, mType));
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
