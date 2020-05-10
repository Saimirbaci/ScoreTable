package com.saimirbaci.PesKatshTabel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class Splash extends Activity {


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash);

        final int []imageArray={R.drawable.king_of_spades,
                R.drawable.k_spa_q_hea,
                R.drawable.k_spa_q_hea_q_spa,
                R.drawable.k_spa_q_hea_q_spa_ace_spa};

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int i=0;
            public void run() {
                ImageView iv = (ImageView) findViewById(R.id.king_of_spades);

                iv.setImageResource(imageArray[i]);

                if(i<imageArray.length-1)
                {
                    i++;
                }
                handler.postDelayed(this, 300*i);  //for interval...
            }
        };
        handler.postDelayed(runnable, 0); //for initial delay..

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(Splash.this,Main.class);
                Splash.this.startActivity(mainIntent);
                Splash.this.finish();
            }
        }, Constants.SPLASH_DISPLAY_LENGTH);
    }

}