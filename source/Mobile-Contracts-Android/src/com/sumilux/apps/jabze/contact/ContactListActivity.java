package com.sumilux.apps.jabze.contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ListView;

import com.sumilux.apps.jabze.contact.adapter.ContactListAdapter;
import com.sumilux.apps.jabze.contact.model.ContactBean;
import com.sumilux.apps.jabze.contact.view.QuickAlphabeticBar;

/**
 * ��ϵ���б�
 * 
 * @author Administrator
 * 
 */
public class ContactListActivity extends Activity {

	private ContactListAdapter adapter;
	private ListView contactList;
	private List<ContactBean> list;
	private AsyncQueryHandler asyncQueryHandler; // �첽��ѯ���ݿ������
	private QuickAlphabeticBar alphabeticBar; // ����������

	private Map<Long, ContactBean> contactIdMap = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_list_view);
		contactList = (ListView) findViewById(R.id.contact_list);
		alphabeticBar = (QuickAlphabeticBar) findViewById(R.id.fast_scroller);

		// ʵ����
		asyncQueryHandler = new MyAsyncQueryHandler(getContentResolver());
		init();

	}

	/**
	 * ��ʼ�����ݿ��ѯ����
	 */
	private void init() {
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // ��ϵ��Uri��
		// ��ѯ���ֶ�
		String[] projection = { ContactsContract.CommonDataKinds.Phone._ID,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.DATA1, "sort_key",
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
				ContactsContract.CommonDataKinds.Photo.PHOTO_ID,
				ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY
				};
		// ����sort_key�����??
		asyncQueryHandler.startQuery(0, null, uri, projection, null, null,
				"sort_key COLLATE LOCALIZED asc");

	}

	/**
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyAsyncQueryHandler extends AsyncQueryHandler {

		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}

		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			if (cursor != null && cursor.getCount() > 0) {
				contactIdMap = new HashMap<Long, ContactBean>();
				list = new ArrayList<ContactBean>();
				cursor.moveToFirst(); // �α��ƶ�����һ��
				for (int i = 0; i < cursor.getCount(); i++) {
					cursor.moveToPosition(i);
					String name = cursor.getString(1);
					String number = cursor.getString(2);
					String sortKey = cursor.getString(3);
					long contactId = cursor.getLong(4);
					long photoId = cursor.getLong(5);
					String lookUpKey = cursor.getString(6);

					if (contactIdMap.containsKey(contactId)) {
						// �޲���
					} else {
						// ������ϵ�˶���
						ContactBean contact = new ContactBean();
						contact.setContactId(contactId);
						contact.setDesplayName(name);
						contact.setPhoneNum(number);
						contact.setSortKey(sortKey);
						contact.setPhotoId(photoId);
						contact.setLookUpKey(lookUpKey);
						list.add(contact);

						contactIdMap.put(contactId, contact);
					}
				}
				if (list.size() > 0) {
					setAdapter(list);
				}
			}

			super.onQueryComplete(token, cookie, cursor);
		}

	}

	private void setAdapter(List<ContactBean> list) {
		adapter = new ContactListAdapter(this, list, alphabeticBar);
		contactList.setAdapter(adapter);
		alphabeticBar.init(ContactListActivity.this);
		alphabeticBar.setListView(contactList);
		alphabeticBar.setHight(alphabeticBar.getHeight());
		alphabeticBar.setVisibility(View.VISIBLE);
	}
}
