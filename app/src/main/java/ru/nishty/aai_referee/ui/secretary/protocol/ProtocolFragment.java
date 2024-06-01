package ru.nishty.aai_referee.ui.secretary.protocol;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.nishty.aai_referee.R;
import ru.nishty.aai_referee.db.secretary.DataBaseContractSecretary;
import ru.nishty.aai_referee.db.secretary.DataBaseHelperSecretary;
import ru.nishty.aai_referee.entity.secretary.PerformanceSecretary;
import ru.nishty.aai_referee.entity.secretary.Player;
import ru.nishty.aai_referee.entity.secretary.Protocol;

public class ProtocolFragment extends Fragment {
    private View view;
    private static final String ARG_PROTOCOL = "protocol";
    private static final String ARG_PERFORMANCE = "performance";
    private static Protocol protocol;
    private TableLayout table;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1;

    private int categoryTourById;
    private PerformanceSecretary performance;
    private ProtocolPagerAdapter pagerAdapter;
    private TableLayout tableLayout;
    private List<String> itemList;
    private int numRows, numCols;
    private List<Protocol> protocols;
    private List<Integer> protocolsId;
    private List<String> judgesName;
    private DataBaseHelperSecretary dataBaseHelperSecretary;
    private SQLiteDatabase db;

    private int currentPage = 0;

    private int position;

    public ProtocolFragment() {
    }

    public static ProtocolFragment newInstance(Protocol protocol, PerformanceSecretary performance, int position) {
        ProtocolFragment fragment = new ProtocolFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putSerializable("protocol", protocol);
        args.putSerializable("performance", performance);
        fragment.setArguments(args);
        return fragment;
    }

