package pro.aedev.deliveryapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Route implements Parcelable {

    private int id;
    private String label;
    private Integer delivererId;

    public Route() {}

    public Route(int id, String label, Integer delivererId) {
        this.id = id;
        this.label = label;
        this.delivererId = delivererId;
    }

    protected Route(Parcel in) {
        id = in.readInt();
        label = in.readString();
        delivererId = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(label);
        dest.writeValue(delivererId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Route> CREATOR = new Creator<Route>() {
        @Override
        public Route createFromParcel(Parcel in) {
            return new Route(in);
        }

        @Override
        public Route[] newArray(int size) {
            return new Route[size];
        }
    };

    //-----------------------------------------------------
    // Getters and Setters
    //-----------------------------------------------------
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public Integer getDelivererId() { return delivererId; }
    public void setDelivererId(Integer delivererId) { this.delivererId = delivererId; }
}
