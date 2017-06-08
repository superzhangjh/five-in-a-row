package com.example.superzhang.five_in_a_row;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by superzhang on 2017/6/8.
 */

public class five_in_a_rowView extends View {

    private final int MAX_LINE = 10;
    private int mPanelWidth;
    private float mLineHeight;
    private float ratioPieceOfLineHeight = 3 * 1.0f /4;

    private Paint mPaint = new Paint();
    private Bitmap mWhitePiece;
    private Bitmap mBlackPiece;

    private List<Point> mWhiteList = new ArrayList<>();
    private List<Point> mBlackList = new ArrayList<>();
    private boolean mIsWhite = true;

    public five_in_a_rowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(0x44FF0000);


        initPaint();
    }

    private void initPaint() {
        mPaint.setAlpha(0);
        //抗锯齿
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = Math.min(widthSize,heightSize);
        if(widthSize == 0){
            width = heightSize;
        }else if(heightSize ==0){
            width = widthSize;
        }

        setMeasuredDimension(width,width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPanelWidth = w;
        mLineHeight = mPanelWidth * 1.0f /MAX_LINE;

        int pieceWidth = (int) (mLineHeight *ratioPieceOfLineHeight);

        mWhitePiece = BitmapFactory.decodeResource(getResources(),R.drawable.stone_w2);
        mBlackPiece = BitmapFactory.decodeResource(getResources(),R.drawable.stone_b2);

        mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece,pieceWidth,pieceWidth,false);
        mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece,pieceWidth,pieceWidth,false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBorad(canvas);
        drawPiece(canvas);
    }

    private void drawPiece(Canvas canvas) {

        for(int i=0,n=mWhiteList.size();i<n;i++) {
            Point whitePoint = mWhiteList.get(i);
            int x = (int) (whitePoint.x - (mLineHeight * ratioPieceOfLineHeight / 2));
            int y = (int) (whitePoint.y - (mLineHeight * ratioPieceOfLineHeight / 2));
            canvas.drawBitmap(mWhitePiece, x, y, null);
        }
        for(int i=0,n=mBlackList.size();i<n;i++){
            Point blackPoint = mBlackList.get(i);
            int x = (int) (blackPoint.x - (mLineHeight * ratioPieceOfLineHeight / 2));
            int y = (int) (blackPoint.y - (mLineHeight * ratioPieceOfLineHeight / 2));
            canvas.drawBitmap(mBlackPiece,x,y,null);
        }
    }

    private void drawBorad(Canvas canvas) {
        float startX = mLineHeight/2;
        float endX = mPanelWidth - mLineHeight/2;
        for(int i=0;i<MAX_LINE;i++){
            canvas.drawLine(startX,startX+i*mLineHeight,endX,startX+i*mLineHeight,mPaint);
            canvas.drawLine(startX+i*mLineHeight,startX,startX+i*mLineHeight,endX,mPaint);
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction()==MotionEvent.ACTION_UP){
            int X = (int) event.getX();
            int Y = (int) event.getY();


            Point p = getValidPoint(X,Y);
            Point p1 = new Point((int)(p.x * mLineHeight + mLineHeight/2),(int)(p.y * mLineHeight + mLineHeight/2));

            if (mWhiteList.contains(p1) || mBlackList.contains(p1))
            {
                return false;
            }


            if (mIsWhite){
                mWhiteList.add(p1);
            }else{
                mBlackList.add(p1);
            }
            invalidate();
            mIsWhite = !mIsWhite;
        }
        return true;
    }

    private Point getValidPoint(int x, int y) {
        return new Point((int) (x/mLineHeight),(int)(y/mLineHeight));
    }
}
