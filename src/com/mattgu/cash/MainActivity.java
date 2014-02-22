package com.mattgu.cash;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final Button btnReload = (Button) findViewById(R.id.button_reload);
		final Button btnPay = (Button) findViewById(R.id.button_pay);
		final Button btnMyAccount = (Button) findViewById(R.id.button_my_account);
		
		View.OnClickListener handler = new View.OnClickListener(){
		
			public void onClick(View v) {
				Intent intentMain = new Intent(MainActivity.this , 
                        LoginActivity.class);
				if(v==btnReload) {
					intentMain.putExtra("action", 1);
				} else if(v==btnPay) {
					intentMain.putExtra("action", 2);
				} else {
					intentMain.putExtra("action", 3);
				}
				MainActivity.this.startActivity(intentMain);
			}
		};
		
		btnReload.setOnClickListener(handler);
		btnPay.setOnClickListener(handler);
		btnMyAccount.setOnClickListener(handler);
	}

}
