package ru.nishty.aai_referee.ui.secretary.competition_list;

import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import ru.nishty.aai_referee.R;
import ru.nishty.aai_referee.db.secretary.DataBaseHelperSecretary;
import ru.nishty.aai_referee.entity.secretary.Category;
import ru.nishty.aai_referee.entity.secretary.CompetitionSecretary;
import ru.nishty.aai_referee.entity.secretary.Judge;
import ru.nishty.aai_referee.entity.secretary.Player;
import ru.nishty.aai_referee.entity.secretary.Region;

public class CompetitionAddFragment extends Fragment {

    private EditText etCompetitionName;
    private EditText etCompetitionDate;
    private EditText etCompetitionDate2;
    private EditText etCompetitionLocation;
    private EditText etHeadJudge;
    private EditText etHeadSecretary;
    private String uuid = generateRandomString();

    private CompetitionSecretary competitionSecretary;
    private DataBaseHelperSecretary dataBaseHelperSecretary;

    private static String generateRandomString() {
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            int randomIndex = random.nextInt(characters.length());
            randomString.append(characters.charAt(randomIndex));
        }
        return randomString.toString();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_competition, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etCompetitionName = view.findViewById(R.id.etCompetitionName);
        etCompetitionDate2 = view.findViewById(R.id.etCompetitionDate2);
        etCompetitionDate = view.findViewById(R.id.etCompetitionDate);
        etCompetitionLocation = view.findViewById(R.id.etCompetitionLocation);
        etHeadJudge = view.findViewById(R.id.etHeadJudge);
        etHeadSecretary = view.findViewById(R.id.etHeadSecretary);
        AtomicBoolean isDatePickerOpened2 = new AtomicBoolean(false);
        AtomicBoolean isDatePickerOpened = new AtomicBoolean(false);



        dataBaseHelperSecretary = new DataBaseHelperSecretary(getContext());
        SQLiteDatabase dbr = dataBaseHelperSecretary.getWritableDatabase();


        // Создание соревнования при создании фрагмента
        competitionSecretary = new CompetitionSecretary();
        competitionSecretary.setUuid(uuid);

        etCompetitionDate2.setOnClickListener(v -> {
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
                        etCompetitionDate2.setText(selectedDate);
                    }, year, month, dayOfMonth);

            // Показываем диалоговое окно выбора даты
            datePickerDialog.show();
        });

// Отключаем свойство фокусировки для etDate EditText
        etCompetitionDate2.setFocusable(false);
        etCompetitionDate2.setClickable(true); // Разрешаем нажатия на etDate EditText

        etCompetitionDate2.setOnFocusChangeListener((v, hasFocus) -> {
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
                            etCompetitionDate2.setText(selectedDate);
                        }, year, month, dayOfMonth);

                // Show the Date Picker Dialog
                datePickerDialog.show();

                // Set the flag to true indicating the Date Picker Dialog has been opened
                isDatePickerOpened2.set(true);
            }
        });

        etCompetitionDate.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Если соревнование в 1 день - не указывайте дату окончания", Toast.LENGTH_SHORT).show();
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
                        etCompetitionDate.setText(selectedDate);
                    }, year, month, dayOfMonth);

            // Показываем диалоговое окно выбора даты
            datePickerDialog.show();
        });

