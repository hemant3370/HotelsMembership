package hotelsmembership.com.Fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
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
import hotelsmembership.com.Model.TableReservationPayload;
import hotelsmembership.com.Model.Vouchers.Voucher;
import hotelsmembership.com.R;
import hotelsmembership.com.Retrofit.Client.RestClient;
import hotelsmembership.com.Retrofit.Services.ApiInterface;
import hotelsmembership.com.databinding.FragmentTableReservationBinding;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TableReservation.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TableReservation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TableReservation extends Fragment implements VoucherPicker, OfferPickerFragment.OfferPicker {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @Inject
    Retrofit mRetrofit;
    private CompositeDisposable compositeDisposable =
            new CompositeDisposable();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TableReservationPayload tableReservationPayload;
    FragmentTableReservationBinding tableReservationBinding;
    private OnFragmentInteractionListener mListener;
    OfferPickerFragment offerPickerFragment;
    VoucherPickerFragment voucherPickerFragment;
    private ProgressDialog progressBar;

    public TableReservation() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TableReservation.
     */
    // TODO: Rename and change types and number of parameters
    public static TableReservation newInstance(String param1, String param2) {
        TableReservation fragment = new TableReservation();
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
        tableReservationBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_table_reservation, container, false);
        tableReservationPayload = new TableReservationPayload();
        tableReservationPayload.setCardNumber(((Initializer) getActivity().getApplication()).getCardContext().getCardNumber());
        tableReservationPayload.setPaxCount(1);
        tableReservationBinding.setData(tableReservationPayload);
        tableReservationBinding.venueName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    chooseVenue();
                }
                return true;
            }
        });
        tableReservationBinding.reservationDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Calendar myCalendar = Calendar.getInstance();
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            Date date = new GregorianCalendar(year, month, dayOfMonth).getTime();
                            DateFormat df = new SimpleDateFormat("dd/MM/yyyy", new Locale("en"));
                            String strDate = df.format(date);
                            tableReservationBinding.reservationDate.setText(strDate);
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
        tableReservationBinding.timeSlot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    final Calendar myCalendar = Calendar.getInstance();
                    new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            Date date = new GregorianCalendar(2017, 9, 1, hourOfDay, minute).getTime();
                            Date endTime = new GregorianCalendar(2017, 9, 1, hourOfDay+1, minute).getTime();
                            if (hourOfDay < myCalendar.get(Calendar.HOUR_OF_DAY) + 6){
                               Toast.makeText(getActivity().getApplicationContext(),"You can only book six hours prior.", Toast.LENGTH_LONG).show();
                            }
                            else {
                                DateFormat df = new SimpleDateFormat("hh:mm aa", new Locale("en"));
                                String strDate = df.format(date);
                                tableReservationBinding.timeSlot.setText(strDate + "-" + df.format(endTime));
                            }
                        }
                    }, myCalendar.get(Calendar.HOUR_OF_DAY),myCalendar.get(Calendar.MINUTE), true).show();
                }
                return true;
            }
        });
        tableReservationBinding.voucherDetail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP){
                    voucherPickerFragment =  VoucherPickerFragment.newInstance("Dine");
                    voucherPickerFragment.setmListener(TableReservation.this);
                    voucherPickerFragment.show(getChildFragmentManager(), voucherPickerFragment.getTag());
                }
                return true;
            }
        });
        tableReservationBinding.discountDetail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    offerPickerFragment = OfferPickerFragment.newInstance("FOOD");
                    offerPickerFragment.setmListener(TableReservation.this);
                    offerPickerFragment.show(getChildFragmentManager(), offerPickerFragment.getTag());
                }
                return true;
            }
        });
        tableReservationBinding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean cancel = false;
                View focusView = null;
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                // Check for a valid password, if the user entered one.

                if (TextUtils.isEmpty(tableReservationBinding.venueName.getText())) {
                    tableReservationBinding.venueName.setError(getString(R.string.error_field_nohotel));
                    focusView = tableReservationBinding.venueName;
                    cancel = true;
                }
                if (TextUtils.isEmpty(tableReservationBinding.timeSlot.getText())) {
                    tableReservationBinding.timeSlot.setError(getString(R.string.error_field_required));
                    focusView = tableReservationBinding.timeSlot;
                    cancel = true;
                }
                if (TextUtils.isEmpty(tableReservationBinding.paxCount.getText())) {
                    tableReservationBinding.paxCount.setError(getString(R.string.error_field_required));
                    focusView = tableReservationBinding.paxCount;
                    cancel = true;
                }
                if (TextUtils.isEmpty(tableReservationBinding.reservationDate.getText())) {
                    tableReservationBinding.reservationDate.setError(getString(R.string.error_field_required));
                    focusView = tableReservationBinding.reservationDate;
                    cancel = true;
                }

                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else if (tableReservationBinding.getData() != null) {
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
        return tableReservationBinding.getRoot();
    }

    private void book() {
        if (mRetrofit == null){
            mRetrofit = RestClient.getClient();
        }
        ApiInterface apiInterface = mRetrofit.create(ApiInterface.class);
        apiInterface.bookTable(tableReservationBinding.getData(), ((Initializer) getActivity().getApplication()).getCardContext().getMembership().getHotel().getHotelId(),((Initializer) getActivity().getApplication()).getCardContext().getMembership().getAuthToken())
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
                tableReservationBinding.venueName.setText(names.get(item));
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    @NonNull
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

    @Override
    public void onVoucherPicked(Voucher voucher) {
        voucherPickerFragment.dismiss();
        tableReservationBinding.voucherDetail.setText(voucher.getVoucherCategory().getCategoryTitle());

    }

    @Override
    public void onOfferPicked(Offer offer) {
        offerPickerFragment.dismiss();
        tableReservationBinding.discountDetail.setText(offer.getDescription());

    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
