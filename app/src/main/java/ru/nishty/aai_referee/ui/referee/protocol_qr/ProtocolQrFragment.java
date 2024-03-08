package ru.nishty.aai_referee.ui.referee.protocol_qr;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import ru.nishty.aai_referee.R;
import ru.nishty.aai_referee.entity.referee.Protocol;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProtocolQrFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProtocolQrFragment extends Fragment {


    private static final String ARG_PROTOCOL = "protocol";
    private static final String ARG_DISCIPLINE = "discipline";
    private static final String ARG_NAME = "name";
    private static Protocol protocol;
    private static int discipline;
    private static String name;

    public ProtocolQrFragment() {
        // Required empty public constructor
    }



    public static ProtocolQrFragment newInstance(Protocol protocol, int discipline) {
        ProtocolQrFragment fragment = new ProtocolQrFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PROTOCOL, protocol);
        args.putInt(ARG_DISCIPLINE,discipline);
        args.putString(ARG_NAME,name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            protocol = (Protocol) getArguments().getSerializable(ARG_PROTOCOL);
            discipline = getArguments().getInt(ARG_DISCIPLINE);
            name = getArguments().getString(ARG_NAME);
        }
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Bundle bundle = new Bundle();
                bundle.putSerializable("id",String.valueOf(protocol.getComp_id()));
                bundle.putInt("discipline",discipline);
                NavHostFragment.findNavController(
                        ProtocolQrFragment.this
                ).navigate(R.id.action_protocolQrFragment_to_fragmentPerformance,bundle);
            }

        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this,callback);

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_protocol_qr, container, false);
        ImageView qrCodeIV = view.findViewById(R.id.idIVQrcode);
        getActivity().getActionBar();
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(name);
        

        String str;
        Gson gson = new Gson();
        str = gson.toJson(protocol);
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