    private void initializeItemList(int conf) {
        switch (conf) {
            case 1:
                itemList = Arrays.asList("пушка", "вилка", "звезда", "стрела", "колодец", "коленчатый вал", "артиллерия", "ракетка", "пулемётное гнездо", "рак", "часовые", "серп", "тир", "самолёт", "письмо");
                break;
            case 2:
                itemList = Arrays.asList("пушка", "вилка", "колодец", "стрела", "артиллерия", "серп", "пулемётное гнездо", "самолёт", "тир", "письмо");
                break;
            case 3:
                itemList = Arrays.asList("пушка", "вилка", "колодец", "коленчатый вал", "артиллерия", "пулемётное гнездо", "часовые", "серп", "тир", "письмо");
                break;
            case 4:
                itemList = Arrays.asList("пушка", "вилка", "звезда", "стрела", "колодец", "коленчатый вал", "артиллерия", "ракетка", "пулемётное гнездо", "рак", "часовые", "серп", "тир", "самолёт", "факс");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            DataBaseHelperSecretary dataBaseHelperSecretary = new DataBaseHelperSecretary(getContext());
            SQLiteDatabase db = dataBaseHelperSecretary.getReadableDatabase();
            performance = (PerformanceSecretary) getArguments().getSerializable("performance");
            currentPage = getArguments().getInt("position");
            protocol = dataBaseHelperSecretary.getProtocolPlayer(db, performance.getComp_id(), performance.getId(), currentPage);
            protocolsId = dataBaseHelperSecretary.getProtocolsByPlayer(db, performance.getComp_id(), performance.getPlayers().get(currentPage).getId());
            judgesName = dataBaseHelperSecretary.getJudgesForPerformance(db, performance.getComp_id(), performance.getPlayers().get(currentPage).getId());
            protocols = dataBaseHelperSecretary.getProtocolsById(db, performance.getComp_id(), protocolsId);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        dataBaseHelperSecretary = new DataBaseHelperSecretary(getContext());
        db = dataBaseHelperSecretary.getWritableDatabase();

        if (getArguments() != null) {
            position = getArguments().getInt("position");
            performance = (PerformanceSecretary) getArguments().getSerializable(ARG_PERFORMANCE);
            currentPage = getArguments().getInt("position");
            protocol = dataBaseHelperSecretary.getProtocolPlayer(db, performance.getComp_id(), performance.getId(), currentPage);
            protocolsId = dataBaseHelperSecretary.getProtocolsByPlayer(db, performance.getComp_id(), performance.getPlayers().get(currentPage).getId());
            protocols = dataBaseHelperSecretary.getProtocolsById(db, performance.getComp_id(), protocolsId);
            judgesName = dataBaseHelperSecretary.getJudgesForPerformance(db, performance.getComp_id(), performance.getPlayers().get(currentPage).getId());
        }
        int x = performance.getPlayers().get(currentPage).getCategoryConfig();
        categoryTourById = dataBaseHelperSecretary.getCategoryTourById(db, performance.getPlayers().get(currentPage).getCategoryId());
        int xx = categoryTourById + 1;
        switch (x) {
            case 1:
            case 4:
                numRows = 15 + xx + 1;
                break;
            case 2:
            case 3:
                numRows = 10 + xx + 1;
                break;

        }
        numCols = categoryTourById * 2 + 1;
        view = inflater.inflate(R.layout.fragment_protocol, container, false);
        LinearLayout linearLayout = view.findViewById(R.id.linearLayout);

        // Получаем размеры экрана
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size1 = new Point();
        display.getSize(size1);
        int screenHeight = size1.y-80;

        // Устанавливаем высоту вашего LinearLayout в высоту экрана
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, screenHeight);
        linearLayout.setLayoutParams(params);

        table = view.findViewById(R.id.tableLayout1);
        createTable(numRows, numCols);

        List<TableRow> tableRows = new ArrayList<>();
        for (int i = 0; i < table.getChildCount(); i++) {
            View child = table.getChildAt(i);
            if (child instanceof TableRow) {
                tableRows.add((TableRow) child);
            }
        }
        int prevGamesSum1 = 0, prevGamesSum2 = 0, prevGamesSum3 = 0;
        int prevGamesSumCounter = 1;
        int size = Math.min(protocols.size(), dataBaseHelperSecretary.getCategoryTourById(db, performance.getPlayers().get(currentPage).getCategoryId()));
        for (int j = 0; j < size; j++) {
            List<List<String>> firstGameShots = parseShots(protocols.get(j).getShots1());
            List<List<String>> secondGameShots = parseShots(protocols.get(j).getShots2());

            for (int rowIndex = 1; rowIndex <= itemList.size() + 1 + categoryTourById; rowIndex++) {
                TableRow tableRow = tableRows.get(rowIndex);

                List<TextView> textViews = new ArrayList<>();
                for (int i = 0; i < tableRow.getChildCount(); i++) {
                    View child = tableRow.getChildAt(i);
                    if (child instanceof TextView) {
                        textViews.add((TextView) child);
                    }
                }
                if (x == 1 || x == 4) {
                    switch (rowIndex) {
                        case 16:
                            int g1 = protocols.get(j).getGame1();
                            int g2 = protocols.get(j).getGame2();
                            textViews.get(j * 2 + 1).setText(String.valueOf(g1));
                            textViews.get(j * 2 + 2).setText(String.valueOf(g2));
                            continue;
                        case 17:
                            int gs = protocols.get(j).getGames_sum();
                            switch (prevGamesSumCounter) {
                                case 1:
                                    prevGamesSum1 = gs;
                                case 2:
                                    prevGamesSum2 = gs;
                                case 3:
                                    prevGamesSum3 = gs;
                            }
                            prevGamesSumCounter++;
                            textViews.get(j + 1).setText(String.valueOf(gs));
                            continue;
                        case 18:
                            if (protocols.size() >= 2) {
                                switch (prevGamesSumCounter - 1) {
                                    case 1:
                                        break;
                                    case 2:
                                        textViews.get(j / 2 + 1).setText(String.valueOf(prevGamesSum1 + prevGamesSum2));
                                        break;
                                    case 3:
                                        textViews.get(j / 2 + 1).setText(String.valueOf(prevGamesSum3));
                                        break;
                                }
                                continue;
                            }
                        case 19:
                            if (protocols.size() >= 3)
                                textViews.get((int) Math.floor(j / 3 + 1)).setText(String.valueOf(prevGamesSum1 + prevGamesSum2 + prevGamesSum3));
                            continue;
                    }
                } else if (x == 2 || x == 3) {
                    switch (rowIndex) {
                        case 11:
                            int g1 = protocols.get(j).getGame1();
                            int g2 = protocols.get(j).getGame2();
                            textViews.get(j * 2 + 1).setText(String.valueOf(g1));
                            textViews.get(j * 2 + 2).setText(String.valueOf(g2));
                            continue;
                        case 12:
                            int gs = protocols.get(j).getGames_sum();
                            switch (prevGamesSumCounter) {
                                case 1:
                                    prevGamesSum1 = gs;
                                case 2:
                                    prevGamesSum2 = gs;
                                case 3:
                                    prevGamesSum3 = gs;
                            }
                            prevGamesSumCounter++;
                            textViews.get(j + 1).setText(String.valueOf(gs));
                            continue;
                        case 13:
                            if (protocols.size() >= 2) {
                                switch (prevGamesSumCounter - 1) {
                                    case 1:
                                        break;
                                    case 2:
                                        textViews.get(j / 2 + 1).setText(String.valueOf(prevGamesSum1 + prevGamesSum2));
                                        break;
                                    case 3:
                                        textViews.get(j / 2 + 1).setText(String.valueOf(prevGamesSum3));
                                        break;
                                }
                                continue;
                            }
                        case 14:
                            if (protocols.size() >= 3)
                                textViews.get((int) Math.floor(j / 3 + 1)).setText(String.valueOf(prevGamesSum1 + prevGamesSum2 + prevGamesSum3));
                            continue;
                    }
                }
                if (rowIndex < itemList.size() + 1) {
                    List<String> currentFirstGameShots = firstGameShots.get(rowIndex - 1);
                    List<String> currentSecondGameShots = secondGameShots.get(rowIndex - 1);

                    if (x == 4) {
                        currentSecondGameShots = secondGameShots.get(itemList.size() - rowIndex);
                    }
                    textViews.get(j * 2 + 1).setText(formatShots(currentFirstGameShots));
                    textViews.get(j * 2 + 2).setText(formatShots(currentSecondGameShots));
                }
            }
        }
        Button myButton = view.findViewById(R.id.myButton);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Создаем PDF и затем открываем его
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        String filePath = createPdf(); // Получаем путь к созданному PDF-файлу
                        openPdf(filePath);
                    } else {
                        // Разрешение на запись не предоставлено, запросить его у пользователя
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Ошибка при создании или открытии PDF", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }

    public void createTable(int numRows1, int numCols1) {
        table.removeAllViews(); // Удаляем все существующие строки перед созданием новых
        initializeItemList(performance.getPlayers().get(currentPage).getCategoryConfig());

        for (int i = 0; i < numRows1; i++) {
            TableRow row = new TableRow(getContext());
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

            if (i == 0) {
                int firstRowCols = (numCols1 - 1) / 2; // Число столбцов в первой строке
                for (int j = 0; j <= firstRowCols; j++) {
                    TextView tv = new TextView(getContext());
                    tv.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f));
                    tv.setBackgroundResource(R.drawable.endind_table); // Устанавливаем фон
                    tv.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.FILL_VERTICAL); // Выравнивание текста и заполнение по вертикали
                    tv.setPadding(4, 4, 4, 4);
                    tv.setSingleLine(false); // Разрешаем перенос строк
                    tv.setTextColor(ContextCompat.getColor(getContext(), R.color.black)); // Устанавливаем цвет текста
                    tv.setTypeface(null, Typeface.BOLD);
                    // Устанавливаем текст в соответствии с позицией столбца
                    if (j == 0) {
                        tv.setText("Фигуры");
                    } else if (j == 1) {
                        tv.setText("1 тур");
                    } else if (j == 2) {
                        tv.setText("2 тур");
                    } else if (j == 3) {
                        tv.setText("3 тур");
                    } else if (j == 4) {
                        tv.setText("4 тур");
                    } else if (j == 5) {
                        tv.setText("5 тур");
                    } else {
                        tv.setText(" "); // Пустая строка для остальных элементов в первой строке
                    }

                    row.addView(tv);
                }
            } else if (i == itemList.size() + 1) {
                int firstRowCols = numCols1 - 1; // Число столбцов в первой строке
                for (int j = 0; j <= firstRowCols; j++) {
                    TextView tv = new TextView(getContext());
                    float weight = (j == 0) ? 1f : 0.5f; // Устанавливаем вес в зависимости от значения j
                    tv.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, weight));
                    tv.setBackgroundResource(R.drawable.endind_table); // Устанавливаем фон
                    tv.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.FILL_VERTICAL); // Выравнивание текста и заполнение по вертикали
                    tv.setPadding(4, 4, 4, 4);
                    tv.setSingleLine(false); // Разрешаем перенос строк
                    tv.setTextColor(ContextCompat.getColor(getContext(), R.color.black)); // Устанавливаем цвет текста
                    tv.setTypeface(null, Typeface.BOLD);

                    // Устанавливаем текст в соответствии с позицией столбца
                    if (j == 0) {
                        tv.setText("Сумма");
                    } else {
                        tv.setText(" "); // Пустая строка для остальных элементов в первой строке
                    }

                    row.addView(tv);
                }
            } else if (i == itemList.size() + 2) {
                int firstRowCols = numCols1 - 1 - categoryTourById; // Число столбцов в первой строке
                for (int j = 0; j <= firstRowCols; j++) {
                    TextView tv = new TextView(getContext());
                    float weight = 1f;
                    tv.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, weight));
                    if (j != 0)
                        tv.setBackgroundResource(R.drawable.endind_table); // Устанавливаем фон
                    tv.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.FILL_VERTICAL); // Выравнивание текста и заполнение по вертикали
                    tv.setPadding(4, 4, 4, 4);
                    tv.setSingleLine(false); // Разрешаем перенос строк
                    tv.setTextColor(ContextCompat.getColor(getContext(), R.color.black)); // Устанавливаем цвет текста
                    tv.setTypeface(null, Typeface.BOLD);
                    row.addView(tv);
                }
            } else if (i == itemList.size() + 3) {
                int firstRowCols = numCols1 - 2 - categoryTourById; // Число столбцов в первой строке
                for (int j = 0; j <= firstRowCols; j++) {
                    TextView tv = new TextView(getContext());
                    float weight = 0.5f;
                    if (j == 1)
                        weight = 1;
                    tv.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, weight));
                    if (j != 0)
                        tv.setBackgroundResource(R.drawable.endind_table); // Устанавливаем фон
                    tv.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.FILL_VERTICAL); // Выравнивание текста и заполнение по вертикали
                    tv.setPadding(4, 4, 4, 4);
                    tv.setSingleLine(false); // Разрешаем перенос строк
                    tv.setTextColor(ContextCompat.getColor(getContext(), R.color.black)); // Устанавливаем цвет текста
                    tv.setTypeface(null, Typeface.BOLD);
                    row.addView(tv);
                }
            } else if (i == itemList.size() + 4) {
                int firstRowCols = numCols1 - 3 - categoryTourById; // Число столбцов в первой строке
                for (int j = 0; j <= firstRowCols; j++) {
                    TextView tv = new TextView(getContext());
                    float weight = 0.5f;
                    if (j == 1)
                        weight = 1.5f;
                    tv.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, weight));
                    if (j != 0)
                        tv.setBackgroundResource(R.drawable.endind_table); // Устанавливаем фон
                    tv.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.FILL_VERTICAL); // Выравнивание текста и заполнение по вертикали
                    tv.setPadding(4, 4, 4, 4);
                    tv.setSingleLine(false); // Разрешаем перенос строк
                    tv.setTextColor(ContextCompat.getColor(getContext(), R.color.black)); // Устанавливаем цвет текста
                    tv.setTypeface(null, Typeface.BOLD);
                    row.addView(tv);
                }
            } else { // Для остальных строк создаем столбцы в соответствии с numCols
                for (int j = 0; j < numCols1; j++) {
                    TextView tv = new TextView(getContext());
                    float weight = 0.5f;
                    if (j == 0 && i <= itemList.size()) {
                        tv.setTypeface(null, Typeface.BOLD);
                        weight = 1f;
                        String item = itemList.get(i - 1);
                        tv.setText(item);
                    }
                    tv.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, weight));
                    tv.setBackgroundResource(R.drawable.endind_table); // Устанавливаем фон
                    tv.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.FILL_VERTICAL); // Выравнивание текста и заполнение по вертикали
                    tv.setPadding(4, 4, 4, 4);
                    tv.setSingleLine(false); // Разрешаем перенос строк
                    tv.setTextColor(ContextCompat.getColor(getContext(), R.color.black)); // Устанавливаем цвет текста
                    row.addView(tv);
                }
            }

            // Проверяем на null перед добавлением в таблицу
            if (table != null) {
                table.addView(row);
            }
        }
    }


    private String formatShots(List<String> shots) {
        StringBuilder sb = new StringBuilder();
        for (String shot : shots) {
            sb.append(shot).append(" ");
        }
        return sb.toString().trim();
    }

    private List<List<String>> parseShots(String shotsString) {
        List<List<String>> shotsList = new ArrayList<>();
        if (shotsString != null && !shotsString.isEmpty()) {
            shotsString = shotsString.trim().replaceAll("^\\[|\\]$", "");
            String[] rowTokens = shotsString.split("\\], \\[");

            for (String row : rowTokens) {
                List<String> rowList = new ArrayList<>();
                String[] items = row.replaceAll("^\\[|\\]$", "").split(", ");
                for (String item : items) {
                    rowList.add(item);
                }
                shotsList.add(rowList);
            }
        }
        return shotsList;
    }

    private String createPdf() throws IOException, DocumentException {
        dataBaseHelperSecretary = new DataBaseHelperSecretary(getContext());
        db = dataBaseHelperSecretary.getReadableDatabase();
        Player player1 = dataBaseHelperSecretary.getPlayerById(db, performance.getComp_id(), performance.getPlayers().get(currentPage).getId());

        Document document = new Document();
        String fileName = "protocol.pdf";
        File file = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName);
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();

        // Шрифты для текста
        Font font = FontFactory.getFont("res/font/timesnewroman.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12);
        Font boldFont = FontFactory.getFont("res/font/timesnewroman.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12, Font.BOLD);

        // Создаем параграфы с текстом заголовка
        Paragraph title1 = new Paragraph("ООО \"ФЕДЕРАЦИЯ ГОРОДОШНОГО СПОРТА РОССИИ\"", font);
        title1.setAlignment(Element.ALIGN_CENTER);
        document.add(title1);

        // Вставляем id игрока в строку "ПРОТОКОЛ N ____"
        int playerId = player1.getId();
        Chunk chunkBefore = new Chunk(" ", font);
        chunkBefore.setUnderline(0.1f, -2f);

        // Вставляем подчеркнутый id игрока в строку "ПРОТОКОЛ N ____"
        Chunk chunk1 = new Chunk("ПРОТОКОЛ N", font);
        Chunk chunk2 = new Chunk(" " + String.valueOf(playerId) + " ", font); // пробелы в начале и в конце playerId
        chunk2.setUnderline(0.1f, -2f); // Подчеркиваем playerId
        Paragraph title2 = new Paragraph();
        title2.add(chunk1);
        title2.add(chunkBefore); // добавляем подчеркнутый пробел перед playerId
        title2.add(chunk2);
        title2.add(chunkBefore); // добавляем подчеркнутый пробел после playerId
        title2.setAlignment(Element.ALIGN_CENTER);
        document.add(title2);


        Paragraph title3 = new Paragraph("Личные соревнования по городошному спорту", font);
        title3.setAlignment(Element.ALIGN_CENTER);
        document.add(title3);

        String compName = dataBaseHelperSecretary.getCompetitionNameById(db, performance.getComp_id());
        String categoryName = dataBaseHelperSecretary.getCategoryNameById(db, player1.getCategoryId());
        String categoryDate = dataBaseHelperSecretary.getAgeLimitByCompAndCategoryId(db, performance.getComp_id(), player1.getCategoryId()) + "г.р.";

        Chunk chunk12 = new Chunk(compName + ",   " + categoryName + "  " + categoryDate, font); // Убираем подчеркивание
        Paragraph title4 = new Paragraph();
        title4.add(chunk12);
        title4.setAlignment(Element.ALIGN_CENTER);
        document.add(title4);


        String compDate = dataBaseHelperSecretary.getFormattedDateByCompId(db, performance.getComp_id());
        String compPlace = dataBaseHelperSecretary.getCompetitionPlaceById(db, performance.getComp_id());
        Chunk chunk13 = new Chunk(compDate + ",  " + compPlace, font);
        Paragraph title5 = new Paragraph();
        title5.add(chunk13);
        title5.setAlignment(Element.ALIGN_CENTER);
        document.add(title5);

        document.add(new Paragraph("\n"));
        //document.add(new Paragraph("\n"));

        String playerName = player1.getName();
        //String playerBirthYear = String.valueOf(player1.getBirthYear());
        Chunk chunk3 = new Chunk("Фамилия, имя, отчество ", font);
        Chunk chunk4 = new Chunk(" " + playerName + " ", font); // пробелы в начале и в конце playerName
        chunk4.setUnderline(0.1f, -2f); // Подчеркиваем playerName
        Chunk chunk5 = new Chunk(" Год рождения ", font);
        Chunk chunk6 = new Chunk(" " + "         " + " ", font); // пробелы в начале и в конце playerBirthYear
        chunk6.setUnderline(0.1f, -2f); // Подчеркиваем playerBirthYear
        Paragraph title6 = new Paragraph();
        title6.add(chunk3);
        title6.add(chunk4);
        title6.add(chunk5);
        title6.add(chunk6);
        title6.setAlignment(Element.ALIGN_CENTER);
        document.add(title6);

        // Добавляем разряд и организацию игрока
        String playerGrade = getContext().getString(DataBaseContractSecretary.GradeHelper.getGrade(player1.getGrade()));
        String playerRegion = dataBaseHelperSecretary.getRegionNameById(db, player1.getRegionId());
        Chunk chunk7 = new Chunk("Разряд ", font);
        Chunk chunk8 = new Chunk(" " + playerGrade + " ", font); // пробелы в начале и в конце playerGrade
        chunk8.setUnderline(0.1f, -2f); // Подчеркиваем playerGrade
        Chunk chunk9 = new Chunk(" Организация ", font);
        Chunk chunk10 = new Chunk(" " + playerRegion + " ", font); // пробелы в начале и в конце playerRegion
        chunk10.setUnderline(0.1f, -2f); // Подчеркиваем playerRegion
        Chunk chunk11 = new Chunk(" Подпись _______", font);
        Paragraph title7 = new Paragraph();
        title7.add(chunk7);
        title7.add(chunk8);
        title7.add(chunk9);
        title7.add(chunk10);
        title7.add(chunk11);
        title7.setAlignment(Element.ALIGN_CENTER);
        document.add(title7);

        // Создаем таблицу PDF
        PdfPTable pdfTable = new PdfPTable(numCols + 1); // Создаем таблицу с numCols + 1 столбцами
        pdfTable.setWidthPercentage(90); // Ширина таблицы на 90% страницы
        pdfTable.setSpacingBefore(20f); // Добавляем отступ перед таблицей

        // Двумерный массив для хранения данных ячеек таблицы
        String[][] tableData = populateTableData(numRows, numCols);

        // Заполнение таблицы данными из двумерного массива
        for (int i = 0; i < tableData.length; i++) {
            String[] rowData = tableData[i];
            for (int j = 0; j < rowData.length; j++) {
                // Выбираем шрифт
                Font cellFont = font;
                int colspan = 1;

                // Устанавливаем ширину столбца для первой строки и остальных строк
                if (i == 0) {
                    if (j >= 0) {
                        colspan = 2;
                    } else {
                        colspan = 1;
                    }
                } else if (j == 0 && i < tableData.length - categoryTourById) {
                    colspan = 2;
                } else if (i >= tableData.length - categoryTourById - 2) {
                    if (j == 0) {
                        colspan = 2;
                    } else {
                        switch (categoryTourById) {
                            case 1:
                                colspan = 2;
                                break;
                            case 2:
                                if (i == tableData.length - categoryTourById - 2) {
                                    colspan = 2;
                                } else {
                                    if (i >= tableData.length - categoryTourById) {
                                        colspan = 2;
                                        break;
                                    } else if (j == 1) {
                                        colspan = 4;
                                    }
                                }
                                break;
                            case 3:
                                if (i == tableData.length - categoryTourById - 2) {
                                    colspan = 2;
                                } else if (i == tableData.length - categoryTourById - 1) {
                                    if (j == 1) {
                                        colspan = 4;
                                    } else {
                                        colspan = 2;
                                    }
                                } else {
                                    if (i > tableData.length - categoryTourById) {
                                        colspan = 2;
                                    } else colspan = 6;
                                }
                                break;
                        }
                    }
                } else {
                    colspan = 1;
                }

                if (colspan >= 2 || rowData[0].equals("Сумма")) {
                    cellFont = boldFont;
                }
                if (colspan >= 2 && (rowData[0].equals("Судья") || rowData[0].equals("Секретарь")) && !rowData[j].equals("Секретарь") && !rowData[j].equals("Судья")) {
                    cellFont = font;
                }

                // Создаем ячейку с текстом
                PdfPCell cell = new PdfPCell(new Phrase(rowData[j], cellFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(5);
                cell.setColspan(colspan);

                pdfTable.addCell(cell);
            }
        }

        // Центрируем таблицу на странице
        pdfTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        document.add(pdfTable);

        document.close();
        return file.getAbsolutePath();
    }


    public String[][] populateTableData(int numRows1, int numCols1) {
        String[][] tableData = new String[numRows1 + 2][];
        tableData[0] = new String[(int) ((numCols1 - 1) / 2 + 1)];
        for (int i = 1; i < numRows1; i++) {
            if (i >= numRows1 - categoryTourById) {
                switch (categoryTourById) {
                    case 1:
                        tableData[i] = new String[2];
                        tableData[i + 1] = new String[2];
                        tableData[i + 2] = new String[2];

                        break;
                    case 2:
                        tableData[i] = new String[3];
                        tableData[i + 1] = new String[2];
                        tableData[i + 2] = new String[3];
                        tableData[i + 3] = new String[3];

                        break;
                    case 3:
                        tableData[i] = new String[4];
                        tableData[i + 1] = new String[3];
                        tableData[i + 2] = new String[2];
                        tableData[i + 3] = new String[4];
                        tableData[i + 4] = new String[4];

                        break;
                }
                break;
            } else tableData[i] = new String[numCols1];
        }

        initializeItemList(performance.getPlayers().get(currentPage).getCategoryConfig());

        for (int i = 0; i < numRows1; i++) {
            String[] rowData = tableData[i];
            for (int j = 0; j < rowData.length; j++) {
                if (i == 0) {
                    // Первая строка
                    if (j < (int) ((numCols1 - 1) / 2 + 1)) {
                        if (j == 0) {
                            tableData[i][j] = "Фигуры";
                        } else {
                            tableData[i][j] = j + " тур";
                        }
                    }
                } else if (i > 0 && i < numRows1 && j > 0) {
                    TableRow row = (TableRow) table.getChildAt(i);
                    TextView textView = (TextView) row.getChildAt(j);
                    tableData[i][j] = textView.getText().toString();
                } else if (i == itemList.size() + 1) {
                    // Последняя строка
                    tableData[i][0] = "Сумма";
                    if (j > 0)
                        tableData[i][j] = " ";
                } else if (i == itemList.size() + 2 || i == itemList.size() + 3 || i == itemList.size() + 4) {
                    tableData[i][j] = " "; // Пустая строка

                } else if (j == 0) {
                    tableData[i][0] = itemList.get(i - 1);
                    if (j > 0)
                        tableData[i][j] = " ";

                } else {
                    tableData[i][j] = " ";
                }
            }
        }

        switch (categoryTourById) {
            case 1:
                tableData[numRows1][0] = "Судья";
                tableData[numRows1][1] = " ";
                tableData[numRows1 + 1][0] = "Секретарь";
                tableData[numRows1 + 1][1] = " ";
                for (int i = 0; i < judgesName.size(); i++) {
                    tableData[numRows1 + 1][i + 1] = judgesName.get(i);
                }
                break;
            case 2:
                tableData[numRows1][0] = "Судья";
                tableData[numRows1][1] = " ";
                tableData[numRows1][2] = " ";
                tableData[numRows1 + 1][0] = "Секретарь";
                tableData[numRows1 + 1][1] = " ";
                tableData[numRows1 + 1][2] = " ";
                for (int i = 0; i < judgesName.size(); i++) {
                    tableData[numRows1 + 1][i + 1] = judgesName.get(i);
                }
                break;
            case 3:
                tableData[numRows1][0] = "Судья";
                tableData[numRows1][1] = " ";
                tableData[numRows1][2] = " ";
                tableData[numRows1][3] = " ";
                tableData[numRows1 + 1][0] = "Секретарь";
                tableData[numRows1 + 1][1] = " ";
                tableData[numRows1 + 1][2] = " ";
                tableData[numRows1 + 1][3] = " ";
                for (int i = 0; i < judgesName.size(); i++) {
                    tableData[numRows1 + 1][i + 1] = judgesName.get(i);
                }
                break;
        }
        return tableData;
    }


    private void openPdf(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            Uri uri = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".fileprovider", file);

            // Создаем Intent для открытия PDF-файла
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // Проверяем, есть ли приложение для просмотра PDF на устройстве
            PackageManager pm = getContext().getPackageManager();
            if (intent.resolveActivity(pm) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "Нет приложения для открытия PDF", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Файл не найден", Toast.LENGTH_SHORT).show();
        }
    }
}
