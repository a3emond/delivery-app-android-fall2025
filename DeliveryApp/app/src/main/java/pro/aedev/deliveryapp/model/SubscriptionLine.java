package pro.aedev.deliveryapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SubscriptionLine implements Parcelable {

    private int id;
    private int subscriptionId;
    private int productId;
    private int quantity;

    public SubscriptionLine() {}

    public SubscriptionLine(int id, int subscriptionId, int productId, int quantity) {
        this.id = id;
        this.subscriptionId = subscriptionId;
        this.productId = productId;
        this.quantity = quantity;
    }

    protected SubscriptionLine(Parcel in) {
        id = in.readInt();
        subscriptionId = in.readInt();
        productId = in.readInt();
        quantity = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(subscriptionId);
        dest.writeInt(productId);
        dest.writeInt(quantity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SubscriptionLine> CREATOR =
            new Creator<SubscriptionLine>() {
                @Override
                public SubscriptionLine createFromParcel(Parcel in) {
                    return new SubscriptionLine(in);
                }

                @Override
                public SubscriptionLine[] newArray(int size) {
                    return new SubscriptionLine[size];
                }
            };

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getSubscriptionId() { return subscriptionId; }
    public void setSubscriptionId(int subscriptionId) { this.subscriptionId = subscriptionId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
