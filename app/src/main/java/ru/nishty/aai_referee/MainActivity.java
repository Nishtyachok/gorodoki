package ru.nishty.aai_referee;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
import ru.nishty.aai_referee.db.referee.DataBaseHelper;
import ru.nishty.aai_referee.entity.referee.Competition;
import ru.nishty.aai_referee.listeners.ScanListener;
import ru.nishty.aai_referee.ui.referee.competition_list.placeholder.CompetitionContent;

public class MainActivity extends AppCompatActivity {


    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        setToolbar();

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

        // Перейдите к фрагменту fragment_role_selection, но сделайте это после завершения загрузки фрагментов
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                // Проверяем, что достигли нужного фрагмента
                if (destination.getId() == R.id.fragment_role_selection) {
                    // Ваш код для дополнительных действий при достижении fragmentRoleSelection
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), R.string.cancelled, Toast.LENGTH_SHORT).show();
            } else {
                // if the intentResult is not null we'll set
                // the content and format of scan message

                Gson gson = new Gson();
                Competition competition;
                competition = gson.fromJson(intentResult.getContents(),Competition.class);

                DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());

                SQLiteDatabase db = dataBaseHelper.getWritableDatabase();

                dataBaseHelper.addCompetition(db, competition);
                db.close();
                dataBaseHelper.close();

                CompetitionContent.onScan();

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}






