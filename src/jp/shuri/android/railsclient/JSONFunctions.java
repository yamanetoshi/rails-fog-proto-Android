package jp.shuri.android.railsclient;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class JSONFunctions {
	public static String getHTTPResponseBodyString(HttpUriRequest request,
			DefaultHttpClient httpclient)
		throws IOException, ClientProtocolException, RuntimeException {
		return httpclient.execute(request,
				new ResponseHandler<String>() {

			@Override
			public String handleResponse(HttpResponse response)
					throws ClientProtocolException, IOException {
				switch (response.getStatusLine().getStatusCode()) {
				case HttpStatus.SC_OK:
				case HttpStatus.SC_UNAUTHORIZED:
					return EntityUtils.toString(response.getEntity(), "UTF-8");
				default:
					throw new RuntimeException("HTTP Status is " + 
								   response.getStatusLine().getStatusCode());
				}
			}
		});
	}
	
    public static String GETfromURL(String url, DefaultHttpClient httpclient)
    	throws IOException, ClientProtocolException, RuntimeException {
    	
        HttpGet request = new HttpGet(url);
        return getHTTPResponseBodyString(request, httpclient);
    }
    
    public static String POSTfromURL(String url, DefaultHttpClient httpclient, 
    		List<NameValuePair> params) 
    				throws IOException, ClientProtocolException, RuntimeException {
    	
    	HttpPost request = new HttpPost(url);
    	request.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
    	return getHTTPResponseBodyString(request, httpclient);
    }
    
    public static String PUTfromURL(String url, DefaultHttpClient httpclient, 
    		List<NameValuePair> params) 
    				throws IOException, ClientProtocolException, RuntimeException {
    	
    	HttpPut request = new HttpPut(url);
    	request.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
    	return getHTTPResponseBodyString(request, httpclient);
    }

    public static String DELETEfromURL(String url, DefaultHttpClient httpclient)
    				throws IOException, ClientProtocolException, RuntimeException {
		return httpclient.execute(new HttpDelete(url),
				new ResponseHandler<String>() {

			@Override
			public String handleResponse(HttpResponse response)
					throws ClientProtocolException, IOException {
				switch (response.getStatusLine().getStatusCode()) {
				case HttpStatus.SC_OK:
					return null;
				default:
					throw new RuntimeException("HTTP Status is " + 
								   response.getStatusLine().getStatusCode());
				}
			}
		});

    }
}
