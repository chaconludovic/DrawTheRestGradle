package com.eldoraludo.drawtherest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class DrawTheRest extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_draw_the_rest);
		final Button commencerPartie = (Button) findViewById(R.id.commencerPartie);

		commencerPartie.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Intent drawIntent = new Intent(this, DrawingActivity.class);
				// startActivity(drawIntent);
				// Intent drawIntent = new Intent(DrawTheRest.this,
				// SettingsActivity.class);
				// startActivity(drawIntent);
				Intent drawIntent = new Intent(DrawTheRest.this,
						DrawingActivityWitView.class);
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
