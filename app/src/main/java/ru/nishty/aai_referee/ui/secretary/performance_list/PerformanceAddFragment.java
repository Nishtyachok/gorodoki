package ru.nishty.aai_referee.ui.secretary.performance_list;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import ru.nishty.aai_referee.R;
import ru.nishty.aai_referee.db.secretary.DataBaseHelperSecretary;
import ru.nishty.aai_referee.entity.secretary.Judge;
import ru.nishty.aai_referee.entity.secretary.PerformanceSecretary;
import ru.nishty.aai_referee.entity.secretary.Player;

public class PerformanceAddFragment extends Fragment {

    private UUID competitionId;
    private DataBaseHelperSecretary dataBaseHelperSecretary;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            competitionId = UUID.fromString(getArguments().getString("id"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_performance, container, false);

        dataBaseHelperSecretary = new DataBaseHelperSecretary(getContext());
        SQLiteDatabase dbr = dataBaseHelperSecretary.getWritableDatabase();

        // Инициализация элементов управления
        Spinner spinnerPlayer1 = view.findViewById(R.id.spinnerPlayer1);
        Spinner spinnerPlayer2 = view.findViewById(R.id.spinnerPlayer2);
        EditText etDate = view.findViewById(R.id.etDate);
        EditText etTime = view.findViewById(R.id.etTime);
        EditText etLocation = view.findViewById(R.id.etLocation);
        EditText etCourtNumber = view.findViewById(R.id.etCourtNumber);
        Spinner spinnerJudge = view.findViewById(R.id.spinnerJudge);
        Button btnCreateGame = view.findViewById(R.id.btnCreateGame);
        AtomicBoolean isTimePickerOpened = new AtomicBoolean(false);
        AtomicBoolean isDatePickerOpened = new AtomicBoolean(false);

        List<Player> player1 = dataBaseHelperSecretary.getPlayers(dataBaseHelperSecretary.getWritableDatabase(), UUID.fromString(String.valueOf(competitionId)));
        List<String> player1Names = new ArrayList<>();
        List<Integer> player1Ids = new ArrayList<>();
        player1Names.add("Выберите первого игрока");
        for (Player player : player1) {
            player1Names.add(player.getName());
            player1Ids.add(player.getId());
        }
        List<Player> player2 = dataBaseHelperSecretary.getPlayers(dataBaseHelperSecretary.getWritableDatabase(), UUID.fromString(String.valueOf(competitionId)));
        List<String> player2Names = new ArrayList<>();
        List<Integer> player2Ids = new ArrayList<>();
        player2Names.add("Выберите второго игрока");
        for (Player player : player2) {
            player2Names.add(player.getName());
            player2Ids.add(player.getId());
        }
        List<Judge> judges = dataBaseHelperSecretary.getJudges(dataBaseHelperSecretary.getWritableDatabase(), UUID.fromString(String.valueOf(competitionId)));
        List<String> judgeNames = new ArrayList<>();
        List<Integer> judgeIds = new ArrayList<>();
        judgeNames.add("Выберите судью");
        for (Judge judge : judges) {
            judgeNames.add(judge.getName());
            judgeIds.add(judge.getId());
        }
        ArrayAdapter<String> player1Adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, player1Names) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if (position == 0) { // Первый элемент списка
                    // Устанавливаем серый цвет для текста
                    ((TextView) view).setTextColor(Color.GRAY);
                } else {
                    // Устанавливаем обычный цвет для текста (черный, например)
                    ((TextView) view).setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        player1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> player2Adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, player2Names) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if (position == 0) { // Первый элемент списка
                    // Устанавливаем серый цвет для текста
                    ((TextView) view).setTextColor(Color.GRAY);
                } else {
                    // Устанавливаем обычный цвет для текста (черный, например)
                    ((TextView) view).setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        player2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        ArrayAdapter<String> judgeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, judgeNames) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if (position == 0) { // Первый элемент списка
                    // Устанавливаем серый цвет для текста
                    ((TextView) view).setTextColor(Color.GRAY);
                } else {
                    // Устанавливаем обычный цвет для текста (черный, например)
                    ((TextView) view).setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        judgeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlayer1.setAdapter(player1Adapter);
        spinnerPlayer2.setAdapter(player2Adapter);
        spinnerJudge.setAdapter(judgeAdapter);
        // Установка OnClickListener для etDate EditText
        etDate.setOnClickListener(v -> {
            // Получаем текущую дату
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            // Открываем диалоговое окно выбора даты
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    (view1, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                        // Форматируем выбранную дату
                        String selectedDate = String.format(Locale.getDefault(), "%02d.%02d.%d", selectedDayOfMonth, selectedMonth + 1, selectedYear);
                        // Устанавливаем выбранную дату в EditText
                        etDate.setText(selectedDate);
                    }, year, month, dayOfMonth);

            // Показываем диалоговое окно выбора даты
            datePickerDialog.show();
        });

// Отключаем свойство фокусировки для etDate EditText
        etDate.setFocusable(false);
        etDate.setClickable(true); // Разрешаем нажатия на etDate EditText

