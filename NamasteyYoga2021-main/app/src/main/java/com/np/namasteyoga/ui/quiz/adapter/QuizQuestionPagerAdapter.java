package com.np.namasteyoga.ui.quiz.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

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
import com.np.namasteyoga.datasource.pojo.PollQuestionDatum;
import com.np.namasteyoga.datasource.pojo.QuizData;
import com.np.namasteyoga.datasource.pojo.SubmitQuizRequest;
import com.np.namasteyoga.datasource.response.AnsDatum;
import com.np.namasteyoga.datasource.response.QuestionDatum;
import com.np.namasteyoga.datasource.response.QuizResultData;
import com.np.namasteyoga.datasource.response.ResponseQuizValidate;
import com.np.namasteyoga.ui.login.LoginActivity;
import com.np.namasteyoga.ui.quiz.QuizActivity;
import com.np.namasteyoga.ui.quiz.QuizThankYouActivity;
import com.np.namasteyoga.utils.C;
import com.np.namasteyoga.utils.ConstUtility;
import com.np.namasteyoga.utils.SharedPreferencesUtils;
import com.np.namasteyoga.utils.UIUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.np.namasteyoga.modules.SharedPreference.DataPrefs;

public class QuizQuestionPagerAdapter extends PagerAdapter implements View.OnClickListener {

    private Context mContext;
    private Activity mActivity;
    private LayoutInflater mLayoutInflater;
    private List<QuestionDatum> alQuestions;
    private List<AnsDatum> ansData;
    List<PollQuestionDatum> lstquestionData;
    private SubmitQuizRequest submitQuizRequest;
    PollQuestionDatum pollQuestionDatum;
    private int selectedAnsPosition = -1;
    private ConstUtility constUtility = new ConstUtility();
    private int quiz_id;
    private SharedPreferences sharedPreferences;

    private ViewHolder viewHolder;

    private int totalItems = 0;
    private View convertView;
    private ViewPager viewPager;
    private int currentPos = 0;

    public QuizQuestionPagerAdapter(Context context, Activity activity, List<QuestionDatum> alQuestions, int quizId, ViewPager viewPager) {
        this.mContext = context;
        this.mActivity = activity;
        this.alQuestions = alQuestions;
        this.quiz_id = quizId;
        this.totalItems = alQuestions.size();
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.viewPager = viewPager;

        lstquestionData = new ArrayList<PollQuestionDatum>(alQuestions.size());

    }

    @Override
    public int getCount() {
        return alQuestions.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        convertView = null;

        //final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.question_viewpager_item, container, false);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ansData = alQuestions.get(position).getAnsData();
        Log.d("position==== ", position + "");
        Log.d("totalItems==== ", totalItems + "");
        int num = position + 1;
        viewHolder.tvQuestion.setText(alQuestions.get(position).getQuestion());
        viewHolder.tvQuestionNum.setText("Question " + num);
        viewHolder.tvQuestionCount.setText("/" + totalItems + "");
        viewHolder.rb1.setText(UIUtils.INSTANCE.getCapitalized(ansData.get(0).getAnsText()));
        viewHolder.rb2.setText(UIUtils.INSTANCE.getCapitalized(ansData.get(1).getAnsText()));
        viewHolder.rb3.setText(UIUtils.INSTANCE.getCapitalized(ansData.get(2).getAnsText()));
        viewHolder.rb4.setText(UIUtils.INSTANCE.getCapitalized(ansData.get(3).getAnsText()));

        if (position == totalItems - 1) {
            viewHolder.tvNext.setText(R.string.finish);
        } else {
            viewHolder.tvNext.setText(R.string.next);
        }
        sharedPreferences = mContext.getSharedPreferences(DataPrefs, Context.MODE_PRIVATE);

        selectedAnsPosition = -1;
        currentPos = position;

        viewHolder.rgOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.rb1:
                        selectedAnsPosition = 0;
                        break;
                    case R.id.rb2:
                        selectedAnsPosition = 1;
                        break;
                    case R.id.rb3:
                        selectedAnsPosition = 2;
                        break;
                    case R.id.rb4:
                        selectedAnsPosition = 3;
                        break;
                }

