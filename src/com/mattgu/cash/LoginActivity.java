package com.mattgu.cash;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import com.mattgu.cash.api.Api;
import com.mattgu.cash.api.ApiClient;
import com.mattgu.cash.models.User;
import com.mattgu.cash.models.UserTransactions;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {
	
	private NfcAdapter mNfcAdapter;
	private EditText mFieldMail;
	private EditText mFieldBadge;
	private EditText mFieldPassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		mFieldBadge = (EditText) findViewById(R.id.badge);
		mFieldMail = (EditText) findViewById(R.id.email);
		mFieldPassword = (EditText) findViewById(R.id.password);
		mFieldMail.setVisibility(View.GONE);
		
		TextView textLogin = (TextView) findViewById(R.id.loginText);
		
		textLogin.setVisibility(View.GONE);
		findViewById(R.id.badge).setVisibility(View.GONE);
		
		mNfcAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
		if(mNfcAdapter == null) {
            Toast.makeText(this, "NFC n'est pas disponible sur cet équipement", Toast.LENGTH_LONG).show();
		}
		else if(!mNfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC est désactivé", Toast.LENGTH_LONG).show();
		} else {
			//textLogin.setText("Vous pouvez soit scanner votre badge, soit tapper votre adresse email.");
			//textLogin.setVisibility(View.VISIBLE);
			findViewById(R.id.badge).setVisibility(View.VISIBLE);
		}
		
		//Bundle b = getIntent().getExtras();
		//final int nextAction = b.getInt("action");
		
		View.OnClickListener handler = new View.OnClickListener(){
			public void onClick(View v) {
				/*Intent intentMain = null;
				switch(nextAction) {
				case 1:
					intentMain = new Intent(LoginActivity.this , 
	                        ReloadActivity.class);
					break;
				case 2:
					intentMain = new Intent(LoginActivity.this , 
	                        PayActivity.class);
					break;
				case 3:
					intentMain = new Intent(LoginActivity.this , 
	                        AccountActivity.class);
					break;
				}*/
				Intent intentMain = new Intent(LoginActivity.this , 
                        MainActivity.class);
				tryToLogin(intentMain);
			}
		};
		findViewById(R.id.sign_in_button).setOnClickListener(handler);
		
	}
	
    protected void tryToLogin(final Intent nextIntent) {
    	// Show loading popup
    	final ProgressDialog progDialog = ProgressDialog.show(this,"Authentification", "Vérification en cours...");
    	
    	// Launch request
    	final Api service = ApiClient.getService();
		
		service.login(mFieldBadge.getText().toString(), mFieldPassword.getText().toString(), new Callback<User>() {
			
			@Override
			public void success(User user, Response arg1) {
				Log.i("coucou", ""+user);
				LoginActivity.this.startActivity(nextIntent);
			}
			
			@Override
			public void failure(RetrofitError arg0) {
				progDialog.dismiss();
				Toast.makeText(LoginActivity.this, "Erreur !", Toast.LENGTH_LONG).show();
				mFieldBadge.setText("");
				mFieldPassword.setText("");
			}
		});
    	
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
