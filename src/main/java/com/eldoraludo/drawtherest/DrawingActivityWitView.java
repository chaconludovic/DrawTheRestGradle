package com.eldoraludo.drawtherest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

public class DrawingActivityWitView extends Activity implements OnTouchListener {
	private DrawView drawView;
	private DrawingPath currentDrawingPath;
	private DrawingPoint currentDrawingPoint;
	private Paint currentPaint;
	private int stylePen = 0;
	private static final String EXTERNAL_SD_TEMP_PATH = "external_sd/Temp";
	private static final String EXTERNAL_SD_TEMP_MY_AWESOME_DRAWING_PNG = "myAwesomeDrawing.png";
	private int nombreDeJoueur = 0;
	private int numeroJoueurCourant;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle bundle = getIntent().getExtras();
		try {
			nombreDeJoueur = Integer
					.valueOf(bundle.getString("nombreDeJoueur"));
		} catch (Throwable t) {
		}
		numeroJoueurCourant = 1;

		setContentView(R.layout.activity_draw_the_rest_with_view);
		setCurrentPaint();

		final TextView joueurLb = (TextView) findViewById(R.id.joueurLb);
		joueurLb.setText("Joueur " + numeroJoueurCourant + " sur "
				+ nombreDeJoueur);

		drawView = (DrawView) findViewById(R.id.drawView);

		drawView.setOnTouchListener(this);

