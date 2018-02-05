package loyaltywallet.com.Model.Hotel;

import android.arch.persistence.room.TypeConverter;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.List;

public class ListConverter {
    @TypeConverter
    public String fromList(List<String> value) {
        return value == null ? null : TextUtils.join("@#",value);
    }

    @TypeConverter
    public List<String> stringToArray(String value) {
        if (value == null) {
            return null;
        } else {
            return Arrays.asList(value.split("@#"));
        }
    }
}
