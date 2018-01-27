package loyaltywallet.com.Fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import loyaltywallet.com.Applications.Initializer;
import loyaltywallet.com.Model.Membership;
import loyaltywallet.com.Model.Vouchers.Voucher;
import loyaltywallet.com.R;
import loyaltywallet.com.databinding.CardDetail;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Membership membership;
    String cardNumber;
    List<Voucher> vouchers;
//    private MembershipsFragment.OnListFragmentInteractionListener mListener;
    CardDetail membershipDetailBinding;
    @Inject
    Retrofit mRetrofit;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private BookingSectionPageAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private OnFragmentInteractionListener mListener;

    public CardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CardFragment newInstance(String param1, String param2) {
        CardFragment fragment = new CardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        membershipDetailBinding = DataBindingUtil.inflate(inflater,R.layout.content_card,container,false);
        mSectionsPagerAdapter = new BookingSectionPageAdapter(getChildFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = membershipDetailBinding.formviewpager;
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(0);
        TabLayout tabLayout = membershipDetailBinding.tabs;
        tabLayout.setupWithViewPager(mViewPager);
        membership = ((Initializer) getActivity().getApplication()).getCardContext().getMembership();
        cardNumber = membership.getCardNumber();
        vouchers = ((Initializer) getActivity().getApplication()).getCardContext().getVouchers();
        setup();
        return membershipDetailBinding.getRoot();
    }
    void setup(){
        if (membership.getCardType().equals("")) {
            membershipDetailBinding.setImageUrl(membership.getHotel().getCardsImageURLs().getGold());
        } else {
            membershipDetailBinding.setImageUrl(membership.getCardType().equals("G") ? membership.getHotel().getCardsImageURLs().getGold() : membership.getHotel().getCardsImageURLs().getSilver());
        }
        membershipDetailBinding.setData(membership);

    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public class BookingSectionPageAdapter extends FragmentPagerAdapter {

        public BookingSectionPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0)
            return RoomReservation.newInstance("","");
            else
                return TableReservation.newInstance("","");
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "ROOM";
                case 1:
                    return "TABLE";
            }
            return null;
        }
    }
}
