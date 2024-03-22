package ru.nishty.aai_referee.ui.role_selection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import ru.nishty.aai_referee.R;

public class RoleSelectionFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_role_selection, container, false);

        Button refereeButton = rootView.findViewById(R.id.referee);
        refereeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ваш код для перехода к competitionFragment
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.action_fragment_role_selection_to_competitionFragment);
            }
        });
        Button secretaryButton = rootView.findViewById(R.id.secretary);

        secretaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.action_fragment_role_selection_to_competitionFragment2);
            }
        });


        // Добавьте обработчики для других элементов, если необходимо

        return rootView;
    }
}
