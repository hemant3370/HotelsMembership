package loyaltywallet.com.Retrofit.Services;


import loyaltywallet.com.Model.AddCardPayload;
import loyaltywallet.com.Model.AddMembershipResponse;
import loyaltywallet.com.Model.BasicResponse;
import loyaltywallet.com.Model.CardNumberPayload;
import loyaltywallet.com.Model.HotelsResponse;
import loyaltywallet.com.Model.OffersResponse;
import loyaltywallet.com.Model.RedeemPayload;
import loyaltywallet.com.Model.RoomReservationPayload;
import loyaltywallet.com.Model.TableReservationPayload;
import loyaltywallet.com.Model.VenuesResponse;
import loyaltywallet.com.Model.VerifyOTPPayload;
import loyaltywallet.com.Model.Vouchers.VouchersResponse;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by HemantSingh on 18/06/16.
 */
public interface ApiInterface {

//    //Hotels
    @GET("hotels")
    Observable<HotelsResponse> getHotels();
    //Enroll
    @POST("enrollMember/{hotelid}")
    Observable<AddMembershipResponse> enrollMember(@Body AddCardPayload payload, @Path("hotelid") String hotelid);

    @POST("memberDetail/{hotelid}")
    Observable<AddMembershipResponse> addMembership(@Body AddCardPayload payload, @Path("hotelid") String hotelid);

    @POST("vouchers/{hotelid}")
    Observable<VouchersResponse> getVouchers(@Body AddCardPayload payload, @Path("hotelid") String hotelid, @Header("auth_token") String authToken);

    @POST("sendOTP/{hotelid}")
    Observable<BasicResponse> sendOTP(@Body RedeemPayload payload, @Path("hotelid") String hotelid, @Header("auth_token") String authToken);

    @POST("redeemVoucher/{hotelid}")
    Observable<BasicResponse> redeemVoucher(@Body VerifyOTPPayload payload, @Path("hotelid") String hotelid, @Header("auth_token") String authToken);

    @POST("offers/{hotelid}")
    Observable<OffersResponse> getOffers(@Body CardNumberPayload payload, @Path("hotelid") String hotelid, @Header("auth_token") String authToken);

    @POST("venues/{hotelid}")
    Observable<VenuesResponse> getVenues(@Body CardNumberPayload payload, @Path("hotelid") String hotelid, @Header("auth_token") String authToken);

    @POST("roomReservation/{hotelid}")
    Observable<BasicResponse> bookRoom(@Body RoomReservationPayload payload, @Path("hotelid") String hotelid, @Header("auth_token") String authToken);

    @POST("tableReservation/{hotelid}")
    Observable<BasicResponse> bookTable(@Body TableReservationPayload payload, @Path("hotelid") String hotelid, @Header("auth_token") String authToken);

}
