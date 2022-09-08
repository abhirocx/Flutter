package com.np.namasteyoga.ui.eventModule.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate;
import com.akexorcist.localizationactivity.core.OnLocaleChangedListener;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.np.namasteyoga.BuildConfig;
import com.np.namasteyoga.R;
import com.np.namasteyoga.comman.CommonString;
import com.np.namasteyoga.datasource.pojo.Event;
import com.np.namasteyoga.datasource.pojo.EventResponse;
import com.np.namasteyoga.datasource.pojo.GetEventRequest;
import com.np.namasteyoga.datasource.pojo.NotifyRequest;
import com.np.namasteyoga.datasource.pojo.NotifyResponse;
import com.np.namasteyoga.datasource.pojo.RatingRequest;
import com.np.namasteyoga.datasource.pojo.RatingResponse;
import com.np.namasteyoga.datasource.response.UserDetail;
import com.np.namasteyoga.interfaces.OnClickItem;
import com.np.namasteyoga.interfaces.Rating;
import com.np.namasteyoga.interfaces.Rating2;
import com.np.namasteyoga.ui.eventModule.adapter.EventListAdapter;
import com.np.namasteyoga.ui.login.LoginActivity;
import com.np.namasteyoga.utils.C;
import com.np.namasteyoga.utils.ConstUtility;
import com.np.namasteyoga.utils.SharedPreference;
import com.np.namasteyoga.utils.SharedPreferencesUtils;
import com.np.namasteyoga.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.np.namasteyoga.modules.SharedPreference.DataPrefs;


public class FragmentUserEvents extends AppCompatActivity implements Rating, Rating2, OnClickItem<Event>, OnLocaleChangedListener {

    @BindView(R.id.rlEvent)
    RelativeLayout rlEvent;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.ivSearch)
    ImageView ivSearch;
    @BindView(R.id.rlHeader)
    RelativeLayout rlHeader;
    @BindView(R.id.llNoEvents)
    LinearLayout llNoEvents;

//    @BindView(R.id.etFullName)
//    EditText etFullName;
//    @BindView(R.id.etEmail)
//    EditText etEmail;
//    @BindView(R.id.etPhone)
//    EditText etPhone;


    @BindView(R.id.rlSubmit)
    RelativeLayout rlSubmit;

    @BindView(R.id.current_event_recycler_view)
    RecyclerView currentEventRecyclerView;
    @BindView(R.id.upcoming_event_recycler_view)
    RecyclerView upcomingEventRecyclerView;
    @BindView(R.id.past_event_recycler_view)
    RecyclerView pastEventRecyclerView;
    @BindView(R.id.llevents)
    LinearLayout llevents;
    //    @BindView(R.id.ivLogin)
//    ImageView ivLogin;
//    @BindView(R.id.llLogin)
//    LinearLayout llLogin;
    @BindView(R.id.llcreateevent)
    LinearLayout llcreateevent;
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmer_view_container;
    ArrayList<Event> currentEventList;
    ArrayList<Event> upcomingEventList;
    ArrayList<Event> pastEventList;
    @BindView(R.id.tvFirstText)
    TextView tvFirstText;
    @BindView(R.id.tvSecondtext)
    TextView tvSecondtext;
//    @BindView(R.id.ivCreateEvent)
//    ImageView ivCreateEvent;


    EventResponse eventResponse;
    EventResponse eventListResponseCurrent;
    EventResponse eventListResponseUpcoming;
    EventResponse eventListResponsePast;
    int page_id = 1;
    //    AdapterEventPager currentEventAdapterEventPager;
    EventListAdapter currentEventAdapterEventPager;
    EventListAdapter upcomingEventAdapterEventPager;
    //    AdapterEventPager upcomingEventAdapterEventPager;
//    AdapterEventPager pastEventAdapterEventPager;
    EventListAdapter pastEventAdapterEventPager;
    @BindView(R.id.btn_today_event)
    Button btnTodayEvent;
    @BindView(R.id.btn_upcoming_event)
    Button btnUpcomingEvent;
    @BindView(R.id.btn_past_event)
    Button btnPastEvent;
    Unbinder unbinder;


    private int lastPage;
    private RequestQueue requestQueue;
    private LinearLayoutManager mLayoutManager1;
    private LinearLayoutManager mLayoutManager2;
    private LinearLayoutManager mLayoutManager3;
    private ProgressDialog progressDialog;
    private RatingResponse ratingResponse = new RatingResponse();
    private int rating = 0;
    String eventType = "";

    boolean current = false;
    boolean upcoming = false;
    boolean past = false;
    boolean currentNoEvents = false;
    boolean upcomingNoEvents = false;
    boolean pastNoEvents = false;
    private CustomDialogEventFragment myRoundedBottomSheet = new CustomDialogEventFragment();
    private MyEventPastDialogFragment myEventPastDialogFragment = new MyEventPastDialogFragment();

    public FragmentUserEvents() {
        // Required empty public constructor
    }

    private SharedPreferences sharedPreferences;

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        sharedPreferences = requireContext().getSharedPreferences(DataPrefs, Context.MODE_PRIVATE);
//        View view = inflater.inflate(R.layout.fragment_events, container, false);
//        unbinder = ButterKnife.bind(this, view);
//        btnTodayEvent.setBackgroundResource(R.drawable.yoga_event_top_active_button_background);
//        btnTodayEvent.setTextColor(getResources().getColor(R.color.white));
//        return view;
//    }


    private LocalizationActivityDelegate localizationDelegate = new LocalizationActivityDelegate(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        localizationDelegate.addOnLocaleChangedListener(this);
        localizationDelegate.onCreate();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_user_events);
        sharedPreferences = getSharedPreferences(DataPrefs, Context.MODE_PRIVATE);
        unbinder = ButterKnife.bind(this);
//        btnUpcomingEvent.setBackgroundResource(R.drawable.yoga_event_top_active_button_background);
//        btnUpcomingEvent.setTextColor(getResources().getColor(R.color.white));

        btnTodayEvent.setBackgroundResource(R.drawable.yoga_event_top_active_button_background);
        btnTodayEvent.setTextColor(getResources().getColor(R.color.white));

        mLayoutManager1 = new LinearLayoutManager(this);
        mLayoutManager2 = new LinearLayoutManager(this);
        mLayoutManager3 = new LinearLayoutManager(this);

        currentEventList = new ArrayList<Event>();
