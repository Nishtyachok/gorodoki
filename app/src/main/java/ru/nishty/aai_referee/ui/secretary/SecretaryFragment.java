package ru.nishty.aai_referee.ui.secretary;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import ru.nishty.aai_referee.R;
import ru.nishty.aai_referee.db.secretary.DataBaseHelperSecretary;
import ru.nishty.aai_referee.entity.secretary.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SecretaryFragment extends Fragment {

    private ImageView qrCodeIV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Инфлейтим макет фрагмента
        View view = inflater.inflate(R.layout.fragment_activity_main_secretary, container, false);
        qrCodeIV = view.findViewById(R.id.idIVQrcode);
        generateDB();
        //generateQRCode();

        return view;
    }

    private void generateDB() {
        CompetitionSecretary competitionSecretary = new CompetitionSecretary();
        competitionSecretary.setUuid(UUID.randomUUID());
        competitionSecretary.setName("Championship TO");
        competitionSecretary.setYear("13.04 - 17.04 2023");
        competitionSecretary.setPlace("Tomsk region, Seversk");
        competitionSecretary.setHeadJudge("Sergey Kon");
        competitionSecretary.setHeadSecretary("Dima Ivan");

        DataBaseHelperSecretary dataBaseHelperSecretary = new DataBaseHelperSecretary(getContext());
        SQLiteDatabase dbr = dataBaseHelperSecretary.getWritableDatabase();

        Category category = new Category();
        category.setComp_id(competitionSecretary.getUuid());
        category.setName("Woman");
        category.setTours(3);
        category.setFigures(15);
        category.setLimit(30);
        long cat = dataBaseHelperSecretary.addCategory(dbr, category, competitionSecretary.getUuid());

        Region region = new Region();
        region.setComp_id(competitionSecretary.getUuid());
        region.setName("Tomsk");
        long reg = dataBaseHelperSecretary.addRegion(dbr, region, competitionSecretary.getUuid());

        Judge judge = new Judge();
        judge.setComp_id(competitionSecretary.getUuid());
        judge.setName("Serega Kon");
        judge.setRegion("Zalupa");
        judge.setCategory("0");
        long jud =dataBaseHelperSecretary.addJudge(dbr, judge, competitionSecretary.getUuid());

        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Player player = new Player(); // Создание нового экземпляра игрока на каждой итерации

            player.setComp_id(competitionSecretary.getUuid());
            player.setCategoryId((int) cat);
            player.setRegionId((int) reg);
            player.setGrade(2);
            player.setName("SerGay"+i);
            long playerId = dataBaseHelperSecretary.addPlayer(dbr, player); // Добавление игрока и получение его идентификатора
            player.setId((int)playerId);
            players.add(player);
        }

        dataBaseHelperSecretary.addCompetition(dbr, competitionSecretary);

        PerformanceSecretary performanceSecretary = new PerformanceSecretary();
        performanceSecretary.setComp_id(competitionSecretary.getUuid());
        performanceSecretary.setPlace("Tut");
        performanceSecretary.setDate("13.04 - 17.04 2023");
        performanceSecretary.setPlayground("tut 1");
        performanceSecretary.setPlayers(players);
        performanceSecretary.setJudgeId((int)jud);
        performanceSecretary.setTime("15:00");
        dataBaseHelperSecretary.addPerformance(dbr, performanceSecretary, players);





        dbr.close();
        dataBaseHelperSecretary.close();

    }

    private void generateQRCode() {
        CompetitionSecretary competitionSecretary = new CompetitionSecretary();
        competitionSecretary.setUuid(UUID.randomUUID());
        competitionSecretary.setName("13.04 - 17.04 2023");
        competitionSecretary.setYear("Championship TO");
        competitionSecretary.setPlace("Tomsk region, Seversk");
        /*
        PerformanceSecretary performanceSecretary = new PerformanceSecretary();
        performance.setName("Sahapov Artem");
        performance.setGrade("kMS");
        performanceSecretary.setRegionId(1);
        performanceSecretary.setPlace("Novosobornaya 10");
        performanceSecretary.setDate("13.04.2023");
        performanceSecretary.setPlayground("playground 2");
        //performanceSecretary.setCategoryId(1);
        performanceSecretary.setTime("13:15");
        competitionSecretary.addPerformance(performanceSecretary);
    */
        String str;
        Gson gson = new Gson();
        str = gson.toJson(competitionSecretary);
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(str, BarcodeFormat.QR_CODE, 1000, 1000);

            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrCodeIV.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
