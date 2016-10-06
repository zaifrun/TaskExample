package com.example.taskexample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

//The class overrides the AsynTask class
//The parameters giving here, means that it takes as input a String, the 1st
//parameter (which is the URL to download).
//Then last parameter is a Bitmap - to this task will return a bitmap
//The second parameter is void - meaning that it is not used.
class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
    
    private ImageView imageView;
    private TextView progress;

	//In the constructor we get the ImageView to store the bitmap in
	//and the TextView where we write any updates to.
    public BitmapDownloaderTask(ImageView imageView,TextView progress) {
        this.imageView = imageView;
        this.progress = progress;
    }

    
    //This method is run in the UI thread
    //so we can make changes to the UI elements
    @Override
    protected void onProgressUpdate(Void... values) {
    	progress.setText("Progress:Downloading.....");
    	super.onProgressUpdate(values);
    }
    
    //This method is run in the UI thread BEFORE
    // doInBackground, so
    //we can make changes to UI elements
    @Override
    protected void onPreExecute() {
    	progress.setText("Progress: Starting....");
    	super.onPreExecute();
    }
    
    @Override
    // Actual download method, run in the task thread
    // This is NOT run on the UI thread, so we cannot make
    // any changes to ANY UI elements in this method
    protected Bitmap doInBackground(String... params) {
         // params comes from the execute() call: params[0] is the url.
        publishProgress();//publish that we made progress - this
		//will call the onProgressUpdate once.
    	return downloadBitmap(params[0]); //download the URL
    }

    //This is run in the UI thread AFTER doInBackground is
    //finished, so here it is allowed to make changes to
    //any UI elements that we wish to update.
    @Override
    // Once the image is downloaded, associates it to the imageView
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) 
        {
            bitmap = null;
        }
		//we have managed to download the URL
        if (bitmap!=null)
            {
				//update our views - possible because this method is run
				//in the UI thread.
        		imageView.setImageBitmap(bitmap);
                progress.setText("Progress: Finished");

            }      
    }
    
    //Forget about the rest of the code in this class
    //It is just need to setup the HTTP connection and
    //starting the download stream.
    static class FlushedInputStream extends FilterInputStream {
	    public FlushedInputStream(InputStream inputStream) {
	        super(inputStream);
	    }

	    @Override
	    public long skip(long n) throws IOException {
	        long totalBytesSkipped = 0L;
	        while (totalBytesSkipped < n) {
	            long bytesSkipped = in.skip(n - totalBytesSkipped);
	            if (bytesSkipped == 0L) {
	                  int b = read();
	                  if (b < 0) {
	                      break;  // we reached EOF
	                  } else {
	                      bytesSkipped = 1; // we read one byte
	                  }
	           }
	            totalBytesSkipped += bytesSkipped;
	        }
	        return totalBytesSkipped;
	    }
	}
    
    public static Bitmap downloadBitmap(String url) {
	    final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
	    final HttpGet getRequest = new HttpGet(url);

	    try {
	        HttpResponse response = client.execute(getRequest);
	        final int statusCode = response.getStatusLine().getStatusCode();
	        if (statusCode != HttpStatus.SC_OK) { 
	            Log.w("ImageDownloader", "Error " + statusCode + " while retrieving bitmap from " + url);            
	            return null;
	        }
	        
	        final HttpEntity entity = response.getEntity();
	        if (entity != null) {
	            InputStream inputStream = null;
	            try {
	                inputStream = entity.getContent(); 
	                final Bitmap bitmap = BitmapFactory.decodeStream(new FlushedInputStream(inputStream));
	                return bitmap;
	            } finally {
	                if (inputStream != null) {
	                    inputStream.close();  
	                }
	                entity.consumeContent();
	            }
	        }
	    } catch (Exception e) {
	        // Could provide a more explicit error message for IOException or IllegalStateException
	        getRequest.abort();
	        Log.w("ImageDownloader2", "Error while retrieving bitmap from " + url +" ,"+ e.getMessage());
	    } finally {
	        if (client != null) {
	            client.close();
	        }
	    }
	    return null;
	}
}
