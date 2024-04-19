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
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.nishty.aai_referee.R;
import ru.nishty.aai_referee.db.referee.DataBaseHelperReferee;
import ru.nishty.aai_referee.db.secretary.DataBaseContractSecretary;
import ru.nishty.aai_referee.entity.referee.Performance;
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
    private List<String> itemList;
    private static final String ARG_PROTOCOL = "protocol";
    private static final String ARG_PERFORMANCE = "performance";

    private static Protocol protocol;
    private static Performance performance;
    private ProtocolFillingPagerAdapter pagerAdapter;
    private int currentPage = 0;
    private int position;

    public ProtocolFillingFragment() {
    }

    public static ProtocolFillingFragment newInstance(Protocol protocol, Performance performance, int position) {
        ProtocolFillingFragment fragment = new ProtocolFillingFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putSerializable("protocol", protocol);
        args.putSerializable("performance", performance);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            protocol = (Protocol) getArguments().getSerializable("protocol");
            performance = (Performance) getArguments().getSerializable("performance");
            currentPage = getArguments().getInt("position");
        }
        initializeItemList();
    }

    private void initializeItemList() {
        itemList = Arrays.asList("пушка", "вилка", "звезда", "стрела", "колодец", "коленчатый вал", "артиллерия", "ракетка", "пулемётное гнездо", "рак", "часовые", "серп", "тир", "самолёт", "письмо",
                "пушка", "вилка", "звезда", "стрела", "колодец", "коленчатый вал", "артиллерия", "ракетка", "пулемётное гнездо", "рак", "часовые", "серп", "тир", "самолёт", "письмо");
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            position = getArguments().getInt("position");
            protocol = (Protocol) getArguments().getSerializable(ARG_PROTOCOL);
            performance = (Performance) getArguments().getSerializable(ARG_PERFORMANCE);
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

        time_t.setText(performance.getTime());
        playground_t.setText(performance.getPlayground() + " площадка");
        date_t.setText(performance.getDate());
        name_t.setText(performance.getPlayers().get(currentPage).getName());
        grade_t.setText(getContext().getString(DataBaseContractSecretary.GradeHelper.getGrade(performance.getPlayers().get(currentPage).getGrade())));
        category_t.setText(performance.getPlayers().get(currentPage).getCategory());
        region_t.setText(performance.getPlayers().get(currentPage).getRegion());

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

        setupButtonListeners(buttons);
        save.setOnClickListener(v -> {
            String s = String.valueOf(textView17.getText());
            protocol.setGame1(s);

            String s1 = String.valueOf(textView18.getText());
            protocol.setGame2(s1);

            String s2 = String.valueOf(textView19.getText());
            protocol.setGames_sum(s2);
            if (s.equals("-") || s1.equals("-") || s2.equals("-")) {
                return;
            }

            List<List<String>> firstPart = new ArrayList<>();
            List<List<String>> secondPart = new ArrayList<>();

            for (int i = 0; i < 15; i++) {
                firstPart.add(combinationsList.get(i));
            }

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
            bundle.putSerializable(ARG_PERFORMANCE, performance);


            NavHostFragment.findNavController(ProtocolFillingFragment.this)
                    .navigate(R.id.action_protocolFillingFragment_to_protocolQrFragment, bundle);
        });

        return view;
    }

    private void setupButtonListeners(List<Button> buttons) {
        for (Button button : buttons) {
            button.setOnClickListener(v -> {
                if (button.getId() == R.id.eight) {
                    handleButton8Click();
                } else {
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

                boolean isNumeric = isNumeric(buttonText);

                if (isNumeric) {
                    int value = Integer.parseInt(buttonText);

                    if (currentSum + value <= 5) {
                        currentSum += value;

                        combinationsList.get(currentItemIndex).add(Integer.toString(value));
                        logButtonActions();
                    }
                } else {
                    buttonActions.add(buttonText);
                    combinationsList.get(currentItemIndex).add(buttonText);
                    logButtonActions();
                }

                if (currentSum == 5) {
                    updateItemList();
                    updateTextViews();
                    logButtonActions();
                }
                String textViewText = textView15.getText().toString();
                if (textViewText.equals("0")) {
                    addMinimumElementsToSubarrays();
                }
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

            if (currentSum < 5) {
                while (currentSum < 5 && subArray.size() < b) {
                    if (i == 14 || i == 29) {
                        while (currentSum < 5) {
                            subArray.add("1");
                            currentSum += 1;
                        }
                    } else if (currentSum == 4) {
                        subArray.add("1");
                        currentSum += 1;
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
                updateItemList();
                updateTextViews();
                logButtonActions();
            }
        }
    }

    private void disableButtons() {
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
        currentItemIndex++;
        if (currentItemIndex >= itemList.size()) {
            currentItemIndex--;
        }
    }

    private void showPreviousItem() {
        if (!combinationsList.isEmpty() && currentItemIndex > 0) {
            currentItemIndex--;
            List<String> previousCombination = combinationsList.get(currentItemIndex);
            displayPreviousCombination(previousCombination);
        } else {
            Log.d("ShowPrevious", "No previous item available");
        }
    }

    private void displayPreviousCombination(List<String> previousCombination) {
        updateTextViews();
        StringBuilder logText = new StringBuilder();
        for (String element : previousCombination) {
            logText.append(element).append("   ");
        }
        textView12.setText(logText.toString());

        if (currentItemIndex >= 0 && currentItemIndex < itemList.size()) {
            TextView textView10 = view.findViewById(R.id.textView10);
            textView10.setText(itemList.get(currentItemIndex));
        }
    }

    private void showNextItem() {
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
        updateTextViews();
        StringBuilder logText = new StringBuilder();
        for (String element : nextCombination) {
            logText.append(element).append("   ");
        }
        textView12.setText(logText.toString());

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
        textView12.setText(logText.toString());
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

        if (allSumsEqualFivePart1 && allSumsEqualFivePart2) {
            TextView textView19 = view.findViewById(R.id.textView19);
            textView19.setText(String.valueOf(countPart1 + countPart2));
        } else {
            TextView textView19 = view.findViewById(R.id.textView19);
            textView19.setText("-");
        }

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
