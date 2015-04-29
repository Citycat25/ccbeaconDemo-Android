package biz.citycat.ccbeacondemo;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.ccbeacon.beacon.Beacon;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends Activity {
    public static final String KEY_UUID = "uuid";
    public static final String KEY_MAJOR = "major";
    public static final String KEY_MINOR = "minor";

    private ListView mDetectList;
    ArrayList<HashMap<String, String>> BeaconsList;
    private ListViewAdapter listadapter;
    private ArrayList<ListItem> itemlist = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemlist = new ArrayList<ListItem>();

        BeaconsList = new ArrayList<HashMap<String, String>>();
        BeaconsList.addAll(getFindBeaconsList());

        initList();
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.
                    getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
        }
    }

    public void initList() {
        listadapter = new ListViewAdapter(this, R.layout.listitem_demo, itemlist);
        mDetectList = (ListView) findViewById(R.id.listView);
        mDetectList.setAdapter(listadapter);
    }


    @Override
    protected void onStart() {
        super.onStart();

        BeaconsList.clear();
        ((ccbeaconService) this.getApplication()).clearFinddollList();

        listadapter.clear();
        listadapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Tell the Application not to pass off ranging updates to this activity
        ((ccbeaconService) this.getApplication()).setActive(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tell the Application to pass off ranging updates to this activity
        ((ccbeaconService) this.getApplication()).setActive(this);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    //  Beacon App
    public ArrayList<HashMap<String, String>> getFindBeaconsList() {
        return ((ccbeaconService) this.getApplication()).getfindDolldollList();
    }

    public void didFindBeaconInRegion(Beacon beacon, double distance) {
        AddNewBeaconProcess(beacon.getId1().toString(), beacon.getId2().toString(), beacon.getId3().toString(), distance);

    }

    public void AddNewBeaconProcess(final String id1, final String id2, final String id3, final double distance) {

        runOnUiThread(new Runnable() {
            public void run() {
                getData(id1 + " - " + id2 + " - " + id3, id1, id2, id3, distance);

            }
        });
    }


    public void getData(String result, String uuid, String major, String minor, double distance) {
        HashMap<String, String> song = null;
        String _uuid, _major, _minor;
        boolean is_bankfind = false;
        double distan = distance;

        ListItem item = new ListItem(result, " 거리 : " + String.format("%.3f", distan) + "m ");

        for (int i = 0; i < BeaconsList.size(); i++) {
            song = BeaconsList.get(i);

            _uuid = song.get(KEY_UUID);
            _major = song.get(KEY_MAJOR);
            _minor = song.get(KEY_MINOR);

            if ((_uuid.equals(uuid) == true) && (_major.equals(major) == true) && (_minor.equals(minor) == true)) {
                is_bankfind = true;
                listadapter.notifyDataSetInvalidated();
                item.setDistance(" 거리 : " + String.format("%.3f", distan) + "m ");
                itemlist.remove(i);
                itemlist.add(i, item);
                listadapter.notifyDataSetChanged();

                break;
            }
        }

        if (is_bankfind == false) {
            ListItem listitem = new ListItem(result, String.valueOf(item.getDistance()));
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(MainActivity.KEY_UUID, uuid);
            map.put(MainActivity.KEY_MAJOR, major);
            map.put(MainActivity.KEY_MINOR, minor);
            BeaconsList.add(map);
            itemlist.add(listitem);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.bt_clear) {
            BeaconsList.clear();
            ((ccbeaconService) this.getApplication()).clearFinddollList();
            listadapter.clear();
            listadapter.notifyDataSetChanged();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);

    }
}
