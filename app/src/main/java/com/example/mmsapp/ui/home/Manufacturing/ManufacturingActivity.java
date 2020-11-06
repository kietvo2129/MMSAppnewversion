package com.example.mmsapp.ui.home.Manufacturing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mmsapp.AlerError.AlerError;
import com.example.mmsapp.R;
import com.example.mmsapp.Url;
import com.example.mmsapp.ui.home.ActualWO.HomeFragment;
import com.example.mmsapp.ui.home.Composite.CompositeActivity;
import com.example.mmsapp.ui.home.Mapping.MappingActivity;
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
    ImageView im_delete;
    public static String checkLast ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manufacturing);
        setTitle("Manufacturing");
        theListView = findViewById(R.id.mainListView);
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_pp_create();
            }
        });
        dialog = new ProgressDialog(this,R.style.AlertDialogCustom);
        getData(page);
    }

    private void open_pp_create() {
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ManufacturingActivity.this, android.R.layout.select_dialog_singlechoice);
        android.app.AlertDialog.Builder builderSingle = new android.app.AlertDialog.Builder(ManufacturingActivity.this);
        builderSingle.setTitle("Select Process Type:");
        arrayAdapter.add("Rotary");
        arrayAdapter.add("Stamp Machine");
        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                if (i==0){
                    new create().execute(webUrl+"ActualWO/Add_w_actual?style_no=" +
                            HomeFragment.product +
                            "&at_no=" +
                            HomeFragment.at_no +
                            "&name=" +
                            "ROT");
                }else if (i==1){
                    new create().execute(webUrl+"ActualWO/Add_w_actual?style_no=" +
                            HomeFragment.product +
                            "&at_no=" +
                            HomeFragment.at_no +
                            "&name=" +
                            "STA");
                }


                dialog.dismiss();
            }
        });
        builderSingle.show();

    }
    private class create extends AsyncTask<String, Void, String> {
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

            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("result")){
                    Toast.makeText(ManufacturingActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    getData(1);
                }else {
                    Toast.makeText(ManufacturingActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", ManufacturingActivity.this);
                dialog.dismiss();
            }
        }

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
                im_delete =  view.findViewById(R.id.im_delete);
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
                        if (pos == actualWOMasterArrayList.size()-1){
                            checkLast = "last";
                        }else {
                            checkLast = "";
                        }
                        startActivity(intent);
                    }
                });
                loaddatadetail(pos);

                im_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(ManufacturingActivity.this, R.style.AlertDialogCustom);
                        alertDialog.setCancelable(false);
                        alertDialog.setTitle("Warning!!!");
                        alertDialog.setMessage("Are you sure Delete?");
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new onDelete().execute(webUrl + "ActualWO/xoa_wactual_con?id=" + actualWOMasterArrayList.get(pos).id_actual);
                                Log.e("onDelete", webUrl + "ActualWO/xoa_wactual_con?id=" + actualWOMasterArrayList.get(pos).id_actual);
                            }
                        });
                        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        alertDialog.show();

                    }
                });
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

    private class onDelete extends AsyncTask<String, Void, String> {
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
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("result")){
                    Toast.makeText(ManufacturingActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    startActivity(getIntent());
                }else {
                    AlerError.Baoloi(jsonObject.getString("kq"), ManufacturingActivity.this);
                }
                dialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", ManufacturingActivity.this);
                dialog.dismiss();
            }
        }

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