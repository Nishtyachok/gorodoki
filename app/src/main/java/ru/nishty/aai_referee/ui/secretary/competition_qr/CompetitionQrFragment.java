package ru.nishty.aai_referee.ui.secretary.competition_qr;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.UUID;

import ru.nishty.aai_referee.R;
import ru.nishty.aai_referee.entity.secretary.CompetitionSecretary;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompetitionQrFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompetitionQrFragment extends Fragment {

    private static final String ARG_COMPETITION_SECRETARY = "competition";
    private static CompetitionSecretary competitionSecretary;

    public CompetitionQrFragment() {
    }

    public static CompetitionQrFragment newInstance(CompetitionSecretary competitionSecretary) {
        CompetitionQrFragment fragment = new CompetitionQrFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_COMPETITION_SECRETARY, competitionSecretary);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            competitionSecretary = (CompetitionSecretary) getArguments().getSerializable(ARG_COMPETITION_SECRETARY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_protocol_qr, container, false);
        ImageView qrCodeIV = view.findViewById(R.id.idIVQrcode);

        QrCodeData qrData = new QrCodeData();
        qrData.setUuid(competitionSecretary.getUuid());
        qrData.setName(competitionSecretary.getName());
        qrData.setPlace(competitionSecretary.getPlace());
        qrData.setYear(competitionSecretary.getYear());

        Gson gson = new Gson();
        String json = gson.toJson(qrData);
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(json, BarcodeFormat.QR_CODE, 1000, 1000);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrCodeIV.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return view;
    }
}

class QrCodeData {
    @SerializedName("i")
    private UUID uuid;
    @SerializedName("n")
    private String name;
    @SerializedName("y")
    private String year;
    @SerializedName("p")
    private String place;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}

