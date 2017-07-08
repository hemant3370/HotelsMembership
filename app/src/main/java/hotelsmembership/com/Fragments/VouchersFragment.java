package hotelsmembership.com.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import hotelsmembership.com.Activities.VouchersActivity;
import hotelsmembership.com.Adapter.VoucherGistAdapter;
import hotelsmembership.com.Interfaces.CardVoucherClickListener;
import hotelsmembership.com.Model.Membership;
import hotelsmembership.com.Model.Vouchers.Voucher;
import hotelsmembership.com.R;
import hotelsmembership.com.databinding.VoucherItemBinding;

public class VouchersFragment extends Fragment implements CardVoucherClickListener {

    // TODO: Customize parameter argument names
    private static final String ARG_VOUCHERS = "vouchers";
    private static final String ARG_CARD = "cardNumber";
    private static final String ARG_MEMBERSHIP = "membership";
    private static final String ARG_INDEX = "index";
    private Listener mListener;
    List<Voucher> vouchers ;
    String cardNumber;
    Membership membership;
    // TODO: Customize parameters
    public static VouchersFragment newInstance(List<Voucher> vouchers, String cardNumber, Membership membership) {
        final VouchersFragment fragment = new VouchersFragment();
        final Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_VOUCHERS, (ArrayList<? extends Parcelable>) vouchers);
        args.putString(ARG_CARD,cardNumber);
        args.putParcelable(ARG_MEMBERSHIP, membership);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onVoucherGistClick(int item) {
        Intent voucherDetail = new Intent(getContext(), VouchersActivity.class);
        voucherDetail.putExtra(ARG_CARD, cardNumber);
        voucherDetail.putExtra(ARG_MEMBERSHIP, membership);
        voucherDetail.putParcelableArrayListExtra(ARG_VOUCHERS, (ArrayList<? extends Parcelable>) vouchers);
        voucherDetail.putExtra(ARG_INDEX, item);
        startActivity(voucherDetail);
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
        recyclerView.setAdapter(new VoucherGistAdapter(getArguments().<Voucher>getParcelableArrayList(ARG_VOUCHERS),this));
        cardNumber = getArguments().getString(ARG_CARD);
        membership = getArguments().getParcelable(ARG_MEMBERSHIP);
        vouchers = getArguments().getParcelableArrayList(ARG_VOUCHERS);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null) {
            mListener = (Listener) parent;
        } else {
            mListener = (Listener) context;
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    public interface Listener {
        void onVoucherClicked(Voucher voucher, String cardNumber, Membership membership);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        private final VoucherItemBinding itemBinding;
        public ViewHolder(VoucherItemBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;
            itemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mListener != null) {
                        mListener.onVoucherClicked(vouchers.get(getAdapterPosition()), cardNumber, membership);
                    }
                }
            });
        }

        public void bind(Voucher item) {
            itemBinding.setData(item);
            itemBinding.setMlistener(mListener);
            itemBinding.executePendingBindings();
        }
    }

    private class VoucherAdapter extends RecyclerView.Adapter<ViewHolder> {

        private  List<Voucher> vouchers;

        VoucherAdapter(List<Voucher> vouchers) {
            this.vouchers = vouchers;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater =
                    LayoutInflater.from(parent.getContext());
            VoucherItemBinding itemBinding =
                    VoucherItemBinding.inflate(layoutInflater, parent, false);
            return new ViewHolder(itemBinding);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
                holder.bind(vouchers.get(position));
        }

        @Override
        public int getItemCount() {
            return vouchers.size();
        }

    }

}
