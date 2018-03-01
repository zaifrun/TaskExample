package com.example.taskexample;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {


	public static boolean haveInternet(Context ctx) {

		NetworkInfo info = ((ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

		if (info == null || !info.isConnected()) {
			return false;
		}
		if (info.isRoaming()) {
			// here is the roaming option you can change it if you want to
			// disable internet while roaming, just return false
			return true;
		}
		return true;
	}

	//The url of the image that we want to download
	//String url = "http://www.idology.com/wp-content/uploads/2013/12/bigstock-Santa-Claus-show-ok-isolated-o-38669275.jpg";
	String url = "http://www.pondar.dk/bigstock-Santa-Claus-show-ok-isolated-o-38669275.jpg";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	//This is used for creating the menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	//clicklistener for the clear button
	public void clear(View view)
	{
		//Get the progress and imageview.
		TextView text = findViewById(R.id.progress);
		ImageView image = findViewById(R.id.imageView);
		text.setText(R.string.progress);
		//simply set our image bitmap to nothing.
		image.setImageBitmap(null);
	}
	//clicklistener for the download button:
	public void download(View view)
	{
		TextView text = findViewById(R.id.progress);
		ImageView image = findViewById(R.id.imageView);
		//do we have Internet.
		if (haveInternet(this)) {
			//create a new downloader task
			BitmapDownloaderTask task = new BitmapDownloaderTask(image, text);
			//execute our task with the URL as parameter
			task.execute(url);
		} else
		{
			Toast.makeText(this,"You do not have Internet!",Toast.LENGTH_SHORT).show();
		}
	}

	
}
