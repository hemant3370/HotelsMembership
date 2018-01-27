package loyaltywallet.com.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import loyaltywallet.com.Adapter.VenueGistAdapter;
import loyaltywallet.com.Applications.Initializer;
import loyaltywallet.com.Interfaces.CardVoucherClickListener;
import loyaltywallet.com.Model.Hotel.HotelVenue;
import loyaltywallet.com.Model.Membership;
import loyaltywallet.com.Model.Vouchers.Voucher;
import loyaltywallet.com.R;

public class VenuesFragment extends Fragment implements CardVoucherClickListener {

    // TODO: Customize parameter argument names
    private static final String ARG_VENUES = "venues";
    private Listener mListener;
    List<HotelVenue> venues ;

    // TODO: Customize parameters

    @Override
    public void onVoucherGistClick(int item) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", venues.get(item).getVenuePhone(), null)));
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        venues = ((Initializer) getActivity().getApplication()).getCardContext().getHotelVenues();
        return inflater.inflate(R.layout.voucher_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(new VenueGistAdapter(venues, this));

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


}
