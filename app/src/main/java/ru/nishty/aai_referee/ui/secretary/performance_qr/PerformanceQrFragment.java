package ru.nishty.aai_referee.ui.secretary.performance_qr;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import ru.nishty.aai_referee.R;
import ru.nishty.aai_referee.entity.secretary.PerformanceSecretary;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerformanceQrFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerformanceQrFragment extends Fragment {


    private static final String ARG_PERFORMANCE_SECRETARY = "performance";
    private static PerformanceSecretary performanceSecretary;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_protocol_qr, container, false);
        ImageView qrCodeIV = view.findViewById(R.id.idIVQrcode);
        getActivity().getActionBar();
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);

        

        String str;
        Gson gson = new Gson();
        str = gson.toJson(performanceSecretary);
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(str, BarcodeFormat.QR_CODE,1000,1000);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrCodeIV.setImageBitmap(bitmap);

        }
        catch (WriterException e){
            e.printStackTrace();
        }
        return view;
    }

}