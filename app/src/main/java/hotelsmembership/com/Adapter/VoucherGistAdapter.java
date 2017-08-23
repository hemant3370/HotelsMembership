package hotelsmembership.com.Adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import hotelsmembership.com.Interfaces.CardVoucherClickListener;
import hotelsmembership.com.Model.Vouchers.Voucher;
import hotelsmembership.com.databinding.VouchergistItemBinding;

/**
 * Created by hemantsingh on 18/06/17.
 */

public class VoucherGistAdapter extends RecyclerView.Adapter<VoucherGistAdapter.ViewHolder> {

    private final List<Voucher> mValues;
    private final CardVoucherClickListener mListener;

    public VoucherGistAdapter(List<Voucher> items, CardVoucherClickListener listener) {

        mValues = items;
        mListener = listener;
    }

    @Override
    public VoucherGistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        VouchergistItemBinding itemBinding =
                VouchergistItemBinding.inflate(layoutInflater, parent, false);
        return new VoucherGistAdapter.ViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(final VoucherGistAdapter.ViewHolder holder, int position) {
        holder.bind(mValues.get(position),position);
        Voucher voucher = mValues.get(position);
        holder.itemBinding.getRoot().setAlpha(voucher.getStatus().equals("Redeemed") ? (float) 0.5 : (float) 1.0);
        holder.itemBinding.voucherStatus.setTextColor(voucher.getStatus().equals("Redeemed") ? Color.RED : Color.GREEN );
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final VouchergistItemBinding itemBinding;
        public ViewHolder(VouchergistItemBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;
        }

        public void bind(Voucher item, int position) {
            itemBinding.setData(item);
            itemBinding.setMlistener(mListener);
            itemBinding.setIndex(position);
            itemBinding.executePendingBindings();
        }
    }

}
