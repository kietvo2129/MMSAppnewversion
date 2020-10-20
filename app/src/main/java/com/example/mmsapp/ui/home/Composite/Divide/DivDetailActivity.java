package com.example.mmsapp.ui.home.Composite.Divide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mmsapp.AlerError.AlerError;
import com.example.mmsapp.R;
import com.example.mmsapp.Url;
import com.example.mmsapp.ui.home.Composite.Divide.DivideActivity;
import com.example.mmsapp.ui.home.Composite.WorkerActivity;
import com.example.mmsapp.ui.home.Manufacturing.ManufacturingActivity;
import com.example.mmsapp.ui.home.Mapping.MappingActivity;
import com.example.mmsapp.ui.home.Mapping.MappingDetailActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.mmsapp.Url.NoiDung_Tu_URL;

public class DivDetailActivity extends AppCompatActivity {
    String webUrl = Url.webUrl;
    String ml_no ="";
    private ProgressDialog dialog;
    TextView nodata;
    RecyclerView recyclerView;

    ArrayList<DivDetailMaster> divDetailMasterArrayList;

    DivideDetailAdapter divideDetailAdapter;

    int Tong = 0;
    String value_old ="";
    String wmtid_end ="";
    String wmtidclick = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_div_detail);
        setTitle("Divide Detail");
        ml_no = DivideActivity.Ml_no;

        recyclerView = findViewById(R.id.recyclerView);
        nodata = findViewById(R.id.nodata);
        nodata.setVisibility(View.GONE);
        dialog = new ProgressDialog(this, R.style.AlertDialogCustom);

    }

    @Override
    protected void onPostResume() {
        getData();
        super.onPostResume();
    }

    private void getData() {
        new getData().execute(webUrl + "ActualWO/ds_mapping_sta?mt_cd=" +
                ml_no +
                "&_search=false&nd=1603168138884&rows=50&page=1&sidx=&sord=asc");
        Log.e("mapping", webUrl + "ActualWO/ds_mapping_sta?mt_cd=" +
                ml_no +
                "&_search=false&nd=1603168138884&rows=50&page=1&sidx=&sord=asc");
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
            divDetailMasterArrayList = new ArrayList<>();
            String no,mt_cd,mt_no,bb_no,wmtid;
            int sl_tru_ng;

            try {

                JSONArray jsonArray = new JSONArray(s);

                if (jsonArray.length() == 0) {
                    dialog.dismiss();
                    nodata.setVisibility(View.VISIBLE);
                    return;
                }
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject objectRow = jsonArray.getJSONObject(i);
                    no = i + 1 + "";
                    mt_cd = objectRow.getString("mt_cd");
                    mt_no = objectRow.getString("mt_no");
                    sl_tru_ng = objectRow.getInt("gr_qty");
                    bb_no = objectRow.getString("bb_no");
                    wmtid = objectRow.getString("wmtid");
                    divDetailMasterArrayList.add(new DivDetailMaster(no,mt_cd,mt_no,bb_no,wmtid, sl_tru_ng));
                    Tong += sl_tru_ng;
                    value_old += sl_tru_ng +",";
                    wmtid_end += wmtid+",";
                }
                dialog.dismiss();
                setRecyc();
            } catch (JSONException e) {
                e.printStackTrace();
                nodata.setVisibility(View.VISIBLE);
                AlerError.Baoloi("Could not connect to server", DivDetailActivity.this);
                dialog.dismiss();
            }
        }

    }

    private void setRecyc() {
        nodata.setVisibility(View.GONE);
        final LinearLayoutManager mLayoutManager;
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        divideDetailAdapter = new DivideDetailAdapter(divDetailMasterArrayList);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(divideDetailAdapter);
        divideDetailAdapter.setOnItemClickListener(new DivideDetailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void changeQuanlity(int position) {
                inputQuanlity(position);
            }

            @Override
            public void changebb(int position) {

                wmtidclick = divDetailMasterArrayList.get(position).wmtid;
                androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(DivDetailActivity.this, R.style.AlertDialogCustom);
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Warning!!!");
                alertDialog.setMessage("Do you want to change Bobbin Code"); //"The data you entered does not exist on the server !!!");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        starScan();
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

    private void starScan() {
        new IntentIntegrator(this).initiateScan();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(DivDetailActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                new savechangeDetail().execute(webUrl+"ActualWO/Changebb_dv?bb_no=" +
                        result.getContents() +
                        "&wmtid=" +
                        wmtidclick);
            }
        }
    }

    private void inputQuanlity(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DivDetailActivity.this);
        View viewInflated = LayoutInflater.from(DivDetailActivity.this).inflate(R.layout.number_input_layout, null);
        builder.setTitle("Input Quantity Reality");
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString() == null) {
                    dialog.dismiss();
                } else if (input.getText().toString().length() == 0) {
                    dialog.dismiss();
                } else {
                    divDetailMasterArrayList.get(position).setSl_tru_ng(Integer.parseInt(input.getText().toString()));
                    divideDetailAdapter.notifyDataSetChanged();
                }
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

            inflater.inflate(R.menu.menu_qc, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.qc_check:
                if (divDetailMasterArrayList.size()!=0) {
                    savechangeDetail();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void savechangeDetail() {
        int tongchange = 0;
        String value_new ="";


        for (int i = 0 ; i<divDetailMasterArrayList.size();i++){
            value_new += divDetailMasterArrayList.get(i).sl_tru_ng+",";
            tongchange += divDetailMasterArrayList.get(i).sl_tru_ng;
        }


        if (Tong == tongchange){
            new savechangeDetail().execute(webUrl+"ActualWO/change_gr_dv?value_new=" +
                    value_new.substring(0,value_new.length()-1) +
                    "&value_old=" +
                    value_old.substring(0,value_new.length()-1)  +
                    "&wmtid=" +
                    wmtid_end.substring(0,value_new.length()-1));

            Log.e("savechangeDetail",webUrl+"ActualWO/change_gr_dv?value_new=" +
                    value_new.substring(0,value_new.length()-1) +
                    "&value_old=" +
                    value_old.substring(0,value_new.length()-1)  +
                    "&wmtid=" +
                    wmtid_end.substring(0,value_new.length()-1));

        }else {
            AlerError.Baoloi("Total input maximum: "+Tong, DivDetailActivity.this);
        }

    }

    private class savechangeDetail extends AsyncTask<String, Void, String> {
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
                if (!jsonObject.getBoolean("result")){
                    dialog.dismiss();
                    AlerError.Baoloi(jsonObject.getString("message"), DivDetailActivity.this);
                    return;
                }
                Toast.makeText(DivDetailActivity.this, "Done", Toast.LENGTH_SHORT).show();
                startActivity(getIntent());
                dialog.dismiss();

            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", DivDetailActivity.this);
                dialog.dismiss();
            }
        }

    }

}