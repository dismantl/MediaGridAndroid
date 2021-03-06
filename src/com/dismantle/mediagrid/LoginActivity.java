package com.dismantle.mediagrid;

import java.util.Date;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginActivity extends ActionBarActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		Button btnOK = (Button)findViewById(R.id.btn_login);
		btnOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				new Thread() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						//String resString=HttpService.doGet("/_session");
						JSONObject res=null;
						try {
							
							Date date=new Date();
							//login test
							res = CouchDB.login("admin", "jtgpgf");
							assert(!res.has("error"));
//							//get session test
							res = CouchDB.getSession();
							assert(!res.has("error"));
//							
							res=CouchDB.getUserDoc("admin");
							assert(!res.has("error"));
							
							
//							res = CouchDB.createFileDocument("dd");
//							assert(!res.has("error"));
//							
//							String id=res.getString("id");
//							String rev=res.getString("rev");
//							
//							res=CouchDB.upload(id, rev, "/ueventd.rc");
//							assert(!res.has("error"));
//							//get file list test
//							res = CouchDB.getFiles(true, true, null);
//							assert(!res.has("error"));
//							//create dir test
//							res = CouchDB.createDir("mytest_"+date.getMinutes()+"_"+date.getSeconds(), null, "DIR", date.toGMTString());
//							assert(!res.has("error"));
//							//register test
//							res = CouchDB.register("test_user_"+date.getMinutes()+"_"+date.getSeconds(), "kkkkkkkk", "user");
//							assert(!res.has("error"));
//							//get user document
//							res = CouchDB.getUserDoc("guoliang");
//							
//							
//							
							Message msg = new Message();
							msg.obj=res;
							msg.what =GlobalUtil.MSG_LOAD_SUCCESS;
							handler.sendMessage(msg);
							//res = CouchDB.createDir("mytest_"+date.getMinutes()+"_"+date.getSeconds(), null, "DIR", date.toGMTString());
						} catch (Exception e) {
							e.printStackTrace(System.err);
						}
						

					}
				}.start();
				
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	private Handler handler= new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GlobalUtil.MSG_LOAD_SUCCESS:
				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
				intent.putExtra("userDoc", msg.obj.toString());
				startActivity(intent);
				break;

			default:
				break;
			}
		};
	};
}
