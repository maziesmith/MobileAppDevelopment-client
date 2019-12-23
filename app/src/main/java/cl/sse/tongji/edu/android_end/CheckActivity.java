package cl.sse.tongji.edu.android_end;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.ArrayList;
import java.util.List;

import cl.sse.tongji.edu.android_end.model.Check;
import cl.sse.tongji.edu.android_end.model.Location;
import cl.sse.tongji.edu.android_end.presenter.check.CheckAdapter;
import cl.sse.tongji.edu.android_end.presenter.check.CheckPresenter;

public class CheckActivity extends AppCompatActivity {

    public LocationClient mLocationClient;
    private Location location;

    TextView loc_type_text;
    TextView cou_pro_text;
    TextView cit_street_text;
    RecyclerView check_recycler;

    CheckPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        setContentView(R.layout.activity_check);

        loc_type_text = findViewById(R.id.loc_type_text);
        cou_pro_text = findViewById(R.id.cou_pro_text);
        cit_street_text = findViewById(R.id.city_street_text);
        check_recycler = findViewById(R.id.check_recycler);

        presenter = new CheckPresenter(this);
        presenter.initCheckList();

        location = new Location();
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(CheckActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(CheckActivity.this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(CheckActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(CheckActivity.this, permissions, 1);
        } else {
            Log.d("CheckActivity","Start");
            requestLocation();
        }

    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(final BDLocation bdLocation) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("CheckActivity","Update Location");
                    location.latitude = bdLocation.getLatitude();
                    location.longitude = bdLocation.getLongitude();
                    location.country = bdLocation.getCountry();
                    location.province = bdLocation.getProvince();
                    location.city = bdLocation.getCity();
                    location.district = bdLocation.getDistrict();
                    location.street = bdLocation.getStreet();

                    if (location.loc_type == BDLocation.TypeGpsLocation) {
                        loc_type_text.setText("Locate Type: GPS");
                    } else if (location.loc_type == BDLocation.TypeNetWorkLocation) {
                        loc_type_text.setText("Locate Type: Network");
                    }
                    cou_pro_text.setText("Longitude "+ location.longitude);
                    cit_street_text.setText("Latitude "+location.latitude);
                }
            });
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0){
                    for (int result : grantResults){
                        if (result!=PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this, "权限不足",Toast.LENGTH_LONG).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this, "未知错误",Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
                default:
        }
    }

    public void loadAllChecks(List<Check>checks){
        check_recycler = findViewById(R.id.check_recycler);
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        check_recycler.setLayoutManager(manager);
        CheckAdapter adapter = new CheckAdapter(checks);
        check_recycler.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }
}
