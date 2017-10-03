package hotelsmembership.com.Fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import hotelsmembership.com.Applications.Initializer;
import hotelsmembership.com.Interfaces.VoucherPicker;
import hotelsmembership.com.Model.BasicResponse;
import hotelsmembership.com.Model.Hotel.HotelVenue;
import hotelsmembership.com.Model.Hotel.Offer;
import hotelsmembership.com.Model.RoomReservationPayload;
import hotelsmembership.com.Model.Vouchers.Voucher;
import hotelsmembership.com.R;
import hotelsmembership.com.Retrofit.Client.RestClient;
import hotelsmembership.com.Retrofit.Services.ApiInterface;
import hotelsmembership.com.databinding.FragmentRoomReservationBinding;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RoomReservation.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RoomReservation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoomReservation extends Fragment implements VoucherPicker, OfferPickerFragment.OfferPicker, OccupancyFragment.OnOccupancyInteractionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ProgressDialog progressBar;
    List<Voucher> vouchers = new ArrayList<>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    @Inject
    Retrofit mRetrofit;
    private CompositeDisposable compositeDisposable =
            new CompositeDisposable();
    RoomReservationPayload roomReservationPayload;
    private OnFragmentInteractionListener mListener;
    FragmentRoomReservationBinding roomReservationBinding;
    private OfferPickerFragment offerPickerFragment;
    private VoucherPickerFragment voucherPickerFragment;
    private OccupancyFragment occupancyFragment;
    public RoomReservation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RoomReservation.
     */
    // TODO: Rename and change types and number of parameters
    public static RoomReservation newInstance(String param1, String param2) {
        RoomReservation fragment = new RoomReservation();
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
        roomReservationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_room_reservation, container, false);
        setupData();
        roomReservationBinding.checkinDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    roomReservationBinding.checkinDate.setError(null);
                    Calendar myCalendar = Calendar.getInstance();
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            Date date = new GregorianCalendar(year, month, dayOfMonth).getTime();
                            DateFormat df = new SimpleDateFormat("dd/MM/yyyy", new Locale("en"));
                            try {
                                if (roomReservationBinding.getData().getCheckOutDate() != null && date.after(df.parse(roomReservationBinding.getData().getCheckOutDate()))){
                                    Toast.makeText(getActivity().getApplicationContext(),"Invalid Date",Toast.LENGTH_LONG).show();
                                }
                                else{
                                    String strDate = df.format(date);
                                    roomReservationBinding.checkinDate.setText(strDate);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
                    datePickerDialog.show();
                }
                return true;
            }
        });
        roomReservationBinding.checkoutDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    roomReservationBinding.checkoutDate.setError(null);
                    Calendar myCalendar = Calendar.getInstance();
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            Date date = new GregorianCalendar(year, month, dayOfMonth).getTime();
                            DateFormat df = new SimpleDateFormat("dd/MM/yyyy", new Locale("en"));
                            try {
                                if (roomReservationBinding.getData().getCheckInDate() != null && date.before( df.parse(roomReservationBinding.getData().getCheckInDate()))){
                                    Toast.makeText(getActivity().getApplicationContext(),"Invalid Date",Toast.LENGTH_LONG).show();
                                }
                                else{
                                    String strDate = df.format(date);
                                    roomReservationBinding.checkoutDate.setText(strDate);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
                    datePickerDialog.show();
                }
                return true;
            }
        });
        roomReservationBinding.occupancy.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP){
                    roomReservationBinding.occupancy.setError(null);
                    occupancyFragment = new OccupancyFragment();
                    occupancyFragment.setmListener(RoomReservation.this);
//                    occupancyFragment.show(getChildFragmentManager(), occupancyFragment.getTag());

                    occupancyFragment.show(getChildFragmentManager(), occupancyFragment.getTag());

                }
                return true;
            }
        });
        roomReservationBinding.voucherDetail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP){
                    voucherPickerFragment = VoucherPickerFragment.newInstance(vouchers);
                    voucherPickerFragment.setmListener(RoomReservation.this);
                    voucherPickerFragment.show(getChildFragmentManager(), voucherPickerFragment.getTag());
                }
                return true;
            }
        });
        roomReservationBinding.discountDetail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                     offerPickerFragment = OfferPickerFragment.newInstance("ROOM");
                    offerPickerFragment.setmListener(RoomReservation.this);
                    offerPickerFragment.show(getChildFragmentManager(), offerPickerFragment.getTag());
                }
                return true;
            }
        });
        roomReservationBinding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean cancel = false;
                View focusView = null;
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                // Check for a valid password, if the user entered one.

                if (TextUtils.isEmpty(roomReservationBinding.checkinDate.getText())) {
                    roomReservationBinding.checkinDate.setError(getString(R.string.error_field_nohotel));
                    focusView = roomReservationBinding.checkinDate;
                    cancel = true;
                }
                if (TextUtils.isEmpty(roomReservationBinding.checkoutDate.getText())) {
                    roomReservationBinding.checkoutDate.setError(getString(R.string.error_field_required));
                    focusView = roomReservationBinding.checkoutDate;
                    cancel = true;
                }
                if (TextUtils.isEmpty(roomReservationBinding.occupancy.getText())) {
                    roomReservationBinding.occupancy.setError(getString(R.string.error_field_required));
                    focusView = roomReservationBinding.occupancy;
                    cancel = true;
                }


                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
