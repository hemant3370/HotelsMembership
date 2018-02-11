package loyaltywallet.com.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
}
