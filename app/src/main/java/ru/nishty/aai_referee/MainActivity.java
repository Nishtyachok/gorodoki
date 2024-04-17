package ru.nishty.aai_referee;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import ru.nishty.aai_referee.db.referee.DataBaseHelperReferee;
import ru.nishty.aai_referee.db.secretary.DataBaseHelperSecretary;
import ru.nishty.aai_referee.entity.referee.Competition;
import ru.nishty.aai_referee.entity.referee.Performance;
import ru.nishty.aai_referee.entity.secretary.CompetitionSecretary;
import ru.nishty.aai_referee.listeners.ScanListener;
import ru.nishty.aai_referee.ui.referee.competition_list.placeholder.CompetitionContent;

public class MainActivity extends AppCompatActivity {

    private int currentApiVersion;

    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        currentApiVersion = android.os.Build.VERSION.SDK_INT;
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;


        if(currentApiVersion >= Build.VERSION_CODES.KITKAT)
        {

            getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = getWindow().getDecorView();

            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
                    {
                        @Override
                        public void onSystemUiVisibilityChange(int visibility)
                        {
                            if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                            {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }

        setContentView(R.layout.activity_main);
        AppBarConfiguration appBarConfiguration;

        FragmentManager fragmentManager = getSupportFragmentManager();

        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph())
                        .build();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);




        CompetitionContent.setClickListener(new ScanListener() {
            @Override
            public void onScan() {
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.setDesiredBarcodeFormats(String.valueOf(BarcodeFormat.QR_CODE));
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.initiateScan();
            }
        });

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.fragment_role_selection) {
                }
            }
        });
    }

    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus)
        {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();



        if (id == R.id.action_settings) {

        }
        if (id == android.R.id.home){
            NavController navController = Navigation.findNavController(this, R.id.toolbar);
            navController.navigateUp();

        }

        return super.onOptionsItemSelected(item);
    }

    public void setToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.onBackPressed();
            }
        });
    }

    public boolean isCompetitionQR(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return jsonObject.has("y");
        } catch (JSONException e) {
            Log.e("QR Parsing", "Invalid JSON format", e);
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), R.string.cancelled, Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                if (isCompetitionQR(intentResult.getContents())) { // QR-код соревнования
                    Competition competition = gson.fromJson(intentResult.getContents(), Competition.class);
                    DataBaseHelperReferee dataBaseHelperReferee = new DataBaseHelperReferee(getApplicationContext());
                    SQLiteDatabase dbr = dataBaseHelperReferee.getWritableDatabase();
                    dataBaseHelperReferee.addCompetition(dbr, competition);
                    dbr.close();
                    dataBaseHelperReferee.close();

                    CompetitionSecretary competition1 = gson.fromJson(intentResult.getContents(), CompetitionSecretary.class);
                    DataBaseHelperSecretary dataBaseHelperSecretary = new DataBaseHelperSecretary(getApplicationContext());
                    SQLiteDatabase dbs = dataBaseHelperSecretary.getWritableDatabase();
                    dataBaseHelperSecretary.addCompetition(dbs, competition1);
                    dbs.close();
                    dataBaseHelperSecretary.close();

                    String competitionUuid = competition.getUuid();
                } else { // QR-код performance
                    Performance performance = gson.fromJson(intentResult.getContents(), Performance.class);
                    String competitionUuid = performance.getComp_id();

                    DataBaseHelperReferee dataBaseHelperReferee = new DataBaseHelperReferee(getApplicationContext());
                    SQLiteDatabase db = dataBaseHelperReferee.getWritableDatabase();
                    Competition competition = dataBaseHelperReferee.getCompetitionByUuid(db, competitionUuid);

                    if (competition != null) {
                        dataBaseHelperReferee.addPerformance(db, competition.getUuid(), performance);
                    }

                    db.close();
                    dataBaseHelperReferee.close();
                }

                CompetitionContent.onScan();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}






