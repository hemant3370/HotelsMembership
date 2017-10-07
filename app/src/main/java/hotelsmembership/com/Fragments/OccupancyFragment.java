package hotelsmembership.com.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import hotelsmembership.com.R;

public class OccupancyFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match


    // TODO: Rename and change types of parameters
     int single,two,extra;
    NumberPicker singleRoom,doubleRoom,extraBed;

    private OnOccupancyInteractionListener mListener;

    public OccupancyFragment() {
        // Required empty public constructor
    }

    public void setmListener(OnOccupancyInteractionListener mListener) {
        this.mListener = mListener;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View root = inflater.inflate(R.layout.fragment_occupancy, null, false);
        singleRoom = (NumberPicker) root.findViewById(R.id.numberPickersingle);
        doubleRoom = (NumberPicker) root.findViewById(R.id.numberPickerdouble);
        extraBed = (NumberPicker) root.findViewById(R.id.numberPickerextra);
        singleRoom.setMinValue(0);
        singleRoom.setMaxValue(3);
        doubleRoom.setMinValue(0);
        doubleRoom.setMaxValue(3);
        extraBed.setMinValue(0);
        extraBed.setMaxValue(3);
        singleRoom.setValue(single);
        doubleRoom.setValue(two);
        extraBed.setValue(extra);
        singleRoom.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                single = newVal;
                sendOccupancy();
            }
        });
        doubleRoom.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                two = newVal;
                sendOccupancy();
            }
        });
        extraBed.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                extra = newVal;
                sendOccupancy();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);
        Toolbar toolbar = new Toolbar(getContext());
        toolbar.setTitle("Select Occupancy");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        return new AlertDialog.Builder(getActivity())
                // set dialog icon
                .setIcon(android.R.drawable.stat_notify_error)
                // set Dialog Title
                .setCustomTitle(toolbar)
                // Set Dialog Message
                .setView(root)

                // positive button
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
    }
       void sendOccupancy(){

            mListener.onOccupancyChanged(single, two, extra);
        }
    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
    public interface OnOccupancyInteractionListener {
        // TODO: Update argument type and name
        void onOccupancyChanged(int single, int two, int extra);
    }
}
