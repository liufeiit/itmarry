package com.jingtuo.android.common.ftp;

import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FtpClient {

	
	public static final int BUFFER_SIZE = 64*1024;
	
	private FTPClient client;
	
	private FtpConfig config;
	
	private boolean connected, login;
	
	private String rootPath;
	
	public FtpClient(FtpConfig config){
		this.config = config;
		client = new FTPClient();
	}
	
	/**
	 * 建立连接
	 */
	public synchronized void connect(){
		if (!client.isConnected()) {
			try {
				client.connect(config.getHostname(), config.getPort());
				connected = FTPReply.isPositiveCompletion(client.getReplyCode());
				if(!connected){
					client.disconnect();
				}else{
					client.setBufferSize(BUFFER_SIZE);
				}
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				if (client.isConnected()) {
					try {
						client.disconnect();
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * 断开连接
	 */
	public synchronized void disconnect(){
		if(connected){
			try {
				client.disconnect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			connected = false;
		}
	}
	
	/**
	 * 检测是否连接
	 * @return
	 */
	public synchronized boolean isConnected(){
		return connected;
	}
	
	/**
	 * 登录
	 */
	public synchronized void login(){
		try {
			login = client.login(config.getUsername(), config.getPassword());
			rootPath = client.printWorkingDirectory();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 退出
	 */
	public synchronized void logout(){
		if(login){
			try {
				boolean flag = client.logout();
				if(flag){
					login = false;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 是否登录
	 * @return
	 */
	public synchronized boolean isLogin(){
		return login;
	}
	
	/**
	 * 上传文件
	 * @param local
	 * @param remote
	 * @return
	 */
	public boolean uploadFile(String local, String remote){
		boolean result = false;
		if (FtpConfig.MODE_LOCAL_PASSIVE == config.getMode()) {
			client.enterLocalPassiveMode();
		}
		try {
			client.setFileStructure(FTP.FILE_STRUCTURE);
			client.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
			client.setFileType(FTP.BINARY_FILE_TYPE);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 下载文件
	 * @param local
	 * @param remote
	 * @return
	 */
	public boolean downloadFile(String local, String remote){
		boolean result = false;
		if (FtpConfig.MODE_LOCAL_PASSIVE == config.getMode()) {
			client.enterLocalPassiveMode();
		}
		try {
			client.setFileStructure(FTP.FILE_STRUCTURE);
			client.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
			client.setFileType(FTP.BINARY_FILE_TYPE);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 创建--要求目录必须
	 * @param path
	 */
	public void createPath(String path){
		
	}
}