//        currentEventAdapterEventPager = new AdapterEventPager(this,currentEventList, FragmentUserEvents.this,sharedPreferences,false);
        currentEventAdapterEventPager = new EventListAdapter(currentEventList, FragmentUserEvents.this, sharedPreferences, false, 0, this, false);
        currentEventRecyclerView.setAdapter(currentEventAdapterEventPager);
        currentEventRecyclerView.setItemAnimator(new DefaultItemAnimator());

        upcomingEventList = new ArrayList<Event>();
//        upcomingEventAdapterEventPager = new AdapterEventPager(this, upcomingEventList, FragmentUserEvents.this,sharedPreferences);
        upcomingEventAdapterEventPager = new EventListAdapter(upcomingEventList, FragmentUserEvents.this, sharedPreferences, false, 1, this, false);
        upcomingEventRecyclerView.setAdapter(upcomingEventAdapterEventPager);
        upcomingEventRecyclerView.setItemAnimator(new DefaultItemAnimator());

        pastEventList = new ArrayList<Event>();
        pastEventAdapterEventPager = new EventListAdapter(pastEventList, FragmentUserEvents.this, sharedPreferences, true, 2, this, false);
        pastEventRecyclerView.setAdapter(pastEventAdapterEventPager);
        pastEventRecyclerView.setItemAnimator(new DefaultItemAnimator());

        eventType = CommonString.CURRENT;
