package biz.citycat.ccbeacondemo;

import android.app.Application;
import android.os.RemoteException;
import android.util.Log;

import org.ccbeacon.beacon.Beacon;
import org.ccbeacon.beacon.BeaconConsumer;
import org.ccbeacon.beacon.BeaconManager;
import org.ccbeacon.beacon.BeaconParser;
import org.ccbeacon.beacon.RangeNotifier;
import org.ccbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Geun on 2015-04-23.
 */
public class ccbeaconService extends Application implements BeaconConsumer {
    private static final String TAG = "ccbeaconService";
    public BeaconManager beaconManager = null;

    ArrayList<HashMap<String, String>> finddollList = new ArrayList<HashMap<String, String>>();

    private MainActivity main = null;
    private double distan;

    @Override
    public void onCreate() {
        super.onCreate();
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().clear();
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);
        beaconManager.setForegroundBetweenScanPeriod(500);

        main = new MainActivity();
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {

                if (beacons.size() > 0) {

                    for (Beacon firstBeacon : beacons) {

                        distan = firstBeacon.getDistance();
                        Log.i(TAG, firstBeacon.getId1().toString()+firstBeacon.getId2() + "************* Distant : " + String.valueOf(distan) );

                        if(main != null){
                            main.didFindBeaconInRegion(firstBeacon,distan);
                        }

                    }
                }
            }
        });
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
        }
    }


    public ArrayList<HashMap<String, String>> getfindDolldollList() {
        return this.finddollList;
    }

    public void setActive(MainActivity activity) {
        this.main = activity;
    }

    public void clearFinddollList() {
        finddollList.clear();
    }

}

