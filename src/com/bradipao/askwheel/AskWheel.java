package com.bradipao.askwheel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.TextView;

public class AskWheel extends Activity {

   // widget handlers
   static TextView txtView;
   static ImageView imgView;
   // wheel animations
   static RotateAnimation rotAni,rotAni2;
   static AnimationSet ani;
   // touch-down holders
   static float py=0,px=0;
   // wheel position and text
   static int pos=0;
   String[] msg = { "Maybe","Yes","Ask a friend","Try again","No way","Sure" };
   
   // called when the activity is first created
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);

      // widget handlers initialization
      txtView = (TextView) findViewById(R.id.txtView);
      imgView = (ImageView) findViewById(R.id.imgView);

      // Animation Listener
      final Animation.AnimationListener mAniListener = new AnimationListener() {
         public void onAnimationStart(Animation animation) {
            // do nothing
         }
         public void onAnimationEnd(Animation animation) {
            txtView.setText(msg[pos]);
         }
         public void onAnimationRepeat(Animation animation) {
            // do nothing
         }
      };
      
      // touch listener on imgView
      View.OnTouchListener mTouchListener = new OnTouchListener() {
         public boolean onTouch(View v, MotionEvent me) {
            
            // if touch down detected
            if (me.getAction()==MotionEvent.ACTION_DOWN) {
               Log.i("WHEEL","TOUCH-DOWN "+me.getX()+","+me.getY());
               rotAni2 = new RotateAnimation(0,0,Animation.RELATIVE_TO_SELF,(float)0.5,Animation.RELATIVE_TO_SELF,(float)0.5);
               imgView.startAnimation(rotAni2);
               px = me.getX();
               py = me.getY();
            }
            
            // if touch up detected
            if (me.getAction()==MotionEvent.ACTION_UP) {

               // calculate duration of spinning
               float nx = me.getX(); 
               float ny = me.getY();
               float imgSize = imgView.getWidth();
               Log.i("WHEEL","TOUCH-UP "+nx+","+ny);
               Log.i("WHEEL","IMGSIZE="+imgSize);
               float dd;
               if ((px<imgSize/2)&(py<imgSize/2)) dd = nx - px;
               else if ((px>=imgSize/2)&(py<imgSize/2)) dd = ny - py;
               else if ((px>=imgSize/2)&(py>=imgSize/2)) dd = px - nx;
               else dd = py - ny;
               long duration = Math.round(dd * 10000 / imgSize);
               Log.i("WHEEL","DURATION="+duration+" DD="+dd);
               if (duration<2000) duration = 2000;

               // calculate rotation degrees
               pos = (Math.round(dd) % 6);
               float degs = 360 * Math.round(duration/1000) + 60 * pos;
               Log.i("WHEEL","DEGS="+degs+" POS="+pos);

               // Create animation
               rotAni = new RotateAnimation(0,degs,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
               rotAni.setInterpolator(new DecelerateInterpolator());
               rotAni.setDuration(duration);
               rotAni.setStartOffset(200);
               rotAni.setFillAfter(true);
               rotAni.setAnimationListener(mAniListener);
               rotAni2 = new RotateAnimation(0,60*((6-pos)%6),Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
               rotAni2.setDuration(1000);
               rotAni2.setStartOffset(duration+2000);
               rotAni2.setFillAfter(true);
               ani = new AnimationSet(false);
               ani.setFillAfter(true);
               ani.addAnimation(rotAni);
               ani.addAnimation(rotAni2);
               imgView.startAnimation(ani);
            }
            
            // if touch move detected
            if (me.getAction()==MotionEvent.ACTION_MOVE) {
               // do nothing
            }
            
            return true;
         }
      };
      // bind touch listener to imgView
      imgView.setOnTouchListener(mTouchListener);   

   }

   // called when menu is invoked
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      super.onCreateOptionsMenu(menu);
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.mainmenu,menu);
      return true;
   }

   // called when a menu item is selected
   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      AlertDialog.Builder builder;
      switch (item.getItemId()) {
      
      // menu LICENSE clicked : show AlertDialog with tiny license info
      case R.id.menu_license:
         builder = new AlertDialog.Builder(this);
         builder.setMessage("This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.");
         builder.setCancelable(false);
         builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
               dialog.cancel();
            }
         });   
         AlertDialog license = builder.create();
         license.show();
         return true;

      // menu VERSION clicked : show AlertDialog with tiny version info
      case R.id.menu_version:
         builder = new AlertDialog.Builder(this);
         builder.setMessage("Ask the Wheel\nVersion 0.1\n - first beta release");
         builder.setCancelable(false);
         builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
               dialog.cancel();
            }
         });   
         AlertDialog version = builder.create();
         version.show();
         return true;
         
      // menu EXIT clicked : exit app
      case R.id.menu_exit:
         finish();
         return true;
      }
      return super.onOptionsItemSelected(item);
   }
   
   


}