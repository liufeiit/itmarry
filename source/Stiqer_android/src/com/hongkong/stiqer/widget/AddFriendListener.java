package com.hongkong.stiqer.widget;

public interface AddFriendListener
{
    public void sendRequest(int position);
    public void sendInvite(int position, int type, String rawfid,String name);
}