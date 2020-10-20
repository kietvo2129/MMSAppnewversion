package com.example.mmsapp.ui.home.Manufacturing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mmsapp.AlerError.AlerError;
import com.example.mmsapp.R;
import com.example.mmsapp.Url;
import com.example.mmsapp.ui.home.ActualWO.HomeFragment;
import com.example.mmsapp.ui.home.Composite.CompositeActivity;
import com.ramotion.foldingcell.FoldingCell;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.mmsapp.Url.NoiDung_Tu_URL;

public class ManufacturingActivity extends AppCompatActivity {
    String webUrl = Url.webUrl;
    ArrayList<ActualWOMaster> actualWOMasterArrayList;
    ArrayList<ActualWOdetailMaster> actualWOdetailMasterArrayList;
    ActualdetailAdapter actualdetailAdapter;
    ActualWOAdapter adapter;
    ListView theListView;
    RecyclerView recyclerView;
    View viewdetail;
    int page =1;
    TextView totaldetail,content_request_btn;
    private ProgressDialog dialog;
    private int vitribam = -1;
    int total=-1;
    public static String id_actual = "";
    public static String qc_code = "";
    public static String style_no ="";
    public static String style_name ="";
    String at_no = HomeFragment.at_no;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manufacturing);
        setTitle("Manufacturing");
        theListView = findViewById(R.id.mainListView);

        dialog = new ProgressDialog(this,R.style.AlertDialogCustom);
        getData(page);
    }
    private void getData(int page) {
        new getData().execute(webUrl+ "ActualWO/Getdataw_actual?rows=50&page="+ page+"&sidx=&sord=asc&at_no="+at_no);
        Log.e("getData",webUrl+ "ActualWO/Getdataw_actual?rows=50&page="+ page+"&sidx=&sord=asc&at_no="+at_no);
    }
    private void getaddData(int page) {
        new getaddData().execute(webUrl+ "ActualWO/Getdataw_actual?rows=50&page="+ page+"&sidx=&sord=asc&at_no="+at_no);
        Log.e("getaddData",webUrl+ "ActualWO/Getdataw_actual?rows=50&page="+ page+"&sidx=&sord=asc&at_no="+at_no);

    }
    private class getData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading...");
            dialog.setCancelable(true);
            dialog.show();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            actualWOMasterArrayList = new ArrayList<>();
            String id_actual,name,date,product,item_vcd,remark,
                    reg_id,reg_dt,chg_id,chg_dt;
            int defect,actual,target;
            try {

                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("rows");

                if (jsonArray.length() == 0) {
                    dialog.dismiss();
                    AlerError.Baoloi("No data", ManufacturingActivity.this);
                    return;
                }

                total = jsonObject.getInt("total");
                page = jsonObject.getInt("page");
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject objectRow = jsonArray.getJSONObject(i);
                    id_actual = objectRow.getString("id_actual");
                    name = objectRow.getString("name");
                    date = objectRow.getString("date");
                    defect = objectRow.getInt("defect");
                    target =0;//target = objectRow.getInt("target");
                    item_vcd = objectRow.getString("item_vcd");
                    product = ""; //product = objectRow.getString("product");
                    actual = objectRow.getInt("actual");
                    remark = "";//remark = objectRow.getString("remark").replace("null","");
                    reg_id = objectRow.getString("reg_id");
                    reg_dt = objectRow.getString("reg_dt");
                    chg_id = objectRow.getString("chg_id");
                    chg_dt = objectRow.getString("chg_dt");
                    actualWOMasterArrayList.add(new ActualWOMaster(id_actual,name,date,defect,target,product,item_vcd,actual,remark,
                            reg_id,reg_dt,chg_id,chg_dt));
                }
                dialog.dismiss();
                setListView();
            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", ManufacturingActivity.this);
                dialog.dismiss();
            }
        }

    }
    private class getaddData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading...");
            dialog.setCancelable(true);
            dialog.show();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String id_actual,name,date,product,item_vcd,remark,
                    reg_id,reg_dt,chg_id,chg_dt;
            int defect,actual,target;
            try {

                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("rows");

                if (jsonArray.length() == 0) {
                    dialog.dismiss();
                    AlerError.Baoloi("No data", ManufacturingActivity.this);
                    return;
                }

                total = jsonObject.getInt("total");
                page = jsonObject.getInt("page");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject objectRow = jsonArray.getJSONObject(i);
                    id_actual = objectRow.getString("id_actual");
                    name = objectRow.getString("name");
                    date = objectRow.getString("date");
                    defect = objectRow.getInt("defect");
                    target = 0;// target = objectRow.getInt("target");
                    item_vcd = objectRow.getString("item_vcd");
                    product = "";//product = objectRow.getString("product");
                    actual = objectRow.getInt("actual");
                    remark = "";// remark = objectRow.getString("remark").replace("null","");
                    reg_id = objectRow.getString("reg_id");
                    reg_dt = objectRow.getString("reg_dt");
                    chg_id = objectRow.getString("chg_id");
                    chg_dt = objectRow.getString("chg_dt");
                    actualWOMasterArrayList.add(new ActualWOMaster(id_actual,name,date,defect,target,product,item_vcd,actual,remark,
                            reg_id,reg_dt,chg_id,chg_dt));
                }
                dialog.dismiss();
                //setListView(total);
                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", ManufacturingActivity.this);
                dialog.dismiss();
            }
        }

    }

    private void setListView() {

        adapter = new ActualWOAdapter(ManufacturingActivity.this, actualWOMasterArrayList);

        theListView.setAdapter(adapter);

        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int pos, long l) {
                // toggle clicked cell state

                recyclerView =  view.findViewById(R.id.recycview);
                totaldetail =  view.findViewById(R.id.totaldetail);
                viewdetail =view;
                vitribam = pos;
                content_request_btn =  view.findViewById(R.id.content_request_btn);
                content_request_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ManufacturingActivity.this, CompositeActivity.class);
                        id_actual = actualWOMasterArrayList.get(pos).getId_actual();
                        qc_code = actualWOMasterArrayList.get(pos).item_vcd;
                        style_no = actualWOMasterArrayList.get(pos).product;
                        style_name = actualWOMasterArrayList.get(pos).name;
                        startActivity(intent);
                    }
                });
                loaddatadetail(pos);
            }
        });

        theListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
                {
                    if (page < total){
                        total = -1;
                        getaddData(page+1);

                    }
                }
            }
        });


    }

    private void loaddatadetail(int pos) {
        new loaddatadetail().execute(webUrl+ "ActualWO/getdetail_actual?id=" +
                actualWOMasterArrayList.get(pos).id_actual +
                "&_search=false&nd=1602733203479&rows=10&page=1&sidx=&sord=asc");
        Log.e("loaddatadetail",webUrl+ "ActualWO/getdetail_actual?id=" +
                actualWOMasterArrayList.get(pos).id_actual +
                "&_search=false&nd=1602733203479&rows=10&page=1&sidx=&sord=asc");
    }
    private class loaddatadetail extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading...");
            dialog.setCancelable(true);
            dialog.show();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            actualWOdetailMasterArrayList = new ArrayList<>();
            String mt_cd,bb_no;
            int gr_qty,count_table2;
            try {

                JSONArray jsonArray = new JSONArray(s);

                if (jsonArray.length() == 0) {
                    dialog.dismiss();
                    setDetail();
                    return;
                }
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject objectRow = jsonArray.getJSONObject(i);
                    mt_cd = objectRow.getString("mt_cd");
                    bb_no = objectRow.getString("bb_no").replace("null","");
                    gr_qty = objectRow.getInt("gr_qty");
                    count_table2 = objectRow.getInt("count_table2");
                    actualWOdetailMasterArrayList.add(new ActualWOdetailMaster(mt_cd,bb_no,gr_qty,count_table2,i+1));
                }
                setDetail();
            } catch (JSONException e) {
                dialog.dismiss();
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", ManufacturingActivity.this);
            }
        }

    }

    private void setDetail() {

        dialog.dismiss();
        RecyclerView.LayoutManager mLayoutManager;
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(ManufacturingActivity.this);
        actualdetailAdapter = new ActualdetailAdapter(actualWOdetailMasterArrayList);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(actualdetailAdapter);
        totaldetail.setText(actualWOdetailMasterArrayList.size()+" ML No");
        ((FoldingCell) viewdetail).toggle(true);
        for (int i = 0;i<actualWOMasterArrayList.size();i++) {
            if (i!=vitribam) {
                adapter.registerFold(i);
            }else {
                adapter.registerToggle(vitribam);
            }
        }

    }
}