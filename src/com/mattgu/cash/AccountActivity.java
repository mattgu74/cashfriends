package com.mattgu.cash;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import com.mattgu.cash.api.Api;
import com.mattgu.cash.api.ApiClient;
import com.mattgu.cash.models.User;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class AccountActivity extends Activity {

	private TextView tvNom;
	private TextView tvPrenom;
	private TextView tvEmail;
	private TextView tvBadge;
	private TextView tvSolde;
	private TextView tvSoldeIn;
	private TextView tvSoldeOut;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);
		
		tvNom = (TextView) findViewById(R.id.nom);
		tvPrenom = (TextView) findViewById(R.id.prenom);
		tvEmail = (TextView) findViewById(R.id.email);
		tvBadge = (TextView) findViewById(R.id.badge);
		tvSolde = (TextView) findViewById(R.id.solde);
		tvSoldeIn = (TextView) findViewById(R.id.soldeIn);
		tvSoldeOut = (TextView) findViewById(R.id.soldeOut);
		
		tvNom.setVisibility(View.GONE);
		tvPrenom.setVisibility(View.GONE);
		
		final Api service = ApiClient.getService();
		service.user(new Callback<User>() {

			@Override
			public void failure(RetrofitError arg0) {
				new AlertDialog.Builder(AccountActivity.this)
			    .setTitle("Erreur")
			    .setMessage("Une erreur à eu lieu. "+arg0)
			    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			            // continue with delete
			        }
			     })
			     .show();
			}

			@Override
			public void success(User user, Response arg1) {
				tvEmail.setText("Email : "+user.email);
				tvBadge.setText("Badge : "+user.badge_uid);
				tvSolde.setText("Solde global : "+user.solde+" €");
				tvSoldeIn.setText("Solde 'rentrant' : "+user.in+" €");
				tvSoldeOut.setText("Solde 'sortant' : -"+user.out+" €");
			}
			
			
		});
		
	}

}
