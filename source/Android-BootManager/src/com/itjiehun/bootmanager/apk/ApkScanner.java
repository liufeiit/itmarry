package com.itjiehun.bootmanager.apk;

public interface ApkScanner {
	void onScannerProgress(ApkInfo paramApkInfo);

	void onScannerStart();

	void onScannerStop();
}