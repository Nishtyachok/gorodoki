package ru.nishty.aai_referee.ui.secretary.performance_qr;

import android.database.sqlite.SQLiteDatabase;
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
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.nishty.aai_referee.R;
import ru.nishty.aai_referee.db.secretary.DataBaseHelperSecretary;
import ru.nishty.aai_referee.entity.secretary.PerformanceSecretary;
import ru.nishty.aai_referee.entity.secretary.Player;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerformanceQrFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerformanceQrFragment extends Fragment {

    private static final String ARG_PERFORMANCE_SECRETARY = "performance";
    private static PerformanceSecretary performanceSecretary;
    private DataBaseHelperSecretary dataBaseHelperSecretary;


    public PerformanceQrFragment() {
        // Required empty public constructor
    }

    public static PerformanceQrFragment newInstance(PerformanceSecretary performanceSecretary) {
        PerformanceQrFragment fragment = new PerformanceQrFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PERFORMANCE_SECRETARY, performanceSecretary);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            performanceSecretary = (PerformanceSecretary) getArguments().getSerializable(ARG_PERFORMANCE_SECRETARY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        dataBaseHelperSecretary = new DataBaseHelperSecretary(getContext());
        SQLiteDatabase dbr = dataBaseHelperSecretary.getWritableDatabase();
        View view = inflater.inflate(R.layout.fragment_protocol_qr, container, false);
        ImageView qrCodeIV = view.findViewById(R.id.idIVQrcode);

        QrCodeData qrData = new QrCodeData();
        qrData.setComp_id(performanceSecretary.getComp_id());
        qrData.setId(performanceSecretary.getId());
        qrData.setPlace(performanceSecretary.getPlace());
        qrData.setDate(performanceSecretary.getDate());
        qrData.setPlayground(performanceSecretary.getPlayground());

        List<PlayerQrData> playerQrDataList = new ArrayList<>();
        for (Player player : performanceSecretary.getPlayers()) {
            PlayerQrData playerQrData = new PlayerQrData();
            playerQrData.setId(player.getId());
            playerQrData.setName(player.getName());
            playerQrData.setRegionId(dataBaseHelperSecretary.getRegionNameById(dbr, player.getRegionId()));
            playerQrData.setCategoryId(dataBaseHelperSecretary.getCategoryNameById(dbr, player.getCategoryId()));
            playerQrData.setGrade(player.getGrade());
            playerQrDataList.add(playerQrData);
        }
        qrData.setPlayers(playerQrDataList);

        qrData.setTime(performanceSecretary.getTime());

        Gson gson = new Gson();
        String json = gson.toJson(qrData);
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 1);
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

            BitMatrix bitMatrix = multiFormatWriter.encode(json, BarcodeFormat.QR_CODE, 1000, 1000, hints);
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
    private String comp_id;
    @SerializedName("id")
    private int id;
    @SerializedName("p")
    private String place;
    @SerializedName("d")
    private String date;
    @SerializedName("pg")
    private String playground;
    @SerializedName("pl")
    private List<PlayerQrData> players;
    @SerializedName("t")
    private String time;

    public String getComp_id() {
        return comp_id;
    }

    public void setComp_id(String comp_id) {
        this.comp_id = comp_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlayground() {
        return playground;
    }

    public void setPlayground(String playground) {
        this.playground = playground;
    }

    public List<PlayerQrData> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerQrData> players) {
        this.players = players;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

class PlayerQrData {
    @SerializedName("iid")
    private int id;
    @SerializedName("n")
    private String name;
    @SerializedName("r")
    private String regionId;
    @SerializedName("c")
    private String categoryId;
    @SerializedName("g")
    private int grade;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}