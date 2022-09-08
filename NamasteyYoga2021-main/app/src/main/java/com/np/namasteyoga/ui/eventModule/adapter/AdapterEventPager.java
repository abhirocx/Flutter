//http://codetheory.in/android-image-slideshow-using-viewpager-pageradapter/
package com.np.namasteyoga.ui.eventModule.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.np.namasteyoga.R;
import com.np.namasteyoga.datasource.pojo.Event;
import com.np.namasteyoga.datasource.pojo.RatingRequest;
import com.np.namasteyoga.datasource.pojo.RatingResponse;
import com.np.namasteyoga.interfaces.Rating;
import com.np.namasteyoga.utils.C;
import com.np.namasteyoga.utils.ConstUtility;
import com.np.namasteyoga.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import im.delight.android.location.SimpleLocation;

public class AdapterEventPager extends RecyclerView.Adapter<AdapterEventPager.ItemViewHolder> {

    private Activity mContext;
    private LayoutInflater mLayoutInflater;
    private List<Event> allEvents;
    RatingResponse ratingResponse = new RatingResponse();

    private ItemViewHolder itemViewHolder;

    List<Event> lstEvent;
    ConstUtility constUtility;
    Rating ratingDialog;
    SharedPreferences sharedPreference;

    public AdapterEventPager(Activity context, List<Event> allEvents, Rating rating, SharedPreferences sharedPreference) {
        this.mContext = context;
        this.allEvents = allEvents;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        lstEvent = allEvents;
        constUtility = new ConstUtility();
        this.ratingDialog = rating;
        this.sharedPreference = sharedPreference;

    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.yoga_event_item_v2, parent, false);
//                .inflate(R.layout.event_item_layout, parent, false);

        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder viewHolder, int position) {
        String eventName = allEvents.get(position).getEventName();
        viewHolder.eventName.setText(eventName);
        viewHolder.tvPaidFree.setText(allEvents.get(position).getMode());
        viewHolder.tvAddress.setText(allEvents.get(position).getAddress());
        viewHolder.tvName.setText(ConstUtility.decrypt(allEvents.get(position).getContactPerson()));
        viewHolder.tvPhone.setText(ConstUtility.decrypt(allEvents.get(position).getContactNo()));
        viewHolder.tvEmail.setText(ConstUtility.decrypt(allEvents.get(position).getEmail()));
        if (allEvents.get(position).getNearest() == 1)
            viewHolder.tvNearestCity.setText(allEvents.get(position).getNearest_distance() + "KM away from " + allEvents.get(position).getCityName());
        else {
            viewHolder.tvNearestCity.setVisibility(View.GONE);
        }
        viewHolder.tvDate.setText(ConstUtility.getDateInNumber(allEvents.get(position).getStartTime()) + " - " + ConstUtility.getDateInNumber(allEvents.get(position).getEndTime()));
        viewHolder.tvTime.setVisibility(View.GONE);
        viewHolder.btnNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleLocation currLoc = new SimpleLocation(mContext);

                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + currLoc.getLatitude() + "," +
                                currLoc.getLongitude() + "&daddr=" + allEvents.get(position).getLat() + "," +
                                allEvents.get(position).getLng()));
                mContext.startActivity(intent);
            }
        });

        viewHolder.btnAddToCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUpToAddEvetnToCaledar(allEvents.get(position));

            }
        });

        viewHolder.llRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (SharedPreference.getInstance(mContext).getBoolean(C.IS_LOGIN)) {
                if (SharedPreferencesUtils.INSTANCE.getUserDetails(sharedPreference)!=null) {
                    ratingDialog(position);
                } else {
                    //  ratingWithoutLogin(position);
                    ratingDialog.ratingDialog(String.valueOf(allEvents.get(position).getId()), position);
                }
            }
        });


        viewHolder.tvRating.setText(allEvents.get(position).getRating() + "");
        viewHolder.llBottomSeparator.setVisibility(View.GONE);
        viewHolder.llEditEvent.setVisibility(View.GONE);

        itemViewHolder = viewHolder;


    }

    int rating = 0;

    public Event getItem(int pos) {
        return allEvents.get(pos);
    }

    @Override
    public int getItemCount() {
        return allEvents.size();
    }

    public void ratingDialog(int pos) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.rating_dialog);
        ImageView ivStar1 = (ImageView) dialog.findViewById(R.id.ivStar1);
        ImageView ivStar2 = (ImageView) dialog.findViewById(R.id.ivStar2);
        ImageView ivStar3 = (ImageView) dialog.findViewById(R.id.ivStar3);
        ImageView ivStar4 = (ImageView) dialog.findViewById(R.id.ivStar4);
        ImageView ivStar5 = (ImageView) dialog.findViewById(R.id.ivStar5);
        ImageView ivCross = (ImageView) dialog.findViewById(R.id.ivCross);

        ivStar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivStar1.setImageResource(R.drawable.feedback_active);
                ivStar2.setImageResource(R.drawable.feedback_deactive);
                ivStar3.setImageResource(R.drawable.feedback_deactive);
                ivStar4.setImageResource(R.drawable.feedback_deactive);
                ivStar5.setImageResource(R.drawable.feedback_deactive);
                doRating(1, pos);
                dialog.dismiss();

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
                doRating(2, pos);
                dialog.dismiss();

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
                doRating(3, pos);
                dialog.dismiss();

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
                doRating(4, pos);
                dialog.dismiss();

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
                doRating(5, pos);
                dialog.dismiss();

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

    public void doRating(int rating, int pos) {
        if (ConstUtility.isNetworkConnectivity(mContext)) {
            RatingRequest ratingRequest = new RatingRequest();
            ratingRequest.setEmail(ConstUtility.encryptLC(SharedPreferencesUtils.INSTANCE.getUserDetails(sharedPreference).getEmail()));
            ratingRequest.setEventId(Integer.valueOf(allEvents.get(pos).getId()));
            ratingRequest.setRating(rating);
            ratingRequest.setName(ConstUtility.encryptLC(SharedPreferencesUtils.INSTANCE.getUserDetails(sharedPreference).getName()));
            ratingRequest.setType("event");

            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            String json = gson.toJson(ratingRequest);
            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest req = new JsonObjectRequest(C.API_ADD_RATING, jsonObj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                VolleyLog.v("Response:%n %s", response.toString(4));

                                ratingResponse = gson.fromJson(response.toString(), RatingResponse.class);
                                if (ratingResponse.getStatus() == C.NP_STATUS_SUCCESS) {
                                    Toast.makeText(mContext, "Thanks for rating", Toast.LENGTH_SHORT).show();
                                    allEvents.get(pos).setRating(Float.parseFloat(ratingResponse.getRatingData().getRating()));
//                                    itemViewHolder.tvRating.setText(ratingResponse.getRatingData().getRating());
                                    notifyDataSetChanged();
                                } else {
                                    Toast.makeText(mContext, ratingResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                    //showErrorPopup();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return ConstUtility.getHeaderPHP(json, mContext);
                }
            };
            RetryPolicy policy = new DefaultRetryPolicy(C.API_TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            req.setRetryPolicy(policy);
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            requestQueue.add(req);
        } else {
            //showNetworkPopup();
        }

    }


    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public String currentDateFormat() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd _HH_mm_ss");
        String currentTimeStamp = sdf.format(new Date());
        return currentTimeStamp;
    }

    public void addItem(List<Event> data) {
        this.lstEvent.addAll(data);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.llPagerView)
        LinearLayout llPagerView;

        @BindView(R.id.tvPaidFree)
        TextView tvPaidFree;

        @BindView(R.id.tvAddress)
        TextView tvAddress;

        @BindView(R.id.btn_Navigate)
        Button btnNavigate;

        @BindView(R.id.tvPhone)
        TextView tvPhone;

        @BindView(R.id.tvName)
        TextView tvName;

        @BindView(R.id.tvEmail)
        TextView tvEmail;

        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.eventName)
        TextView eventName;

        @BindView(R.id.btn_AddtoCal)
        Button btnAddToCal;

        @BindView(R.id.tvRating)
        TextView tvRating;
        @BindView(R.id.tvNearestCity)
        TextView tvNearestCity;

        @BindView(R.id.ivStar)
        ImageView ivStar;
        @BindView(R.id.ll_rating)
        LinearLayout llRating;

        /////////////////////////////////////////////
        @BindView(R.id.ll_bottom_separator)
        View llBottomSeparator;
        @BindView(R.id.ll_edit_event)
        LinearLayout llEditEvent;
        ////////////////////////////////////////////

