package ru.nishty.aai_referee.ui.secretary.players_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import ru.nishty.aai_referee.R;
import ru.nishty.aai_referee.databinding.FragmentPlayersBinding;

public class PlayersFragment extends Fragment {

    private FragmentPlayersBinding binding;
    private String competitionUuid;

    public static PlayersFragment newInstance(String uuid) {
        PlayersFragment fragment = new PlayersFragment();
        Bundle args = new Bundle();
        args.putString("competitionUuid", uuid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            competitionUuid = getArguments().getString("competitionUuid");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPlayersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnCategories.setOnClickListener(v -> navigateToCategoriesFragment());
        binding.btnRegions.setOnClickListener(v -> navigateToRegionsFragment());
        binding.btnPlayers.setOnClickListener(v -> navigateToPlayerListFragment());
    }

    private void navigateToCategoriesFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("competitionUuid", competitionUuid);
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_playersFragment_to_categoriesFragment, bundle);
    }

    private void navigateToRegionsFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("competitionUuid", competitionUuid);
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_playersFragment_to_regionsFragment, bundle);
    }

    private void navigateToPlayerListFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("competitionUuid", competitionUuid);
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_playersFragment_to_playerListFragment, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}




