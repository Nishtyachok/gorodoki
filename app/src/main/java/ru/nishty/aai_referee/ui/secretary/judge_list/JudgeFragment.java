package ru.nishty.aai_referee.ui.secretary.judge_list;

import android.app.AlertDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import ru.nishty.aai_referee.R;
import ru.nishty.aai_referee.db.secretary.DataBaseHelperSecretary;
import ru.nishty.aai_referee.entity.secretary.Judge;

import java.util.ArrayList;
import java.util.UUID;

public class JudgeFragment extends Fragment {

    private RecyclerView recyclerView;
    private JudgesAdapter judgesAdapter;
    private ArrayList<Judge> judgesList;
    private DataBaseHelperSecretary dataBaseHelperSecretary;
    private String competitionUuid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_judges, container, false);

        judgesList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.rvJudges);
        setupRecyclerView();

        dataBaseHelperSecretary = new DataBaseHelperSecretary(getContext());

        // Получаем UUID соревнования из аргументов фрагмента
        Bundle arguments = getArguments();
        if (arguments != null) {
            competitionUuid = arguments.getString("competitionUuid");
        }

        FloatingActionButton fabAddJudge = view.findViewById(R.id.fabAddJudge);
        fabAddJudge.setOnClickListener(v -> showAddJudgeDialog());

        return view;
    }

    private void setupRecyclerView() {
        judgesAdapter = new JudgesAdapter(judgesList);
        recyclerView.setAdapter(judgesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void showAddJudgeDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View dialogView = layoutInflater.inflate(R.layout.dialog_add_judge, null);
        EditText etJudgeName = dialogView.findViewById(R.id.etJudgeName);
        EditText etJudgeRegion = dialogView.findViewById(R.id.etJudgeRegion);
        EditText etJudgeCategory = dialogView.findViewById(R.id.etJudgeCategory);

        new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setPositiveButton("Добавить", (dialog, which) -> {
                    String name = etJudgeName.getText().toString();
                    String region = etJudgeRegion.getText().toString();
                    String category = etJudgeCategory.getText().toString();
                    Judge judge = new Judge();
                    judge.setComp_id(UUID.fromString(competitionUuid));
                    judge.setName(name);
                    judge.setRegion(region);
                    judge.setCategory(category);

                    SQLiteDatabase db = dataBaseHelperSecretary.getWritableDatabase();
                    long judgeId = dataBaseHelperSecretary.addJudge(db, judge, UUID.fromString(competitionUuid));

                    if (judgeId != -1) {
                        judgesList.add(judge);
                        judgesAdapter.notifyItemInserted(judgesList.size() - 1);
                    }
                })
                .setNegativeButton("Отмена", null)
                .show();
    }
}
