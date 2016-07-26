package com.sadhika.azurelocalsearch.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class LocationSearchResult implements Parcelable{

    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Latitude")
    @Expose
    private Double latitude;
    @SerializedName("Longitude")
    @Expose
    private Double longitude;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("ArrivalTime")
    @Expose
    private String arrivalTime;

    protected LocationSearchResult(Parcel in) {
        iD = in.readInt();
        name = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        address = in.readString();
        arrivalTime = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(iD);
        dest.writeString(name);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(address);
        dest.writeString(arrivalTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LocationSearchResult> CREATOR = new Creator<LocationSearchResult>() {
        @Override
        public LocationSearchResult createFromParcel(Parcel in) {
            return new LocationSearchResult(in);
        }

        @Override
        public LocationSearchResult[] newArray(int size) {
            return new LocationSearchResult[size];
        }
    };

    /**
     *
     * @return
     * The iD
     */
    public Integer getID() {
        return iD;
    }

    /**
     *
     * @param iD
     * The ID
     */
    public void setID(Integer iD) {
        this.iD = iD;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The latitude
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     *
     * @param latitude
     * The Latitude
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     *
     * @return
     * The longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     *
     * @param longitude
     * The Longitude
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     *
     * @return
     * The address
     */
    public String getAddress() {
        return address;
    }

    /**
     *
     * @param address
     * The Address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     *
     * @return
     * The arrivalTime
     */
    public String getArrivalTime() {
        return arrivalTime;
    }

    public long getArrivalTimeStamp() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(arrivalTime);
        } catch (ParseException e) {
        }
        return  null == date ? -1 : date.getTime();
    }


    public String getRemainingTimeToArrive() {

        long timeToReach = getArrivalTimeStamp() - new Date().getTime();
        return timeStampToReadableFormat(timeToReach);
    }

    private String timeStampToReadableFormat(long timeStamp) {
        StringBuilder sb = new StringBuilder();
        long days = TimeUnit.MILLISECONDS.toDays(timeStamp);
        if (days > 0) {
            sb.append(days + " days ");
        }
        long hours = TimeUnit.MILLISECONDS.toHours(timeStamp) -
                TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toHours(timeStamp));

        if (hours > 0) {
            sb.append(hours + " hours ");
        }

        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeStamp) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeStamp));
        if (minutes > 0) {
            sb.append(minutes + " minutes ");
        }

        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeStamp) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeStamp));

        if (seconds > 0 ) {
            sb.append(seconds + " seconds");
        }

        return sb.toString();
    }
    /**
     *
     * @param arrivalTime
     * The ArrivalTime
     */
    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

}
