package com.steps.networking;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.steps.networking.OnFinishListener;


public class CursorAsyncTask extends
		AsyncTask<String, Integer, Cursor> {
//	private static final String SERVER_ADDRESS = "http://192.168.1.102:1337";
	private static final String SERVER_ADDRESS = "http://10.0.2.2:1337";
//	private static final String SERVER_ADDRESS = "http://192.168.80.37:1337";
	private Uri uri;
	private OnFinishListener onFinishListener;
	private String[] columnNames;
	
	private ErrorCode err;

	public CursorAsyncTask(Uri uri, String[] columnNames,
			OnFinishListener onFinishListener) {
		super();
		this.uri = uri;
		this.onFinishListener = onFinishListener;
		this.columnNames = columnNames;
	}

	@Override
	protected Cursor doInBackground(String... params) {
		Uri server = Uri.parse(SERVER_ADDRESS);
		server = Uri.withAppendedPath(server, uri.toString());
		Log.d("BLA", server.toString());
		try {
			URL url = new URL(server.toString());
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			InputStream in = new BufferedInputStream(
					connection.getInputStream());

			Reader reader = new InputStreamReader(in);

			BufferedReader br = new BufferedReader(reader);
			StringBuilder sb = new StringBuilder();
			while (true) {
				String str = br.readLine();
				if (str == null)
					break;
				sb.append(str);
			}

			JSONObject jsonObject = new JSONObject(sb.toString());
			
			if(jsonObject.has("err")){
				if(jsonObject.getString("err").equals("ER_DUP_ENTRY"))
					err = ErrorCode.ER_DUP_ENTRY;
				else
					err = ErrorCode.SERVER_ERROR;
			}else{
				if(columnNames == null) return null;
				
				JSONArray jsonArray = jsonObject.getJSONArray("rows");
				int numRows = jsonObject.getInt("length");
				MatrixCursor mc = new MatrixCursor(columnNames, numRows);

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jo = jsonArray.getJSONObject(i);
					// extract the properties from the JSONObject and use it
					// with the addRow() method below
					mc.addRow(getRow(jo, columnNames));
				}
				return mc;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			err = ErrorCode.SERVER_ERROR;
		} catch (IOException e) {
			e.printStackTrace();
			err = ErrorCode.SERVER_ERROR;
		} catch (JSONException e) {
			e.printStackTrace();
			err = ErrorCode.SERVER_ERROR;
		}
		return null;
	}

	private String[] getRow(JSONObject jo, String[] columnNames)
			throws JSONException {
		String[] arr = new String[columnNames.length];
		for (int i = 0; i < columnNames.length; i++)
			arr[i] = jo.getString(columnNames[i]);
		return arr;
	}

	@Override
	protected void onPostExecute(Cursor result) {
		if (onFinishListener != null)
			onFinishListener.OnFinish(result, err);
	}
}
