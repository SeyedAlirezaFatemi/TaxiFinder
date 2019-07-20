package ir.sharif.taxifinder.webservice.webservices.driverDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DriverDetailResponse {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private DriverDetail driverDetail;

    public DriverDetailResponse(Integer code, String message, DriverDetail driverDetail) {
        this.code = code;
        this.message = message;
        this.driverDetail = driverDetail;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DriverDetail getDriverDetail() {
        return driverDetail;
    }

    public void setDriverDetail(DriverDetail driverDetail) {
        this.driverDetail = driverDetail;
    }

}