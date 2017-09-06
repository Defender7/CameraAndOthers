package demo.optimizel.dn.com.myqqc60.AccessCamera.Camera.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import demo.optimizel.dn.com.myqqc60.R;


public class VerticalTextView extends TextView {

	public final static int DIRECTION_UP_TO_DOWN = 0;
	public final static int DIRECTION_DOWN_TO_UP = 1;
	public final static int DIRECTION_LEFT_TO_RIGHT = 2;
	public final static int DIRECTION_RIGHT_TO_LEFT = 3;

	Rect text_bounds = new Rect();
	private int direction;
	

	public VerticalTextView(Context context) {
		super(context);
		
		invalidate();
	}

	public VerticalTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.verticaltextview);
		direction = a.getInt(R.styleable.verticaltextview_direction, 0);
		a.recycle();
		
		invalidate();
	}
	
	
	public void setText(String text) {
		super.setText(text);
		
		invalidate();
	} 
	
	public void setTextColor(int color) {
		this.getPaint().setColor(color);
		
		invalidate();
	}
	
	//方向
	public void setDirection(int d) {
		this.direction = d;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		getPaint().getTextBounds(getText().toString(), 0, getText().length(),
				text_bounds);
		if (direction == DIRECTION_LEFT_TO_RIGHT
				|| direction == DIRECTION_RIGHT_TO_LEFT) {
			setMeasuredDimension(measureHeight(widthMeasureSpec),
					measureWidth(heightMeasureSpec));
		} else if (direction == DIRECTION_UP_TO_DOWN
				|| direction == DIRECTION_DOWN_TO_UP) {
			setMeasuredDimension(measureWidth(widthMeasureSpec),
					measureHeight(heightMeasureSpec));
		}

	}

	private int measureWidth(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			result = text_bounds.height() + getPaddingTop()
					+ getPaddingBottom();
			// result = text_bounds.height();
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	private int measureHeight(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			result = text_bounds.width() + getPaddingLeft() + getPaddingRight();
			// result = text_bounds.width();
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	@Override
	protected void onDraw(Canvas canvas) {
//		 super.onDraw(canvas);
		
		canvas.save();
		
		int startX = 0;
		int startY = 0;
		
		if (direction == DIRECTION_DOWN_TO_UP) {

			canvas.rotate(-90);
			canvas.translate(-getHeight(), 0);

			startX = (getHeight() - text_bounds.width()) >> 1;
			startY = (getWidth() + text_bounds.height()) >> 1;
			canvas.drawText((String) getText(), startX, startY, getPaint());
			
		} else if(direction == DIRECTION_UP_TO_DOWN) {
			
			canvas.rotate(90);
			canvas.translate(0, -getWidth());

			startX = (getHeight() - text_bounds.width()) >> 1;
			startY = (getWidth() + text_bounds.height()) >> 1;
			canvas.drawText((String) getText(), startX, startY, getPaint());
			
		} else if(direction == DIRECTION_LEFT_TO_RIGHT) {
			
			startX = (getWidth() - text_bounds.width()) >> 1;
			startY = (getHeight() + text_bounds.height()) >> 1;
			canvas.drawText((String) getText(), startX, startY, getPaint());
			
		} else if(direction == DIRECTION_RIGHT_TO_LEFT) {
			
			canvas.rotate(180);
			canvas.translate(-getWidth(), -getHeight());
			
			startX = (getWidth() - text_bounds.width()) >> 1;
			startY = (getHeight() + text_bounds.height()) >> 1;
			canvas.drawText((String) getText(), startX, startY, getPaint());
			
		}
		
		Log.i("chenyg", "ondraw(), getText().toString=" + getText().toString());
		
		canvas.restore();
	}
}
