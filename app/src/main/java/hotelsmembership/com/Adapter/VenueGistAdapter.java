package hotelsmembership.com.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import hotelsmembership.com.Interfaces.CardVoucherClickListener;
import hotelsmembership.com.Model.Hotel.HotelVenue;
import hotelsmembership.com.databinding.VenuegistItemBinding;

/**
 * Created by hemantsingh on 18/06/17.
 */

public class VenueGistAdapter extends RecyclerView.Adapter<VenueGistAdapter.ViewHolder> {

    private final List<HotelVenue> mValues;
    private final CardVoucherClickListener mListener;

    public VenueGistAdapter(List<HotelVenue> items, CardVoucherClickListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public VenueGistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        VenuegistItemBinding itemBinding =
                VenuegistItemBinding.inflate(layoutInflater, parent, false);
        return new VenueGistAdapter.ViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(final VenueGistAdapter.ViewHolder holder, int position) {
        holder.bind(mValues.get(position),position);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final VenuegistItemBinding itemBinding;
        public ViewHolder(VenuegistItemBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;
        }

        public void bind(HotelVenue item, int position) {
            itemBinding.setData(item);
            itemBinding.setMlistener(mListener);
            itemBinding.setIndex(position);
            itemBinding.executePendingBindings();
        }
    }

}
