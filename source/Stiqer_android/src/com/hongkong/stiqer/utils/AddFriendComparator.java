package com.hongkong.stiqer.utils;

import java.util.Comparator;

import com.hongkong.stiqer.entity.AddFriend;
import com.hongkong.stiqer.entity.Friend;

public class AddFriendComparator implements Comparator<AddFriend> {

	public int compare(AddFriend o1, AddFriend o2) {
		if (o1.getSortKey().equals("#")
				|| o2.getSortKey().equals("#")) {
			return -1;
		} else {
			return o1.getSortKey().compareTo(o2.getSortKey());
		}
	}


}
