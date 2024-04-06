package ru.nishty.aai_referee.ui.referee.protocol_filling;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.nishty.aai_referee.R;
import ru.nishty.aai_referee.db.referee.DataBaseHelperReferee;
import ru.nishty.aai_referee.entity.referee.Protocol;


public class ProtocolFillingFragment extends Fragment {
    private View view;
    private List<Button> buttons;
    private List<String> buttonActions;
    private TextView textView12;
    private TextView textView13;
    private TextView textView15;
    private TextView textView17;
    private TextView textView18;
    private TextView textView19;

    private List<String> itemList;  // Список из 15 элементов

    private static final String ARG_PROTOCOL = "protocol";
    private static final String ARG_DISCIPLINE = "discipline";
    private static final String ARG_NAME = "name";
    private static final String ARG_GRADE = "grade";
    private static final String ARG_REGION = "region";
    private static final String ARG_DATE = "date";
    private static final String ARG_PLAYGROUND = "playground";
    private static final String ARG_CATEGORY = "category";
    private static final String ARG_TIME = "time";

    private static Protocol protocol;
    private static int discipline;
    private static String name;
    private static String grade;
    private static String region;
    private static String date;
    private static String playground;
    private static String category;
    private static String time;


    public ProtocolFillingFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Инициализация списка элементов
        itemList = Arrays.asList("пушка", "вилка", "звезда", "стрела", "колодец", "коленчатый вал", "артиллерия", "ракетка", "пулемётное гнездо", "рак", "часовые", "серп", "тир", "самолёт", "письмо",
                "пушка", "вилка", "звезда", "стрела", "колодец", "коленчатый вал", "артиллерия", "ракетка", "пулемётное гнездо", "рак", "часовые", "серп", "тир", "самолёт", "письмо");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            protocol = (Protocol) getArguments().getSerializable(ARG_PROTOCOL);
            discipline = getArguments().getInt(ARG_DISCIPLINE);
            name = getArguments().getString(ARG_NAME);
            grade = getArguments().getString(ARG_GRADE);
            region = getArguments().getString(ARG_REGION);
            date = getArguments().getString(ARG_DATE);
            playground = getArguments().getString(ARG_PLAYGROUND);
            category = getArguments().getString(ARG_CATEGORY);
            time = getArguments().getString(ARG_TIME);
        }

        view = inflater.inflate(R.layout.fragment_protocol_filling, container, false);
        Button save = view.findViewById(R.id.save_protocol);
        TextView name_t = view.findViewById(R.id.performance_name);
        TextView time_t = view.findViewById(R.id.performance_time);
        TextView playground_t = view.findViewById(R.id.performance_playground);
        TextView date_t = view.findViewById(R.id.performance_date);
        TextView grade_t = view.findViewById(R.id.performance_grade);
        TextView category_t = view.findViewById(R.id.performance_category);
        TextView region_t = view.findViewById(R.id.performance_region);

        name_t.setText(name);
        time_t.setText(time);
        playground_t.setText(playground);
        date_t.setText(date);
        grade_t.setText(grade);
        category_t.setText(category);
        region_t.setText(region);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(name);

        buttons = Arrays.asList(
                view.findViewById(R.id.one),
                view.findViewById(R.id.two),
                view.findViewById(R.id.three),
                view.findViewById(R.id.four),
                view.findViewById(R.id.five),
                view.findViewById(R.id.six),
                view.findViewById(R.id.seven),
                view.findViewById(R.id.eight)
        );

        buttonActions = new ArrayList<>();
        textView12 = view.findViewById(R.id.textView12);
        textView13 = view.findViewById(R.id.textView13);
        textView15 = view.findViewById(R.id.textView15);
        textView17 = view.findViewById(R.id.textView17);
        textView18 = view.findViewById(R.id.textView18);
        textView19 = view.findViewById(R.id.textView19);
        initializeCombinationsList();

        Button showPreviousButton = view.findViewById(R.id.save_protocol5);
        showPreviousButton.setOnClickListener(v -> showPreviousItem());

        Button showNextButton = view.findViewById(R.id.save_protocol3);
        showNextButton.setOnClickListener(v -> showNextItem());

        TextView textView10 = view.findViewById(R.id.textView10);
        if (currentItemIndex >= 0 && currentItemIndex < itemList.size()) {
            textView10.setText(itemList.get(currentItemIndex));
        }


        // Настройка для всех кнопок
        setupButtonListeners(buttons);
        save.setOnClickListener(v -> {
            String s = String.valueOf(textView17.getText());
            protocol.setGame1(s);

            String s1 = String.valueOf(textView18.getText());
            protocol.setGame2(s1);

            String s2 = String.valueOf(textView19.getText());
            protocol.setGames_sum(s2);
            if (s.equals("-") || s1.equals("-") || s2.equals("-")) {
                return; // Прерываем выполнение кода, так как не все поля заполнены
            }

            List<List<String>> firstPart = new ArrayList<>();
            List<List<String>> secondPart = new ArrayList<>();

// Заполняем первый массив первыми 15 элементами combinationsList
            for (int i = 0; i < 15; i++) {
                firstPart.add(combinationsList.get(i));
            }

// Заполняем второй массив последующими 15 элементами combinationsList
            for (int i = 15; i < combinationsList.size(); i++) {
                secondPart.add(combinationsList.get(i));
            }
            protocol.setShots1(firstPart.toString());
            protocol.setShots2(secondPart.toString());
            // TODO: set proper name
            protocol.setName("test");
            DataBaseHelperReferee dataBaseHelperReferee = new DataBaseHelperReferee(getContext());
            SQLiteDatabase db = dataBaseHelperReferee.getWritableDatabase();

            dataBaseHelperReferee.setProtocol(db, protocol);

            Bundle bundle = new Bundle();
            bundle.putSerializable(ARG_PROTOCOL, protocol);
            bundle.putInt(ARG_DISCIPLINE, discipline);
            bundle.putString(ARG_NAME, name);
            bundle.putString(ARG_TIME, time);
            bundle.putString(ARG_CATEGORY, category);
            bundle.putString(ARG_PLAYGROUND, playground);
            bundle.putString(ARG_DATE, date);
            bundle.putString(ARG_GRADE, grade);
            bundle.putString(ARG_REGION, region);

            NavHostFragment.findNavController(ProtocolFillingFragment.this)
                    .navigate(R.id.action_protocolFillingFragment_to_protocolQrFragment, bundle);
        });

        return view;
    }

    private void setupButtonListeners(List<Button> buttons) {
        for (Button button : buttons) {
            button.setOnClickListener(v -> {
                if (button.getId() == R.id.eight) {
                    // Если нажата кнопка 8, обработать её клик
                    handleButton8Click();
                } else {
                    // Иначе обработать нажатие кнопок 1-7
                    handleButtonClick(buttons.indexOf(button), button);
                }
            });
        }
    }

    private void handleButtonClick(int index, Button clickedButton) {
        resetButtonStates(buttons);

        if (clickedButton.isSelected()) {
            clickedButton.setSelected(false);
            lastSelectedButtonIndex = -1;
        } else {
            clickedButton.setSelected(true);
            lastSelectedButtonIndex = index;
        }
    }

    private int lastSelectedButtonIndex = -1;
    private int currentItemIndex = 0;
    private List<List<String>> combinationsList;

    private void initializeCombinationsList() {
        combinationsList = new ArrayList<>(30);
        for (int i = 0; i < 30; i++) {
            combinationsList.add(new ArrayList<>());
        }
    }

    private void handleButton8Click() {
        if (lastSelectedButtonIndex != -1) {
            int currentSum;
            currentSum = calculateSum(currentItemIndex);

            if (currentSum == 5) {
                logButtonActions();
            } else {
                Button lastSelectedButton = buttons.get(lastSelectedButtonIndex);
                String buttonText = lastSelectedButton.getText().toString();

                // Проверка на числовой формат
                boolean isNumeric = isNumeric(buttonText);

                if (isNumeric) {
                    int value = Integer.parseInt(buttonText);

                    // Проверяем, что сумма с новым значением не превышает 5
                    if (currentSum + value <= 5) {
                        currentSum += value;

                        // Добавляем элемент в текущий подмассив
                        combinationsList.get(currentItemIndex).add(Integer.toString(value));
                        logButtonActions();
                    }
                } else {
                    // Если элемент типа string, добавляем его в массив
                    buttonActions.add(buttonText);

                    // Добавляем элемент в текущий подмассив
                    combinationsList.get(currentItemIndex).add(buttonText);
                    logButtonActions();
                }

                // Проверка суммы чисел в массиве
                if (currentSum == 5) {
                    // Если сумма равна 5, изменить элементы списка
                    updateItemList();
                    updateTextViews();
                    logButtonActions();
                }
                String textViewText = textView15.getText().toString();
                if (textViewText.equals("0")) {
                    addMinimumElementsToSubarrays();
                }
                // Сбросить состояние всех кнопок после обработки кнопки 8
                resetButtonStates(buttons);
            }
            lastSelectedButtonIndex = -1;
        }
    }

    private int calculateSum(int subArrayIndex) {
        int sum = 0;
        List<String> subArray = combinationsList.get(subArrayIndex);
        for (String element : subArray) {
            if (isNumeric(element)) {
                sum += Integer.parseInt(element);
            }
        }
        return sum;
    }

    private void addMinimumElementsToSubarrays() {
        // Перебираем подмассивы
        int a, b;
        String s = String.valueOf(textView17.getText());
        if (s.equals("-")) {
            a = 0;
            b = 15;
        } else {
            a = 15;
            b = 30;
        }
        for (int i = a; i < b; i++) {
            List<String> subArray = combinationsList.get(i);
            int currentSum = calculateSum(i);

            // Проверяем, если сумма в подмассиве меньше 5
            if (currentSum < 5) {
                // Добавляем минимальное количество элементов до достижения суммы 5
                while (currentSum < 5 && subArray.size() < b) {
                    // Добавить элементы в подмассив в зависимости от текущей суммы
                    if (i == 14 || i == 29) {
                        while (currentSum < 5) {
                            subArray.add("1");
                            currentSum += 1;
                        }
                    } else if (currentSum == 4) {
                        subArray.add("1");
                        currentSum += 1;  // Обеспечиваем выход из цикла
                    } else if (currentSum == 3) {
                        subArray.add("1");
                        subArray.add("1");
                        currentSum += 2;
                    } else if (currentSum == 2) {
                        subArray.add("2");
                        subArray.add("1");
                        currentSum += 3;
                    } else if (currentSum == 1) {
                        subArray.add("3");
                        subArray.add("1");
                        currentSum += 4;
                    } else if (currentSum == 0) {
                        subArray.add("4");
                        subArray.add("1");
                        currentSum += 5;
                    }
                }
                // Обновляем элементы списка и текстовые представления
                updateItemList();
                updateTextViews();
                logButtonActions();
            }
        }
    }

    private void disableButtons() {
        // Блокируем все кнопки после достижения суммы 5
        for (Button button : buttons) {
            button.setEnabled(false);
        }
    }

    private void updateTextViews() {
        if (currentItemIndex >= 0 && currentItemIndex < itemList.size()) {
            TextView textView10 = view.findViewById(R.id.textView10);
            textView10.setText(itemList.get(currentItemIndex));
        }
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void updateItemList() {
        // Логика обновления элементов списка
        currentItemIndex++;
        // currentSum = 0; // Сбросить текущую сумму
        if (currentItemIndex >= itemList.size()) {
            currentItemIndex--;
        }
    }

    private void showPreviousItem() {
        // Проверяем, что есть предыдущие элементы в списке
        if (!combinationsList.isEmpty() && currentItemIndex > 0) {
            // Уменьшаем индекс для отображения предыдущего элемента
            currentItemIndex--;
            List<String> previousCombination = combinationsList.get(currentItemIndex);
            displayPreviousCombination(previousCombination);
        } else {
            Log.d("ShowPrevious", "No previous item available");
        }
    }

    private void displayPreviousCombination(List<String> previousCombination) {
        updateTextViews(); // Обновить текст на экране
        StringBuilder logText = new StringBuilder();
        for (String element : previousCombination) {
            logText.append(element).append("   ");
        }
        textView12.setText(logText.toString());

        // Показать предыдущий элемент itemList
        if (currentItemIndex >= 0 && currentItemIndex < itemList.size()) {
            TextView textView10 = view.findViewById(R.id.textView10);
            textView10.setText(itemList.get(currentItemIndex));
        }
    }

    private void showNextItem() {
        // Переходим к следующему подмассиву


        currentItemIndex++;
        if (currentItemIndex >= combinationsList.size()) {
            currentItemIndex--;
        }
        if (!combinationsList.isEmpty()) {
            List<String> nextCombination = combinationsList.get(currentItemIndex);
            displayNextCombination(nextCombination);
        }
    }

    private void displayNextCombination(List<String> nextCombination) {
        updateTextViews(); // Обновить текст на экране
        StringBuilder logText = new StringBuilder();
        for (String element : nextCombination) {
            logText.append(element).append("   ");
        }
        textView12.setText(logText.toString());

        // Показать следующий элемент itemList
        if (currentItemIndex >= 0 && currentItemIndex < itemList.size()) {
            TextView textView10 = view.findViewById(R.id.textView10);
            textView10.setText(itemList.get(currentItemIndex));
        }
    }

    private void resetButtonStates(List<Button> buttons) {
        for (Button button : buttons) {
            button.setSelected(false);
        }
    }

    private void logButtonActions() {
        StringBuilder logText = new StringBuilder();
        List<String> currentCombination = combinationsList.get(currentItemIndex);
        for (String element : currentCombination) {
            logText.append(element).append("   ");
        }
        // Отображаем лог-текст в textView12
        textView12.setText(logText.toString());
        // Лог длины каждого подмассива
        Log.d("CombinationsList", combinationsList.toString());
        // Лог длины всех подмассивов
        int countPart1 = 0;
        int countPart2 = 0;

        for (int i = 0; i < combinationsList.size(); i++) {
            List<String> combination = combinationsList.get(i);
            if (i < 15) {
                countPart1 += combination.size();
            } else {
                countPart2 += combination.size();
            }
        }

        boolean allSumsEqualFivePart1 = true;
        boolean allSumsEqualFivePart2 = true;
        // Проверка условия для отображения textView17
        int startIndex = (currentItemIndex < 15) ? 0 : 15;
        int endIndex = (currentItemIndex < 15) ? 15 : 30;

        for (int i = startIndex; i < endIndex; i++) {
            int sum = calculateSum(i);
            if (sum != 5) {
                if (i < 15) {
                    allSumsEqualFivePart1 = false;
                } else {
                    allSumsEqualFivePart2 = false;
                }
                break;
            }
        }

        // Обновление textView17
        if (allSumsEqualFivePart1) {
            TextView textView17 = view.findViewById(R.id.textView17);
            textView17.setText(String.valueOf(countPart1));
        } else {
            TextView textView17 = view.findViewById(R.id.textView17);
            textView17.setText("-");
        }

        if (!allSumsEqualFivePart1) {
            TextView textView18 = view.findViewById(R.id.textView18);
            textView18.setText("-");
        } else if (allSumsEqualFivePart2) {
            TextView textView18 = view.findViewById(R.id.textView18);
            textView18.setText(String.valueOf(countPart2));
        } else {
            TextView textView18 = view.findViewById(R.id.textView18);
            textView18.setText("-");
        }

        // Обновление textView19
        if (allSumsEqualFivePart1 && allSumsEqualFivePart2) {
            TextView textView19 = view.findViewById(R.id.textView19);
            textView19.setText(String.valueOf(countPart1 + countPart2));
        } else {
            TextView textView19 = view.findViewById(R.id.textView19);
            textView19.setText("-");
        }

        // Обновляем textView13
        int count;
        String countText;
        if (currentItemIndex < 15) {
            count = countPart1;
        } else {
            count = countPart2;
        }
        countText = String.valueOf(count);

        TextView textView13 = view.findViewById(R.id.textView13);
        textView13.setText(countText);

        // Обновляем textView15
        int count15 = 30;
        int count30 = 30;

        int difference;
        if (currentItemIndex < 15) {
            difference = count15 - countPart1;
        } else {
            difference = count30 - countPart2;
        }
        String count15Text = String.valueOf(difference);
        TextView textView15 = view.findViewById(R.id.textView15);
        textView15.setText(count15Text);
    }


}
