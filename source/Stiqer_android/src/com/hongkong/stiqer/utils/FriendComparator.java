package com.hongkong.stiqer.utils;

import java.util.Comparator;

import com.hongkong.stiqer.entity.Friend;

public class FriendComparator implements Comparator<Friend> {

	public int compare(Friend o1, Friend o2) {
		if (o1.getUser_index().equals("#")
				|| o2.getUser_index().equals("#")) {
			return -1;
		} else {
			return o1.getUser_index().compareTo(o2.getUser_index());
		}
	}


}
