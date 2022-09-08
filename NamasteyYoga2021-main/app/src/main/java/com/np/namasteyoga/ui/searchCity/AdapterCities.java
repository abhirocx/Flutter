package com.np.namasteyoga.ui.searchCity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.np.namasteyoga.R;
import com.np.namasteyoga.datasource.pojo.City;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class AdapterCities extends BaseAdapter implements Filterable {

    private List<City> cityList;
    private final Activity activity;
    private LayoutInflater inflate;
    private RelativeLayout rel_Bg;
    Animation scaleUp;

    String userType;
    String userId;
    String user_type_name;
    Drawable drawable1;
    Drawable drawable2;
    Drawable drawable3;
    Drawable drawable4;
    Drawable drawable5;

    final ArrayList<City> originalCities;

    public AdapterCities(Activity activity, List<City> cityList) {
        this.activity = activity;
        this.cityList = cityList;
        this.originalCities = new ArrayList<City>(cityList);
        inflate = LayoutInflater.from(activity);
        scaleUp = AnimationUtils.loadAnimation(activity, R.anim.scale_up);

    }

    @Override
    public int getCount() {
        return cityList.size();
    }

    @Override
    public City getItem(int position) {
        return cityList.get(position);
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflate.inflate(R.layout.item_city, null);

        }

        LinearLayout llContent = (LinearLayout) convertView.findViewById(R.id.llcontent);
        TextView tvCityName = (TextView) convertView.findViewById(R.id.tvCityName);
        ImageView ivTick = (ImageView) convertView.findViewById(R.id.ivTick);
        TextView tvStateName = (TextView) convertView.findViewById(R.id.tvStateName);

        tvStateName.setText(getItem(position).getState_name() + " , " + getItem(position).getCountry_name());
        tvCityName.setText(getItem(position).getCity());
        ivTick.setVisibility(View.GONE);
        return convertView;
    }


    private double getDistance(Location loc1, Location loc2) {

        double distanceInMeters = loc1.distanceTo(loc2);
        DecimalFormat df = new DecimalFormat("#.#");
        distanceInMeters = distanceInMeters / 1000;

        return Double.parseDouble(df.format(distanceInMeters)
        );
    }
    public void clear() {
        cityList.clear();
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            String filterString = charSequence.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<City> list = originalCities;

            int count = list.size();
            final ArrayList<City> nlist = new ArrayList<City>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getName();
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(list.get(i));
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            cityList = (ArrayList<City>) filterResults.values;
            notifyDataSetChanged();
        }
    };
}
