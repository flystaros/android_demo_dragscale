package com.example.drag;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.util.FloatMath;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private ImageView imageView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		imageView = (ImageView) this.findViewById(R.id.imageView);
		
		imageView.setOnTouchListener(new TouchListener());
		
	}
	
	
	private final class TouchListener implements OnTouchListener
	{
		private PointF startpoint = new PointF();
		private Matrix matrix = new Matrix();
		private Matrix currentMatrix = new Matrix();
		private int mode = 0;
		private static final int DRAG = 1;
		private static final int ZOOM = 2;
		private float startDis;
		private float endDis;
		private PointF midPoint;
		@Override
		public boolean onTouch(View v, MotionEvent event) 
		{
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				mode = DRAG;
				currentMatrix.set(imageView.getImageMatrix());  //每次按下的时候记录curentMatrix
				startpoint.set(event.getX(), event.getY());    //得到开始点的位置
				
				break;
			case MotionEvent.ACTION_MOVE:
				if(mode == DRAG)
				{
					//得到X方向上移动的的距离
					float dx = event.getX() - startpoint.x;
					//得到Y方向上移动的距离
					float dy = event.getY() - startpoint.y;
					//开始移动，如何移动？  实用Matrix
					matrix.set(currentMatrix);
					matrix.postTranslate(dx, dy);  // 每次移动都要记录一下当前的位置
				}
				else if(mode == ZOOM)
				{
					endDis = distance(event);
					if(endDis > 10f)
					{
						float scale = endDis / startDis;  //得到缩放比
						matrix.set(currentMatrix);
						matrix.postScale(scale, scale, midPoint.x, midPoint.y);
					}
				}
				break;
				
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				mode = 0;
				break;
				
			case MotionEvent.ACTION_POINTER_DOWN:
				mode = ZOOM;
				startDis = distance(event);  //获取两点初始距离
				if(startDis > 10f)
				{
					midPoint = mid(event);
					currentMatrix.set(imageView.getImageMatrix());
				}
				break;

			default:
				break;
			}
			imageView.setImageMatrix(matrix);
			return true;
		}
		

		private PointF mid(MotionEvent event) 
		{
			float midX = (event.getX(1)+event.getX(0))/2;
			float midY = (event.getY(1)+event.getY(0))/2;
			
			return new PointF(midX, midY);
		}


		/**
		 * 计算两个点之间的初始距离 
		 * @param event
		 * @return
		 */
		private float distance(MotionEvent event)
		{
			float dx = event.getX(1) - event.getX(0);
			float dy = event.getY(1) - event.getY(0);
			return FloatMath.sqrt(dx*dx+dy*dy);
		}
		
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

}
