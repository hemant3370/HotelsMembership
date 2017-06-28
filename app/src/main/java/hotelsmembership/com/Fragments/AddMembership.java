package hotelsmembership.com.Fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.inject.Inject;

import hotelsmembership.com.Applications.Initializer;
import hotelsmembership.com.Model.AddCardPayload;
import hotelsmembership.com.Model.AddMembershipResponse;
import hotelsmembership.com.Model.Hotel.Hotel;
import hotelsmembership.com.Model.HotelsDatabase;
import hotelsmembership.com.Model.Membership;
import hotelsmembership.com.R;
import hotelsmembership.com.Retrofit.Client.RestClient;
import hotelsmembership.com.Retrofit.Services.ApiInterface;
import hotelsmembership.com.databinding.FragmentAddMembershipBinding;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

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
    @Inject
    Retrofit mRetrofit;
    HotelsDatabase hotelsDatabase;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Hotel selectedHotel;
    AddCardPayload addCardPayload;
    FragmentAddMembershipBinding fragmentAddCardBinding;
    private OnFragmentInteractionListener mListener;
    private ProgressDialog progressBar;
    private CompositeDisposable compositeDisposable =
            new CompositeDisposable();
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
        fragmentAddCardBinding.cardExpiryDate.setOnTouchListener(new View.OnTouchListener() {
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
                    focusView.requestFocus();
                } else if (fragmentAddCardBinding.getData() != null) {
                    // Show a progress spinner,

                    progressBar=new ProgressDialog(getContext());
                    progressBar.setMessage("Submitting...");
                    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressBar.setIndeterminate(true);
                    progressBar.show();
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
        if (mRetrofit == null){
            mRetrofit = RestClient.getClient();
        }
        ApiInterface apiInterface = mRetrofit.create(ApiInterface.class);
        apiInterface.addMembership(fragmentAddCardBinding.getData(), selectedHotel.getHotelId())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<AddMembershipResponse>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(final AddMembershipResponse addMembershipResponse) {
                        if (addMembershipResponse.getStatusCode() == 200 && addMembershipResponse.getContent() != null ){
                            new Thread(new Runnable() {
                                public void run() {
                                    // a potentially  time consuming task
                                    Membership membership = addMembershipResponse.getContent();
                                    membership.setHotel(selectedHotel);
                                    hotelsDatabase.daoAccess().insertOnlySingleRecord(membership);
                                }
                            }).start();
                            Toast.makeText(getContext(),"Membership Added",Toast.LENGTH_SHORT).show();
                            if (mListener != null) {
                                mListener.onMembershipAdded();
                            }
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
        compositeDisposable.clear();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onMembershipAdded();
    }
}
