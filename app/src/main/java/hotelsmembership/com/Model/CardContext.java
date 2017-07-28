package hotelsmembership.com.Model;

import java.util.List;

import hotelsmembership.com.Model.Hotel.HotelVenue;
import hotelsmembership.com.Model.Hotel.Offer;
import hotelsmembership.com.Model.Vouchers.Voucher;

/**
 * Created by hemantsingh on 18/06/17.
 */
public class CardContext {
    private Membership membership;
    private String cardNumber;
    private List<Voucher> vouchers;
    private List<Offer> offers;
    private List<HotelVenue> hotelVenues;

    public CardContext(Membership membership, String cardNumber, List<Voucher> vouchers) {
        this.membership = membership;
        this.cardNumber = cardNumber;
        this.vouchers = vouchers;
    }

    public CardContext(Membership membership, String cardNumber, List<Voucher> vouchers, List<Offer> offers, List<HotelVenue> hotelVenues) {
        this.membership = membership;
        this.cardNumber = cardNumber;
        this.vouchers = vouchers;
        this.offers = offers;
        this.hotelVenues = hotelVenues;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public List<HotelVenue> getHotelVenues() {
        return hotelVenues;
    }

    public Membership getMembership() {
        return membership;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public List<Voucher> getVouchers() {
        return vouchers;
    }

    public void setVouchers(List<Voucher> vouchers) {
        this.vouchers = vouchers;
    }
}
