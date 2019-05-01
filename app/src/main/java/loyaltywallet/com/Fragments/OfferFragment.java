package loyaltywallet.com.Fragments;



import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import loyaltywallet.com.Model.Hotel.Offer;
import loyaltywallet.com.R;
import loyaltywallet.com.databinding.OfferBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OfferFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class OfferFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_OFFER = "offer";


    // TODO: Rename and change types of parameters
    private Offer offer;



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment OfferFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OfferFragment newInstance(Offer param1) {
        OfferFragment fragment = new OfferFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_OFFER, param1);

        fragment.setArguments(args);
        return fragment;
    }
    public OfferFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            offer = getArguments().getParcelable(ARG_OFFER);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        OfferBinding offerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_offer_details, container, false);
        offerBinding.setData(offer);

        return offerBinding.getRoot();
    }

}