		final Button colorBlueBtn = (Button) findViewById(R.id.colorBlueBtn);
		colorBlueBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				actionButton(colorBlueBtn);
			}
		});
		final Button colorGreenBtn = (Button) findViewById(R.id.colorGreenBtn);
		colorGreenBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				actionButton(colorGreenBtn);
			}
		});
		final Button colorRedBtn = (Button) findViewById(R.id.colorRedBtn);
		colorRedBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				actionButton(colorRedBtn);
			}
		});
		final Button envoyer = (Button) findViewById(R.id.envoyer);
		envoyer.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				actionButton(envoyer);
			}
		});
		final Button boutonNouvellePartie = (Button) findViewById(R.id.boutonNouvellePartie);
		boutonNouvellePartie.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				actionButton(boutonNouvellePartie);
			}
		});
		final Button swithchStylePen = (Button) findViewById(R.id.swithchStylePen);
		swithchStylePen.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				actionButton(swithchStylePen);
			}
		});

		final Button restaurer = (Button) findViewById(R.id.restaurer);
		restaurer.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				actionButton(restaurer);
			}
		});

		final Button recharger = (Button) findViewById(R.id.recharger);
		recharger.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				actionButton(recharger);
			}
		});
		final Button sauvegarder = (Button) findViewById(R.id.sauvegarder);
		sauvegarder.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				actionButton(sauvegarder);
			}
		});
	}

	private void setCurrentPaint() {
		currentPaint = new Paint();
		currentPaint.setDither(true);
		currentPaint.setColor(Color.GREEN);
		currentPaint.setStyle(Paint.Style.STROKE);
		currentPaint.setStrokeJoin(Paint.Join.ROUND);
		currentPaint.setStrokeCap(Paint.Cap.ROUND);
		currentPaint.setStrokeWidth(5);
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent motionEvent) {
		if (stylePen == 0) {
			if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
				currentDrawingPath = new DrawingPath();
				currentDrawingPath.paint = currentPaint;
				currentDrawingPath.path = new Path();
				currentDrawingPath.path.moveTo(motionEvent.getX(),
						motionEvent.getY());
				currentDrawingPath.path.lineTo(motionEvent.getX(),
						motionEvent.getY());
				Log.i("DrawingActivity - onTouch ACTION_DOWN",
						motionEvent.getX() + " " + motionEvent.getY());
			} else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
				currentDrawingPath.path.lineTo(motionEvent.getX(),
						motionEvent.getY());
				drawView.addDrawingPath(currentDrawingPath);
				Log.i("DrawingActivity - onTouch ACTION_MOVE",
						motionEvent.getX() + " " + motionEvent.getY());
			} else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
				currentDrawingPath.path.lineTo(motionEvent.getX(),
						motionEvent.getY());
				drawView.addDrawingPath(currentDrawingPath);
				Log.i("DrawingActivity - onTouch ACTION_UP", motionEvent.getX()
						+ " " + motionEvent.getY());
			}
		} else {
			if (motionEvent.getAction() == MotionEvent.ACTION_DOWN
					|| motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
				currentDrawingPoint = new DrawingPoint();
				Paint qc = new Paint(currentPaint);
				qc.setStyle(Style.FILL);
				currentDrawingPoint.paint = qc;
				currentDrawingPoint.x = motionEvent.getX();
				currentDrawingPoint.y = motionEvent.getY();

				Log.i("DrawingActivity - onTouch ACTION_DOWN",
						motionEvent.getX() + " " + motionEvent.getY());
				drawView.addDrawingPoint(currentDrawingPoint);

			}
		}

		return true;
	}

	private void actionButton(View view) {
		Log.i("DrawingActivity - onClick", "click");
		switch (view.getId()) {
		case R.id.swithchStylePen:
			if (stylePen == 0) {
				stylePen = 1;
			} else {
				stylePen = 0;
			}
			break;
		case R.id.boutonNouvellePartie:
			drawView.resetImage();
			break;
		case R.id.colorRedBtn:
			currentPaint = new Paint();
			currentPaint.setDither(true);
			currentPaint.setColor(Color.RED);
			currentPaint.setStyle(Paint.Style.STROKE);
			currentPaint.setStrokeJoin(Paint.Join.ROUND);
			currentPaint.setStrokeCap(Paint.Cap.ROUND);
			currentPaint.setStrokeWidth(3);
			Log.i("DrawingActivity - onClick", "Red");
			break;
		case R.id.colorBlueBtn:
			currentPaint = new Paint();
			currentPaint.setDither(true);
			currentPaint.setColor(Color.BLUE);
			currentPaint.setStyle(Paint.Style.STROKE);
			currentPaint.setStrokeJoin(Paint.Join.ROUND);
			currentPaint.setStrokeCap(Paint.Cap.ROUND);
			currentPaint.setStrokeWidth(3);
			Log.i("DrawingActivity - onClick", "Blue");
			break;
		case R.id.colorGreenBtn:
			currentPaint = new Paint();
			currentPaint.setDither(true);
			currentPaint.setColor(Color.GREEN);
			currentPaint.setStyle(Paint.Style.STROKE);
			currentPaint.setStrokeJoin(Paint.Join.ROUND);
			currentPaint.setStrokeCap(Paint.Cap.ROUND);
			currentPaint.setStrokeWidth(3);
			Log.i("DrawingActivity - onClick", "Green");
			break;
		case R.id.envoyer:
			drawView.envoyer();
			break;
		case R.id.sauvegarder:
			final Activity currentActivity = this;
			Handler saveHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					final AlertDialog alertDialog = new AlertDialog.Builder(
							currentActivity).create();
					alertDialog.setTitle("Saved 1");
					alertDialog.setMessage("Your drawing had been saved :)");
					alertDialog.setButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									return;
								}
							});
					alertDialog.show();
				}
			};
			new ExportBitmapToFile(this, saveHandler, drawView.getBitmap())
					.execute();
			break;
		case R.id.restaurer:
			drawView.restaurer();
			break;
		case R.id.recharger:
			Bitmap recharger = recharger();
			drawView.restaurer(recharger);
			break;
		}
	}

	private class ExportBitmapToFile extends AsyncTask<Intent, Void, Boolean> {

		private Context mContext;
		private Handler mHandler;
		private Bitmap nBitmap;

		public ExportBitmapToFile(Context context, Handler handler,
				Bitmap bitmap) {
			mContext = context;
			nBitmap = bitmap;
			mHandler = handler;
		}

		@Override
		protected Boolean doInBackground(Intent... arg0) {
			try {
				if (!new File(EXTERNAL_SD_TEMP_PATH).exists()) {
					new File(EXTERNAL_SD_TEMP_PATH).mkdirs();
				}
				File file = new File(Environment.getExternalStorageDirectory()
						.toString(), EXTERNAL_SD_TEMP_PATH + "/"
						+ EXTERNAL_SD_TEMP_MY_AWESOME_DRAWING_PNG);
				FileOutputStream out = new FileOutputStream(file);
				nBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
				out.flush();
				out.close();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean bool) {
			super.onPostExecute(bool);
			if (bool) {
				mHandler.sendEmptyMessage(1);
			}
		}
	}

	public boolean isSdReadable() {

		boolean mExternalStorageAvailable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			mExternalStorageAvailable = true;
			Log.i("isSdReadable", "External storage card is readable.");
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			Log.i("isSdReadable", "External storage card is readable.");
			mExternalStorageAvailable = true;
		} else {
			// Something else is wrong. It may be one of many other
			// states, but all we need to know is we can neither read nor write
			mExternalStorageAvailable = false;
		}

		return mExternalStorageAvailable;
	}

	public Bitmap recharger() {

		String fullPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/" + EXTERNAL_SD_TEMP_PATH;
		Bitmap thumbnail = null;

		// Look for the file on the external storage
		try {
			if (isSdReadable() == true) {
				thumbnail = BitmapFactory.decodeFile(fullPath + "/"
						+ EXTERNAL_SD_TEMP_MY_AWESOME_DRAWING_PNG);
			}
		} catch (Exception e) {
			Log.e("getThumbnail() on external storage", e.getMessage());
		}

		// If no file on external storage, look in internal storage
		if (thumbnail == null) {
			try {

				File filePath = getBaseContext().getFileStreamPath(
						EXTERNAL_SD_TEMP_MY_AWESOME_DRAWING_PNG);
				FileInputStream fi = new FileInputStream(filePath);
				thumbnail = BitmapFactory.decodeStream(fi);
			} catch (Exception ex) {
				Log.e("getThumbnail() on internal storage", ex.getMessage());
			}
		}
		return thumbnail;
	}
}