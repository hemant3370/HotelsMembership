package hotelsmembership.com.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import hotelsmembership.com.Interfaces.VoucherPicker;
import hotelsmembership.com.Model.Vouchers.Voucher;
import hotelsmembership.com.databinding.VoucherPickerItemBinding;

/**
 * Created by hemantsingh on 18/06/17.
 */

public class VoucherPickerAdapter extends RecyclerView.Adapter<VoucherPickerAdapter.ViewHolder> {

    private final List<Voucher> mValues;
    private final VoucherPicker mListener;

    public VoucherPickerAdapter(List<Voucher> items, VoucherPicker listener, String type) {
        List<Voucher> sorted = new ArrayList<>();
        for (Voucher v :
                items) {
            if (!v.getStatus().equals("Redeemed") && v.getVoucherCategory().getCategoryType().equals(type)) {
                sorted.add(v);
            }
            }

        mValues = sorted;
        mListener = listener;
    }

    @Override
    public VoucherPickerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        VoucherPickerItemBinding itemBinding =
                VoucherPickerItemBinding.inflate(layoutInflater, parent, false);
        return new VoucherPickerAdapter.ViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(final VoucherPickerAdapter.ViewHolder holder, int position) {
        holder.bind(mValues.get(position),position);
        Voucher voucher = mValues.get(position);
        holder.itemBinding.getRoot().setAlpha(voucher.getStatus().equals("Redeemed") ? (float) 0.5 : (float) 1.0);

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final VoucherPickerItemBinding itemBinding;
        public ViewHolder(VoucherPickerItemBinding binding) {
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
