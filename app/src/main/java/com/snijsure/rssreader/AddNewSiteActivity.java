package com.snijsure.rssreader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.snijsure.rssreader.model.RSSDatabaseHandler;
import com.snijsure.rssreader.model.WebSite;

import java.util.ArrayList;

public class AddNewSiteActivity extends Activity {

	Button btnSubmit;
	Button btnCancel;
	EditText txtUrl;
	TextView lblMessage;
    static String Url;
	private ProgressDialog pDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_site);

		// buttons
		btnSubmit = (Button) findViewById(R.id.btnSubmit);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		txtUrl = (EditText) findViewById(R.id.txtUrl);
		lblMessage = (TextView) findViewById(R.id.lblMessage);

		// Submit button click event
		btnSubmit.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				String url = txtUrl.getText().toString();
				Url = url;
				// Validation url
				Log.d("URL Length", "" + url.length());
				// check if user entered any data in EditText
				if (url.length() > 0) {
					lblMessage.setText("");
					String urlPattern = "^http(s{0,1})://[a-zA-Z0-9_/\\-\\.]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%]*";
					//boolean isURL = Patterns.WEB_URL.matcher(url).matches();
					if (url.matches(urlPattern)) {
						startService(url);
						finish();

					} else {

						lblMessage.setText("Please enter a valid url");
					}
				} else {
					lblMessage.setText("Please enter website url");
				}

			}
		});

		btnCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void startService(String url) {
		ArrayList<String> urls = new ArrayList<>();
		urls.add(url);
		Intent intent = new Intent(getApplicationContext(), RssService.class);
		intent.putExtra(RssService.URL, urls);
		getApplicationContext().startService(intent);
	}

}