//        @BindView(R.id.ivCity)
//        ImageView ivCity;

        ItemViewHolder(View view) {

            super(view);
            ButterKnife.bind(this, view);
        }

    }

    void showPopUpToAddEvetnToCaledar(Event event) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
        alertBuilder.setCancelable(false);
        alertBuilder.setTitle(R.string.alert);
        alertBuilder.setMessage(R.string.are_you_sure_you_want_to_add);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {


            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void onClick(DialogInterface dialog, int which) {
                onAddEventClicked(event);
                dialog.dismiss();
            }
        });

        alertBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = alertBuilder.create();
        /*alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(mContext.getResources().getColor(R.color.version_2_blue));
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(mContext.getResources().getColor(R.color.version_2_blue));*/
        alert.show();
    }

    public void onAddEventClicked(Event event) {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");


        String startTime = event.getStartTime();
        String endTime = event.getEndTime();

        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, convertStringToTimestamp(startTime));
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, convertStringToTimestamp(endTime));
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

        intent.putExtra(CalendarContract.Events.TITLE, event.getEventName());
//        intent.putExtra(CalendarContract.Events.DESCRIPTION, event.getDescription());
//        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, event.getAddress() + ", " + event.getCityName() + ", " + event.getStateName());
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, event.getAddress());

        intent.putExtra(CalendarContract.Events.RRULE, "FREQ=YEARLY;COUNT=1");

        mContext.startActivity(intent);
    }

    public long convertStringToTimestamp(String str_date) {
        try {
            SimpleDateFormat formatter;
//            2019-06-04 15:06:00
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // you can change format of date
            Date date = formatter.parse(str_date);

            return date.getTime();
        } catch (ParseException e) {
            System.out.println("Exception :" + e);
            return 0;
        }
    }

}
