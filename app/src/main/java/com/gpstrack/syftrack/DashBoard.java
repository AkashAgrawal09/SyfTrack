package com.gpstrack.syftrack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.gpstrack.syftrack.Fragment.DashbOard;
import com.gpstrack.syftrack.Fragment.StoppageSummary;
import com.gpstrack.syftrack.Fragment.TravelSummary;
import com.gpstrack.syftrack.Fragment.VehicleStatus;
import com.gpstrack.syftrack.Utils.AppUtils;
import com.gpstrack.syftrack.Utils.FragUtilis;
import com.gpstrack.syftrack.Utils.Online;

public class DashBoard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    DrawerLayout drawer;
    Toolbar toolbar;
    private static final int REQUEST= 112;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        fragmentManager=getSupportFragmentManager();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        oo();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getFragmentManager().getBackStackEntryCount() > 1) {
                getFragmentManager().popBackStack();
            }
            else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout)
        {
            if(new AppUtils(DashBoard.this).getlogintype().equalsIgnoreCase("number"))
            {
                new AppUtils(DashBoard.this).setnumberremeber(false);
                Intent i = new Intent(DashBoard.this, OTP.class);
                startActivity(i);
            }
            else {
                new AppUtils(DashBoard.this).setLogSkipStatus(false);
                Intent i = new Intent(DashBoard.this, LoginActivity.class);
                startActivity(i);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment=null;
        if (id == R.id.dashboard)
        {

            fragment=new DashbOard();
           // FragUtilis.openFrag(DashBoard.this,R.id.fm,new DashbOard(),null);
        } else if (id == R.id.vehicle_stats)
        {

            fragment=new VehicleStatus("25");
           // FragUtilis.openFrag(DashBoard.this,R.id.fm,new VehicleStatus("25"),null);
        }
        else if (id == R.id.nearst_vehicle)
        {
            String[] PERMISSIONS = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION};

            if (!hasPermissions(DashBoard.this, PERMISSIONS)) {
                ActivityCompat.requestPermissions((Activity) this, PERMISSIONS, REQUEST );
            }
            else
            {
                startActivity(new Intent(DashBoard.this,NearstVehicle.class));
            }
        }
        else if (id == R.id.travel_summary)
        {

            fragment=new TravelSummary();
          //  FragUtilis.openFrag(DashBoard.this,R.id.fm,new TravelSummary(),null);
        } else if (id == R.id.distance_report)
        {
            startActivity( new Intent( DashBoard.this,DistanceReport.class ) );
        } else if (id == R.id.stoppagereport)
        {

            fragment=new StoppageSummary();
         //   FragUtilis.openFrag(DashBoard.this,R.id.fm,new StoppageSummary(),null);
        }else if (id == R.id.settings) {
            Intent as=new Intent(DashBoard.this,SettingActivity.class);
            startActivity(as);
        }
        if (fragment != null) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fm, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void oo(){
        if(Online.isOnline(DashBoard.this))
        {

          //  fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fm, new DashbOard());
          //  fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else{
            Snackbar snackbar1 = Snackbar.make(drawer, "No Internet Connection. Please check your internet connection", Snackbar.LENGTH_LONG);
            snackbar1.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(DashBoard.this,NearstVehicle.class));
                } else
                {
                    Toast.makeText(DashBoard.this, "PERMISSIONS Denied", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    public static boolean hasPermissions(Context context, String... permissions)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null)
        {
            for (String permission : permissions)
            {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                {
                    return false;
                }
            }

        }

        return true;
    }
}
