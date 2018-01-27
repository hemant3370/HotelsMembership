
package loyaltywallet.com.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import loyaltywallet.com.Model.Hotel.Hotel;

public class HotelsResponse {

    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("content")
    @Expose
    private List<Hotel> content = null;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Hotel> getContent() {
        return content;
    }

    public void setContent(List<Hotel> content) {
        this.content = content;
    }

}
