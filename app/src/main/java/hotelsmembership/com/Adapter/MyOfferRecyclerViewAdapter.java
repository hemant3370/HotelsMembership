package hotelsmembership.com.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import hotelsmembership.com.Fragments.OfferPickerFragment;
import hotelsmembership.com.Model.Hotel.Offer;
import hotelsmembership.com.databinding.OfferItemBinding;


public class MyOfferRecyclerViewAdapter extends RecyclerView.Adapter<MyOfferRecyclerViewAdapter.ViewHolder> {

    private final List<Offer> mValues;
    private final OfferPickerFragment.OfferPicker mListener;

    public MyOfferRecyclerViewAdapter(List<Offer> items, OfferPickerFragment.OfferPicker listener, String type) {
        mValues = new ArrayList<>();
        for (Offer offer : items){
            if (offer.getOfferCategory().equals(type)){
                mValues.add(offer);
            }
        }
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        OfferItemBinding offerItemBinding = OfferItemBinding.inflate(layoutInflater, parent, false);

        return new ViewHolder(offerItemBinding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bind(mValues.get(position));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final OfferItemBinding itemBinding;
        public ViewHolder(OfferItemBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;
        }

        public void bind(Offer item) {
            itemBinding.setData(item);
            itemBinding.setMlistener(mListener);
            itemBinding.executePendingBindings();
        }

    }
}
