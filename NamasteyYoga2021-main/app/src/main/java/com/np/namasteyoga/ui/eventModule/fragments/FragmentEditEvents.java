package com.np.namasteyoga.ui.eventModule.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.np.namasteyoga.BuildConfig;
import com.np.namasteyoga.R;
import com.np.namasteyoga.datasource.pojo.City;
import com.np.namasteyoga.datasource.pojo.CreateEventRequest;
import com.np.namasteyoga.datasource.pojo.CreateEventResponse;
import com.np.namasteyoga.datasource.pojo.Event;
import com.np.namasteyoga.datasource.response.UserDetail;
import com.np.namasteyoga.ui.login.LoginActivity;
import com.np.namasteyoga.ui.map.MapActivity;
import com.np.namasteyoga.ui.searchCity.ActivitySearchCity;
import com.np.namasteyoga.utils.C;
import com.np.namasteyoga.utils.ConstUtility;
import com.np.namasteyoga.utils.IntentUtil;
import com.np.namasteyoga.utils.Logger;
import com.np.namasteyoga.utils.PatternUtil;
import com.np.namasteyoga.utils.SharedPreferencesUtils;
import com.np.namasteyoga.utils.TextUtils;
import com.np.namasteyoga.utils.UIUtils;
import com.np.namasteyoga.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static android.text.InputType.TYPE_CLASS_NUMBER;
import static com.np.namasteyoga.modules.SharedPreference.DataPrefs;

public class FragmentEditEvents extends Fragment {

    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.llHeader)
    RelativeLayout llHeader;
    @BindView(R.id.etEventName)
    AutoCompleteTextView etEventName;
    @BindView(R.id.etAddress)
    TextView etAddress;
    @BindView(R.id.etAddressManual)
    EditText etAddressManual;
    @BindView(R.id.tvContactName)
    AutoCompleteTextView tvContactName;
    @BindView(R.id.tvPhoneNumber)
    AutoCompleteTextView tvPhoneNumber;
    @BindView(R.id.tvEmailAddress)
    AutoCompleteTextView tvEmailAddress;
    @BindView(R.id.tvStartDate)
    EditText tvStartDate;
    @BindView(R.id.tvEventEndDate)
    EditText tvEventEndDate;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    @BindView(R.id.tvHeader)
    TextView tvHeader;
    @BindView(R.id.tvMeetingLink)
    AutoCompleteTextView tvMeetingLink;
    @BindView(R.id.isHighlight)
    CheckBox isHighlight;

    @BindView(R.id.etEventDescription)
    EditText etEventDescription;
    @BindView(R.id.etJoiningInstruction)
    EditText etJoiningInstructions;


    @BindView(R.id.rlSubmitEvent)
    RelativeLayout rlSubmitEvent;
    @BindView(R.id.etSelectCity)
    AutoCompleteTextView etSelectCity;
    @BindView(R.id.etSeatingCapacity)
    EditText etSittingCapacity;

    ConstUtility constUtility = new ConstUtility();
    CreateEventResponse createEventResponse;
    private final static int PLACE_PICKER_REQUEST = 111;
    double latitude;
    double longitude;
    String city;
    String countryName;
    String state_name;
    String zip;
    long startTimeStamp;
    long endTimeStamp;
    int selectedDate = 0;
    Unbinder unbinder;
    private long startTime;
    private long endTime;
    private final static int CITY_PICKER_REQUEST = 222;
    City citySelected;

    Calendar myCalendar = Calendar.getInstance(Locale.getDefault());
    public static int nearestCityVar = -1;

    String startDate;
    String endDate;

    Event event;
    private boolean isCityUpdated = false;

    public FragmentEditEvents() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_events, container, false);

        event = (Event) getArguments().getSerializable(C.EVENTDATA);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if (C.DEBUG) {
//            etEventName.setText("test_6777");
//            tvEmailAddress.setText("abhinav.tiwari@npgobal.com");
//            tvPhoneNumber.setText("9292992929");
//            tvContactName.setText("test_shubh");
        }

        etSittingCapacity.setInputType(TYPE_CLASS_NUMBER);
