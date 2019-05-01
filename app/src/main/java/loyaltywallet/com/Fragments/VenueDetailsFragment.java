package loyaltywallet.com.Fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import loyaltywallet.com.Model.Hotel.HotelVenue;
import loyaltywallet.com.R;
import loyaltywallet.com.databinding.VenueItemBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VenueDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VenueDetailsFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_VENUE = "venue";

    // TODO: Rename and change types of parameters
    private HotelVenue venue;



    public VenueDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment VenueDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VenueDetailsFragment newInstance(HotelVenue param1) {
        VenueDetailsFragment fragment = new VenueDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_VENUE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            venue = getArguments().getParcelable(ARG_PARAM_VENUE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        VenueItemBinding venueItemBinding = DataBindingUtil.inflate(inflater, R.layout.venue, container, false);
        venueItemBinding.setData(venue);
        return venueItemBinding.getRoot();
    }

}
