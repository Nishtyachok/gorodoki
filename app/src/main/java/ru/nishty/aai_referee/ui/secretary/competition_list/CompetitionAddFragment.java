package ru.nishty.aai_referee.ui.secretary.competition_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import ru.nishty.aai_referee.R;
import ru.nishty.aai_referee.db.secretary.DataBaseHelperSecretary;
import ru.nishty.aai_referee.entity.secretary.CompetitionSecretary;

import java.util.UUID;

public class CompetitionAddFragment extends Fragment {

    private EditText etCompetitionName;
    private EditText etCompetitionDate;
    private EditText etCompetitionLocation;
    private EditText etHeadJudge;
    private EditText etHeadSecretary;

    private CompetitionSecretary competitionSecretary;
    private DataBaseHelperSecretary dataBaseHelperSecretary;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_competition, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etCompetitionName = view.findViewById(R.id.etCompetitionName);
        etCompetitionDate = view.findViewById(R.id.etCompetitionDate);
        etCompetitionLocation = view.findViewById(R.id.etCompetitionLocation);
        etHeadJudge = view.findViewById(R.id.etHeadJudge);
        etHeadSecretary = view.findViewById(R.id.etHeadSecretary);

        dataBaseHelperSecretary = new DataBaseHelperSecretary(getContext());

        Button addCompetitionButton = view.findViewById(R.id.btnCreateCompetition);
        addCompetitionButton.setOnClickListener(v -> {
            saveCompetitionToDatabase();
        });

        view.findViewById(R.id.btnJudges).setOnClickListener(v -> {
            navigateToJudgesFragment();
        });

        view.findViewById(R.id.btnPlayers).setOnClickListener(v -> {
            navigateToPlayersFragment();
        });
    }

    private void saveCompetitionToDatabase() {
        competitionSecretary = new CompetitionSecretary();
        competitionSecretary.setUuid(UUID.randomUUID());
        competitionSecretary.setName(etCompetitionName.getText().toString().trim());
        competitionSecretary.setYear(etCompetitionDate.getText().toString().trim());
        competitionSecretary.setPlace(etCompetitionLocation.getText().toString().trim());
        competitionSecretary.setHeadJudge(etHeadJudge.getText().toString().trim());
        competitionSecretary.setHeadSecretary(etHeadSecretary.getText().toString().trim());


        clearInputFields();
    }

    private void navigateToJudgesFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("competitionUuid", competitionSecretary.getUuid().toString());
        Navigation.findNavController(requireView()).navigate(R.id.action_competitionAddFragment_to_judgesFragment, bundle);
    }
    private void navigateToPlayersFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("competitionUuid", competitionSecretary.getUuid().toString());
        Navigation.findNavController(requireView()).navigate(R.id.action_competitionAddFragment_to_playersFragment, bundle);
    }

    private void clearInputFields() {
        etCompetitionName.setText("");
        etCompetitionDate.setText("");
        etCompetitionLocation.setText("");
        etHeadJudge.setText("");
        etHeadSecretary.setText("");
    }
}