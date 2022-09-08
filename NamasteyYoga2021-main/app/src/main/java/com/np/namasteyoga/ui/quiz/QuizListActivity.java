package com.np.namasteyoga.ui.quiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate;
import com.akexorcist.localizationactivity.core.OnLocaleChangedListener;
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
import com.np.namasteyoga.datasource.request.ValidateQuizRequest;
import com.np.namasteyoga.datasource.response.Datum;
import com.np.namasteyoga.datasource.response.ResponsePollList;
import com.np.namasteyoga.datasource.response.ResponseQuizValidate;
import com.np.namasteyoga.ui.login.LoginActivity;
import com.np.namasteyoga.ui.quiz.adapter.QuizListAdapter;
import com.np.namasteyoga.ui.quiz.fragments.QuizBottomsheet;
import com.np.namasteyoga.utils.C;
import com.np.namasteyoga.utils.ConstUtility;
import com.np.namasteyoga.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import static com.np.namasteyoga.modules.SharedPreference.DataPrefs;

public class QuizListActivity extends AppCompatActivity implements QuizListAdapter.ItemListener, OnLocaleChangedListener {

    TextView toolbarTitle;
    private ConstUtility constUtility = new ConstUtility();
    private ResponsePollList responsePollList;
    private ResponseQuizValidate responseQuizValidate;
    private RecyclerView rvQuizList;
    private LinearLayout llStartQuiz;
    Datum pollData;
    private SharedPreferences sharedPreferences;
    private QuizBottomsheet myRoundedBottomSheet = new QuizBottomsheet();
    private LocalizationActivityDelegate localizationDelegate =  new LocalizationActivityDelegate(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        localizationDelegate.addOnLocaleChangedListener(this);
        localizationDelegate.onCreate();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_list);

        ImageView onBackPressedClick = findViewById(R.id.onBackPressedClick);
        onBackPressedClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        toolbarTitle = findViewById(R.id.toolbarTitle);
        rvQuizList = findViewById(R.id.rvQuizList);
        llStartQuiz = findViewById(R.id.llStartQuiz);
        toolbarTitle.setText(R.string.quiz);
        sharedPreferences = getSharedPreferences(DataPrefs, Context.MODE_PRIVATE);

        llStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLogin(pollData);
            }
        });
        getQuizList();
    }

    private void checkLogin(Datum pollData) {
        if (SharedPreferencesUtils.INSTANCE.getUserDetails(sharedPreferences) == null) {

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        } else {

            validatePoll(pollData);
//            Intent intent = new Intent(this, PollActivity.class);
//            startActivity(intent);
        }
    }

    private void validatePoll(Datum pollData) {
        if (ConstUtility.isNetworkConnectivity(this)) {
            {
                constUtility.showDialog(true, getString(R.string.please_wait_), this);

////////////////////////////////////////////////////////////////////////////////
                ValidateQuizRequest validateQuizRequest = new ValidateQuizRequest();
                validateQuizRequest.setQuiz_id(pollData.getQuiz_id());
                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                String json = gson.toJson(validateQuizRequest);


                JSONObject jsonObj = null;
                try {
                    VolleyLog.v("Response:%n %s", json);
                    jsonObj = new JSONObject(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println("TTTTTTTTTTTTTTTTTTTTTT" + C.API_QUIZ_VALIDATE);

                JsonObjectRequest req = new JsonObjectRequest(1, BuildConfig.BASE_URL + C.API_QUIZ_VALIDATE, jsonObj,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    constUtility.hideDialog();
                                    if (C.DEBUG) {
                                        System.out.println("TTTTTTTTTTTTTTTTTTTTTT" + response.toString());
                                        VolleyLog.v("Response:%n %s", response.toString(4));
                                    }
                                    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                                    responseQuizValidate = gson.fromJson(response.toString(), ResponseQuizValidate.class);

                                    if (responseQuizValidate.getStatus().equals(C.NP_STATUS_SUCCESS)) {
                                        // showPollList(responsePollList.getData());
                                        Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
                                        Gson gsonn = new Gson();
                                        intent.putExtra("PollData", gsonn.toJson(pollData));
                                        startActivity(intent);
                                    } else if (responseQuizValidate.getStatus().equals(C.NP_INVALID_TOKEN) || responseQuizValidate.getStatus().equals(C.NP_TOKEN_EXPIRED) || responseQuizValidate.getStatus().equals(C.NP_TOKEN_NOT_FOUND)) {
                                        SharedPreferencesUtils.INSTANCE.setUserDetails(sharedPreferences, null);
                                        Toast.makeText(QuizListActivity.this, responseQuizValidate.message, Toast.LENGTH_LONG).show();
                                        finish();

                                    } else {
                                        openRoundedBottomSheet(responseQuizValidate.getMessage());

                                    }

                                } catch (JSONException e) {
                                    constUtility.hideDialog();
                                    if (C.DEBUG)
                                        e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        constUtility.hideDialog();
                        Toast.makeText(QuizListActivity.this, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                        if (C.DEBUG) {
                            VolleyLog.e("Error: ", error.getMessage());
                            System.out.println("TTTTTTTTTTTTTTTTTTTTTT " + error.getMessage());
                        }
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        return ConstUtility.getHeaderPHP(json, getApplicationContext());
                    }
                };
                RetryPolicy policy = new DefaultRetryPolicy(C.API_TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                req.setRetryPolicy(policy);
                RequestQueue requestQueue = Volley.newRequestQueue(QuizListActivity.this);
                requestQueue.add(req);

//////////////////////////////////////////////////////////////////////////////////
            }
        } else {
            Toast.makeText(this, R.string.no_internet_connection,
                    Toast.LENGTH_LONG).show();
        }
    }

    private void getQuizList() {
        if (ConstUtility.isNetworkConnectivity(this)) {
            {
                constUtility.showDialog(true, getString(R.string.please_wait_), this);

////////////////////////////////////////////////////////////////////////////////
                JSONObject jsonObj = new JSONObject();
                jsonObj = null;

                System.out.println("TTTTTTTTTTTTTTTTTTTTTT " + C.API_QUIZ_LIST);

                JsonObjectRequest req = new JsonObjectRequest(0, BuildConfig.BASE_URL + C.API_QUIZ_LIST, jsonObj,
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
                                    responsePollList = gson.fromJson(response.toString(), ResponsePollList.class);

                                    if (responsePollList.getStatus().equals(C.NP_STATUS_SUCCESS)) {
                                        if (responsePollList.getData().size() > 0)
                                            showQuizList(responsePollList.getData());
                                        else {
                                            Toast.makeText(QuizListActivity.this, getString(R.string.no_record_found),
                                                    Toast.LENGTH_LONG).show();
//                                            finish();
                                        }
                                    } else if (responsePollList.getStatus().equals(C.NP_INVALID_TOKEN) || responsePollList.getStatus().equals(C.NP_TOKEN_EXPIRED) || responsePollList.getStatus().equals(C.NP_TOKEN_NOT_FOUND)) {
                                        SharedPreferencesUtils.INSTANCE.setUserDetails(sharedPreferences, null);
                                        Toast.makeText(QuizListActivity.this, responsePollList.getMessage(), Toast.LENGTH_LONG).show();
                                        setResult(RESULT_OK);
                                        finish();
                                    } else {
                                        Toast.makeText(QuizListActivity.this, responsePollList.getMessage(),
                                                Toast.LENGTH_LONG).show();
//                                        finish();
                                    }

                                } catch (JSONException e) {
                                    constUtility.hideDialog();
                                    Toast.makeText(QuizListActivity.this, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                                    if (C.DEBUG)
                                        e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        constUtility.hideDialog();
                        Toast.makeText(QuizListActivity.this, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                        if (C.DEBUG) {
                            VolleyLog.e("Error: ", error.getMessage());
                            System.out.println("TTTTTTTTTTTTTTTTTTTTTT " + error.getMessage());
                        }
                    }
                }) /*{
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Gson gson = new Gson();
                        Map<String, String> stringStringMap = ConstUtility.getHeaderPHP();
                        return stringStringMap;
                    }
                }*/;
                RetryPolicy policy = new DefaultRetryPolicy(C.API_TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                req.setRetryPolicy(policy);
                RequestQueue requestQueue = Volley.newRequestQueue(QuizListActivity.this);
                requestQueue.add(req);

//////////////////////////////////////////////////////////////////////////////////
            }
        } else {
            Toast.makeText(this, R.string.no_internet_connection,
                    Toast.LENGTH_LONG).show();
        }
    }

    private void showQuizList(List<Datum> data) {
        QuizListAdapter adapter = new QuizListAdapter(this, data, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvQuizList.setLayoutManager(linearLayoutManager);
        rvQuizList.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        localizationDelegate.onResume(this);
    }

    @Override
    public void onItemClick(Datum item) {
        pollData = item;
        llStartQuiz.setVisibility(View.VISIBLE);
    }

    private void openRoundedBottomSheet(String message) {
        if (myRoundedBottomSheet.isAdded() || myRoundedBottomSheet.isVisible()) {
            myRoundedBottomSheet.dismiss();
        }
        myRoundedBottomSheet.show(getSupportFragmentManager(), myRoundedBottomSheet.getTag());
        new Handler(Looper.getMainLooper()).postDelayed(() -> myRoundedBottomSheet.setData(message), 300);
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        applyOverrideConfiguration(localizationDelegate.updateConfigurationLocale(newBase));
        super.attachBaseContext(newBase);
    }

    @Override
    public Context getApplicationContext() {
        return localizationDelegate.getApplicationContext(super.getApplicationContext());
    }

    @Override
    public Resources getResources() {
        return localizationDelegate.getResources(super.getResources());
    }

    @Override
    public void onAfterLocaleChanged() {

    }

    @Override
    public void onBeforeLocaleChanged() {

    }
}