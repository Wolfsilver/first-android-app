package com.example.test.myapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.test.myapp.camera.CameraInterface;
import com.example.test.myapp.camera.preview.CameraSurfaceView;
import com.example.test.myapp.util.DisplayUtil;

public class MyFloatViewActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    private WindowManager wm = null;
    private WindowManager.LayoutParams wmParams = null;

    private MyFloatView myFV = null;

    private CameraSurfaceView cameraFV = null;


    ImageButton shutterBtn;
    float previewRate = -1f;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("w", "MyFloatViewActivity in...");
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_float_view);
        setContentView(R.layout.activity_camera);
        //创建悬浮窗口
        createView();

    }


    private void createView() {
        myFV = new MyFloatView(getApplicationContext());
        myFV.setImageResource(R.drawable.btn_camera_all);  //这里简单的用自带的Icom来做演示

//        cameraFV = (CameraSurfaceView) findViewById(R.id.camera_surfaceview);

        initUI();
        initViewParams();


        //获取WindowManager
        wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        //设置LayoutParams(全局变量）相关参数
        wmParams = ((MyApplication) getApplication()).getMywmParams();

        /**
         *以下都是WindowManager.LayoutParams的相关属性
         * 具体用途可参考SDK文档
         */
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;   //设置window type
        wmParams.format = PixelFormat.TRANSPARENT;   //设置图片格式，效果为背景透明

        //设置Window flag
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        /*
         * 下面的flags属性的效果形同“锁定”。
         * 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
         wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL
                               | LayoutParams.FLAG_NOT_FOCUSABLE
                               | LayoutParams.FLAG_NOT_TOUCHABLE;
        */


        wmParams.gravity = Gravity.LEFT | Gravity.TOP;   //调整悬浮窗口至左上角，便于调整坐标
        //以屏幕左上角为原点，设置x、y初始值
        wmParams.x = 0;
        wmParams.y = 0;

        //设置悬浮窗口长宽数据
        wmParams.width = 300;
        wmParams.height = 300;

        //显示myFloatView图像
//        wm.addView(myFV, wmParams);

        ViewGroup p = (ViewGroup)cameraFV.getParent();
        p.removeView(cameraFV);

        if (null == cameraFV) {
            Toast.makeText(getApplicationContext(), "对象为空", Toast.LENGTH_SHORT).show();
        } else {
            wm.addView(cameraFV, wmParams);
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在程序退出(Activity销毁）时销毁悬浮窗口
//        wm.removeView(myFV);
        if (null != cameraFV){
            wm.removeView(cameraFV);
        }
    }


    private void initUI() {
        cameraFV = (CameraSurfaceView) findViewById(R.id.camera_surfaceview);
        shutterBtn = (ImageButton) findViewById(R.id.btn_shutter);
    }



    private void initViewParams() {
        ViewGroup.LayoutParams params = cameraFV.getLayoutParams();
        Point p = DisplayUtil.getScreenMetrics(this);
        params.width = p.x;
        params.height = p.y;
        previewRate = DisplayUtil.getScreenRate(this); //默认全屏的比例预览
        cameraFV.setLayoutParams(params);

        //手动设置拍照ImageButton的大小为120dip×120dip,原图片大小是64×64
        ViewGroup.LayoutParams p2 = shutterBtn.getLayoutParams();
        p2.width = DisplayUtil.dip2px(this, 50);
        p2.height = DisplayUtil.dip2px(this, 50);
        shutterBtn.setLayoutParams(p2);

    }

    /**
     * button按钮事件
     * @param view
     */
    public void click(View view){
        Log.d("d", "button...");
        CameraInterface.getInstance().doTakePicture();
    }
}
