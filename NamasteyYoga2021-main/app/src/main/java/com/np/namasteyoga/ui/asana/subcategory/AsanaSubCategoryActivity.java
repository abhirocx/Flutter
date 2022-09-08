package com.np.namasteyoga.ui.asana.subcategory;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.np.namasteyoga.R;
import com.np.namasteyoga.datasource.response.AsanaCategoryModel;
import com.np.namasteyoga.datasource.response.SubCategoryData;
import com.np.namasteyoga.ui.asana.catagory.ActivityAsanList;
import com.np.namasteyoga.ui.asana.subcategory.adapter.AsanaSubCategoryAdapter;

import java.util.ArrayList;

public class AsanaSubCategoryActivity extends AppCompatActivity implements AsanaSubCategoryAdapter.ItemListener {

    private AsanaCategoryModel asanaCategory = new AsanaCategoryModel();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asana_sub_category);

        String asanaStr = getIntent().getStringExtra("categories");
        Gson gson = new Gson();
        asanaCategory = gson.fromJson(asanaStr, AsanaCategoryModel.class);

        init();
    }

    private void init() {
        TextView toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(asanaCategory.getCategory_name());

        ImageView onBackPressedClick = findViewById(R.id.onBackPressedClick);
        onBackPressedClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView = findViewById(R.id.recyclerView);
        if(asanaCategory!=null && asanaCategory.getSub_category_data().size() > 0){
            TextView noRecordFound = findViewById(R.id.noRecordFound);
            noRecordFound.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
//            finish();
        }



        AsanaSubCategoryAdapter adapter = new AsanaSubCategoryAdapter(this, asanaCategory.getSub_category_data(), this);
        RecyclerView.LayoutManager manager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(SubCategoryData item) {
        Intent intent = new Intent(this, ActivityAsanList.class);
        Gson gson = new Gson();
        intent.putExtra("subCategoryData", gson.toJson(item));
        startActivity(intent);
    }
}