
package hotelsmembership.com.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddCardPayload {

    @SerializedName("cardNumber")
    @Expose
    private String cardNumber;
    @SerializedName("cardExpiryDate")
    @Expose
    private String cardExpiryDate;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;

    public AddCardPayload() {
    }

    public AddCardPayload(String cardNumber, String cardExpiryDate, String phoneNumber) {
        this.cardNumber = cardNumber;
        this.cardExpiryDate = cardExpiryDate;
        this.phoneNumber = phoneNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardExpiryDate() {
        return cardExpiryDate;
    }

    public void setCardExpiryDate(String cardExpiryDate) {
        this.cardExpiryDate = cardExpiryDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
