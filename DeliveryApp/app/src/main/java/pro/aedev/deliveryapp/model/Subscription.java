package pro.aedev.deliveryapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Subscription implements Parcelable {

    private int id;
    private int clientId;
    private int routeId;
    private String address;
    private String startDate;
    private String endDate;

    public Subscription() {}

    public Subscription(int id, int clientId, int routeId,
                        String address, String startDate, String endDate) {
        this.id = id;
        this.clientId = clientId;
        this.routeId = routeId;
        this.address = address;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    protected Subscription(Parcel in) {
        id = in.readInt();
        clientId = in.readInt();
        routeId = in.readInt();
        address = in.readString();
        startDate = in.readString();
        endDate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(clientId);
        dest.writeInt(routeId);
        dest.writeString(address);
        dest.writeString(startDate);
        dest.writeString(endDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Subscription> CREATOR = new Creator<Subscription>() {
        @Override
        public Subscription createFromParcel(Parcel in) {
            return new Subscription(in);
        }

        @Override
        public Subscription[] newArray(int size) {
            return new Subscription[size];
        }
    };

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }

    public int getRouteId() { return routeId; }
    public void setRouteId(int routeId) { this.routeId = routeId; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
}
