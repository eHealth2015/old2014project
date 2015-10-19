package enseirb.t3.e_health.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.project.e_health.R;

public class SplashScreen extends Activity {

	/** Durée d'affichage du SplashScreen */
	   protected int _splashTime = 500; 

	   private Thread splashTread;

	   /** Chargement de l'Activity */
	   @Override
	   public void onCreate(Bundle savedInstanceState) 
	   {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.activity_splash_screen);
	      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

	      final SplashScreen sPlashScreen = this; 

	      /** Thread pour l'affichage du SplashScreen */
	      splashTread = new Thread() 
	      {
	         @Override
	         public void run() 
	         {
	            try 
	            {
	                 synchronized(this)
	                 {
	                    wait(_splashTime);
	                 }
	             } catch(InterruptedException e) {} 
	             finally 
	             {
	                finish();
	                Intent i = new Intent();
	                i.setClass(sPlashScreen, AuthentificationActivity.class);
	                startActivity(i);
	             }
	          }
	       };

	       splashTread.start();
	    }
}