// Отключаем свойство фокусировки для etDate EditText
        etCompetitionDate.setFocusable(false);
        etCompetitionDate.setClickable(true); // Разрешаем нажатия на etDate EditText

        etCompetitionDate.setOnFocusChangeListener((v, hasFocus) -> {
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
                            etCompetitionDate.setText(selectedDate);
                        }, year, month, dayOfMonth);

                // Show the Date Picker Dialog
                datePickerDialog.show();

                // Set the flag to true indicating the Date Picker Dialog has been opened
                isDatePickerOpened.set(true);
            }
        });

        Button addCompetitionButton = view.findViewById(R.id.btnCreateCompetition);
        addCompetitionButton.setOnClickListener(v -> {
            saveCompetitionToDatabase(dbr);
        });

        view.findViewById(R.id.btnJudges).setOnClickListener(v -> {
            navigateToJudgesFragment();
        });

        view.findViewById(R.id.btnPlayers).setOnClickListener(v -> {
            navigateToPlayersFragment();
        });
    }


    private void saveCompetitionToDatabase(SQLiteDatabase dbr) {
        String competitionName = etCompetitionName.getText().toString().trim();
        String ctd = etCompetitionDate.getText().toString().trim();
        String ctd2 = etCompetitionDate2.getText().toString().trim();
        String competitionDate = (!ctd.equals(""))?(ctd2 + " - " + ctd):(ctd2);
        String competitionLocation = etCompetitionLocation.getText().toString().trim();
        String headJudge = etHeadJudge.getText().toString().trim();
        String headSecretary = etHeadSecretary.getText().toString().trim();

        List<Region> regions = dataBaseHelperSecretary.getRegions(dbr, uuid);
        List<Category> categories = dataBaseHelperSecretary.getCategories(dbr, uuid);
        List<Player> players = dataBaseHelperSecretary.getPlayers(dbr, uuid);
        List<Judge> judgs = dataBaseHelperSecretary.getJudges(dbr, uuid);

        if (TextUtils.isEmpty(competitionName)) {
            Toast.makeText(getActivity(), "Введите название соревнования", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(competitionDate)) {
            Toast.makeText(getActivity(), "Введите дату соревнования", Toast.LENGTH_SHORT).show();
            return;
        }



        if (TextUtils.isEmpty(competitionLocation)) {
            Toast.makeText(getActivity(), "Введите место проведения соревнования", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(headJudge)) {
            Toast.makeText(getActivity(), "Введите главного судью", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(headSecretary)) {
            Toast.makeText(getActivity(), "Введите главного секретаря", Toast.LENGTH_SHORT).show();
            return;
        }
        if (categories.size() == 0) {
            Toast.makeText(getActivity(), "Добавьте категории в соревнование", Toast.LENGTH_SHORT).show();
            return;
        } else if (regions.size() == 0) {
            Toast.makeText(getActivity(), "Добавьте регионы в соревнование", Toast.LENGTH_SHORT).show();
            return;
        } else if (players.size() == 0) {
            Toast.makeText(getActivity(), "Добавьте игроков в соревнование", Toast.LENGTH_SHORT).show();
            return;
        } else if (judgs.size() == 0) {
            Toast.makeText(getActivity(), "Добавьте судей в соревнование", Toast.LENGTH_SHORT).show();
            return;
        }


        // Установка данных соревнования
        competitionSecretary.setName(competitionName);
        competitionSecretary.setYear(competitionDate);
        competitionSecretary.setPlace(competitionLocation);
        competitionSecretary.setHeadJudge(headJudge);
        competitionSecretary.setHeadSecretary(headSecretary);


        // Сохранение соревнования в базе данных
        dataBaseHelperSecretary.addCompetition(dbr, competitionSecretary);

        NavHostFragment.findNavController(this)
                .navigateUp();
    }



    private void navigateToJudgesFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("competitionUuid", competitionSecretary.getUuid());
        Navigation.findNavController(requireView()).navigate(R.id.action_competitionAddFragment_to_judgesFragment, bundle);
    }

    private void navigateToPlayersFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("competitionUuid", competitionSecretary.getUuid());
        Navigation.findNavController(requireView()).navigate(R.id.action_competitionAddFragment_to_playersFragment, bundle);
    }

    private void clearInputFields() {
        etCompetitionName.setText("");
        etCompetitionDate.setText("");
        etCompetitionDate2.setText("");
        etCompetitionLocation.setText("");
        etHeadJudge.setText("");
        etHeadSecretary.setText("");
    }
}
