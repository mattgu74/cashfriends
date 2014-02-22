package com.mattgu.cash;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class AccountActivity extends Activity {

	private TextView tvNom;
	private TextView tvPrenom;
	private TextView tvEmail;
	private TextView tvBadge;
	private TextView tvSolde;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);
		
		tvNom = (TextView) findViewById(R.id.nom);
		tvPrenom = (TextView) findViewById(R.id.prenom);
		tvEmail = (TextView) findViewById(R.id.email);
		tvBadge = (TextView) findViewById(R.id.badge);
		tvSolde = (TextView) findViewById(R.id.solde);
		
		tvNom.setVisibility(View.GONE);
		tvPrenom.setVisibility(View.GONE);
		
	}

}
