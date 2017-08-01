package hotelsmembership.com.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hotelsmembership.com.Adapter.MyOfferRecyclerViewAdapter;
import hotelsmembership.com.Applications.Initializer;
import hotelsmembership.com.Model.Hotel.Offer;
import hotelsmembership.com.R;


public class OfferPickerFragment extends BottomSheetDialogFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_TYPE = "column-count";
    // TODO: Customize parameters
    private String mType;
    private OfferPicker mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OfferPickerFragment() {
    }

    public void setmListener(OfferPicker mListener) {
        this.mListener = mListener;
    }

    // TODO: Customize parameter initialization

    public static OfferPickerFragment newInstance(String type) {
        OfferPickerFragment fragment = new OfferPickerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mType = getArguments().getString(ARG_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offer_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new MyOfferRecyclerViewAdapter(((Initializer) getActivity().getApplication()).getCardContext().getOffers(), mListener, mType));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OfferPicker {
        void onOfferPicked(Offer offer);
    }
}
