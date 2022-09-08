package com.np.namasteyoga.ui.asana.catagory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.np.namasteyoga.BuildConfig;
import com.np.namasteyoga.R;
import com.np.namasteyoga.datasource.pojo.AsanaListResponse;
import com.np.namasteyoga.datasource.response.AsanaListData;
import com.np.namasteyoga.datasource.response.SubCategoryData;
import com.np.namasteyoga.interfaces.PaginationListener;
import com.np.namasteyoga.ui.asana.catagory.adapter.AsanaListAdapter;
import com.np.namasteyoga.ui.asana.video.AsanaVideoActivity;
import com.np.namasteyoga.utils.C;
import com.np.namasteyoga.utils.ConstUtility;
import com.skyhope.showmoretextview.ShowMoreTextView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class ActivityAsanList extends AppCompatActivity implements AsanaListAdapter.ItemListener, PaginationListener {

    private SubCategoryData subCategoryData = new SubCategoryData();
    private ConstUtility constUtility = new ConstUtility();

    private RecyclerView rvAsanaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asan_list);

        String dataStr = getIntent().getStringExtra("subCategoryData");
        Gson gson = new Gson();
        subCategoryData = gson.fromJson(dataStr, SubCategoryData.class);

        initView();

        getAsanasList();
        showAsanaList();
    }

    private void initView() {
        ImageView onBackPressedClick = findViewById(R.id.onBackPressedClick);
        onBackPressedClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(subCategoryData.getSubcategory_name());

        ImageView ivAsana = findViewById(R.id.ivAsana);
        Picasso.get().load(subCategoryData.getSub_category_image_path()).placeholder(R.drawable.logo).into(ivAsana);

        TextView tvVideosCount = findViewById(R.id.tvVideosCount);
        String exersise = "";
        if (subCategoryData.getTotal_aasana() > 1) {
            exersise = getString(R.string.exercises);
        } else {
            exersise = getString(R.string.exercise);
        }
        tvVideosCount.setText(subCategoryData.getTotal_aasana() + " " + exersise);

        ShowMoreTextView tvShortDescription = findViewById(R.id.tvShortDescription);
        tvShortDescription.addShowMoreText(getString(R.string.read_more));
        tvShortDescription.setShowingLine(3);

        tvShortDescription.setShowMoreColor(ContextCompat.getColor(this, R.color.colorPrimary));
        tvShortDescription.setText(subCategoryData.getSubcategory_description());
//        tvShortDescription.setText(R.string.on_board_third_msg);

    }

    private int page = 0;
    private boolean isPage = true;
    private void getAsanasList() {
        if (ConstUtility.isNetworkConnectivity(this)) {
            {
                constUtility.showDialog(true, getString(R.string.please_wait_), this);
                page++;

////////////////////////////////////////////////////////////////////////////////
                JSONObject jsonObj = new JSONObject();
                try {
                    jsonObj.put("page", page);
                    jsonObj.put("aasana_categories_id", subCategoryData.getAasana_categories_id());
                    jsonObj.put("aasana_sub_categories_id", subCategoryData.getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                System.out.println("TTTTTTTTTTTTTTTTTTTTTT " + C.API_ASANA_LIST);
                JsonObjectRequest req = new JsonObjectRequest(BuildConfig.BASE_URL + C.API_ASANA_LIST, jsonObj,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    constUtility.hideDialog();
                                    if (C.DEBUG) {
                                        System.out.println("TTTTTTTTTTTTTTTTTTTTTT " + response.toString());
                                        VolleyLog.v("Response:%n %s", response.toString(4));
                                    }
                                    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                                    AsanaListResponse asanaListResponse = gson.fromJson(response.toString(), AsanaListResponse.class);

                                    if (asanaListResponse.getStatus().equals(C.NP_STATUS_SUCCESS)) {
                                        isPage = true;
                                        if (asanaListResponse.getData() != null)
                                            asanaListDataList.addAll(asanaListResponse.getData());

                                    } else {
                                        isPage = false;
                                        if (asanaListDataList.size()<1)
                                        Toast.makeText(ActivityAsanList.this, asanaListResponse.getMessage(), Toast.LENGTH_LONG).show();
                                    }

                                } catch (JSONException e) {
                                    constUtility.hideDialog();
                                    if (C.DEBUG)
                                        e.printStackTrace();
                                    isPage = false;
                                }
                                Objects.requireNonNull(rvAsanaList.getAdapter()).notifyDataSetChanged();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        isPage = false;
                        constUtility.hideDialog();
                        Toast.makeText(ActivityAsanList.this, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                        if (C.DEBUG) {
                            VolleyLog.e("Error: ", error.getMessage());
                            System.out.println("TTTTTTTTTTTTTTTTTTTTTT " + error.getMessage());
                        }
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Gson gson = new Gson();
                        Map<String, String> stringStringMap = ConstUtility.getHeaderPHP(gson.toJson(jsonObj), ActivityAsanList.this);
                        return stringStringMap;
                    }
                };
                RetryPolicy policy = new DefaultRetryPolicy(C.API_TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                req.setRetryPolicy(policy);
                RequestQueue requestQueue = Volley.newRequestQueue(ActivityAsanList.this);
                requestQueue.add(req);

//////////////////////////////////////////////////////////////////////////////////
            }
        } else {
            Toast.makeText(this, R.string.no_internet_connection,
                    Toast.LENGTH_LONG).show();
        }
    }

    private ArrayList<AsanaListData> asanaListDataList = new ArrayList<>();

    private void showAsanaList() {
        asanaListDataList.clear();
        rvAsanaList = findViewById(R.id.rvAsanaList);
        AsanaListAdapter adapter = new AsanaListAdapter(this, asanaListDataList, this,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvAsanaList.setLayoutManager(linearLayoutManager);
        rvAsanaList.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AsanaListData item) {
        // Toast.makeText(this, item.getAasana_name(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, AsanaVideoActivity.class);
        Gson gson = new Gson();
        intent.putExtra("asanaData", gson.toJson(item));
        startActivity(intent);
    }

    @Override
    public void page() {
        if ( isPage )
        getAsanasList();
    }
}