//        etSittingCapacity.setFilters(arrayOf(InputFilter.LengthFilter(SittingCapacityLength)));
        etSittingCapacity.setHint(R.string.sitting_capacity);

//        etSittingCapacity.manageEditText(
//                TYPE_CLASS_NUMBER,
//                CommonInt.SittingCapacityLength,
//                getString(R.string.sitting_capacity)
//        );

        etSelectCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    selectCityPopup();
//                    Intent intent = new Intent(getActivity(), ActivitySearchCity.class);
//                    startActivityForResult(intent, CITY_PICKER_REQUEST);
                }
            }
        });
        etAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    openPlacePicker();
                }
            }
        });
        tvStartDate.setCursorVisible(false); //Set the cursor in the input box to be invisible
        tvStartDate.setFocusable(false); //no focus
        tvStartDate.setFocusableInTouchMode(false);
        tvStartDate.setInputType(InputType.TYPE_NULL);

        tvEventEndDate.setCursorVisible(false); //Set the cursor in the input box to be invisible
        tvEventEndDate.setFocusable(false); //no focus
        tvEventEndDate.setFocusableInTouchMode(false);
        tvEventEndDate.setInputType(InputType.TYPE_NULL);
        setUi();
    }

    private void setUi() {
        Logger.INSTANCE.Debug(event.toString(), "TTTTTTTTTTTTTTTTTTTTTT");
        citySelected = new City();
        citySelected.setState_name(event.getStateName());
        citySelected.setCountry_name(event.getCountryName());
        citySelected.setCity(event.getCityName());
        latitude = event.getLat();
        longitude = event.getLng();
        tvHeader.setText(getResources().getString(R.string.edit_event));
        etEventName.setText(event.getEventName());
        startDate = event.getStartTime();
        endDate = event.getEndTime();
        tvStartDate.setText(event.getStartTime());
        tvEventEndDate.setText(event.getEndTime());
        tvContactName.setText(ConstUtility.decrypt(event.getContactPerson()));
        nearestCityVar= event.getNearest();
//        tvContactName.setText(event.getContactPerson());
        tvPhoneNumber.setText(ConstUtility.decrypt(event.getContactNo()));
        tvEmailAddress.setText(ConstUtility.decrypt(event.getEmail()));
        etSittingCapacity.setText(event.getSittingCapacity());
        etJoiningInstructions.setText(event.getJoining_instruction());
        etEventDescription.setText(event.getShort_description());
        etSelectCity.setText(event.getCityName());
        etAddressManual.setText(event.getAddress());
        tvMeetingLink.setText(event.getMeeting_link());
//        latitude = latLng.latitude;
//        longitude = latLng.longitude;
        String lat = "" + event.getLat() + "," + event.getLng();
        etAddress.setText(lat);
        if (event.getMode().equals("FREE")) {
            checkbox.setChecked(true);
        } else
            checkbox.setChecked(false);


        System.out.println("CITYSELECTED " + citySelected.getLat() + "," + citySelected.getLng());
        System.out.println("EVENT_LOC " + event.getLat() + "," + event.getLng());


    }


    @OnClick({R.id.ivBack, R.id.tvStartDate, R.id.tvEventEndDate, R.id.checkbox, R.id.rlSubmitEvent, R.id.etAddress, R.id.etSelectCity})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                ConstUtility.hideKeyBoard(getActivity());
                getActivity().onBackPressed();
                break;
            case R.id.etAddress:
                openPlacePicker();
                break;
            case R.id.tvStartDate:
                selectedDate = 1;
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                setStartDateTimeField(mYear, mMonth, mDay);
                break;
            case R.id.tvEventEndDate:
                selectedDate = 2;
                Calendar c1 = Calendar.getInstance();
                int mYear1 = c1.get(Calendar.YEAR);
                int mMonth1 = c1.get(Calendar.MONTH);
                int mDay1 = c1.get(Calendar.DAY_OF_MONTH);
                setStartDateTimeField(mYear1, mMonth1, mDay1);
                break;
            case R.id.checkbox:
                break;
            case R.id.rlSubmitEvent:
                System.out.println(",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,");
                submitEvent();
                break;
            case R.id.etSelectCity:

                selectCityPopup();


                break;
        }
    }

    private void selectCityPopup() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.popup_select_event_city);
        RelativeLayout currentCity = (RelativeLayout) dialog.findViewById(R.id.btnCurrentCity);
        RelativeLayout nearestCity = (RelativeLayout) dialog.findViewById(R.id.btnNearestCity);
        ImageView ivCross = (ImageView) dialog.findViewById(R.id.ivCross);

        currentCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nearestCityVar = 0;
                dialog.dismiss();
                Intent intent = new Intent(getActivity(), ActivitySearchCity.class);
                startActivityForResult(intent, CITY_PICKER_REQUEST);
            }
        });
        nearestCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nearestCityVar = 1;
                dialog.dismiss();
                Intent intent = new Intent(getActivity(), ActivitySearchCity.class);
                startActivityForResult(intent, CITY_PICKER_REQUEST);
            }
        });


        ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void submitEvent() {

        if (ConstUtility.isNetworkConnectivity(getActivity())) {
//            startTime = (Utils.getTimestamp(tvStartDate.getText().toString()));
//            endTime = (Utils.getTimestamp(tvEventEndDate.getText().toString()));
            if (isAllValid()) {
                constUtility.showDialog(true, getString(R.string.please_wait_), getActivity());

                CreateEventRequest createEventRequest = new CreateEventRequest();
                String name = UIUtils.INSTANCE.getCapitalized(etEventName.getText().toString().trim());
//                createEventRequest.setEventName(etEventName.getText().toString().trim());
                createEventRequest.setEventName(name);
                createEventRequest.setEvent_id(event.getId());
//                createEventRequest.setUserId(Integer.valueOf(event.getUserId()));

//                String address = etAddress.getText().toString();
                String address = etAddressManual.getText().toString();
                address = address.replace("/", "__");
                createEventRequest.setAddress(address);

//                createEventRequest.setAddress(etAddress.getText().toString());
                createEventRequest.setContactNo(ConstUtility.encryptLC(tvPhoneNumber.getText().toString()));
                createEventRequest.setEmail(ConstUtility.encryptLC(tvEmailAddress.getText().toString()));
                String nameC = UIUtils.INSTANCE.getCapitalized(tvContactName.getText().toString().trim());
//                createEventRequest.setContactPerson(ConstUtility.encryptLC(tvContactName.getText().toString()));
                createEventRequest.setContactPerson(ConstUtility.encryptLC(nameC));

                createEventRequest.setSittingCapacity(Integer.valueOf(etSittingCapacity.getText().toString()));
                createEventRequest.setJoining_instruction(etJoiningInstructions.getText().toString());
                createEventRequest.setShort_description(etEventDescription.getText().toString());
                createEventRequest.setMeeting_link(tvMeetingLink.getText().toString());
//                String s = System.currentTimeMillis() + "";
                createEventRequest.setStartTime(startDate);
                createEventRequest.setEndTime(endDate);

                createEventRequest.setState(citySelected.getState_name());
                createEventRequest.setCity(citySelected.getCity());
                createEventRequest.setCountry(citySelected.getCountry_name());

                double lat = 0;
                double lng = 0;
                if (nearestCityVar == -1) {
                    createEventRequest.setNearest(event.getNearest());
                } else {
                    createEventRequest.setNearest(nearestCityVar);
                }
//                Location loc1 = new Location("");
//                Location loc2 = new Location("");
//                loc1.setLatitude(latitude);
//                loc1.setLongitude(longitude);


                SharedPreferences sharedPreferences = getContext().getSharedPreferences(DataPrefs, Context.MODE_PRIVATE);
//                UserDetail userDetail = SharedPreferencesUtils.INSTANCE.getUserDetails(sharedPreferences);

                if (isLocationUpdate) {

                    if (citySelected.getLat() != null) {
                        lat = Double.parseDouble(citySelected.getLat());
                    }
                    if (citySelected.getLng() != null) {
                        lng = Double.parseDouble(citySelected.getLng());
                    }
                    LatLng latLngDouble = new LatLng(lat, lng);
                    LatLng latLngDouble2 = new LatLng(latitude, longitude);
                    float distance = Util.INSTANCE.distance(latLngDouble2, latLngDouble);
//                createEventRequest.setNearest_distance(String.valueOf(ConstUtility.getDistance(loc1, loc2)));

                    createEventRequest.setNearest_distance(Util.INSTANCE.formatKM(distance));
                } else {
                    createEventRequest.setNearest_distance(event.getNearest_distance());
                }


                if (isCityUpdated) {


                    if (citySelected.getLat() != null) {
                        lat = Double.parseDouble(citySelected.getLat());
                    }
                    if (citySelected.getLng() != null) {
                        lng = Double.parseDouble(citySelected.getLng());
                    }
                    LatLng latLngDouble = new LatLng(lat, lng);
                    LatLng latLngDouble2 = new LatLng(latitude, longitude);
                    float distance = Util.INSTANCE.distance(latLngDouble2, latLngDouble);
//                createEventRequest.setNearest_distance(String.valueOf(ConstUtility.getDistance(loc1, loc2)));

                    createEventRequest.setNearest_distance(Util.INSTANCE.formatKM(distance));
                } else {

                    createEventRequest.setNearest_distance(event.getNearest_distance());
                }


//                createEventRequest.setNearest_distance(String.valueOf(ConstUtility.getDistance(loc1, loc2)));

//                createEventRequest.setStartTime(String.valueOf(Utils.getTimestamp(tvStartDate.getText().toString())));
//                createEventRequest.setEndTime(String.valueOf(Utils.getTimestamp(tvEventEndDate.getText().toString())));
                if (checkbox.isChecked()) {
                    createEventRequest.setMode("FREE");
                } else {
                    createEventRequest.setMode("PAID");
                }

                createEventRequest.setIsHighlight(Integer.valueOf(event.getIsHighlight()));

                createEventRequest.setZip("3233");
                createEventRequest.setLat(latitude);
                createEventRequest.setLng(longitude);

                // createEventRequest.setUserId(userDetail.getId());
                //createEventRequest.setSittingCapacity();
////////////////////////////////////////////////////////////////////////////////
                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                String json = gson.toJson(createEventRequest);
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                System.out.println("update_event_API: " + C.API_UPDATE_EVENT);
                System.out.println("Update_event_request: " + jsonObj.toString());
                JsonObjectRequest req = new JsonObjectRequest(BuildConfig.BASE_URL + C.API_UPDATE_EVENT, jsonObj,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    constUtility.hideDialog();
                                    if (C.DEBUG) {
                                        System.out.println("update_event_response: " + response.toString());
                                        VolleyLog.v("update_event_response:%n %s", response.toString(4));
                                    }
                                    createEventResponse = gson.fromJson(response.toString(), CreateEventResponse.class);

                                    if (createEventResponse.getStatus().equals(C.NP_STATUS_SUCCESS)) {
                                        Toast.makeText(getActivity(), R.string.event_updated_successfully_,
                                                Toast.LENGTH_LONG).show();
                                        getActivity().finish();
//                                        getActivity().onBackPressed();
                                    } else if (createEventResponse.getStatus().equals(C.NP_INVALID_TOKEN) || createEventResponse.getStatus().equals(C.NP_TOKEN_EXPIRED) || createEventResponse.getStatus().equals(C.NP_TOKEN_NOT_FOUND)) {
                                        SharedPreferencesUtils.INSTANCE.setUserDetails(sharedPreferences, null);
                                        Toast.makeText(getActivity(), createEventResponse.getMessage(), Toast.LENGTH_LONG).show();
                                        getActivity().finish();
                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        getActivity().startActivity(intent);

                                    } else {
                                        Toast.makeText(getActivity(), createEventResponse.getMessage(),
                                                Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                        if (C.DEBUG) {
                            VolleyLog.e("Error: ", error.getMessage());
                            System.out.println("TTTTTTTTTTTTTTTTTTTTTT " + error.getMessage());
                        }
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        if (C.DEBUG)
                            System.out.println("TTTTTTTTTTTTTTTTTTTTTT " + json);
                        Map<String, String> stringStringMap = ConstUtility.getHeaderPHP(json, getContext());
                        return stringStringMap;
                    }
                };
                RetryPolicy policy = new DefaultRetryPolicy(C.API_TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                req.setRetryPolicy(policy);
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(req);

//////////////////////////////////////////////////////////////////////////////////
            }
        } else {
            Toast.makeText(getActivity(), R.string.no_internet_connection,
                    Toast.LENGTH_LONG).show();
        }
    }

    private boolean isAllValid() {
        if (etEventName.getText().toString().length() == 0) {
            etEventName.setError(getActivity().getResources().getString(R.string.cant_be_blank));
            etEventName.requestFocus();
            return false;
        }

        if (tvStartDate.getText().toString().length() == 0) {
            Toast.makeText(getActivity(), R.string.please_enter_start_date,
                    Toast.LENGTH_LONG).show();
            //tvStartDate.setError(getActivity().getResources().getString(R.string.cant_be_blank));
            tvStartDate.requestFocus();
            return false;
        }
        if (!ConstUtility.checkValidReturnDate(new Date(), tvStartDate.getText().toString())) {
            Toast.makeText(getActivity(), R.string.please_check_start_date, Toast.LENGTH_LONG).show();
            tvStartDate.requestFocus();
            return false;
        }
        if (tvEventEndDate.getText().toString().length() == 0) {
            //tvEventEndDate.setError(getActivity().getResources().getString(R.string.cant_be_blank));
            Toast.makeText(getActivity(), R.string.please_enter_end_date,
                    Toast.LENGTH_LONG).show();
            tvEventEndDate.requestFocus();
            return false;
        }

        if (!ConstUtility.checkValidReturnDate(tvStartDate.getText().toString(), tvEventEndDate.getText().toString())) {
            Toast.makeText(getActivity(), R.string.please_check_dates,
                    Toast.LENGTH_LONG).show();
            tvEventEndDate.requestFocus();
            return false;
        }

        String cap = etSittingCapacity.getText().toString();
        try {
            if (cap.length() < 2 || cap.length() > 5 || Integer.parseInt(cap) > 10000) {
                etSittingCapacity.setError(getString(R.string.sitting_capacity_validation_msg));
                etSittingCapacity.requestFocus();
                return false;
            }
        }catch (Exception e){
            etSittingCapacity.setError(getString(R.string.sitting_capacity_validation_msg));
            etSittingCapacity.requestFocus();
            return false;
        }


        if (etAddress.getText().toString().length() == 0) {
            etAddress.setError(getActivity().getResources().getString(R.string.location_required));
            etAddress.requestFocus();
            return false;
        }
        if (etAddressManual.getText().toString().length() == 0) {
            etAddressManual.setError(getActivity().getResources().getString(R.string.address_required));
            etAddressManual.requestFocus();
            return false;
        }

        if (etSittingCapacity.getText().toString().length() == 0) {
            etSittingCapacity.setError(getActivity().getResources().getString(R.string.sitting_capacity_validation_msg));
            etSittingCapacity.requestFocus();
            return false;
        }

//        if (etSittingCapacity.getText().toString().length() < 2 || etSittingCapacity.getText().toString().length() > 5) {
//            etSittingCapacity.setError(getActivity().getResources().getString(R.string.sitting_capacity_validation_msg));
//            etSittingCapacity.requestFocus();
//            return false;
//        }



        if (citySelected == null) {
            etSelectCity.setError(getActivity().getResources().getString(R.string.select_city));
            etSelectCity.requestFocus();
            return false;
        }
        if (tvContactName.getText().toString().length() == 0) {
            tvContactName.setError(getActivity().getResources().getString(R.string.cant_be_blank));
            tvContactName.requestFocus();
            return false;
        }
        if (PatternUtil.INSTANCE.isValidName(tvContactName.getText().toString())){
            TextUtils.INSTANCE.errorValidText(tvContactName, getString(R.string.name));
            return false;
        }
        if (tvPhoneNumber.getText().toString().length() < 5 || tvPhoneNumber.getText().toString().length() > 15) {
            tvPhoneNumber.setError(getActivity().getResources().getString(R.string.please_enter_valid_mobile_number));
            tvPhoneNumber.requestFocus();
            return false;
        } else if (tvPhoneNumber.getText().toString().startsWith("0")) {
            tvPhoneNumber.setError(getActivity().getResources().getString(R.string.number_strts_with_zero));
            tvPhoneNumber.requestFocus();
            return false;
        }

        if (tvEmailAddress.getText().toString().length() == 0) {
            tvEmailAddress.setError(getActivity().getResources().getString(R.string.email_required));
            tvEmailAddress.requestFocus();
            return false;
        }
        if (!constUtility.isValidMail(tvEmailAddress.getText().toString())) {
            tvEmailAddress.setError(getActivity().getResources().getString(R.string.email_is_not_valid));
            tvEmailAddress.requestFocus();
            return false;
        }

        String potentialUrl = tvMeetingLink.getText().toString().trim();
        if (potentialUrl.length() > 0) {
            boolean isValid = Patterns.WEB_URL.matcher(potentialUrl).matches();
            if (!isValid) {
                tvMeetingLink.setError(getString(R.string.meeting_msg));
                tvMeetingLink.requestFocus();
                return false;
            }
        }


        return true;
    }

    private final static int LatLongKey = 1115;
    private LatLng latLng = null;

    private void openPlacePicker() {
        Intent intent = new Intent(getContext(), MapActivity.class);
//        intent.putExtra(IntentUtil.key, latLng);
        startActivityForResult(intent, LatLongKey);
    }


    boolean isLocationUpdate = false;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PLACE_PICKER_REQUEST:
//                    Place place = PlacePicker.getPlace(getActivity(), data);
//                    String placeName = String.format("Place: %s", place.getName());
//                    latitude = place.getLatLng().latitude;
//                    longitude = place.getLatLng().longitude;
//
//                    getAddress(latitude, longitude);
//                    LatLng coordinate = new LatLng(latitude, longitude);
//                    String add = place.getAddress().toString();
//                    String addr = place.getName().toString() + add;
//                    etAddress.setText(addr);
//                    Log.d(TAG,placeName +":::" +coordinate);
                    break;
                case CITY_PICKER_REQUEST:
                    citySelected = (City) data.getSerializableExtra(C.CITY);
                    etSelectCity.setText(citySelected.getCity());
//                    latitude = Double.parseDouble(citySelected.getLat());
//                    longitude = Double.parseDouble(citySelected.getLng());
                    isCityUpdated = true;
                    isLocationUpdate = true;
                    break;
                case LatLongKey:
                    if (data != null && data.hasExtra(IntentUtil.key)) {
                        latLng = data.getParcelableExtra(IntentUtil.key);
                        if (latLng != null) {
                            latitude = latLng.latitude;
                            longitude = latLng.longitude;
                            String lat = "" + latLng.latitude + "," + latLng.longitude;
                            etAddress.setText(lat);
                            etSelectCity.setText("");
                            citySelected = null;
                            isLocationUpdate = true;
                        }
                    }
                    break;
            }
        }

    }

    private static final String TAG = "FragmentEditEvents";

    private void getAddress(double lat, double lng) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            state_name = (addresses.get(0).getAdminArea());
            countryName = (addresses.get(0).getCountryName());

        } catch (Exception e) {
            ConstUtility.showToast(getActivity(), R.string.unable_to_get_location, false);
            e.printStackTrace();
        }
    }

    private void setStartDateTimeField(int year, int month, int day) {
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), R.style.MyAppTheme, date, year, month, day);
        //     DatePickerDialog dialog = new DatePickerDialog(getActivity(), date, year, month, day);

        dialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dialog.show();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            new TimePickerDialog(getContext(), R.style.MyAppTheme, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    myCalendar.set(Calendar.MINUTE, minute);
                    updateLabel();
                }
            }, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), false).show();
        }
    };

    private void updateLabel() {
//        String myFormat = "dd-MMM-yyyy hh:mm a";
        String myFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        if (selectedDate == 1) {
            tvStartDate.setText(sdf.format(myCalendar.getTime()));
            startDate = ConstUtility.getDateformate(myCalendar.getTime());
        }
        if (selectedDate == 2) {
            tvEventEndDate.setText(sdf.format(myCalendar.getTime()));
            endDate = ConstUtility.getDateformate(myCalendar.getTime());
        }
    }
}