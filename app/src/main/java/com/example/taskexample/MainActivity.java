package com.example.taskexample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends Activity {
	
	//The url of the image that we want to download
	//String url = "http://www.idology.com/wp-content/uploads/2013/12/bigstock-Santa-Claus-show-ok-isolated-o-38669275.jpg";
	String url = "http://www.pondar.dk/bigstock-Santa-Claus-show-ok-isolated-o-38669275.jpg";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	//clicklistener for the clear button
	public void clear(View view)
	{
		TextView text = (TextView) findViewById(R.id.progress);
		ImageView image = (ImageView) findViewById(R.id.imageView);
		text.setText("Progress:");
		//simply set our image bitmap to nothing.
		image.setImageBitmap(null);
	}
	//clicklistener for the download button:
	public void download(View view)
	{
		TextView text = (TextView) findViewById(R.id.progress);
		ImageView image = (ImageView) findViewById(R.id.imageView);
		//create a new downloader task
		BitmapDownloaderTask task = new BitmapDownloaderTask(image, text);
		//execute our task with the URL as parameter
		task.execute(url);
	}

	
}
