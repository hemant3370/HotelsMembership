package hotelsmembership.com.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hemantsingh on 22/06/17.
 */

public class OTPFinder {
    public String getOTPFrom(String message){
         Pattern p = Pattern.compile("(|^)\\d{6}");
        Matcher m = p.matcher(message);
        if(m.find()) {
            return m.group(0);
        }
        else
        {
            //something went wrong
            return "";
        }

    }
}
