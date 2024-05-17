package ru.nishty.aai_referee.ui.secretary.players_list.category_list;

import android.app.AlertDialog;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import ru.nishty.aai_referee.R;
import ru.nishty.aai_referee.db.secretary.DataBaseContractSecretary;
import ru.nishty.aai_referee.db.secretary.DataBaseHelperSecretary;
import ru.nishty.aai_referee.entity.secretary.Category;

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
        categoriesList = dataBaseHelperSecretary.getCategories(dbr, competitionUuid);
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
        Spinner spinnerCategoryConfig = dialogView.findViewById(R.id.etFigures);
        Spinner spinnerCategoryTour = dialogView.findViewById(R.id.etTours);
        EditText etLimit = dialogView.findViewById(R.id.etLimit);

        List<String> CategoryConfigNames = new ArrayList<>();
        CategoryConfigNames.add("Выберите дисциплину");
        for (int i = 1; i <= 3; i++) {
            int categoryConfResourceId = DataBaseContractSecretary.CategoryHelper.getCategoryConf(i);
            if (categoryConfResourceId != -1) {
                CategoryConfigNames.add(getContext().getString(categoryConfResourceId));
            }
        }
        ArrayAdapter<String> categoryConfAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, CategoryConfigNames) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if (position == 0) {
                    ((TextView) view).setTextColor(Color.GRAY);
                } else {
                    ((TextView) view).setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        categoryConfAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoryConfig.setAdapter(categoryConfAdapter);

        List<String> CategoryTourNames = new ArrayList<>();
        CategoryTourNames.add("Выберите кол-во туров");
        for (int i = 1; i <= 3; i++) {
            CategoryTourNames.add(String.valueOf(i));
        }
        ArrayAdapter<String> categoryTourAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, CategoryTourNames) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if (position == 0) {
                    ((TextView) view).setTextColor(Color.GRAY);
                } else {
                    ((TextView) view).setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        categoryTourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoryTour.setAdapter(categoryTourAdapter);

        new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setPositiveButton("Добавить", (dialog, which) -> {
                    String name = etCategoryName.getText().toString();
                    String ageLimit = etCategoryAgeLimit.getText().toString();
                    int selectedCategoryTour = spinnerCategoryTour.getSelectedItemPosition();
                    int selectedCategoryConfig = spinnerCategoryConfig.getSelectedItemPosition();
                    int limit = Integer.parseInt(etLimit.getText().toString());
                    if (selectedCategoryConfig == 0) {
                        Toast.makeText(getActivity(), "Пожалуйста, выберите дисциплину", Toast.LENGTH_SHORT).show();
                    }
                    if (selectedCategoryTour == 0) {
                        Toast.makeText(getActivity(), "Пожалуйста, выберите кол-во туров", Toast.LENGTH_SHORT).show();
                    }
                    Category category = new Category();
                    category.setComp_id(competitionUuid);
                    category.setName(name);
                    category.setAgelimit(ageLimit);
                    category.setTours(selectedCategoryTour);
                    category.setConfig(selectedCategoryConfig);
                    category.setLimit(limit);


                    SQLiteDatabase db = dataBaseHelperSecretary.getWritableDatabase();
                    long categoryId = dataBaseHelperSecretary.addCategory(db, category, competitionUuid);

                    if (categoryId != -1) {
                        categoriesList.add(category);
                        categoriesAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Отмена", null)
                .show();
    }
}
