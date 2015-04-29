package biz.citycat.ccbeacondemo;

/**
 * Created by Geun on 2015-04-01.
 */
public class ListItem {
    String BeaconId;
    String Distance;

    public ListItem(String beaconId, String distance) {
        super();
        BeaconId = beaconId;
        this.Distance = distance;
    }

    public String getBeaconId() {
        return BeaconId;
    }
    public void setBeaconId(String itemName) {
        BeaconId = itemName;
    }
    public String getDistance() {
        return Distance;
    }
    public void setDistance(String distance) {
        this.Distance = distance;
    }
}
