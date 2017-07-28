package hotelsmembership.com.Fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import hotelsmembership.com.Applications.Initializer;
import hotelsmembership.com.Model.Hotel.HotelVenue;
import hotelsmembership.com.Model.TableReservationPayload;
import hotelsmembership.com.R;
import hotelsmembership.com.databinding.FragmentTableReservationBinding;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TableReservation.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TableReservation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TableReservation extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TableReservationPayload tableReservationPayload;
    FragmentTableReservationBinding tableReservationBinding;
    private OnFragmentInteractionListener mListener;

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
                    new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            Date date = new GregorianCalendar(year, month, dayOfMonth).getTime();
                            DateFormat df = new SimpleDateFormat("dd/MM/yyyy", new Locale("en"));
                            String strDate = df.format(date);
                            tableReservationBinding.reservationDate.setText(strDate);
                        }
                    }, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
                return true;
            }
        });
        tableReservationBinding.timeSlot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Calendar myCalendar = Calendar.getInstance();
                    new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            Date date = new GregorianCalendar(2017, 9, 1, hourOfDay, minute).getTime();
                            Date endTime = new GregorianCalendar(2017, 9, 1, hourOfDay+1, minute).getTime();
                            DateFormat df = new SimpleDateFormat("hh:mm aa", new Locale("en"));
                            String strDate = df.format(date);
                            tableReservationBinding.timeSlot.setText(strDate + "-" + df.format(endTime));
                        }
                    }, myCalendar.get(Calendar.HOUR_OF_DAY),myCalendar.get(Calendar.MINUTE), true).show();
                }
                return true;
            }
        });
        return tableReservationBinding.getRoot();
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
}
