package loyaltywallet.com.Fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import loyaltywallet.com.Adapter.MyMembershipRecyclerViewAdapter;
import loyaltywallet.com.Model.HotelsDatabase;
import loyaltywallet.com.Model.Membership;
import loyaltywallet.com.R;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MembershipsFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    HotelsDatabase hotelsDatabase;
    LiveData<List<Membership>> memberships;
    private OnListFragmentInteractionListener mListener;
    @BindView(R.id.membership_list)
    RecyclerView recyclerView;
    @BindView(R.id.addmembership_onlist)
    Button addMembersipButton;
    SwipeableRecyclerViewTouchListener swipeTouchListener;

    public MembershipsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MembershipsFragment newInstance(int columnCount) {
        MembershipsFragment fragment = new MembershipsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_membership_list, container, false);
         ButterKnife.bind(this,view);
            Context context = view.getContext();
            addMembersipButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onAddClicked();
                }
            });
            swipeTouchListener = new SwipeableRecyclerViewTouchListener(recyclerView,
                    new SwipeableRecyclerViewTouchListener.SwipeListener() {
                        @Override
                        public boolean canSwipeLeft(int position) {
                            return true;
                        }

                        @Override
                        public boolean canSwipeRight(int position) {
                            return false;
                        }

                        @Override
                        public void onDismissedBySwipeLeft(final RecyclerView recyclerView, final int[] reverseSortedPositions) {
                            final Snackbar mySnackbar = Snackbar.make(getActivity().findViewById(R.id.main_coordinator),
                                    "Confirm Card Deletion ?", Snackbar.LENGTH_LONG);
                            mySnackbar.setAction("YES", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            hotelsDatabase.daoAccess().deleteMembership(memberships.getValue().get(reverseSortedPositions[0]));
                                        }
                                    }).start();
                                }
                            });

                            mySnackbar.show();

                        }

                        @Override
                        public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {

                        }
                    });
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

             hotelsDatabase = Room.databaseBuilder(getContext(),
                    HotelsDatabase.class, "hotel-db").fallbackToDestructiveMigration().build();
            memberships = hotelsDatabase.daoAccess().fetchAllMemberships();
            memberships.observe(this, new Observer<List<Membership>>() {
                @Override
                public void onChanged(@Nullable List<Membership> memberships) {
                    recyclerView.setAdapter(new MyMembershipRecyclerViewAdapter(memberships, mListener, recyclerView.getContext()));
                    recyclerView.requestLayout();
                }
            });
            recyclerView.addOnItemTouchListener(swipeTouchListener);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onCardClicked(Membership item);
        void onAddClicked();
        void onCardFullScreenAction(Membership item);
        void onDelete(Membership item);
        void onInfoClick(Membership item);
    }
}
