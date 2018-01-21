package hotelsmembership.com.Fragments;

import android.app.DatePickerDialog;
import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import hotelsmembership.com.Applications.Initializer;
import hotelsmembership.com.Model.AddCardPayload;
import hotelsmembership.com.Model.Hotel.Hotel;
import hotelsmembership.com.Model.HotelsDatabase;
import hotelsmembership.com.R;
import hotelsmembership.com.databinding.FragmentAddMembershipBinding;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddMembership.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddMembership#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMembership extends LifecycleFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    HotelsDatabase hotelsDatabase;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Hotel selectedHotel;
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy", new Locale("en"));
    AddCardPayload addCardPayload;
    FragmentAddMembershipBinding fragmentAddCardBinding;
    private OnFragmentInteractionListener mListener;
    public AddMembership() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddMembership.
     */
    // TODO: Rename and change types and number of parameters
    public static AddMembership newInstance(String param1, String param2) {
        AddMembership fragment = new AddMembership();
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
        fragmentAddCardBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_add_membership, container, false);
        ((Initializer) getActivity().getApplication()).getNetComponent().inject(this);
        addCardPayload = new AddCardPayload();
        fragmentAddCardBinding.setData(addCardPayload);
         hotelsDatabase = Room.databaseBuilder(getContext(),
                HotelsDatabase.class, "hotel-db").build();
        LiveData<String[]> hotelnames = hotelsDatabase.daoAccess().fetchAllNames();
        hotelnames.observe(this, new Observer<String[]>() {
            @Override
            public void onChanged(@Nullable String[] strings) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_dropdown_item_1line, strings);
                fragmentAddCardBinding.hotelName.setAdapter(adapter);
            }
        });
        fragmentAddCardBinding.hotelName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new Thread(new Runnable() {
                    public void run() {
                        // a potentially  time consuming task
                        selectedHotel = hotelsDatabase.daoAccess().getSingleRecord(fragmentAddCardBinding.hotelName.getText().toString());
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                fragmentAddCardBinding.hotelLogo.setVisibility(View.VISIBLE);
                                fragmentAddCardBinding.setImageUrl(selectedHotel.getHotelLogoURL());
                                fragmentAddCardBinding.executePendingBindings();
                            }
                        });
                    }
                }).start();

            }
        });
        fragmentAddCardBinding.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentAddCardBinding.checkbox.setError(null);
            }
        });
        fragmentAddCardBinding.acceptTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedHotel != null && selectedHotel.getMembershipTermsAndConditions() != null && selectedHotel.getMembershipTermsAndConditions().size() > 0) {
                    String[] terms = new String[selectedHotel.getMembershipTermsAndConditions().size()];
                    for (int i = 0; i < terms.length; i++) {
                        terms[i] = (i + 1) + ". " + selectedHotel.getMembershipTermsAndConditions().get(i);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
                    adapter.addAll(terms);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.MyDialogTheme);
                    builder.setTitle("Terms and Conditions");
                    builder.setAdapter(adapter, null);
                    final AlertDialog alert = builder.create();
                    alert.setCancelable(false);
                    alert.setButton(DialogInterface.BUTTON_POSITIVE, "Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            fragmentAddCardBinding.checkbox.setChecked(true);
                            alert.hide();
                        }
                    });
                    alert.setButton(DialogInterface.BUTTON_NEGATIVE, "Deny", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            fragmentAddCardBinding.checkbox.setChecked(false);
                            alert.hide();
                        }
                    });
                    alert.show();
                }
            }
        });
        fragmentAddCardBinding.cardExpiryDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    fragmentAddCardBinding.cardExpiryDate.setError(null);
                    Calendar myCalendar = Calendar.getInstance();
                    if(fragmentAddCardBinding.getData().getCardExpiryDate() != null) {
                        try {
                            Date selectedDate = df.parse(fragmentAddCardBinding.getData().getCardExpiryDate());
                            myCalendar.setTime(selectedDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            Date date = new GregorianCalendar(year, month, dayOfMonth).getTime();
                            DateFormat df = new SimpleDateFormat("dd/MM/yyyy", new Locale("en"));
                            String strDate = df.format(date);
                            fragmentAddCardBinding.cardExpiryDate.setText(strDate);
                        }
                    }, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
                return true;
            }
        });
        fragmentAddCardBinding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean cancel = false;
                View focusView = null;
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                // Check for a valid password, if the user entered one.
                if(!fragmentAddCardBinding.checkbox.isChecked()){
                    fragmentAddCardBinding.checkbox.setError("Please accept terms & conditions.");
                    focusView = fragmentAddCardBinding.checkbox;
                    cancel = true;
                }
                if (TextUtils.isEmpty(fragmentAddCardBinding.hotelName.getText()) || selectedHotel == null) {
                    fragmentAddCardBinding.hotelName.setError(getString(R.string.error_field_nohotel));
                    focusView = fragmentAddCardBinding.hotelName;
                    cancel = true;
                }
                if (TextUtils.isEmpty(fragmentAddCardBinding.cardNumber.getText())) {
                    fragmentAddCardBinding.cardNumber.setError(getString(R.string.error_field_required));
                    focusView = fragmentAddCardBinding.cardNumber;
                    cancel = true;
                }
                if (TextUtils.isEmpty(fragmentAddCardBinding.holderName.getText())) {
                    fragmentAddCardBinding.holderName.setError(getString(R.string.error_field_required));
                    focusView = fragmentAddCardBinding.holderName;
                    cancel = true;
                }
                if (TextUtils.isEmpty(fragmentAddCardBinding.phoneNumber.getText())) {
                    fragmentAddCardBinding.phoneNumber.setError(getString(R.string.error_field_required));
                    focusView = fragmentAddCardBinding.phoneNumber;
                    cancel = true;
                }
                if (TextUtils.isEmpty(fragmentAddCardBinding.cardExpiryDate.getText())) {
                    fragmentAddCardBinding.cardExpiryDate.setError(getString(R.string.error_field_required));
                    focusView = fragmentAddCardBinding.cardExpiryDate;
                    cancel = true;
                }

                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
//                    focusView.requestFocus();
                } else if (fragmentAddCardBinding.getData() != null) {
                    // Show a progress spinner,
                    addCard();
                }
                else {
                    Toast.makeText(getContext(),"Fill All Details",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return fragmentAddCardBinding.getRoot();
    }
    void addCard(){
        if (mListener != null) {
            mListener.addCard(selectedHotel, fragmentAddCardBinding.getData());
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void addCard(final Hotel selectedHotel, AddCardPayload payload);
    }
}
