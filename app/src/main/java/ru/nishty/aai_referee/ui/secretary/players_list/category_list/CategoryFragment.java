package ru.nishty.aai_referee.ui.secretary.players_list.category_list;

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
import ru.nishty.aai_referee.entity.secretary.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CategoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private CategoriesAdapter categoriesAdapter;
    private List<Category> categoriesList;
    private DataBaseHelperSecretary dataBaseHelperSecretary;
    private String competitionUuid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        recyclerView = view.findViewById(R.id.rvCategories);
        dataBaseHelperSecretary = new DataBaseHelperSecretary(getContext());
        SQLiteDatabase dbr = dataBaseHelperSecretary.getWritableDatabase();


        // Получаем UUID соревнования из аргументов фрагмента
        Bundle arguments = getArguments();
        if (arguments != null) {
            competitionUuid = arguments.getString("competitionUuid");
        }
        categoriesList = dataBaseHelperSecretary.getCategories(dbr, UUID.fromString(competitionUuid));
        if(categoriesList==null)
            categoriesList = new ArrayList<>();
        setupRecyclerView();
        FloatingActionButton fabAddCategory = view.findViewById(R.id.fabAddCategory);
        fabAddCategory.setOnClickListener(v -> showAddCategoryDialog());

        return view;
    }

    private void setupRecyclerView() {
        categoriesAdapter = new CategoriesAdapter(categoriesList);
        recyclerView.setAdapter(categoriesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void showAddCategoryDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View dialogView = layoutInflater.inflate(R.layout.dialog_add_category, null);
        EditText etCategoryName = dialogView.findViewById(R.id.etCategoryName);
        EditText etCategoryAgeLimit = dialogView.findViewById(R.id.etCategoryAgeLimit);
        EditText etTours = dialogView.findViewById(R.id.etTours);
        EditText etFigures = dialogView.findViewById(R.id.etFigures);
        EditText etLimit = dialogView.findViewById(R.id.etLimit);



        new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setPositiveButton("Добавить", (dialog, which) -> {
                    String name = etCategoryName.getText().toString();
                    String ageLimit = etCategoryAgeLimit.getText().toString();
                    int tours = Integer.parseInt(etTours.getText().toString());
                    int figures = Integer.parseInt(etFigures.getText().toString());
                    int limit = Integer.parseInt(etLimit.getText().toString());

                    Category category = new Category();
                    category.setComp_id(UUID.fromString(competitionUuid));
                    category.setName(name);
                    category.setAgelimit(ageLimit);
                    category.setTours(tours);
                    category.setFigures(figures);
                    category.setLimit(limit);


                    SQLiteDatabase db = dataBaseHelperSecretary.getWritableDatabase();
                    long categoryId = dataBaseHelperSecretary.addCategory(db, category, UUID.fromString(competitionUuid));

                    if (categoryId != -1) {
                        categoriesList.add(category);
                        categoriesAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

}
