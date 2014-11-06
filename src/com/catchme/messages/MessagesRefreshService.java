package com.catchme.messages;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;

public class MessagesRefreshService extends IntentService {
	private Handler handler = new Handler();
	private int i;

	public MessagesRefreshService() {
		super("name");
		i = 0;
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		while (i < 10) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			handler.post(new Runnable() {
				public void run() {
					/*Toast.makeText(getApplicationContext(),
							"Liczymy do 10ciu!\n " + i, Toast.LENGTH_SHORT)
							.show();*/
				}
			});
			i++;
		}
	}

}
