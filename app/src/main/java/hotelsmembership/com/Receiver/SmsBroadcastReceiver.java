package hotelsmembership.com.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;

import hotelsmembership.com.Interfaces.SmsListener;

import static android.provider.Telephony.Sms.Intents.SMS_RECEIVED_ACTION;

/**
 * Created by hemantsingh on 22/06/17.
 */

public class SmsBroadcastReceiver extends BroadcastReceiver {

    public static final String SMS_BUNDLE = "pdus";
    private static SmsListener mListener;
    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            if (sms != null) {
                for (Object sm : sms) {
                    SmsMessage smsMessage;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        smsMessage = SmsMessage.createFromPdu((byte[]) sm, intent.getStringExtra(SMS_RECEIVED_ACTION));
                    }
                    else {
                        smsMessage = SmsMessage.createFromPdu((byte[]) sm);
                    }
                    String smsBody = smsMessage.getMessageBody();
                    String address = smsMessage.getOriginatingAddress();

                    smsMessageStr += "SMS From: " + address + "\n";
                    smsMessageStr += smsBody + "\n";
                }
                // Toast.makeText(context, smsMessageStr, Toast.LENGTH_SHORT).show();
                mListener.messageReceived(smsMessageStr);
                //this will update the UI with message
//            VouchersActivity inst = VouchersActivity.ins
//            inst.updateList(smsMessageStr);
            }
        }
    }
    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}