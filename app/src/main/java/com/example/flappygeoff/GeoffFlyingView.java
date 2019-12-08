package com.example.flappygeoff;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;

public class GeoffFlyingView extends View {
    private Bitmap geoff[] = new Bitmap[2] ;
    private int geoffX = 120;
    private int geoffY;
    private boolean touchTim = false;
    private int geoffSpeed;

    private int score;
    private int keyBoardX = -4000, keyBoardY = -4000, keyBoardSpeed = 16;
    private int highBoardX = -4000, highBoardY = -4000;
    private Bitmap keyBoard;

    private int canvasWidth, canvasHeight;
    private Bitmap background;
    private Paint scorePaint = new Paint();

    public GeoffFlyingView(Context tim) {
        super(tim);

        geoff[0] = BitmapFactory.decodeResource(getResources(), R.drawable.geoff_in_main);
        geoff[1] = BitmapFactory.decodeResource(getResources(), R.drawable.geoff_in_main);

        keyBoard = BitmapFactory.decodeResource(getResources(), R.drawable.keyboard);

        background = BitmapFactory.decodeResource(getResources(), R.drawable.background_real);

        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(70);
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint.setAntiAlias(true);

        geoffY = 550;
        score = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvasHeight = canvas.getHeight();
        canvasWidth = canvas.getWidth();
        canvas.drawBitmap(background, 0, 0, null);

        int minGeoffY = 0;
        int maxGeoffY = canvasHeight - geoff[0].getHeight();

        if (geoffY < minGeoffY) {
            geoffY = minGeoffY;
        } else if (geoffY > maxGeoffY) {
            geoffY = maxGeoffY;
        }

        geoffY += geoffSpeed;

        geoffSpeed += 2;

        keyBoardX -= keyBoardSpeed;
        highBoardX -= keyBoardSpeed;

        if (hit(geoffY, keyBoardX, keyBoardY, highBoardX, highBoardY)) {
            Intent gameOverIntent = new Intent(getContext(), GameOver.class);
            gameOverIntent.putExtra("score", score);
            gameOverIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            getContext().startActivity(gameOverIntent);
            geoffX = -4000;
        } else {
            score += 1;
        }

        if (keyBoardX < 0 - keyBoard.getWidth()) {
            keyBoardX = canvasWidth + 21;
            keyBoardY = (int) Math.floor(Math.random() * (maxGeoffY - minGeoffY)) + geoff[0].getHeight() + minGeoffY + 60;
        }
        if (highBoardX < 0 - keyBoard.getWidth()) {
            highBoardX = canvasWidth + 21;
            highBoardY = keyBoardY - keyBoard.getHeight() - geoff[0].getHeight() - 320;
        }

        canvas.drawBitmap(keyBoard, keyBoardX, keyBoardY, null);
        canvas.drawBitmap(keyBoard, highBoardX, highBoardY, null);

        if (touchTim) {
            canvas.drawBitmap(geoff[0], geoffX, geoffY, null);
            touchTim = false;
        } else {
            canvas.drawBitmap(geoff[0], geoffX, geoffY, null);
        }
        canvas.drawText("Time : " + score + " ms", 20, 80, scorePaint);
    }

    public boolean hit(int geoffY, int keyBoardX, int keyBoardY, int highBoardX, int highBoardY){
        Rect geoffHead = new Rect(geoffX, geoffY, geoffX + geoff[0].getWidth(), geoffY + geoff[0].getHeight());
        Rect highKeyboard = new Rect(highBoardX, highBoardY, highBoardX + keyBoard.getWidth(), highBoardY + keyBoard.getHeight());
        Rect lowKeyboard = new Rect(keyBoardX, keyBoardY, keyBoardX + keyBoard.getWidth(), keyBoardY + keyBoard.getHeight());

        return geoffHead.intersect(highKeyboard) || geoffHead.intersect(lowKeyboard);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            touchTim = true;
            geoffSpeed = -28;
        }
        return true;
    }

}
