package com.itjiehun.bootmanager;

import java.util.Comparator;

import com.itjiehun.bootmanager.widget.Item;

public class SortMethods {
	static final int SORTMETHOD_DEFAULT = 1;
	static final int SORTMETHOD_NAME_DOWN = 0;
	static final int SORTMETHOD_NAME_UP = 10;
	static final int SORTMETHOD_SIZE_DOWN = 2;
	static final int SORTMETHOD_SIZE_UP = 14;
	static final int SORTMETHOD_TIME_DOWN = 1;
	static final int SORTMETHOD_TIME_UP = 12;
	private static Comparator<Item> nameDown;
	private static Comparator<Item> nameUp = null;
	private static Comparator<Item> sizeDown;
	private static Comparator<Item> sizeUp;
	private static Comparator<Item> timeDown;
	private static Comparator<Item> timeUp;

	static {
		nameDown = null;
		timeUp = null;
		timeDown = null;
		sizeUp = null;
		sizeDown = null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Comparator<Item> getComparator(int paramInt) {
		switch (paramInt) {
		case 10:
			if (nameUp == null) {
				nameUp = new Comparator() {

					@Override
					public int compare(Object arg0, Object arg1) {
						if (((Item) arg0).enable != ((Item) arg1).enable) {
							if (((Item) arg0).enable)
								return -1;
							else
								return 1;
						}
						String str0 = ((Item) arg0).apkInfo.name;
						String str1 = ((Item) arg1).apkInfo.name;
						return str0.compareTo(str1);
					}
				};
			}
			return nameUp;
		case 0:
			if (nameDown == null) {
				nameDown = new Comparator() {

					@Override
					public int compare(Object arg0, Object arg1) {
						if (((Item) arg0).enable != ((Item) arg1).enable) {
							if (((Item) arg0).enable)
								return -1;
							else
								return 1;
						}
						String str0 = ((Item) arg0).apkInfo.name;
						String str1 = ((Item) arg1).apkInfo.name;
						return str1.compareTo(str0);
					}
				};
			}
			return nameDown;
		case 12:
			if (timeUp == null) {
				timeUp = new Comparator() {

					@Override
					public int compare(Object arg0, Object arg1) {
						if (((Item) arg0).enable != ((Item) arg1).enable) {
							if (((Item) arg0).enable)
								return -1;
							else
								return 1;
						}
						long date0 = ((Item) arg0).apkInfo.date;
						long date1 = ((Item) arg1).apkInfo.date;
						return ((Long) date0).compareTo(((Long) date1));
					}
				};
			}
			return timeUp;
		case 1:
			if (timeDown == null) {
				timeDown = new Comparator() {
					@Override
					public int compare(Object arg0, Object arg1) {
						if (((Item) arg0).enable != ((Item) arg1).enable) {
							if (((Item) arg0).enable)
								return -1;
							else
								return 1;
						}
						long date0 = ((Item) arg0).apkInfo.date;
						long date1 = ((Item) arg1).apkInfo.date;
						return ((Long) date1).compareTo(((Long) date0));
					}
				};
			}
			return timeDown;
		case 14:
			if (sizeUp == null) {
				sizeUp = new Comparator() {

					@Override
					public int compare(Object arg0, Object arg1) {
						if (((Item) arg0).enable != ((Item) arg1).enable) {
							if (((Item) arg0).enable)
								return -1;
							else
								return 1;
						}
						long size0 = ((Item) arg0).apkInfo.fileSize;
						long size1 = ((Item) arg1).apkInfo.fileSize;
						return ((Long) size0).compareTo(((Long) size1));
					}
				};
			}
			return sizeUp;
		case 2:
			if (sizeDown == null) {
				sizeDown = new Comparator() {
					@Override
					public int compare(Object arg0, Object arg1) {
						if (((Item) arg0).enable != ((Item) arg1).enable) {
							if (((Item) arg0).enable)
								return -1;
							else
								return 1;
						}
						long size0 = ((Item) arg0).apkInfo.fileSize;
						long size1 = ((Item) arg1).apkInfo.fileSize;
						return ((Long) size1).compareTo(((Long) size0));
					}
				};
			}
			return sizeDown;
		default:
			return null;
		}
	}
}