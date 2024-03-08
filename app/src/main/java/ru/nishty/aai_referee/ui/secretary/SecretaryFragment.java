package ru.nishty.aai_referee.ui.secretary;

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
import ru.nishty.aai_referee.entity.secretary.Competition;
import ru.nishty.aai_referee.entity.secretary.Performance;

import java.util.UUID;

public class SecretaryFragment extends Fragment {

    private ImageView qrCodeIV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Инфлейтим макет фрагмента
        View view = inflater.inflate(R.layout.fragment_activity_main_secretary, container, false);
        qrCodeIV = view.findViewById(R.id.idIVQrcode);

        generateQRCode();

        return view;
    }

    private void generateQRCode() {
        Competition competition = new Competition();
        competition.setUuid(UUID.randomUUID());
        competition.setDiscipline(1);
        competition.setName("13.04 - 17.04 2023");
        competition.setYear("Championship TO");
        competition.setPlace("Tomsk region, Seversk");

        Performance performance = new Performance();
        performance.setName("Sahapov Artem");
        performance.setGrade("kMS");
        performance.setRegion("Tomsk region");
        performance.setPlace("Novosobornaya 10");
        performance.setDate("13.04.2023");
        performance.setPlayground("playground 2");
        performance.setCategory("(eldest boys)");
        performance.setTime("13:15");
        competition.addPerformance(performance);

        String str;
        Gson gson = new Gson();
        str = gson.toJson(competition);
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
