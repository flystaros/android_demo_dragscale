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
				currentMatrix.set(imageView.getImageMatrix());  //ÿ�ΰ��µ�ʱ���¼curentMatrix
				startpoint.set(event.getX(), event.getY());    //�õ���ʼ���λ��
				
				break;
			case MotionEvent.ACTION_MOVE:
				if(mode == DRAG)
				{
					//�õ�X�������ƶ��ĵľ���
					float dx = event.getX() - startpoint.x;
					//�õ�Y�������ƶ��ľ���
					float dy = event.getY() - startpoint.y;
					//��ʼ�ƶ�������ƶ���  ʵ��Matrix
					matrix.set(currentMatrix);
					matrix.postTranslate(dx, dy);  // ÿ���ƶ���Ҫ��¼һ�µ�ǰ��λ��
				}
				else if(mode == ZOOM)
				{
					endDis = distance(event);
					if(endDis > 10f)
					{
						float scale = endDis / startDis;  //�õ����ű�
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
				startDis = distance(event);  //��ȡ�����ʼ����
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
		 * ����������֮��ĳ�ʼ���� 
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
