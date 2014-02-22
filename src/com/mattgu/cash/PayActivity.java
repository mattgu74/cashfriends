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
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PayActivity extends Activity {

	private NfcAdapter mNfcAdapter;
	private EditText mFieldAmount;
	private EditText mFieldDesc;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay);
		
		mFieldDesc = (EditText) findViewById(R.id.desc);
		mFieldAmount = (EditText) findViewById(R.id.amount);
		
		mNfcAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
		if(mNfcAdapter == null) {
            Toast.makeText(this, "NFC n'est pas disponible sur cet équipement", Toast.LENGTH_LONG).show();
		}
		else if(!mNfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC est désactivé", Toast.LENGTH_LONG).show();
		}

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
        
		final ProgressDialog progDialog1 = ProgressDialog.show(this,"Transaction", "Transaction en cours...");
        // Show loading popup
    	progDialog1.show();
    	
    	// Launch request
    	final Api service = ApiClient.getService();

    	int amount = 0;
    	try {
    		amount = Integer.parseInt(mFieldAmount.getText().toString());
    		if(amount > 0) {
				service.postTransaction(id, "", mFieldDesc.getText().toString(), amount, new Callback<Transaction>() {
					

					@Override
					public void success(Transaction transaction, Response arg1) {
						progDialog1.dismiss();
						new AlertDialog.Builder(PayActivity.this)
					    .setTitle("Transaction réussi")
					    .setMessage("Le paiement à réussi.")
					    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int which) { 
					            // continue with delete
					        }
					     })
					     .show();
					}
					
					@Override
					public void failure(RetrofitError arg0) {
						progDialog1.dismiss();
						new AlertDialog.Builder(PayActivity.this)
					    .setTitle("Erreur")
					    .setMessage("Une erreur à eu lieu, vous avez surement plus d'argent.")
					    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int which) { 
					            // continue with delete
					        }
					     })
					     .show();
					}
				});
    		} else {
    			new AlertDialog.Builder(PayActivity.this)
			    .setTitle("Erreur")
			    .setMessage("Vous ne pouvez pas mettre un montant négtif !")
			    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			            // continue with delete
			        }
			     })
			     .show();
    		}
    	} finally {
    		if(findViewById(R.id.emptyForm).isSelected()) {
    			mFieldDesc.setText("");
    			mFieldAmount.setText("");
    		}
    	}
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
