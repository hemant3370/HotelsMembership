package loyaltywallet.com.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import loyaltywallet.com.Fragments.MembershipsFragment.OnListFragmentInteractionListener;
import loyaltywallet.com.Model.Membership;
import loyaltywallet.com.R;
import loyaltywallet.com.databinding.MembershipItemBinding;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Membership} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyMembershipRecyclerViewAdapter extends RecyclerView.Adapter<MyMembershipRecyclerViewAdapter.ViewHolder> {

    private final List<Membership> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context context;
    public MyMembershipRecyclerViewAdapter(List<Membership> items, OnListFragmentInteractionListener listener, Context context) {
        mValues = items;
        mListener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        MembershipItemBinding itemBinding =
                MembershipItemBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.bind(mValues.get(position));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mListener.onCardFullScreenAction(mValues.get(holder.getAdapterPosition()));
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final MembershipItemBinding itemBinding;
        public ViewHolder(MembershipItemBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;
        }

        public void bind(Membership item) {
            if(item.getCardType().equals("G")){
                itemBinding.frontCardContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.gold));
            }
            else if(item.getCardType().equals("S")){
                itemBinding.frontCardContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.silver));
            }
            if (item.getCardType().equals("")) {
                itemBinding.setImageUrl(item.getHotel().getCardsImageURLs().getGold());
            } else {
                itemBinding.setImageUrl(item.getCardType().equals("G") ? item.getHotel().getCardsImageURLs().getGold() : item.getHotel().getCardsImageURLs().getSilver());
            }
            itemBinding.setData(item);
            itemBinding.setMlistener(mListener);
            itemBinding.executePendingBindings();
        }

    }
}
