package com.guide.geek.ui;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guide.geek.R;
import com.guide.geek.app.BaseFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainFragment extends BaseFragment {  
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  
        return inflater.inflate(R.layout.my_main, null);  
    }  
  
    private SlidingMenu menu;  
  
    private LinearLayout layoutSearchType;  
    private Button btnSearch;  
    private ImageView ivMore;  
    private ImageView ivSelectCity;  
    
    public MainFragment(SlidingMenu menu) {  
        this.menu = menu;  
    }  
  
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
  
        this.initViews();  
    }  
  
    private void initViews() {  
        View parent = this.getView();  
  
        this.layoutSearchType = (LinearLayout) parent.findViewById(R.id.layout_search_type);  
        this.btnSearch = (Button) parent.findViewById(R.id.btn_search);  
        this.ivMore = (ImageView) parent.findViewById(R.id.btn_more);  
        this.ivSelectCity = (ImageView) parent.findViewById(R.id.btn_select_city);  
  
        this.layoutSearchType.setOnClickListener(this);  
        this.btnSearch.setOnClickListener(this);  
        this.ivMore.setOnClickListener(this);  
        this.ivSelectCity.setOnClickListener(this);  
  
        DisplayMetrics dm = new DisplayMetrics();  
        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);  
        int screenWidth = dm.widthPixels;  
        int offsetX = (int) (12 * dm.density);  
        int spacing = (int) (8 * dm.density);  
        int itemWidth = (screenWidth - 3 * offsetX) / 2;  
        int itemHeight = (int) (112.5f * itemWidth / 142.5f);  
        MyAdapter adapter = new MyAdapter(context, getDataList());  
  
        LinearLayout classifyLayout = (LinearLayout) parent.findViewById(R.id.layout_classify);  
        classifyLayout.setPadding(offsetX, offsetX - spacing, offsetX, offsetX);  
  
        LinearLayout row = null;  
        View view;  
        LinearLayout.LayoutParams layoutParams;  
        for (int i = 0; i < adapter.getCount(); i++) {  
            layoutParams = new LinearLayout.LayoutParams(itemWidth, itemHeight);  
            if (i % 2 == 0) {  
                row = new LinearLayout(context);  
                row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));  
                classifyLayout.addView(row);  
            } else {  
                layoutParams.leftMargin = spacing;  
            }  
            view = adapter.getView(i, null, null);  
            layoutParams.topMargin = spacing;  
            row.addView(view, layoutParams);  
        }  
  
    }  
  
    @Override  
    public void onClick(View v) {  
        switch (v.getId()) {  
        case R.id.btn_more: {  
            this.menu.showMenu();  
            break;  
        }  
        case R.id.btn_select_city: {  
            this.menu.showSecondaryMenu();  
            break;  
        }  
        }  
    }  
  
    private class MyAdapter extends BaseAdapter {  
        private Context context;  
        private ArrayList<MyData> datas;  
        private LayoutInflater inflater;  
  
        public MyAdapter(Context context, ArrayList<MyData> datas) {  
            this.context = context;  
            this.datas = datas;  
            if (this.datas == null) {  
                this.datas = new ArrayList<MyData>();  
            }  
            this.inflater = LayoutInflater.from(this.context);  
        }  
  
        @Override  
        public int getCount() {  
            return this.datas.size();  
        }  
  
        @Override  
        public Object getItem(int position) {  
            return position;  
        }  
  
        @Override  
        public long getItemId(int position) {  
            return position;  
        }  
  
        @Override  
        public View getView(int position, View convertView, ViewGroup parent) {  
            MyHolder holder;  
            if (convertView == null) {  
                convertView = inflater.inflate(R.layout.my_main_inner_grid, null);  
                holder = new MyHolder();  
                holder.img = (ImageView) convertView.findViewById(R.id.inner_img);  
                holder.txt = (TextView) convertView.findViewById(R.id.txt_tile);  
                holder.img.setOnClickListener(this.clickListener);  
                // holder.img.setOnTouchListener(this.touchListener);  
                convertView.setTag(holder);  
            } else {  
                holder = (MyHolder) convertView.getTag();  
            }  
  
            MyData data = this.datas.get(position);  
            holder.data = data;  
            holder.img.setBackgroundResource(data.rid);  
            holder.txt.setText(data.name);  
            holder.img.setTag(holder);  
            return convertView;  
        }  
  
        private View.OnClickListener clickListener = new View.OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                ((MyHolder) v.getTag()).data.type.toAct(context);  
            }  
        };  
        private View.OnTouchListener touchListener = new View.OnTouchListener() {  
            @Override  
            public boolean onTouch(View v, MotionEvent event) {  
                // TODO Auto-generated method stub  
                return false;  
            }  
        };  
    }  
  
    private class MyHolder {  
        ImageView img;  
        TextView txt;  
        MyData data;  
    }  
  
    private static enum MyClassify {  
        Recommend, MyCenter, Sell, Rent, SchoolDistrictRoom, ResidentialHousing;  
  
        public void toAct(Context context) {  
            switch (this) {  
            case Recommend:  
                Toast.makeText(context, "代开发中.......", Toast.LENGTH_LONG).show();  
                break;  
            case MyCenter:  
                Toast.makeText(context, "代开发中.......", Toast.LENGTH_LONG).show();  
                break;  
            case Sell:  
                Toast.makeText(context, "代开发中.......", Toast.LENGTH_LONG).show();  
                break;  
            case Rent:  
                Toast.makeText(context, "代开发中.......", Toast.LENGTH_LONG).show();  
                break;  
            case SchoolDistrictRoom:  
  
                Toast.makeText(context, "代开发中.......", Toast.LENGTH_LONG).show();  
                break;  
            case ResidentialHousing:  
                Toast.makeText(context, "代开发中.......", Toast.LENGTH_LONG).show();  
                break;  
            }  
        }  
    }  
  
    private static class MyData {  
        private int rid;  
        private int name;  
        private MyClassify type;  
  
        public MyData(int rid, int name, MyClassify type) {  
            this.rid = rid;  
            this.name = name;  
            this.type = type;  
        }  
    }  
  
    private static final ArrayList<MyData> getDataList() {  
        ArrayList<MyData> list = new ArrayList<MyData>();  
        list.add(new MyData(R.drawable.ic_pic_recommend, R.string.txt_recommend, MyClassify.Recommend));  
        list.add(new MyData(R.drawable.ic_pic_my_center, R.string.txt_my_center, MyClassify.MyCenter));  
        list.add(new MyData(R.drawable.ic_pic_sell, R.string.txt_sell, MyClassify.Sell));  
        list.add(new MyData(R.drawable.ic_pic_rent, R.string.txt_rent, MyClassify.Rent));  
        list.add(new MyData(R.drawable.ic_pic_school_district_room, R.string.txt_school_district_room, MyClassify.SchoolDistrictRoom));  
        list.add(new MyData(R.drawable.ic_pic_residential_housing, R.string.txt_residential_housing, MyClassify.ResidentialHousing));  
        return list;  
    }  
  
}  