//                    focusView.requestFocus();
                } else if (roomReservationBinding.getData() != null) {
                    // Show a progress spinner,

                    progressBar=new ProgressDialog(getContext());
                    progressBar.setMessage("Submitting...");
                    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressBar.setIndeterminate(true);
                    progressBar.show();
                    book();
                }
                else {
                    Toast.makeText(getContext(),"Fill All Details",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return roomReservationBinding.getRoot();
    }
    private void setupData(){
        roomReservationPayload = new RoomReservationPayload();
        roomReservationPayload.setCardNumber(((Initializer) getActivity().getApplication()).getCardContext().getCardNumber());
        if (((Initializer) getActivity().getApplication()).getCardContext().getOffers().size() > 0){
            roomReservationPayload.setDiscountDetail(((Initializer) getActivity().getApplication()).getCardContext().getOffers().get(0).getDescription());
        }
        List<Voucher> sorted = new ArrayList<>();
        for (Voucher v :
                ((Initializer) getActivity().getApplication()).getCardContext().getVouchers()) {
            if (!v.getStatus().equals("Redeemed") && v.getVoucherCategory().getCategoryType().equals("Stay") && !checkDuplicate(v.getVoucherCategory().getCategoryCode(), sorted)) {
                sorted.add(v);
            }
        }
        vouchers = sorted;
        roomReservationBinding.setData(roomReservationPayload);
    }
    private void book() {
        if (mRetrofit == null){
            mRetrofit = RestClient.getClient();
        }
        ApiInterface apiInterface = mRetrofit.create(ApiInterface.class);
        apiInterface.bookRoom(roomReservationBinding.getData(), ((Initializer) getActivity().getApplication()).getCardContext().getMembership().getHotel().getHotelId(),((Initializer) getActivity().getApplication()).getCardContext().getMembership().getAuthToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<BasicResponse>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(final BasicResponse addMembershipResponse) {
                        progressBar.dismiss();
                        if (addMembershipResponse.getStatusCode() == 200 && addMembershipResponse.getContent() != null ){
                            setupData();
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Success!!!");
                            builder.setMessage(addMembershipResponse.getContent());
                            // Create the AlertDialog object and return it
                             builder.create().show();

                        }
                        else {
                            Toast.makeText(getContext(),"Error " + addMembershipResponse.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        progressBar.dismiss();
                        Toast.makeText(getContext(),throwable.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        progressBar.dismiss();
                    }
                });
    }
    private boolean checkDuplicate(String type, List<Voucher> list){
        for (Voucher voucher : list){
            if(voucher.getVoucherCategory().getCategoryCode().equals(type)){
                return true;
            }
        }
        return  false;
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    public void chooseVenue() {
        List<HotelVenue> venues = ((Initializer)getActivity().getApplication()).getCardContext().getHotelVenues();
        final List<String> names =  new ArrayList<>();
        for (HotelVenue hotelVenue : venues) {
            if(hotelVenue.getVenueName() != null) {
                names.add(hotelVenue.getVenueName());
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);
        builder.setTitle("Choose Venue");
        builder.setItems( names.toArray(new CharSequence[names.size()]), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
//                roomReservationBinding.venueName.setText(names.get(item));
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
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

    @Override
    public void onVoucherPicked(Voucher voucher) {
        if(voucher != null) {
            voucher.isSelected = !voucher.isSelected;
            String selectedVouchers = "";
            for (Voucher v : vouchers) {
                if (v.isSelected) {
                    selectedVouchers = selectedVouchers.concat(v.getVoucherCategory().getCategoryTitle() + "\n");
                }
            }
            voucherPickerFragment.resetData();
            roomReservationBinding.voucherDetail.setText(selectedVouchers);
        }
        else{
            for (Voucher v : vouchers) {
                v.isSelected = false;
            }
            voucherPickerFragment.dismiss();
            roomReservationBinding.voucherDetail.setText("");
        }
    }

    @Override
    public void onOfferPicked(Offer offer) {
        offerPickerFragment.dismiss();
            roomReservationBinding.discountDetail.setText(offer != null ? offer.getDescription() : "");
    }

    @Override
    public void onOccupancyChanged(String occupancy) {
        roomReservationBinding.occupancy.setText(occupancy);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
