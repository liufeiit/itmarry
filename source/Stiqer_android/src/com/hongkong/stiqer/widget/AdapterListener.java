package com.hongkong.stiqer.widget;

public interface AdapterListener
{
    public void sendData(int position, int num, int type);
    public void OpenComment(String fid, int position, int comment_num);
}