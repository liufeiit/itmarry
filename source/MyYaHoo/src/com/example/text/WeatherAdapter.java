package com.example.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class WeatherAdapter extends BaseAdapter {
	private static final int FORECAST = 0;
	private static final int WEATHER_DETAILS = 1;
	private static final int WIND_PRESSURE = 2;
	private static final int PRECIPITATION = 3;
	private static final int SUN_MOON = 4;
	private LayoutInflater mLayoutInflater;
	private List<Integer> mTags;

	public static <E> ArrayList<E> newArrayList(E... elements) {
		int capacity = (elements.length * 110) / 100 + 5;
		ArrayList<E> list = new ArrayList<E>(capacity);
		Collections.addAll(list, elements);
		return list;
	}

	public WeatherAdapter(Context context) {
//		mTags = Collections.unmodifiableList(WeatherAdapter
//				.newArrayList(FORECAST, WEATHER_DETAILS, WIND_PRESSURE,
//						PRECIPITATION, SUN_MOON));
		mTags = new ArrayList<Integer>();
		mTags.add(FORECAST);
		mTags.add(WEATHER_DETAILS);
		mTags.add(WIND_PRESSURE);
		mTags.add(PRECIPITATION);
		mTags.add(SUN_MOON);
		mLayoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mTags.size();
	}

	@Override
	public Integer getItem(int position) {
		return mTags.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int itemType = mTags.get(position);
		ViewHolder holder;
		if (convertView == null
				|| convertView.getTag(R.drawable.ic_launcher + itemType) == null) {
			holder = new ViewHolder();
			switch (itemType) {
			case FORECAST:
				convertView = mLayoutInflater.inflate(R.layout.forecast,
						parent, false);
				break;
			case WEATHER_DETAILS:
				convertView = mLayoutInflater.inflate(R.layout.weather_details,
						parent, false);
				break;
			case WIND_PRESSURE:
				convertView = mLayoutInflater.inflate(R.layout.wind_pressure,
						parent, false);
				break;
			case PRECIPITATION:
				convertView = mLayoutInflater.inflate(R.layout.precipitation,
						parent, false);
				break;
			case SUN_MOON:
				convertView = mLayoutInflater.inflate(R.layout.sun_moon,
						parent, false);
				break;
			default:
				break;
			}
			convertView.setTag(R.drawable.ic_launcher + itemType, holder);
			// convertView.setTag(R.string.app_name, R.drawable.ic_launcher
			// + getItemViewType(position));
		} else {
			holder = (ViewHolder) convertView.getTag(R.drawable.ic_launcher
					+ itemType);
		}
		return convertView;
	}

	static class ViewHolder {

	}

	// @Override
	// public int getItemViewType(int position) {
	// if (position >= mTags.size())
	// return -1;
	// return mTags.get(position);
	// }

	public void remove(Integer item) {
		if (mTags != null && mTags.size() > 0) {
			mTags.remove(item);
			notifyDataSetChanged();
		}
	}

	public void insert(Integer item, int to) {
		if (mTags != null) {
			mTags.add(to, item);
			notifyDataSetChanged();
		}
	}

}
