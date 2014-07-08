package com.dismantle.mediagrid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;

import com.dismantle.mediagrid.RTPullListView.OnRefreshListener;

public class ChooseMemberActivity extends Activity {
	private RTPullListView pullListView;

	List<Map<String, Object>> datalist = null;
	// private List<String> dataList;
	private SimpleAdapter adapter;
	
	public ChooseMemberActivity() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_list);
		pullListView = (RTPullListView) this.findViewById(R.id.member_list);

		datalist = new ArrayList<Map<String, Object>>();

		// adapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1, dataList);
		adapter = new SimpleAdapter(this, datalist,
				R.layout.member_list_item, 
				new String[] {"member_name"}, 
				new int[] {R.id.member_name});
		// setListAdapter(adapter);
		pullListView.setAdapter(adapter);



		loadData(GlobalUtil.MSG_LOAD_SUCCESS,false);

		pullListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				if (id <= -1)
					return;
			}

		});
		// ����ˢ�¼�����
		pullListView.setonRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {

				loadData(GlobalUtil.MSG_LOAD_SUCCESS,false);

			}
		});

	}
	// �������
		private Handler myHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				ArrayList<Map<String, Object>> data = (ArrayList<Map<String, Object>>) msg
						.getData().getSerializable("data");
				if (data != null && data.size() != 0) {
					if(msg.what==GlobalUtil.MSG_LOAD_SUCCESS)
						datalist.clear();
					
					datalist.addAll(data);
				}
				switch (msg.what) {
				case GlobalUtil.MSG_LOAD_SUCCESS:
					adapter.notifyDataSetChanged();
					pullListView.onRefreshComplete();
					pullListView.setSelectionAfterHeaderView();
					
					break;
				default:
					break;
				}
			}

		};

		private void loadData(final int code,final boolean isMore) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					JSONArray jsonArray = null;
					try {				
						jsonArray = CouchDB.getMemberList();

						ArrayList<Map<String, Object>> dList = new ArrayList<Map<String, Object>>();

						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonFile = jsonArray.getJSONObject(i);
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("member_name", jsonFile.get("member_name"));
							dList.add(map);
						}
						Message message = new Message();
						Bundle bundle = new Bundle();
						bundle.putSerializable("data", dList);
						message.setData(bundle);
						message.what = code;
						myHandler.sendMessage(message);
					} catch (Exception e) {
						e.printStackTrace();
						myHandler.sendEmptyMessage(GlobalUtil.MSG_LOAD_FAILED);
					}

				}
			}).start();
}
}