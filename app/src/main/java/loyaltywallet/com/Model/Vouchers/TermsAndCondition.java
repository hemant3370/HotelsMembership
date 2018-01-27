
package loyaltywallet.com.Model.Vouchers;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TermsAndCondition implements Parcelable {

    @SerializedName("termDescription")
    @Expose
    private String termDescription;

    public String getTermDescription() {
        return termDescription;
    }

    public void setTermDescription(String termDescription) {
        this.termDescription = termDescription;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.termDescription);
    }

    public TermsAndCondition() {
    }

    protected TermsAndCondition(Parcel in) {
        this.termDescription = in.readString();
    }

    public static final Parcelable.Creator<TermsAndCondition> CREATOR = new Parcelable.Creator<TermsAndCondition>() {
        @Override
        public TermsAndCondition createFromParcel(Parcel source) {
            return new TermsAndCondition(source);
        }

        @Override
        public TermsAndCondition[] newArray(int size) {
            return new TermsAndCondition[size];
        }
    };
}
