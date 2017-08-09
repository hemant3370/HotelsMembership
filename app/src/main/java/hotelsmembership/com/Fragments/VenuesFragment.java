package hotelsmembership.com.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import hotelsmembership.com.Adapter.VenueGistAdapter;
import hotelsmembership.com.Applications.Initializer;
import hotelsmembership.com.Interfaces.CardVoucherClickListener;
import hotelsmembership.com.Model.Hotel.HotelVenue;
import hotelsmembership.com.Model.Membership;
import hotelsmembership.com.Model.Vouchers.Voucher;
import hotelsmembership.com.R;

public class VenuesFragment extends Fragment implements CardVoucherClickListener {

    // TODO: Customize parameter argument names
    private static final String ARG_VENUES = "venues";
    private Listener mListener;
    List<HotelVenue> venues ;

    // TODO: Customize parameters

    @Override
    public void onVoucherGistClick(int item) {

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        venues = ((Initializer) getActivity().getApplication()).getCardContext().getHotelVenues();
        return inflater.inflate(R.layout.fragment_voucher_list_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) view;
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