//        eventType = CommonString.UPCOMING;
        getData(eventType);

        btnTodayEvent.setOnClickListener(view -> {
            setNotifyButtonVisibility(false);

            llevents.setVisibility(View.VISIBLE);
            btnTodayEvent.setBackgroundResource(R.drawable.yoga_event_top_active_button_background);
            btnTodayEvent.setTextColor(getResources().getColor(R.color.white));
            btnUpcomingEvent.setBackgroundResource(R.drawable.yoga_event_top_deactive_button_background);
            btnUpcomingEvent.setTextColor(getResources().getColor(R.color.yoga_event_deactive_text_color));
            btnPastEvent.setBackgroundResource(R.drawable.yoga_event_top_deactive_button_background);
            btnPastEvent.setTextColor(getResources().getColor(R.color.yoga_event_deactive_text_color));
            eventType = CommonString.CURRENT;
            currentEventRecyclerView.setVisibility(View.VISIBLE);
            upcomingEventRecyclerView.setVisibility(View.GONE);
            pastEventRecyclerView.setVisibility(View.GONE);
            if (currentEventList.size() > 0) {
                llNoEvents.setVisibility(View.GONE);
                //ivSearch.setVisibility(View.VISIBLE);
            } else {
                llNoEvents.setVisibility(View.VISIBLE);
//                    ivSearch.setVisibility(View.GONE);
            }
            if (currentNoEvents || current) {
                return;
            }
            getData(eventType);
        });

        btnUpcomingEvent.setOnClickListener(view -> {
            setNotifyButtonVisibility(false);
            llevents.setVisibility(View.VISIBLE);

            btnTodayEvent.setBackgroundResource(R.drawable.yoga_event_top_deactive_button_background);
            btnTodayEvent.setTextColor(getResources().getColor(R.color.yoga_event_deactive_text_color));
            btnUpcomingEvent.setBackgroundResource(R.drawable.yoga_event_top_active_button_background);
            btnUpcomingEvent.setTextColor(getResources().getColor(R.color.white));
            btnPastEvent.setBackgroundResource(R.drawable.yoga_event_top_deactive_button_background);
            btnPastEvent.setTextColor(getResources().getColor(R.color.yoga_event_deactive_text_color));
            eventType = CommonString.UPCOMING;
            currentEventRecyclerView.setVisibility(View.GONE);
            upcomingEventRecyclerView.setVisibility(View.VISIBLE);
            pastEventRecyclerView.setVisibility(View.GONE);
            if (upcomingEventList.size() > 0) {
                llNoEvents.setVisibility(View.GONE);
                //ivSearch.setVisibility(View.VISIBLE);
            } else {
                llNoEvents.setVisibility(View.VISIBLE);
//                    ivSearch.setVisibility(View.GONE);
            }
            if (upcomingNoEvents || upcoming) {
                return;
            }
            getData(eventType);
        });

        btnPastEvent.setOnClickListener(view -> {
            setNotifyButtonVisibility(true);
            llevents.setVisibility(View.VISIBLE);

            btnTodayEvent.setBackgroundResource(R.drawable.yoga_event_top_deactive_button_background);
            btnTodayEvent.setTextColor(getResources().getColor(R.color.yoga_event_deactive_text_color));
            btnUpcomingEvent.setBackgroundResource(R.drawable.yoga_event_top_deactive_button_background);
            btnUpcomingEvent.setTextColor(getResources().getColor(R.color.yoga_event_deactive_text_color));
            btnPastEvent.setBackgroundResource(R.drawable.yoga_event_top_active_button_background);
            btnPastEvent.setTextColor(ContextCompat.getColor(FragmentUserEvents.this, R.color.white));
            eventType = CommonString.PAST;
            currentEventRecyclerView.setVisibility(View.GONE);
            upcomingEventRecyclerView.setVisibility(View.GONE);
            pastEventRecyclerView.setVisibility(View.VISIBLE);
            if (pastEventList.size() > 0) {
                llNoEvents.setVisibility(View.GONE);
                //ivSearch.setVisibility(View.VISIBLE);
            } else {
                llNoEvents.setVisibility(View.VISIBLE);
//                    ivSearch.setVisibility(View.GONE);
            }
            if (pastNoEvents || past) {
                return;
            }
            getData(eventType);
        });

        llevents.setOnClickListener(view -> {

        });
    }

    private void setNotifyButtonVisibility(boolean isHide) {
        if (isHide) {
            tvSecondtext.setVisibility(View.GONE);
            rlSubmit.setVisibility(View.GONE);
        } else {
            tvSecondtext.setVisibility(View.VISIBLE);
            rlSubmit.setVisibility(View.VISIBLE);
        }
    }

    private void getData(String type) {
        eventType = type;
        getEvents(type);
        llNoEvents.setVisibility(View.GONE);
        shimmer_view_container.startShimmer();
        shimmer_view_container.setVisibility(View.VISIBLE);

        if (eventType.equals(CommonString.CURRENT)) {
            currentEventRecyclerView.setLayoutManager(mLayoutManager1);
            currentEventRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
//                if (rlSearch.getVisibility() == View.VISIBLE)
//                    return;
                    if (progressDialog != null && progressDialog.isShowing())
                        return;
                    int visibleItemCount = mLayoutManager1.getChildCount();
                    int totalItemCount = mLayoutManager1.getItemCount();
                    int firstVisibleItemPosition = mLayoutManager1.findFirstVisibleItemPosition();
                    if ((visibleItemCount + firstVisibleItemPosition) >=
                            totalItemCount && firstVisibleItemPosition >= 0) {
                        if (lastPage > page_id)
                            additems(page_id + 1, CommonString.CURRENT);
                    }
                }
            });
        }
        if (eventType.equals(CommonString.UPCOMING)) {
            upcomingEventRecyclerView.setLayoutManager(mLayoutManager2);
            upcomingEventRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
//                if (rlSearch.getVisibility() == View.VISIBLE)
//                    return;
                    if (progressDialog != null && progressDialog.isShowing())
                        return;
                    int visibleItemCount = mLayoutManager2.getChildCount();
                    int totalItemCount = mLayoutManager2.getItemCount();
                    int firstVisibleItemPosition = mLayoutManager2.findFirstVisibleItemPosition();
                    if ((visibleItemCount + firstVisibleItemPosition) >=
                            totalItemCount && firstVisibleItemPosition >= 0) {
                        if (lastPage > page_id)
                            additems(page_id + 1, CommonString.UPCOMING);
                    }
                }
            });
        }

        if (eventType.equals(CommonString.PAST)) {
            pastEventRecyclerView.setLayoutManager(mLayoutManager3);
            pastEventRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
//                if (rlSearch.getVisibility() == View.VISIBLE)
//                    return;
                    if (progressDialog != null && progressDialog.isShowing())
                        return;
                    int visibleItemCount = mLayoutManager3.getChildCount();
                    int totalItemCount = mLayoutManager3.getItemCount();
                    int firstVisibleItemPosition = mLayoutManager3.findFirstVisibleItemPosition();
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                        if (lastPage > page_id)
                            additems(page_id + 1, CommonString.PAST);
                    }
                }
            });
        }


    }

    private void additems(int page, String eventType) {
//        if (ConstUtility.isNetworkConnectivity(getActivity())) {
        if (ConstUtility.isNetworkConnectivity(this)) {
            GetEventRequest getEventRequest = new GetEventRequest();
//            getEventRequest.setCity(Util.INSTANCE.getCitySelected().getCity());
//            getEventRequest.setState(Util.INSTANCE.getCitySelected().getState_name());
//            getEventRequest.setCountry(Util.INSTANCE.getCitySelected().getCountry_name());
            getEventRequest.setEventType(eventType);

            getEventRequest.setPage(page);

            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            String json = gson.toJson(getEventRequest);
            JSONObject jsonObj = null;
            try {
                VolleyLog.v("Response:%n %s", json);
                jsonObj = new JSONObject(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            progressDialog = ConstUtility.getProgressDialog(getActivity(), "Getting Events...");
            progressDialog = ConstUtility.getProgressDialog(this, getString(R.string.getting_events));
            progressDialog.show();

            JsonObjectRequest req = new JsonObjectRequest(BuildConfig.BASE_URL + C.API_MY_EVENT, jsonObj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                VolleyLog.v("Response:%n %s", response.toString(4));

                                progressDialog.dismiss();
//                            flag_loading = false;
//                                eventListResponse = gson.fromJson(response.toString(), EventResponse.class);
//                                page_id = eventListResponse.getCurrentPage();
//                                eventList = eventListResponse.getData();
                                if (eventType.equals(CommonString.CURRENT)) {
                                    eventListResponseCurrent = gson.fromJson(response.toString(), EventResponse.class);
                                    page_id = eventListResponseCurrent.getCurrentPage();
                                    currentEventList = eventListResponseCurrent.getData();
                                    if (currentEventAdapterEventPager != null && currentEventAdapterEventPager.getItemCount() < eventListResponseCurrent.getTotalRecord()) {
//                                recyclerView.setAdapter(adapterEventPager);
                                        currentEventAdapterEventPager.addItem(eventListResponseCurrent.getData());
                                        currentEventAdapterEventPager.notifyDataSetChanged();
                                    }
                                }
                                if (eventType.equals(CommonString.UPCOMING)) {
                                    eventListResponseUpcoming = gson.fromJson(response.toString(), EventResponse.class);
                                    page_id = eventListResponseUpcoming.getCurrentPage();
                                    upcomingEventList = eventListResponseUpcoming.getData();
                                    if (upcomingEventAdapterEventPager != null && upcomingEventAdapterEventPager.getItemCount() < eventListResponseUpcoming.getTotalRecord()) {
//                                recyclerView.setAdapter(adapterEventPager);
                                        upcomingEventAdapterEventPager.addItem(eventListResponseUpcoming.getData());
                                        upcomingEventAdapterEventPager.notifyDataSetChanged();
                                    }
                                }
                                if (eventType.equals(CommonString.PAST)) {
                                    eventListResponsePast = gson.fromJson(response.toString(), EventResponse.class);
                                    page_id = eventListResponsePast.getCurrentPage();
                                    pastEventList = eventListResponsePast.getData();
                                    if (pastEventAdapterEventPager != null && pastEventAdapterEventPager.getItemCount() < eventListResponsePast.getTotalRecord()) {
//                                recyclerView.setAdapter(adapterEventPager);
                                        pastEventAdapterEventPager.addItem(eventListResponsePast.getData());
                                        pastEventAdapterEventPager.notifyDataSetChanged();
                                    }
                                }



                                /*if (adapterEventPager != null && adapterEventPager.getItemCount() < eventListResponse.getTotalRecord()) {
//                                recyclerView.setAdapter(adapterEventPager);
                                    adapterEventPager.addItem(eventListResponse.getData());
                                    adapterEventPager.notifyDataSetChanged();
                                }*/
                                //showLoginButtton();
                            } catch (JSONException e) {
                                progressDialog.dismiss();
                                e.printStackTrace();
                            } catch (Exception e) {
                                progressDialog.dismiss();

                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    VolleyLog.e("Error: ", error.getMessage());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return ConstUtility.getHeaderPHP(json, getApplicationContext());
                }
            };
            RetryPolicy policy = new DefaultRetryPolicy(C.API_TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            req.setRetryPolicy(policy);
//            requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(req);
        } else {
            Toast.makeText(this, getString(R.string.no_internet_connection),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void getEvents(String type) {
//        if (ConstUtility.isNetworkConnectivity(getActivity())) {
        if (ConstUtility.isNetworkConnectivity(this)) {

            GetEventRequest getEventRequest = new GetEventRequest();

//            if (Util.INSTANCE.getCitySelected() != null) {
            getEventRequest.setPage(1);
            getEventRequest.setEventType(type);

            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            String json = gson.toJson(getEventRequest);
            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            switch (type) {
                case CommonString.CURRENT:
                    getCurrentEvents(json, jsonObj);
                    break;
                case CommonString.UPCOMING:
                    getUpcomingEvents(json, jsonObj);
                    break;
                case CommonString.PAST:
                    getPastEvents(json, jsonObj);
                    break;
            }

            ////////////////////////////////////////////////////////////////////////////////
                /*JsonObjectRequest req = new JsonObjectRequest(C.API_MY_EVENT, jsonObj,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
//                            utils.hideDialog();
                                    if (C.DEBUG) {
                                        Log.d("RESPONSE ------------- ", response.toString());
                                    }
                                    shimmer_view_container.stopShimmer();
                                    shimmer_view_container.setVisibility(View.GONE);
                                    if (eventType.equals(CommonString.CURRENT)) {
                                        current = true;
                                        currentEventRecyclerView.setVisibility(View.VISIBLE);
                                        upcomingEventRecyclerView.setVisibility(View.GONE);
                                        pastEventRecyclerView.setVisibility(View.GONE);
                                    }
                                    if (eventType.equals(CommonString.UPCOMING)) {
                                        upcoming = true;
                                        currentEventRecyclerView.setVisibility(View.GONE);
                                        upcomingEventRecyclerView.setVisibility(View.VISIBLE);
                                        pastEventRecyclerView.setVisibility(View.GONE);
                                    }
                                    if (eventType.equals(CommonString.PAST)) {
                                        past = true;
                                        currentEventRecyclerView.setVisibility(View.GONE);
                                        upcomingEventRecyclerView.setVisibility(View.GONE);
                                        pastEventRecyclerView.setVisibility(View.VISIBLE);
                                    }


                                    VolleyLog.v("Response:%n %s", response.toString(4));


                                    eventResponse = gson.fromJson(response.toString(), EventResponse.class);
                                    if (eventResponse.getStatus().equals(C.NP_STATUS_SUCCESS)) {
                                        if (eventResponse.getData().size() > 0) {

                                            if (eventType.equals(CommonString.CURRENT)) {
                                                currentNoEvents = true;
                                                currentEventList.addAll(eventResponse.getData());

                                                if (currentEventList.size() != 0) {
                                                    llNoEvents.setVisibility(View.GONE);
                                                    ivSearch.setVisibility(View.VISIBLE);
                                                    page_id = eventResponse.getCurrentPage();
                                                    lastPage = eventResponse.getLastPage();
                                                    currentEventAdapterEventPager.notifyDataSetChanged();
                                                    showLoginButtton();
                                                } else {
                                                    hideUI();
                                                }
                                            }
                                            if (eventType.equals(CommonString.UPCOMING)) {
                                                upcomingNoEvents = true;
                                                upcomingEventList.addAll(eventResponse.getData());
                                                if (upcomingEventList.size() != 0) {
                                                    llNoEvents.setVisibility(View.GONE);
                                                    ivSearch.setVisibility(View.VISIBLE);
                                                    page_id = eventResponse.getCurrentPage();
                                                    lastPage = eventResponse.getLastPage();
                                                    upcomingEventAdapterEventPager.notifyDataSetChanged();
                                                    showLoginButtton();
                                                } else {
                                                    hideUI();
                                                }
                                            }
                                            if (eventType.equals(CommonString.PAST)) {
                                                pastNoEvents = true;
                                                pastEventList.addAll(eventResponse.getData());
                                                if (pastEventList.size() != 0) {
                                                    llNoEvents.setVisibility(View.GONE);
                                                    ivSearch.setVisibility(View.VISIBLE);
                                                    page_id = eventResponse.getCurrentPage();
                                                    lastPage = eventResponse.getLastPage();
                                                    pastEventAdapterEventPager.notifyDataSetChanged();
                                                    showLoginButtton();
                                                } else {
                                                    hideUI();
                                                }
                                            }


                                        } else {
                                            int count = eventResponse.getCount();
                                            SpannableString text = new SpannableString(getString(R.string.listing_no) + count);
                                            text.setSpan(new RelativeSizeSpan(1.5f), 27, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(getResources().getColor(R.color.version_2_blue));

                                            text.setSpan(foregroundSpan, 27, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                            tvFirstText.setText(text);
                                            tvSecondtext.setText(R.string.eventsecondtext);
                                            hideUI();

                                        }
                                    } else if (eventResponse.getStatus().equals(C.NP_STATUS_FAIL)) {
                                        hideUI();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }


                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        /// utils.hideDialog();
                        SpannableString text = new SpannableString(getString(R.string.listing_no,0));
                        text.setSpan(new RelativeSizeSpan(1.5f), 27, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(getResources().getColor(R.color.orange));

                        text.setSpan(foregroundSpan, 27, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        tvFirstText.setText(text);
                        tvSecondtext.setText(R.string.eventsecondtext);
                        hideUI();
                        Toast.makeText(FragmentUserEvents.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                        VolleyLog.e("Error: ", error.getMessage());
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        return ConstUtility.getHeaderPHP(json, getApplicationContext());
                    }
                };
                RetryPolicy policy = new DefaultRetryPolicy(C.API_TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                req.setRetryPolicy(policy);
                requestQueue = Volley.newRequestQueue(FragmentUserEvents.this);
                requestQueue.add(req);*/

            //////////////////////////////////////////////////////////
//            }
        } else {
            Toast.makeText(FragmentUserEvents.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
        }

    }

    private void getPastEvents(String json, JSONObject jsonObj) {


        JsonObjectRequest req = new JsonObjectRequest(BuildConfig.BASE_URL + C.API_MY_EVENT, jsonObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (C.DEBUG) {
                                Log.d("RESPONSE ------------- ", response.toString());
                            }
                            shimmer_view_container.stopShimmer();
                            shimmer_view_container.setVisibility(View.GONE);
                            past = true;
                            currentEventRecyclerView.setVisibility(View.GONE);
                            upcomingEventRecyclerView.setVisibility(View.GONE);
                            pastEventRecyclerView.setVisibility(View.VISIBLE);


                            VolleyLog.v("Response:%n %s", response.toString(4));

                            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                            eventResponse = gson.fromJson(response.toString(), EventResponse.class);
                            if (eventResponse.getStatus().equals(C.NP_INVALID_TOKEN) || eventResponse.getStatus().equals(C.NP_TOKEN_EXPIRED) || eventResponse.getStatus().equals(C.NP_TOKEN_NOT_FOUND)) {
                                SharedPreferencesUtils.INSTANCE.setUserDetails(ConstUtility.getSharedPrefereences(FragmentUserEvents.this), null);
                                Toast.makeText(FragmentUserEvents.this, eventResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                finish();
                                return;
                            }
                            if (eventResponse.getStatus().equals(C.NP_STATUS_SUCCESS)) {
                                if (eventResponse.getData() != null && eventResponse.getData().size() > 0) {

                                    pastNoEvents = true;
                                    pastEventList.addAll(eventResponse.getData());
                                    if (pastEventList.size() != 0) {
                                        llNoEvents.setVisibility(View.GONE);
                                        //ivSearch.setVisibility(View.VISIBLE);
                                        page_id = eventResponse.getCurrentPage();
                                        lastPage = eventResponse.getLastPage();
                                        pastEventAdapterEventPager.notifyDataSetChanged();
//                                        showLoginButtton();
                                    } else {
                                        hideUI();
                                    }
                                } else {
                                    int count = eventResponse.getCount();
                                    SpannableString text = new SpannableString(getString(R.string.listing_no) + count);
                                    text.setSpan(new RelativeSizeSpan(1.5f), 26, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(getResources().getColor(R.color.version_2_blue));

                                    text.setSpan(foregroundSpan, 26, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                    tvFirstText.setText(text);
                                    tvSecondtext.setText(R.string.eventsecondtext);
                                    hideUI();

                                }
                            } else if (eventResponse.getStatus().equals(C.NP_STATUS_FAIL)) {
                                hideUI();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /// utils.hideDialog();
                SpannableString text = new SpannableString(getString(R.string.listing_no));
                text.setSpan(new RelativeSizeSpan(1.5f), 26, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(getResources().getColor(R.color.orange));

                text.setSpan(foregroundSpan, 26, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                tvFirstText.setText(text);
                tvSecondtext.setText(R.string.eventsecondtext);
                hideUI();
                Toast.makeText(FragmentUserEvents.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                VolleyLog.e("Error: ", error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return ConstUtility.getHeaderPHP(json, getApplicationContext());
            }
        };
        RetryPolicy policy = new DefaultRetryPolicy(C.API_TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        req.setRetryPolicy(policy);
        requestQueue = Volley.newRequestQueue(FragmentUserEvents.this);
        requestQueue.add(req);


    }

    private void getCurrentEvents(String json, JSONObject jsonObj) {

        JsonObjectRequest req = new JsonObjectRequest(BuildConfig.BASE_URL + C.API_MY_EVENT, jsonObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (C.DEBUG) {
                                Log.d("RESPONSE ------------- ", response.toString());
                            }
                            shimmer_view_container.stopShimmer();
                            shimmer_view_container.setVisibility(View.GONE);
                            current = true;
                            currentEventRecyclerView.setVisibility(View.VISIBLE);
                            upcomingEventRecyclerView.setVisibility(View.GONE);
                            pastEventRecyclerView.setVisibility(View.GONE);


                            VolleyLog.v("Response:%n %s", response.toString(4));

                            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                            eventResponse = gson.fromJson(response.toString(), EventResponse.class);
                            if (eventResponse.getStatus().equals(C.NP_INVALID_TOKEN) || eventResponse.getStatus().equals(C.NP_TOKEN_EXPIRED) || eventResponse.getStatus().equals(C.NP_TOKEN_NOT_FOUND)) {
                                SharedPreferencesUtils.INSTANCE.setUserDetails(ConstUtility.getSharedPrefereences(FragmentUserEvents.this), null);
                                Toast.makeText(FragmentUserEvents.this, eventResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                finish();
                                return;
                            }
                            if (eventResponse.getStatus().equals(C.NP_STATUS_SUCCESS)) {
                                if (eventResponse.getData() != null && eventResponse.getData().size() > 0) {

                                    if (eventType.equals(CommonString.CURRENT)) {
                                        currentNoEvents = true;
                                        currentEventList.addAll(eventResponse.getData());

                                        if (currentEventList.size() != 0) {
                                            llNoEvents.setVisibility(View.GONE);
                                            //ivSearch.setVisibility(View.VISIBLE);
                                            page_id = eventResponse.getCurrentPage();
                                            lastPage = eventResponse.getLastPage();
                                            currentEventAdapterEventPager.notifyDataSetChanged();
//                                            showLoginButtton();
                                        } else {
                                            hideUI();
                                        }
                                    }


                                } else {
                                    int count = eventResponse.getCount();
                                    SpannableString text = new SpannableString(getString(R.string.listing_no) + count);
                                    text.setSpan(new RelativeSizeSpan(1.5f), 26, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(getResources().getColor(R.color.version_2_blue));

                                    text.setSpan(foregroundSpan, 26, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                    tvFirstText.setText(text);
                                    tvSecondtext.setText(R.string.eventsecondtext);
                                    hideUI();

                                }
                            } else if (eventResponse.getStatus().equals(C.NP_STATUS_FAIL)) {
                                hideUI();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /// utils.hideDialog();
                SpannableString text = new SpannableString(getString(R.string.listing_no));
                text.setSpan(new RelativeSizeSpan(1.5f), 26, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(getResources().getColor(R.color.orange));

                text.setSpan(foregroundSpan, 26, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                tvFirstText.setText(text);
                tvSecondtext.setText(R.string.eventsecondtext);
                hideUI();
                Toast.makeText(FragmentUserEvents.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                VolleyLog.e("Error: ", error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return ConstUtility.getHeaderPHP(json, getApplicationContext());
            }
        };
        RetryPolicy policy = new DefaultRetryPolicy(C.API_TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        req.setRetryPolicy(policy);
        requestQueue = Volley.newRequestQueue(FragmentUserEvents.this);
        requestQueue.add(req);

    }

    private void getUpcomingEvents(String json, JSONObject jsonObj) {


        JsonObjectRequest req = new JsonObjectRequest(BuildConfig.BASE_URL + C.API_MY_EVENT, jsonObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
//                            utils.hideDialog();
                            if (C.DEBUG) {
                                Log.d("RESPONSE ------------- ", response.toString());
                            }
                            shimmer_view_container.stopShimmer();
                            shimmer_view_container.setVisibility(View.GONE);

                            upcoming = true;
                            currentEventRecyclerView.setVisibility(View.GONE);
                            upcomingEventRecyclerView.setVisibility(View.VISIBLE);
                            pastEventRecyclerView.setVisibility(View.GONE);


                            VolleyLog.v("Response:%n %s", response.toString(4));

                            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                            eventResponse = gson.fromJson(response.toString(), EventResponse.class);
                            if (eventResponse.getStatus().equals(C.NP_INVALID_TOKEN) || eventResponse.getStatus().equals(C.NP_TOKEN_EXPIRED) || eventResponse.getStatus().equals(C.NP_TOKEN_NOT_FOUND)) {
                                SharedPreferencesUtils.INSTANCE.setUserDetails(ConstUtility.getSharedPrefereences(FragmentUserEvents.this), null);
                                Toast.makeText(FragmentUserEvents.this, eventResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                finish();
                                return;
                            }
                            if (eventResponse.getStatus().equals(C.NP_STATUS_SUCCESS)) {

                                if (eventResponse.getData() != null && eventResponse.getData().size() > 0) {

                                    upcomingNoEvents = true;
                                    upcomingEventList.addAll(eventResponse.getData());
                                    if (upcomingEventList.size() != 0) {
                                        llNoEvents.setVisibility(View.GONE);
                                        //ivSearch.setVisibility(View.VISIBLE);
                                        page_id = eventResponse.getCurrentPage();
                                        lastPage = eventResponse.getLastPage();
                                        upcomingEventAdapterEventPager.notifyDataSetChanged();
//                                        showLoginButtton();
                                    } else {
                                        hideUI();
                                    }


                                } else {
                                    int count = eventResponse.getCount();
                                    SpannableString text = new SpannableString(getString(R.string.listing_no) + count);
                                    text.setSpan(new RelativeSizeSpan(1.5f), 26, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(getResources().getColor(R.color.version_2_blue));

                                    text.setSpan(foregroundSpan, 26, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                    tvFirstText.setText(text);
                                    tvSecondtext.setText(R.string.eventsecondtext);
                                    hideUI();

                                }
                            } else if (eventResponse.getStatus().equals(C.NP_STATUS_FAIL)) {
                                hideUI();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /// utils.hideDialog();
                SpannableString text = new SpannableString(getString(R.string.listing_no));
                text.setSpan(new RelativeSizeSpan(1.5f), 26, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(getResources().getColor(R.color.orange));

                text.setSpan(foregroundSpan, 26, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                tvFirstText.setText(text);
                tvSecondtext.setText(R.string.eventsecondtext);
                hideUI();
                Toast.makeText(FragmentUserEvents.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                VolleyLog.e("Error: ", error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return ConstUtility.getHeaderPHP(json, getApplicationContext());
            }
        };
        RetryPolicy policy = new DefaultRetryPolicy(C.API_TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        req.setRetryPolicy(policy);
        requestQueue = Volley.newRequestQueue(FragmentUserEvents.this);
        requestQueue.add(req);

    }

//    private void showLoginButtton() {
//        if (SharedPreferencesUtils.INSTANCE.getUserDetails(sharedPreferences) == null) {
//            Animation slideFromRight = AnimationUtils.loadAnimation(FragmentUserEvents.this, R.anim.slide_from_right);
//            llLogin.setVisibility(View.VISIBLE);
//            llcreateevent.setVisibility(View.GONE);
//            llLogin.startAnimation(slideFromRight);
//        } else {
//            Animation slideFromRight = AnimationUtils.loadAnimation(FragmentUserEvents.this, R.anim.slide_from_right);
//            llLogin.setVisibility(View.GONE);
//            llcreateevent.setVisibility(View.VISIBLE);
//            llcreateevent.startAnimation(slideFromRight);
//        }
//    }

    private void hideUI() {
//        showLoginButtton();
        ivSearch.setVisibility(View.INVISIBLE);
        shimmer_view_container.setVisibility(View.GONE);
        llNoEvents.setVisibility(View.VISIBLE);

        if (SharedPreferencesUtils.INSTANCE.getUserDetails(sharedPreferences) == null) {
//            llLogin.setVisibility(View.VISIBLE);
        } else
            llevents.setVisibility(View.GONE);
    }

    @OnClick({R.id.ivBack, R.id.rlSubmit, R.id.llcreateevent})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                ConstUtility.hideKeyBoard(FragmentUserEvents.this);
                onBackPressed();
                break;
            case R.id.rlSubmit:
                doNotify();
                break;

            case R.id.llcreateevent:
//                startActivity(new Intent(this, CreateEventActivity.class));
                FragmentCreateEvents fragment = new FragmentCreateEvents();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.rlEvent, fragment, C.TAG_FRAGMENT_CREATE_EVENT);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_CREATE_EVENT);
                fragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
                break;

        }
    }


    private void doNotify() {
        UserDetail userDetail = SharedPreferencesUtils.INSTANCE.getUserDetails(sharedPreferences);
        NotifyRequest notifyRequest = new NotifyRequest();
        notifyRequest.setName(ConstUtility.encryptLC(userDetail.getName()));
        notifyRequest.setPhone(ConstUtility.encryptLC(userDetail.get_phone()));
        notifyRequest.setEmail(ConstUtility.encryptLC(userDetail.get_email()));
        notifyRequest.setCity(Util.INSTANCE.getCitySelected().getCity());
        notifyRequest.setState(Util.INSTANCE.getCitySelected().getState_name());
        notifyRequest.setCountry(Util.INSTANCE.getCitySelected().getCountry_name());
        notifyRequest.setType("center");

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String json = gson.toJson(notifyRequest);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog = ConstUtility.getProgressDialog(FragmentUserEvents.this, getString(R.string.sending_request_));
        progressDialog.show();
        if (ConstUtility.isNetworkConnectivity(FragmentUserEvents.this)) {
            JsonObjectRequest req = new JsonObjectRequest(BuildConfig.BASE_URL + C.API_NOTIFY, jsonObj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                progressDialog.dismiss();
                                VolleyLog.v("Response:%n %s", response.toString(4));
                                NotifyResponse notifyResponse = new NotifyResponse();
                                notifyResponse = gson.fromJson(response.toString(), NotifyResponse.class);
                                if (notifyResponse.getStatus().equals(C.NP_STATUS_SUCCESS)) {
                                    Toast.makeText(FragmentUserEvents.this, R.string.requested_for_this_location,
                                            Toast.LENGTH_LONG).show();
                                    onBackPressed();
                                } else if (notifyResponse.getStatus().equals(C.NP_STATUS_FAIL)) {
                                    Toast.makeText(FragmentUserEvents.this, notifyResponse.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(FragmentUserEvents.this, R.string.already_requested_for_this_location,
                                            Toast.LENGTH_LONG).show();
                                }

                            } catch (Exception e) {
                                progressDialog.dismiss();

                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    VolleyLog.e("Error: ", error.getMessage());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return ConstUtility.getMD5EncryptedString(json);
                }
            };
            RetryPolicy policy = new DefaultRetryPolicy(C.API_TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            req.setRetryPolicy(policy);
            requestQueue = Volley.newRequestQueue(FragmentUserEvents.this);
            requestQueue.add(req);
        } else {
            Toast.makeText(FragmentUserEvents.this, getString(R.string.no_internet_connection),
                    Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (requestQueue != null) {
            requestQueue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return true;
                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    private boolean isAllValid2() {
        if (rating == 0) {
            Toast.makeText(FragmentUserEvents.this, R.string.select_rating, Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }


    public void doRating2Update(int rating, Event event, final Dialog dialog) {
        if (ConstUtility.isNetworkConnectivity(FragmentUserEvents.this)) {
            UserDetail userDetail = SharedPreferencesUtils.INSTANCE.getUserDetails(sharedPreferences);
            RatingRequest ratingRequest = new RatingRequest();
            ratingRequest.setEmail(userDetail.get_email());
            ratingRequest.setEventId(Integer.valueOf(event.getId()));
            ratingRequest.setRating(rating);
            ratingRequest.setName(userDetail.getName());
            ratingRequest.setType("event");

            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            String json = gson.toJson(ratingRequest);
            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest req = new JsonObjectRequest(BuildConfig.BASE_URL + C.API_ADD_RATING, jsonObj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                VolleyLog.v("Response:%n %s", response.toString(4));

                                ratingResponse = gson.fromJson(response.toString(), RatingResponse.class);
                                if (ratingResponse.getStatus().equals(C.NP_STATUS_SUCCESS)) {
                                    Toast.makeText(FragmentUserEvents.this, R.string.thanks_for_rating, Toast.LENGTH_SHORT).show();
//                                    llLogin.setVisibility(View.VISIBLE);
                                    //rlRatingSheet.setVisibility(View.GONE);
                                    dialog.dismiss();
                                    /*adapterEventPager.getItem(pos).setRating(Float.parseFloat(ratingResponse.getRatingData().getRating()));
                                    adapterEventPager.notifyDataSetChanged();*/
                                    if (eventType.equals(CommonString.CURRENT)) {
//                                        currentEventAdapterEventPager.getItem(pos).setRating(Float.parseFloat(ratingResponse.getRatingData().getRating()));
                                        event.setRating(Float.parseFloat(ratingResponse.getRatingData().getRating()));
                                        currentEventAdapterEventPager.notifyDataSetChanged();
                                    }
                                    if (eventType.equals(CommonString.UPCOMING)) {
//                                        upcomingEventAdapterEventPager.getItem(pos).setRating(Float.parseFloat(ratingResponse.getRatingData().getRating()));
                                        event.setRating(Float.parseFloat(ratingResponse.getRatingData().getRating()));
                                        upcomingEventAdapterEventPager.notifyDataSetChanged();
                                    }
                                    if (eventType.equals(CommonString.PAST)) {
//                                        pastEventAdapterEventPager.getItem(pos).setRating(Float.parseFloat(ratingResponse.getRatingData().getRating()));
                                        event.setRating(Float.parseFloat(ratingResponse.getRatingData().getRating()));
                                        pastEventAdapterEventPager.notifyDataSetChanged();
                                    }
                                    clearData(dialog);
                                } else {
                                    Toast.makeText(FragmentUserEvents.this, ratingResponse.getMessage(), Toast.LENGTH_SHORT).show();
//                                    llLogin.setVisibility(View.VISIBLE);
                                    // rlRatingSheet.setVisibility(View.GONE);
                                    dialog.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                    dialog.dismiss();
                    //showErrorPopup();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return ConstUtility.getMD5EncryptedString(json);
                }
            };
            RetryPolicy policy = new DefaultRetryPolicy(C.API_TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            req.setRetryPolicy(policy);
            RequestQueue requestQueue = Volley.newRequestQueue(FragmentUserEvents.this);
            requestQueue.add(req);
        } else {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }

    }


    private void clearData(Dialog dialog) {

//        EditText etPhone = dialog.findViewById(R.id.etPhone);
//        EditText etEmail = dialog.findViewById(R.id.etEmail);
//        EditText etFullName = dialog.findViewById(R.id.etFullName);
        ImageView ivStar1 = dialog.findViewById(R.id.ivStar1);
        ImageView ivStar2 = dialog.findViewById(R.id.ivStar2);
        ImageView ivStar3 = dialog.findViewById(R.id.ivStar3);
        ImageView ivStar4 = dialog.findViewById(R.id.ivStar4);
        ImageView ivStar5 = dialog.findViewById(R.id.ivStar5);

//        etFullName.setText("");
//        etEmail.setText("");
//        etPhone.setText("");
        ivStar1.setImageResource(R.drawable.feedback_deactive);
        ivStar2.setImageResource(R.drawable.feedback_deactive);
        ivStar3.setImageResource(R.drawable.feedback_deactive);
        ivStar4.setImageResource(R.drawable.feedback_deactive);
        ivStar5.setImageResource(R.drawable.feedback_deactive);
        rating = 0;
    }


    public void ratingDialogUpdate(Event event) {
        final Dialog dialog = new Dialog(FragmentUserEvents.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.rating_dialog_2);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        lp.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setAttributes(lp);

        ImageView ivStar1 = dialog.findViewById(R.id.ivStar1);
        ImageView ivStar2 = dialog.findViewById(R.id.ivStar2);
        ImageView ivStar3 = dialog.findViewById(R.id.ivStar3);
        ImageView ivStar4 = dialog.findViewById(R.id.ivStar4);
        ImageView ivStar5 = dialog.findViewById(R.id.ivStar5);
        ImageView ivCross = dialog.findViewById(R.id.ivCross);
        ImageView cancel_action = dialog.findViewById(R.id.cancel_action);
        View rlSubmit = dialog.findViewById(R.id.rlSubmit);

//        EditText etPhone = dialog.findViewById(R.id.etPhone);
//        EditText etEmail = dialog.findViewById(R.id.etEmail);
//        EditText etFullName = dialog.findViewById(R.id.etFullName);

        //dialog.getWindow().getAttributes().windowAnimations = R.style.DialogSlideAnim;
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ivStar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivStar1.setImageResource(R.drawable.feedback_active);
                ivStar2.setImageResource(R.drawable.feedback_deactive);
                ivStar3.setImageResource(R.drawable.feedback_deactive);
                ivStar4.setImageResource(R.drawable.feedback_deactive);
                ivStar5.setImageResource(R.drawable.feedback_deactive);
                rating = 1;
            }
        });
        ivStar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivStar1.setImageResource(R.drawable.feedback_active);
                ivStar2.setImageResource(R.drawable.feedback_active);
                ivStar3.setImageResource(R.drawable.feedback_deactive);
                ivStar4.setImageResource(R.drawable.feedback_deactive);
                ivStar5.setImageResource(R.drawable.feedback_deactive);
                rating = 2;

            }
        });
        ivStar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivStar1.setImageResource(R.drawable.feedback_active);
                ivStar2.setImageResource(R.drawable.feedback_active);
                ivStar3.setImageResource(R.drawable.feedback_active);
                ivStar4.setImageResource(R.drawable.feedback_deactive);
                ivStar5.setImageResource(R.drawable.feedback_deactive);
                rating = 3;

            }
        });
        ivStar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ivStar1.setImageResource(R.drawable.feedback_active);
                ivStar2.setImageResource(R.drawable.feedback_active);
                ivStar3.setImageResource(R.drawable.feedback_active);
                ivStar4.setImageResource(R.drawable.feedback_active);
                ivStar5.setImageResource(R.drawable.feedback_deactive);
                rating = 4;
            }
        });
        ivStar5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivStar1.setImageResource(R.drawable.feedback_active);
                ivStar2.setImageResource(R.drawable.feedback_active);
                ivStar3.setImageResource(R.drawable.feedback_active);
                ivStar4.setImageResource(R.drawable.feedback_active);
                ivStar5.setImageResource(R.drawable.feedback_active);
                rating = 5;

            }
        });

        cancel_action.setOnClickListener(v -> {
                    hideKeyboard(dialog);
                    dialog.dismiss();
//                    llLogin.setVisibility(View.VISIBLE);
                }
        );
        rlSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                doRating2Update(rating, event, dialog);
            }
        });

        dialog.show();

    }

    private void hideKeyboard(Dialog dialog) {
        if (dialog.getCurrentFocus() != null) {
            InputMethodManager inputManager = (InputMethodManager) Objects.requireNonNull(FragmentUserEvents.this).getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(dialog.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    @Override
    public void onClick(int position, @org.jetbrains.annotations.Nullable Event event) {
        if (event != null) {
            switch (position) {
                case 0:
                case 1:
                    openRoundedBottomSheet(event);
                    break;
                case 2:
                    openMyEventPastBottomSheet(event);
//                    openRoundedBottomSheet(event);
//                    if (SharedPreferencesUtils.INSTANCE.getUserDetails(sharedPreferences) != null)
//                        ratingDialogUpdate(event);
//                    else {
//                        startActivityForResult(new Intent(this, LoginActivity.class), 100);
//                    }
            }

        }
    }

    private void openMyEventPastBottomSheet(Event event) {
        FragmentCreateEvents fragmentCreateEvents = (FragmentCreateEvents) getSupportFragmentManager().findFragmentByTag(C.TAG_FRAGMENT_CREATE_EVENT);

        if (fragmentCreateEvents != null && fragmentCreateEvents.isVisible()) {
        } else {
            if (myEventPastDialogFragment.isAdded() || myEventPastDialogFragment.isVisible()) {
                myEventPastDialogFragment.dismiss();
            }
            myEventPastDialogFragment.show(getSupportFragmentManager(), myEventPastDialogFragment.getTag());
            new Handler(Looper.getMainLooper()).postDelayed(() -> myEventPastDialogFragment.setData(event), 300);
        }
    }

    private void openRoundedBottomSheet(Event event) {

        FragmentCreateEvents fragmentCreateEvents = (FragmentCreateEvents) getSupportFragmentManager().findFragmentByTag(C.TAG_FRAGMENT_CREATE_EVENT);
        FragmentEditEvents fragmentEditEvents = (FragmentEditEvents) getSupportFragmentManager().findFragmentByTag(C.TAG_FRAGMENT_EDIT_EVENT);
        if ((fragmentCreateEvents != null && fragmentCreateEvents.isVisible()) || (fragmentEditEvents != null && fragmentEditEvents.isVisible())) {

        } else {
            if (myRoundedBottomSheet.isAdded() || myRoundedBottomSheet.isVisible()) {
                myRoundedBottomSheet.dismiss();
            }

            myRoundedBottomSheet.show(getSupportFragmentManager(), myRoundedBottomSheet.getTag());


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    myRoundedBottomSheet.setData(event, eventType);
                }
            }, 300);
        }
    }

    public void openEditEvent(Event event) {
        if (myRoundedBottomSheet.isAdded() || myRoundedBottomSheet.isVisible()) {
            myRoundedBottomSheet.dismiss();
        }

        FragmentEditEvents fragment = new FragmentEditEvents();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putSerializable(C.EVENTDATA, event);
        fragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.rlEvent, fragment, C.TAG_FRAGMENT_EDIT_EVENT);
        fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_EDIT_EVENT);
        fragmentTransaction.commit();
        getSupportFragmentManager().executePendingTransactions();

    }

    @Override
    public void ratingDialog(@org.jetbrains.annotations.Nullable String pos, int position) {

    }
    @Override
    public void ratingDialog(@org.jetbrains.annotations.Nullable Event event) {

    }


    @Override
    protected void onResume() {
        super.onResume();
        localizationDelegate.onResume(this);
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
