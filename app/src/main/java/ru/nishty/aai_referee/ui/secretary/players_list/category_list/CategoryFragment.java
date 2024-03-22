package ru.nishty.aai_referee.ui.secretary.players_list.category_list;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import ru.nishty.aai_referee.databinding.FragmentCategoryBinding;
import ru.nishty.aai_referee.db.secretary.DataBaseHelperSecretary;
import ru.nishty.aai_referee.entity.secretary.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// CategoryFragment.java
public class CategoryFragment extends Fragment {

    private FragmentCategoryBinding binding;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;
    private DataBaseHelperSecretary dataBaseHelperSecretary;
    private String competitionUuid;

    public static CategoryFragment newInstance(String uuid) {
        CategoryFragment fragment = new CategoryFragment();
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
        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(categoryList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(categoryAdapter);

        dataBaseHelperSecretary = new DataBaseHelperSecretary(getContext());
        loadCategoriesFromDatabase();
    }

    private void loadCategoriesFromDatabase() {
        SQLiteDatabase db = dataBaseHelperSecretary.getReadableDatabase();
        categoryList.clear();
        categoryList.addAll(dataBaseHelperSecretary.getCategories(db, UUID.fromString(competitionUuid)));
        categoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