                Log.i("Abhinav", "selectedAnsPosition: " + selectedAnsPosition);
            }
        });

        viewHolder.tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Abhinav", "Check Radio -- " + position + " -- " + viewHolder.rb1.isSelected() + " -- " +
                        viewHolder.rb2.isSelected() + " -- " + viewHolder.rb3.isSelected() + " -- " + viewHolder.rb4.isSelected());

                if (selectedAnsPosition != -1) {
                    pollQuestionDatum = new PollQuestionDatum();
                    pollQuestionDatum.setQueId(alQuestions.get(position).getQueId());
                    pollQuestionDatum.setAnsId(alQuestions.get(position).getAnsData().get(selectedAnsPosition).getAnsId());

                    Log.i("Abhinav", "Que Id: " + alQuestions.get(position).getQueId());
                    Log.i("Abhinav", "Ans Id: " + alQuestions.get(position).getAnsData().get(selectedAnsPosition).getAnsId());

                    lstquestionData.add(pollQuestionDatum);

                    if (position != totalItems - 1) {
                        viewPager.setCurrentItem(position + 1);
                        selectedAnsPosition = -1;
                    } else {
                        submitRecords(lstquestionData);
                        // Toast.makeText(mContext, "Quiz finished", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(mContext, "Select a answer to proceed", Toast.LENGTH_LONG).show();
                }

                Log.i("Abhinav", "lstquestionData size " + lstquestionData.size());
                Log.i("Abhinav", "=================");


                /*if (position == totalItems - 1) {
                    //   viewPager.setCurrentItem(position + 1);


                } else {
                    if (!(viewHolder.rb1.isSelected() || viewHolder.rb2.isSelected() || viewHolder.rb3.isSelected() || viewHolder.rb4.isSelected())) {
                        viewPager.setCurrentItem(position + 1);
                    } else {
//                        viewPager.setCurrentItem(position + 1);
                    }

                   *//* if (viewHolder.rgOptions.isSelected())
                    {
                        // no radio buttons are checked
                    }*//*

                }*/
            }
        });
        convertView.setTag(viewHolder);
        container.addView(convertView);
        return convertView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }


    @Override
    public void onClick(View view) {
        /*if (view.getId() == R.id.btnSave) {
            submitRecords();
        }*/
    }

    private void submitRecords(List<PollQuestionDatum> lstquestionData) {

        if (ConstUtility.isNetworkConnectivity(mContext)) {
            {
                constUtility.showDialog(true, mContext.getString(R.string.please_wait_), mContext);

////////////////////////////////////////////////////////////////////////////////
                SubmitQuizRequest submitQuizRequest = new SubmitQuizRequest();
                QuizData quizData = new QuizData();
                quizData.setQuestionData(lstquestionData);
                quizData.setQuizId(quiz_id);
                quizData.setQuiz_responses_time(((QuizActivity) mContext).getTime());

                submitQuizRequest.setData(quizData);
                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                String json = gson.toJson(submitQuizRequest);


                JSONObject jsonObj = null;
                try {
                    VolleyLog.v("Response:%n %s", json);
                    jsonObj = new JSONObject(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println("TTTTTTTTTTTTTTTTTTTTTT " + C.API_QUIZ_SUBMIT);

                JsonObjectRequest req = new JsonObjectRequest(1, BuildConfig.BASE_URL + C.API_QUIZ_SUBMIT, jsonObj,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    constUtility.hideDialog();
                                    if (C.DEBUG) {
                                        System.out.println("SUBMIT QUIZ" + response.toString());
                                        VolleyLog.v("Response:%n %s", response.toString(4));
                                    }
                                    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                                    ResponseQuizValidate responseQuizSubmit = gson.fromJson(response.toString(), ResponseQuizValidate.class);

                                    if (responseQuizSubmit.getStatus().equals(C.NP_STATUS_SUCCESS)) {
                                        // showPollList(responsePollList.getData());
                                        openThankYouScreen(responseQuizSubmit.data);

                                    } else if (responseQuizSubmit.getStatus().equals(C.NP_INVALID_TOKEN) || responseQuizSubmit.getStatus().equals(C.NP_TOKEN_EXPIRED) || responseQuizSubmit.getStatus().equals(C.NP_TOKEN_NOT_FOUND)) {
                                        SharedPreferencesUtils.INSTANCE.setUserDetails(sharedPreferences, null);
                                        Toast.makeText(mContext, responseQuizSubmit.getMessage(), Toast.LENGTH_LONG).show();
                                        mActivity.finish();
                                        Intent intent = new Intent(mContext, LoginActivity.class);
                                        mActivity.startActivity(intent);

                                    } else {
                                        Toast.makeText(mContext, responseQuizSubmit.getMessage(), Toast.LENGTH_LONG).show();

//                                        openRoundedBottomSheet();
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
                        Toast.makeText(mContext, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                        if (C.DEBUG) {
                            VolleyLog.e("Error: ", error.getMessage());
                            System.out.println("TTTTTTTTTTTTTTTTTTTTTT " + error.getMessage());
                        }
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        return ConstUtility.getHeaderPHP(json, mContext.getApplicationContext());
                    }
                };
                RetryPolicy policy = new DefaultRetryPolicy(C.API_TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                req.setRetryPolicy(policy);
                RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                requestQueue.add(req);

//////////////////////////////////////////////////////////////////////////////////
            }
        } else {
            Toast.makeText(mContext, R.string.no_internet_connection,
                    Toast.LENGTH_LONG).show();
        }
    }

    private void openThankYouScreen(QuizResultData data) {

        int percentage = (data.marksObtained / data.totalMarks) * 100;
        Intent intent = new Intent(mContext, QuizThankYouActivity.class);
        if (percentage > 50) {
            intent.putExtra("Quiz", 1);
            intent.putExtra("Score", (data.marksObtained).toString() + "/" + (data.totalMarks).toString());
        } else {
            intent.putExtra("Score", (data.marksObtained).toString() + "/" + (data.totalMarks).toString());
            intent.putExtra("Quiz", 0);
        }
        mContext.startActivity(intent);
        ((QuizActivity) mContext).finish();
    }


    class ViewHolder {
       /* @BindView(R.id.questionviewPager)
        RelativeLayout rlPagerView;*/

        @BindView(R.id.tvQuestion)
        TextView tvQuestion;

        @BindView(R.id.tvQuestionNum)
        TextView tvQuestionNum;

        @BindView(R.id.tvQuestionCount)
        TextView tvQuestionCount;

        @BindView(R.id.rgOptions)
        RadioGroup rgOptions;

        @BindView(R.id.rb1)
        RadioButton rb1;

        @BindView(R.id.rb2)
        RadioButton rb2;

        @BindView(R.id.rb3)
        RadioButton rb3;

        @BindView(R.id.rb4)
        RadioButton rb4;

        @BindView(R.id.tvNext)
        TextView tvNext;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

    public int getCurrentPos() {
        return currentPos;
    }
}
