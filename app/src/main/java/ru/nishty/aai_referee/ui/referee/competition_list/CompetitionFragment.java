package ru.nishty.aai_referee.ui.referee.competition_list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import ru.nishty.aai_referee.R;
import ru.nishty.aai_referee.db.referee.DataBaseHelperReferee;
import ru.nishty.aai_referee.db.secretary.DataBaseHelperSecretary;
import ru.nishty.aai_referee.entity.referee.Competition;
import ru.nishty.aai_referee.entity.referee.Performance;
import ru.nishty.aai_referee.entity.secretary.CompetitionSecretary;
import ru.nishty.aai_referee.listeners.ScanListener;
import ru.nishty.aai_referee.ui.referee.competition_list.placeholder.CompetitionContent;

/**
 * A fragment representing a list of Items.
 */
public class CompetitionFragment extends Fragment {


    RecyclerView recyclerView;
    private FloatingActionButton fab2;

    FloatingActionButton scanBtn;
    private static final int PICK_IMAGE_REQUEST = 1;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private MyCompetitionRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CompetitionFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static CompetitionFragment newInstance(int columnCount) {
        CompetitionFragment fragment = new CompetitionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_competition_list, container, false);

        fab2 = view.findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Открываете галерею для выбора изображения
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
            }
        });

        recyclerView = view.findViewById(R.id.list);

        fill();

        adapter = new MyCompetitionRecyclerViewAdapter(CompetitionContent.ITEMS, new MyCompetitionRecyclerViewAdapter.OnStateClickListener() {
            @Override
            public void onStateClick(Competition competition, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("i",competition.getUuid());
                NavHostFragment.findNavController(CompetitionFragment.this)
                        .navigate(R.id.action_competitionFragment_to_fragmentPerformance,bundle);
            }

        });


        CompetitionContent.setScanListener(new ScanListener() {
            @Override
            public void onScan() {
                update();
            }
        });

        // Set the adapter

        Context context = view.getContext();

        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        recyclerView.setAdapter(adapter);


        scanBtn = view.findViewById(R.id.fab);

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompetitionContent.onClick();
            }
        });

        return view;
    }

    public void update(){
        fill();
        adapter.update();
    }

    public void fill(){
        DataBaseHelperReferee dataBaseHelperReferee = new DataBaseHelperReferee(getContext());
        SQLiteDatabase db = dataBaseHelperReferee.getReadableDatabase();
        CompetitionContent.fill(dataBaseHelperReferee.getCompetitions(db));
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            // Получаем URI выбранного изображения из галереи
            Uri imageUri = data.getData();

            // Загружаем выбранное изображение из галереи
            Bitmap bitmap = getBitmapFromUri(imageUri);

            // Проверяем, удалось ли загрузить изображение
            if (bitmap != null) {
                // Сканируем изображение на наличие QR-кода
                Barcode qrCode = scanQRCode(bitmap);

                // Проверяем, найден ли QR-код на изображении
                if (qrCode != null) {
                    // Обрабатываем содержимое QR-кода
                    String qrContent = qrCode.displayValue;
                    handleQRContent(qrContent);
                } else {
                    Toast.makeText(requireContext(), "QR Code not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            // Загружаем изображение из URI
            InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Barcode scanQRCode(Bitmap bitmap) {
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(getContext())
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        if (!barcodeDetector.isOperational()) {
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(requireContext(), "Could not set up the barcode detector", Toast.LENGTH_SHORT).show();
                }
            });
            return null;
        }


        Frame frame = new Frame.Builder().setBitmap(bitmap).build();

        SparseArray<Barcode> barcodes = barcodeDetector.detect(frame);

        if (barcodes != null && barcodes.size() > 0) {
            return barcodes.valueAt(0);
        } else {
            return null;
        }
    }

    private void handleQRContent(String qrContent) {
        Gson gson = new Gson();
        if (isCompetitionQR(qrContent)) { // QR-код соревнования
            Competition competition = gson.fromJson(qrContent, Competition.class);
            DataBaseHelperReferee dataBaseHelperReferee = new DataBaseHelperReferee(getContext());
            SQLiteDatabase dbr = dataBaseHelperReferee.getWritableDatabase();
            dataBaseHelperReferee.addCompetition(dbr, competition);
            dbr.close();
            dataBaseHelperReferee.close();

            CompetitionSecretary competition1 = gson.fromJson(qrContent, CompetitionSecretary.class);
            DataBaseHelperSecretary dataBaseHelperSecretary = new DataBaseHelperSecretary(getContext());
            SQLiteDatabase dbs = dataBaseHelperSecretary.getWritableDatabase();
            dataBaseHelperSecretary.addCompetition(dbs, competition1);
            dbs.close();
            dataBaseHelperSecretary.close();
            CompetitionContent.onScan();
        } else if (isPerformanceQR(qrContent)) {  // QR-код performance
            Performance performance = gson.fromJson(qrContent, Performance.class);
            String competitionUuid = performance.getComp_id();

            DataBaseHelperReferee dataBaseHelperReferee = new DataBaseHelperReferee(getContext());
            SQLiteDatabase db = dataBaseHelperReferee.getWritableDatabase();
            Competition competition = dataBaseHelperReferee.getCompetitionByUuid(db, competitionUuid);

            if (competition != null) {
                dataBaseHelperReferee.addPerformance(db, competition.getUuid(), performance);
            }

            db.close();
            dataBaseHelperReferee.close();
            CompetitionContent.onScan();
        }else {
            Toast.makeText(requireContext(), "Выберите QR-код игры или соревнования", Toast.LENGTH_SHORT).show();
        }
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
    public boolean isPerformanceQR(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return jsonObject.has("pl") && jsonObject.has("p") && jsonObject.has("t") && jsonObject.has("d");
        } catch (JSONException e) {
            Log.e("QR Parsing", "Invalid JSON format", e);
            return false;
        }
    }


}
