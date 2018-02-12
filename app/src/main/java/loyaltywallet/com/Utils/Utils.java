package loyaltywallet.com.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import loyaltywallet.com.Model.Hotel.Hotel;
import loyaltywallet.com.R;

/**
 * Created by hemantsingh on 11/02/18.
 */

public class Utils {
    public static Date stringToDate(String stringDate) {
        Date thisDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
             thisDate = format.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return thisDate;
    }
    public static void showTerms(Hotel selectedHotel, Context context){
        if (selectedHotel != null && selectedHotel.getMembershipTermsAndConditions() != null && selectedHotel.getMembershipTermsAndConditions().size() > 0) {
            String[] terms = new String[selectedHotel.getMembershipTermsAndConditions().size()];
            for (int i = 0; i < terms.length; i++) {
                terms[i] = (i + 1) + ". " + selectedHotel.getMembershipTermsAndConditions().get(i);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);
            adapter.addAll(terms);
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
            builder.setTitle("Terms and Conditions");
            builder.setAdapter(adapter, null);
            final AlertDialog alert = builder.create();
            alert.setCancelable(false);
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "OKAY", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alert.hide();
                }
            });

            alert.show();
        }
    }
}
