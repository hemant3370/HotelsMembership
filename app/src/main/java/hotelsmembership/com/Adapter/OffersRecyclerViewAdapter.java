package hotelsmembership.com.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import hotelsmembership.com.Model.Hotel.Offer;
import hotelsmembership.com.databinding.OfferItemBinding;


public class OffersRecyclerViewAdapter extends RecyclerView.Adapter<OffersRecyclerViewAdapter.ViewHolder> {

    private final List<Offer> mValues;

    public OffersRecyclerViewAdapter(List<Offer> items) {
        mValues = items;
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
            itemBinding.executePendingBindings();
        }

    }
}
