
package loyaltywallet.com.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyOTPPayload {

    @SerializedName("otp")
    @Expose
    private String otp;
    @SerializedName("voucherNumber")
    @Expose
    private String voucherNumber;

    /**
     * No args constructor for use in serialization
     * 
     */
    public VerifyOTPPayload(String voucherNumber) {
        super();
        this.voucherNumber = voucherNumber;
    }

    /**
     * 
     * @param voucherNumber
     * @param otp
     */
    public VerifyOTPPayload(String otp, String voucherNumber) {
        super();
        this.otp = otp;
        this.voucherNumber = voucherNumber;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getVoucherNumber() {
        return voucherNumber;
    }

    public void setVoucherNumber(String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

}
