package com.sumilux.apps.jabze.contact.adapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.sumilux.apps.jabze.contact.model.ContactBean;
import com.sumilux.apps.jabze.contact.view.QuickAlphabeticBar;
import com.sumilux.apps.jabze.contact.R;

public class ContactListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<ContactBean> list;
	private HashMap<String, Integer> alphaIndexer; // 字母索引
	private String[] sections; // 存储每个章节
	private Context ctx; // 上下文

	public ContactListAdapter(Context context, List<ContactBean> list,
			QuickAlphabeticBar alpha) {
		this.ctx = context;
		this.inflater = LayoutInflater.from(context);
		this.list = list;
		this.alphaIndexer = new HashMap<String, Integer>();
		this.sections = new String[list.size()];

		for (int i = 0; i < list.size(); i++) {
			// 得到字母
			String name = getAlpha(list.get(i).getSortKey());
			if (!alphaIndexer.containsKey(name)) {
				alphaIndexer.put(name, i);
			}
		}

		Set<String> sectionLetters = alphaIndexer.keySet();
		ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);
		Collections.sort(sectionList);
		sections = new String[sectionList.size()];
		sectionList.toArray(sections);

		alpha.setAlphaIndexer(alphaIndexer);

	}
	
	public byte[] getPhoto(String people_id) {
		  String photo_id = null;
		  String selection1 = ContactsContract.Contacts._ID + " = " + people_id;
		  Cursor cur1 = ctx.getContentResolver().query(
		    ContactsContract.Contacts.CONTENT_URI, null, selection1, null,
		    null);
		  if (cur1.getCount() > 0) {
		   cur1.moveToFirst();
		   photo_id = cur1.getString(cur1
		     .getColumnIndex(ContactsContract.Contacts.PHOTO_ID));
		   //System.out.println("photo_id:" + photo_id);
		  }
		  String[] projection = new String[] {
		  ContactsContract.Data.DATA15
		  };
		  String selection = ContactsContract.Data._ID + " = " + photo_id;
		  Cursor cur = ctx.getContentResolver().query(
		    ContactsContract.Data.CONTENT_URI, projection, selection, null,
		    null);
		  cur.moveToFirst();
		  byte[] contactIcon = cur.getBlob(0);
		  System.out.println("conTactIcon:" + contactIcon);
		  if (contactIcon == null) {
		   return null;
		  } else {
		   return contactIcon;
		  }
		}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void remove(int position) {
		list.remove(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.contact_list_item, null);
			holder = new ViewHolder();
			holder.quickContactBadge = (QuickContactBadge) convertView
					.findViewById(R.id.qcb);
			holder.alpha = (TextView) convertView.findViewById(R.id.alpha);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.head=(ImageView) convertView.findViewById(R.id.head);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ContactBean contact = list.get(position);
		String name = contact.getDesplayName();
		//String number = contact.getPhoneNum();
		holder.name.setText(name);
		holder.quickContactBadge.assignContactUri(Contacts.getLookupUri(
				contact.getContactId(), contact.getLookUpKey()));
		if (0 == contact.getPhotoId()) {
			holder.head.setImageResource(R.drawable.touxiang);
		} else {
			Log.v("cid::::",""+contact.getContactId());
			Uri uri = ContentUris.withAppendedId(
					ContactsContract.Contacts.CONTENT_URI,
					contact.getContactId());
			InputStream input = ContactsContract.Contacts
					.openContactPhotoInputStream(ctx.getContentResolver(), uri);
			Bitmap contactPhoto = BitmapFactory.decodeStream(input);
			holder.head.setImageBitmap(contactPhoto);
		}
		// 当前字母
		String currentStr = getAlpha(contact.getSortKey());
		// 前面的字母
		String previewStr = (position - 1) >= 0 ? getAlpha(list.get(
				position - 1).getSortKey()) : " ";

		if (!previewStr.equals(currentStr)) {
			holder.alpha.setVisibility(View.VISIBLE);
			holder.alpha.setText(currentStr);
		} else {
			holder.alpha.setVisibility(View.GONE);
		}
		return convertView;
	}

	public void saveBitmap(Bitmap bm,String picName) {
		 
		  File f = new File("/sdcard/namecard/", picName);
		  if (f.exists()) {
		   f.delete();
		  }
		  try {
		   FileOutputStream out = new FileOutputStream(f);
		   bm.compress(Bitmap.CompressFormat.PNG, 90, out);
		   out.flush();
		   out.close();
		  
		  } catch (FileNotFoundException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  } catch (IOException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  }

		 }
	
	private static class ViewHolder {
		QuickContactBadge quickContactBadge;
		TextView alpha;
		TextView name;
		//TextView number;
		ImageView head;
	}

	/**
	 * 获取首字母
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		if (str == null) {
			return "#";
		}
		if (str.trim().length() == 0) {
			return "#";
		}
		char c = str.trim().substring(0, 1).charAt(0);
		// 正则表达式匹配
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase(); // 将小写字母转换为大写
		} else {
			return "#";
		}
	}
}
