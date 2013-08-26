package com.eldoraludo.drawtherest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SettingsActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		final TextView nombreDeJoueurTxt = (TextView) findViewById(R.id.nombreDeJoueurTxt);

		final Button valierNombreDeJoueurBt = (Button) findViewById(R.id.valierNombreDeJoueurBt);
		valierNombreDeJoueurBt.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent drawIntent = new Intent(SettingsActivity.this,
						DrawingActivityWitView.class);
				Bundle bundle = new Bundle();
				bundle.putString("nombreDeJoueur", nombreDeJoueurTxt.getText()
						.toString());
				drawIntent.putExtras(bundle);
				startActivity(drawIntent);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.activity_draw_the_rest, menu);
		return true;
	}
}