        etDate.setOnFocusChangeListener((v, hasFocus) -> {
            // Open Date Picker Dialog only if it has not been opened before
            if (hasFocus) {
                // Get current date
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        (view1, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                            // Format the selected date
                            String selectedDate = String.format(Locale.getDefault(), "%02d-%02d-%d", selectedDayOfMonth, selectedMonth + 1, selectedYear);
                            // Set the selected date to the EditText
                            etDate.setText(selectedDate);
                        }, year, month, dayOfMonth);

                // Show the Date Picker Dialog
                datePickerDialog.show();

                // Set the flag to true indicating the Date Picker Dialog has been opened
                isDatePickerOpened.set(true);
            }
        });

        // Установка OnClickListener для etTime EditText
        etTime.setOnClickListener(v -> {
            // Получаем текущее время
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            // Открываем диалоговое окно выбора времени
            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                    (view1, selectedHour, selectedMinute) -> {
                        // Форматируем выбранное время
                        String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);
                        // Устанавливаем выбранное время в EditText
                        etTime.setText(selectedTime);
                    }, hour, minute, true);

            // Показываем диалоговое окно выбора времени
            timePickerDialog.show();
        });

// Отключаем свойство фокусировки для etTime EditText
        etTime.setFocusable(false);
        etTime.setClickable(true); // Разрешаем нажатия на etTime EditText


        // Set OnFocusChangeListener to etTime EditText
        etTime.setOnFocusChangeListener((v, hasFocus) -> {
            // Open Time Picker Dialog only if it has not been opened before
            if (hasFocus) {
                // Get current time
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        (view1, selectedHour, selectedMinute) -> {
                            // Format the selected time
                            String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);
                            // Set the selected time to the EditText
                            etTime.setText(selectedTime);
                        }, hour, minute, true);

                // Show the Time Picker Dialog
                timePickerDialog.show();

                // Set the flag to true indicating the Time Picker Dialog has been opened
                isTimePickerOpened.set(true);
            }
        });


        btnCreateGame.setOnClickListener(v -> {
            // Получение данных из элементов управления
            int selectedPositionPlayer1 = spinnerPlayer1.getSelectedItemPosition() - 1;
            int selectedPositionPlayer2 = spinnerPlayer2.getSelectedItemPosition() - 1;
            String date = etDate.getText().toString();
            String time = etTime.getText().toString();
            String location = etLocation.getText().toString();
            String courtNumber = etCourtNumber.getText().toString();
            int selectedPositionJudge = spinnerJudge.getSelectedItemPosition() - 1;

            if (selectedPositionPlayer1 == -1) {
                Toast.makeText(getActivity(), "Пожалуйста, выберите первого игрока", Toast.LENGTH_SHORT).show();
                return;
            } else if (selectedPositionPlayer2 == -1) {
                Toast.makeText(getActivity(), "Пожалуйста, выберите второго игрока", Toast.LENGTH_SHORT).show();
                return;
            } else if (date.isEmpty()) {
                Toast.makeText(getActivity(), "Пожалуйста, введите дату", Toast.LENGTH_SHORT).show();
                return;
            } else if (time.isEmpty()) {
                Toast.makeText(getActivity(), "Пожалуйста, введите время", Toast.LENGTH_SHORT).show();
                return;
            } else if (location.isEmpty()) {
                Toast.makeText(getActivity(), "Пожалуйста, укажите место проведения", Toast.LENGTH_SHORT).show();
                return;
            } else if (courtNumber.isEmpty()) {
                Toast.makeText(getActivity(), "Пожалуйста, укажите номер площадки", Toast.LENGTH_SHORT).show();
                return;
            } else if (selectedPositionPlayer2 == selectedPositionPlayer1) {
                Toast.makeText(getActivity(), "Пожалуйста, выберите разных игроков", Toast.LENGTH_SHORT).show();
                return;
            } else if (selectedPositionJudge == -1) {
                Toast.makeText(getActivity(), "Пожалуйста, выберите судью", Toast.LENGTH_SHORT).show();
                return;
            } else {

                int selectedPlayer1 = player1Ids.get(selectedPositionPlayer1);
                int selectedPlayer2 = player2Ids.get(selectedPositionPlayer2);
                int selectedJudge = judgeIds.get(selectedPositionJudge);

                Player playerOne = dataBaseHelperSecretary.getPlayerById(dbr, UUID.fromString(String.valueOf(competitionId)), selectedPlayer1);
                Player playerTwo = dataBaseHelperSecretary.getPlayerById(dbr, UUID.fromString(String.valueOf(competitionId)), selectedPlayer2);

                List<Player> players = new ArrayList<>();
                if (playerOne != null && playerTwo != null) {
                    players.add(playerOne);
                    players.add(playerTwo);
                } else {
                    // Если объекты игроков не найдены, выводим сообщение об ошибке
                    Toast.makeText(getActivity(), "Ошибка при получении данных об игроках", Toast.LENGTH_SHORT).show();
                    return;
                }
                PerformanceSecretary performanceSecretary = new PerformanceSecretary();
                performanceSecretary.setComp_id(competitionId);
                performanceSecretary.setPlayers(players);
                performanceSecretary.setDate(date);
                performanceSecretary.setTime(time);
                performanceSecretary.setPlace(location);
                performanceSecretary.setPlayground(courtNumber);
                performanceSecretary.setJudgeId(selectedJudge);

                SQLiteDatabase db = dataBaseHelperSecretary.getWritableDatabase();
                dataBaseHelperSecretary.addPerformance(db, performanceSecretary, players);
            }

            NavHostFragment.findNavController(this)
                    .navigateUp();
        });

        return view;
    }
}

