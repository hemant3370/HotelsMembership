
package loyaltywallet.com.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RedeemPayload {

    @SerializedName("cardNumber")
    @Expose
    private String cardNumber;
    @SerializedName("voucherNumber")
    @Expose
    private String voucherNumber;

    /**
     * No args constructor for use in serialization
     * 
     */
    public RedeemPayload() {
    }

    /**
     * 
     * @param voucherNumber
     * @param cardNumber
     */
    public RedeemPayload(String cardNumber, String voucherNumber) {
        super();
        this.cardNumber = cardNumber;
        this.voucherNumber = voucherNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getVoucherNumber() {
        return voucherNumber;
    }

    public void setVoucherNumber(String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

}
