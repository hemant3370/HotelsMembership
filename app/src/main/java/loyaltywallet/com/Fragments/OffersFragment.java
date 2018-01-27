package loyaltywallet.com.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import loyaltywallet.com.Adapter.OffersRecyclerViewAdapter;
import loyaltywallet.com.Applications.Initializer;
import loyaltywallet.com.R;


public class OffersFragment extends BottomSheetDialogFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_TYPE = "column-count";
    // TODO: Customize parameters

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OffersFragment() {
    }


    // TODO: Customize parameter initialization

    public static OffersFragment newInstance(String type) {
        OffersFragment fragment = new OffersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offer_list, container, false);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setNestedScrollingEnabled(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new OffersRecyclerViewAdapter(((Initializer) getActivity().getApplication()).getCardContext().getOffers()));
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

    }

}
