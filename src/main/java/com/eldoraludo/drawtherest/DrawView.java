package com.eldoraludo.drawtherest;

import java.nio.Buffer;
import java.nio.ByteBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * voir
 * http://stackoverflow.com/questions/2423327/android-view-ondraw-always-has
 * -a-clean-canvas
 * 
 * une autre voie pour le cache serait setDrawingCacheEnabled
 * 
 * @author ludovic
 * 
 */
public class DrawView extends View {

	Paint paint = new Paint();
	DrawingPath drawingPath;
	DrawingPoint drawingPoint;
	private Bitmap bitmap;

	private Canvas canvas;

	private static final String ETAT_DRAWING = "DRAWING";
	private static final String ETAT_DRAW_LINE = "ETAT_DRAW_LINE";
	private static final String ETAT_RESTAURATION_BITMAP = "RESTAURATION_BITMAP";
	private static final String ETAT_JOUEUR_SUIVANT = "ETAT_JOUEUR_SUIVANT";

	private String etat = ETAT_DRAW_LINE;
	private byte[] tabByteImageComplete;

	private byte[] tabBytePartiDuBas;

	public DrawView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// setDrawingCacheEnabled(true);
		// canvas = new Canvas();
		// bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
		// canvas.setBitmap(bitmap);

	}

	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		if (bitmap != null) {
			bitmap.recycle();
		}
		canvas = new Canvas();
		bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		canvas.setBitmap(bitmap);
	}

	@Override
	public void onDraw(Canvas c) {
		if (etat.equals(ETAT_DRAWING)) {
			if (drawingPath != null) {
				canvas.drawPath(drawingPath.path, drawingPath.paint);
			}
			if (drawingPoint != null) {
				// canvas.drawPoint(drawingPoint.x, drawingPoint.y,
				// drawingPoint.paint);
				int tailleOval = 8;
				canvas.drawOval(new RectF(drawingPoint.x - tailleOval,
						drawingPoint.y - tailleOval, drawingPoint.x
								+ tailleOval, drawingPoint.y + tailleOval),
						drawingPoint.paint);
			}
			canvas.drawBitmap(this.bitmap, 0, 0, null);
			c.drawBitmap(this.bitmap, 0, 0, null);
		} else if (etat.equals(ETAT_RESTAURATION_BITMAP)) {
			canvas.drawBitmap(this.bitmap, 0, 0, null);
			c.drawBitmap(this.bitmap, 0, 0, null);
			this.etat = ETAT_DRAWING;
		} else if (etat.equals(ETAT_JOUEUR_SUIVANT)) {
			canvas.drawBitmap(this.bitmap, 0, 0, null);
			drawPathEnBas(canvas);
			drawPathEnHaut(canvas);
			c.drawBitmap(this.bitmap, 0, 0, null);
			this.etat = ETAT_DRAWING;
		} else if (etat.equals(ETAT_DRAW_LINE)) {
			drawPathEnBas(canvas);
			c.drawBitmap(this.bitmap, 0, 0, null);
			this.etat = ETAT_DRAWING;
		}
	}

	private void drawPathEnBas(Canvas canvas_) {
		Path path = new Path();
		path.moveTo(0, 3 * getMeasuredHeight() / 4);
		path.lineTo(getMeasuredWidth(), 3 * getMeasuredHeight() / 4);
		Paint paint = new Paint();
		paint.setDither(true);
		paint.setColor(Color.YELLOW);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeWidth(5);
		canvas_.drawPath(path, paint);
	}

	private void drawPathEnHaut(Canvas canvas_) {
		Path path = new Path();
		path.moveTo(0, 1 * getMeasuredHeight() / 4);
		path.lineTo(getMeasuredWidth(), 1 * getMeasuredHeight() / 4);
		Paint paint = new Paint();
		paint.setDither(true);
		paint.setColor(Color.YELLOW);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeWidth(5);
		canvas_.drawPath(path, paint);
	}

	public void addDrawingPath(DrawingPath currentDrawingPath) {
		this.drawingPath = currentDrawingPath;
		this.invalidate();
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void addDrawingPoint(DrawingPoint currentDrawingPoint) {
		this.drawingPoint = currentDrawingPoint;
		this.invalidate();
	}

	public void resetImage() {
		this.destroy();

	}

	public void destroy() {
		if (bitmap != null) {
			bitmap.recycle();
		}
		canvas = new Canvas();
		bitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(),
				Bitmap.Config.ARGB_8888);
		canvas.setBitmap(bitmap);
		int size = bitmap.getHeight() * bitmap.getRowBytes();
		Buffer buffer_ = ByteBuffer.allocateDirect(size * 4);
		bitmap.copyPixelsToBuffer(buffer_);
		drawingPath = null;
		drawingPoint = null;
		this.etat = ETAT_DRAW_LINE;
		this.invalidate();
	}

	public void sauvegarderImage() {
		// sauvegarde de l'image complete
		tabByteImageComplete = sauvegardeBitmap(bitmap);
		// sauvegarde de la partie du bas
		tabBytePartiDuBas = sauvegardePartieDuBasDeBitmap(bitmap);

		this.destroy();

	}

	private byte[] sauvegardeBitmap(Bitmap bitmap_) {
		byte[] tabByteImageComplete = null;
		int size = bitmap_.getRowBytes() * bitmap_.getHeight();
		ByteBuffer buffer = ByteBuffer.allocate(size * 4);
		bitmap_.copyPixelsToBuffer(buffer);
		tabByteImageComplete = buffer.array();
		return tabByteImageComplete;
	}

	private byte[] sauvegardePartieDuBasDeBitmap(Bitmap bitmap_) {
		Bitmap bitmapSousLigneJaune = Bitmap.createBitmap(bitmap_, 0,
				bitmap_.getHeight() * 3 / 4, bitmap_.getWidth(),
				bitmap_.getHeight() - bitmap_.getHeight() * 3 / 4);
		int size = bitmapSousLigneJaune.getRowBytes()
				* bitmapSousLigneJaune.getHeight();
		ByteBuffer buffer = ByteBuffer.allocate(size * 4);
		bitmapSousLigneJaune.copyPixelsToBuffer(buffer);
		return buffer.array();

	}

	public byte[] getTabByteVierge(int size) {

		ByteBuffer buffer = ByteBuffer.allocate(size * 4);
		return buffer.array();
	}

	public byte[] fusionneTabs(byte[] tab1, byte[] tab2) {
		ByteBuffer bufferAvecPartieBas = ByteBuffer.wrap(tab1);
		bufferAvecPartieBas = ByteBuffer.wrap(tab2);
		return bufferAvecPartieBas.array();
	}

	public void restaurer() {
		byte[] tabByteVierge = getTabByteVierge(bitmap.getRowBytes()
				* bitmap.getHeight());
		byte[] tabByte = fusionneTabs(tabByteVierge, tabBytePartiDuBas);
		ByteBuffer buffer_ = ByteBuffer.wrap(tabByte);
		bitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(),
				Bitmap.Config.ARGB_8888);
		bitmap.copyPixelsFromBuffer(buffer_);
		canvas = new Canvas();
		canvas.setBitmap(bitmap);
		drawingPath = null;
		drawingPoint = null;
		this.etat = ETAT_JOUEUR_SUIVANT;
		this.invalidate();
	}

	public void restaurer(Bitmap bitmapToRestaure) {
		// // draw the preserved image, scaling it to a thumbnail first
		// Bitmap createScaledBitmap =
		// Bitmap.createScaledBitmap(bitmapToRestaure,
		// getMeasuredWidth(), getMeasuredHeight(), true);
		this.etat = ETAT_RESTAURATION_BITMAP;
		// if (this.bitmap != null) {
		// this.bitmap.recycle();
		// }
		// this.bitmap = Bitmap.createBitmap(bitmapToRestaure, 0, 0,
		// bitmapToRestaure.getWidth(), bitmapToRestaure.getHeight(),
		// null, true);
		this.bitmap = Bitmap
				.createScaledBitmap(bitmapToRestaure,
						bitmapToRestaure.getWidth(),
						bitmapToRestaure.getHeight(), true);

		Log.i("**************** est mutable **************", "est mutable "
				+ String.valueOf(this.bitmap.isMutable()));
		// TODO PROBLEME ON NE PEUT PLUS MODIFIER APRES LE DESSIN
		this.invalidate();
	}

	public void envoyer() {
		sauvegarderImage();
		restaurer();
	}

}