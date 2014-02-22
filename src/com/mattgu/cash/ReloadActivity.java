package com.mattgu.cash;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import com.mattgu.cash.api.Api;
import com.mattgu.cash.api.ApiClient;

import com.mattgu.cash.models.Transaction;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ReloadActivity extends Activity {

	private NfcAdapter mNfcAdapter;
	private EditText mFieldMail;
	private EditText mFieldBadge;
	private EditText mFieldAmount;
	private EditText mFieldNom;
	private EditText mFieldPrenom;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reload);
		
		mFieldBadge = (EditText) findViewById(R.id.badge);
		mFieldMail = (EditText) findViewById(R.id.email);
		mFieldNom = (EditText) findViewById(R.id.nom);
		mFieldPrenom = (EditText) findViewById(R.id.prenom);
		mFieldAmount = (EditText) findViewById(R.id.amount);
		
		// On cache les champs inutilisés pour le moment
		//mFieldMail.setVisibility(View.GONE);
		mFieldNom.setVisibility(View.GONE);
		mFieldPrenom.setVisibility(View.GONE);
		//findViewById(R.id.labelEmail).setVisibility(View.GONE);
		findViewById(R.id.labelNom).setVisibility(View.GONE);
		findViewById(R.id.labelPrenom).setVisibility(View.GONE);
		
		mNfcAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
		if(mNfcAdapter == null) {
            Toast.makeText(this, "NFC n'est pas disponible sur cet équipement", Toast.LENGTH_LONG).show();
		}
		else if(!mNfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC est désactivé", Toast.LENGTH_LONG).show();
		}
		
		final ProgressDialog progDialog1 = ProgressDialog.show(this,"Dépôt d'argent", "Dépot en cours...");
		progDialog1.hide();
		View.OnClickListener handler = new View.OnClickListener(){
			public void onClick(View v) {
				// Show loading popup
		    	progDialog1.show();
		    	
		    	// Launch request
		    	final Api service = ApiClient.getService();

		    	int amount = 0;
		    	try {
		    		amount = Integer.parseInt(mFieldAmount.getText().toString());
		    		if(amount > 0) {
		    			amount *= -1;
						service.postTransaction(mFieldBadge.getText().toString(), mFieldMail.getText().toString(), "Dépôt de cash", amount, new Callback<Transaction>() {
							
							@Override
							public void success(Transaction transaction, Response arg1) {
								progDialog1.dismiss();
							}
							
							@Override
							public void failure(RetrofitError arg0) {
								progDialog1.dismiss();
								Toast.makeText(ReloadActivity.this, "Erreur !", Toast.LENGTH_LONG).show();
							}
						});
		    		} else {
		    			Toast.makeText(ReloadActivity.this, "Montant négatif !", Toast.LENGTH_LONG).show();
		    		}
		    	} finally {
		    		mFieldBadge.setText("");
		    		mFieldMail.setText("");
		    		mFieldAmount.setText("");
		    	}
			}
		};
		findViewById(R.id.reload_button).setOnClickListener(handler);
	}
	
	final protected Boolean identifierIsAvailable() {
		return mNfcAdapter != null;
	}
	
    private String ByteArrayToHexString(byte [] inarray) 
    {
    	int i, j, in;
    	String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
    	String out= "";

    	for(j = 0 ; j < inarray.length ; ++j) 
        {
    		in = (int) inarray[j] & 0xff;
    		i = (in >> 4) & 0x0f;
    		out += hex[i];
    		i = in & 0x0f;
    		out += hex[i];
        }
    	return out;
    }
    
	@Override
    protected void onNewIntent(Intent intent) {
    	// Lecture de l'id du tag nfc
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        final String id = ByteArrayToHexString(tag.getId());
        Log.d("NFCREAD", "Tag: " + id);

        mFieldBadge.setText(id);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
		if(identifierIsAvailable()) {
			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
	 
			IntentFilter ndefFilter = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
	 
			IntentFilter[] filters = {ndefFilter};
	 
			String[][] techs = {{Ndef.class.getName()}};
	 
			mNfcAdapter.enableForegroundDispatch(this, pendingIntent, filters, techs);
		}
    }
    
    @Override
	protected void onPause() {
    	super.onPause();
		if(identifierIsAvailable())
			mNfcAdapter.disableForegroundDispatch(this);
	}

}
