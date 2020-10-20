package com.example.mmsapp.ui.home.Mapping;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mmsapp.AlerError.AlerError;
import com.example.mmsapp.R;
import com.example.mmsapp.Url;
import com.example.mmsapp.ui.home.ActualWO.HomeFragment;
import com.example.mmsapp.ui.home.Composite.AddmoldActivity;
import com.example.mmsapp.ui.home.Composite.CompositeActivity;
import com.example.mmsapp.ui.home.Composite.MachineActivity;
import com.example.mmsapp.ui.home.Composite.WorkerActivity;
import com.example.mmsapp.ui.home.Manufacturing.ManufacturingActivity;
import com.example.mmsapp.ui.home.Mapping.MappingActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.OnBoomListener;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.mmsapp.Url.NoiDung_Tu_URL;

public class MappingDetailActivity extends AppCompatActivity {
    String webUrl = Url.webUrl;
    String Mt_cd = MappingActivity.Ml_no;
    private ProgressDialog dialog;
    ArrayList<MappingDetailMaster> mappingDetailMasterArrayList;
    MappingDetailAdapter mappingDetailAdapter;
    TextView nodata;
    RecyclerView recyclerView;
    BoomMenuButton bmb;
    String vt_scan = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapping_detail);
        setTitle("Mapping Detail");
        nodata = findViewById(R.id.nodata);
        recyclerView = findViewById(R.id.recyclerView);
        nodata.setVisibility(View.GONE);
        dialog = new ProgressDialog(MappingDetailActivity.this, R.style.AlertDialogCustom);
        bmb = findViewById(R.id.bmb);
        bmb.setButtonEnum(ButtonEnum.TextOutsideCircle);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_2_1);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_2_1);
        bmb.addBuilder(new TextOutsideCircleButton.Builder()
                .normalImageRes(R.drawable.ic_mater)
                .normalTextRes(R.string.MaterialMapping));
        bmb.addBuilder(new TextOutsideCircleButton.Builder()
                .normalImageRes(R.drawable.ic_container)
                .normalTextRes(R.string.ContainerMapping));

        bmb.setOnBoomListener(new OnBoomListener() {
            @Override
            public void onClicked(int index, BoomButton boomButton) {
                switch (index) {
                    case 0:
                        vt_scan = "MT";
                        MaterialMapping();
                        break;
                    case 1:
                        vt_scan = "CT";
                        MaterialMapping();
                        break;

                }
            }

            @Override
            public void onBackgroundClick() {

            }

            @Override
            public void onBoomWillHide() {

            }

            @Override
            public void onBoomDidHide() {

            }

            @Override
            public void onBoomWillShow() {

            }

            @Override
            public void onBoomDidShow() {

            }
        });

        getData();
    }

    //scan
    private void MaterialMapping() {
        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(MappingDetailActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                if (vt_scan.equals("MT")) {
                    new Mappingmaterial().execute(webUrl + "ActualWO/insertw_material_mping?mt_cd=" +
                            Mt_cd +
                            "&mt_mapping=" +
                            result.getContents() +
                            "&id_actual=" +
                            ManufacturingActivity.id_actual +
                            "&bb_no=" +
                            "");
                    Log.e("Mappingmaterial", webUrl + "ActualWO/insertw_material_mping?mt_cd=" +
                            Mt_cd +
                            "&mt_mapping=" +
                            result.getContents() +
                            "&id_actual=" +
                            ManufacturingActivity.id_actual +
                            "&bb_no=" +
                            "");
                } else {
                    new Mappingmaterial().execute(webUrl + "ActualWO/insertw_material_mping?mt_cd=" +
                            Mt_cd +
                            "&mt_mapping=" +
                            "" +
                            "&id_actual=" +
                            ManufacturingActivity.id_actual +
                            "&bb_no=" +
                            result.getContents());
                    Log.e("Mappingmaterial", webUrl + "ActualWO/insertw_material_mping?mt_cd=" +
                            Mt_cd +
                            "&mt_mapping=" +
                            "" +
                            "&id_actual=" +
                            ManufacturingActivity.id_actual +
                            "&bb_no=" +
                            result.getContents());
                }
            }
        }
    }

    private class Mappingmaterial extends AsyncTask<String, Void, String> {
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

                if (jsonObject.has("result")) {
                    if (!jsonObject.getBoolean("result")) {
                        dialog.dismiss();
                        AlerError.Baoloi(jsonObject.getString("message"), MappingDetailActivity.this);
                        return;
                    } else {
                        dialog.dismiss();
                        Toast.makeText(MappingDetailActivity.this, "Done", Toast.LENGTH_SHORT).show();
                        startActivity(getIntent());
                    }
                }else {
                    dialog.dismiss();
                    Toast.makeText(MappingDetailActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    startActivity(getIntent());
                }

            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", MappingDetailActivity.this);
                dialog.dismiss();
            }
        }

    }
    ////////

    private void getData() {
        new getData().execute(webUrl + "ActualWO/ds_mapping_w?mt_cd=" + Mt_cd);
        Log.e("mapping Detail", webUrl + "ActualWO/ds_mapping_w?mt_cd=" + Mt_cd);
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
            mappingDetailMasterArrayList = new ArrayList<>();
            String no, wmmid, mt_lot, mt_cd, use_yn, reg_dt, mt_no, bb_no, Remain;
            int gr_qty, Used;
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
                    wmmid = objectRow.getString("wmmid");
                    mt_lot = objectRow.getString("mt_lot");
                    gr_qty = objectRow.getInt("gr_qty");
                    Used = objectRow.getInt("Used");
                    mt_no = objectRow.getString("mt_no");
                    use_yn = objectRow.getString("use_yn");
                    reg_dt = objectRow.getString("reg_dt");
                    Remain = objectRow.getString("Remain").replace("null", "0");
                    bb_no = objectRow.getString("bb_no").replace("null", "");
                    mt_cd = objectRow.getString("mt_cd");
                    mappingDetailMasterArrayList.add(new MappingDetailMaster(no, wmmid, mt_lot, mt_cd, use_yn, reg_dt, mt_no, bb_no, Remain, gr_qty, Used));
                }
                dialog.dismiss();
                setRecyc();
            } catch (JSONException e) {
                e.printStackTrace();
                nodata.setVisibility(View.VISIBLE);
                AlerError.Baoloi("Could not connect to server", MappingDetailActivity.this);
                dialog.dismiss();
            }
        }

    }

    private void setRecyc() {
        nodata.setVisibility(View.GONE);
        final LinearLayoutManager mLayoutManager;
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(MappingDetailActivity.this);
        mappingDetailAdapter = new MappingDetailAdapter(mappingDetailMasterArrayList);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mappingDetailAdapter);

        mappingDetailAdapter.setOnItemClickListener(new MappingDetailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onR(int position, TextView edittext) {
                inputnum(position);
            }

            @Override
            public void onF(final int position, TextView edittext) {

                if (mappingDetailMasterArrayList.get(position).getUse_yn().equals("N")){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MappingDetailActivity.this, R.style.AlertDialogCustom);
                    alertDialog.setCancelable(false);
                    alertDialog.setTitle("Warning!!!");
                    alertDialog.setMessage("Is A sure you want to return to the used state"); //"The data you entered does not exist on the server !!!");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new Cancel_mapping().execute(webUrl+"ActualWO/Finish_back?wmmid="+ mappingDetailMasterArrayList.get(position).wmmid);
                        }
                    });
                    alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    alertDialog.show();
                }else {
                    new Cancel_mapping().execute(webUrl+"ActualWO/Finish_back?wmmid="+ mappingDetailMasterArrayList.get(position).wmmid);
                }

            }

            @Override
            public void ondelete(final int position, TextView edittext) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MappingDetailActivity.this, R.style.AlertDialogCustom);
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Warning!!!");
                alertDialog.setMessage("Are you sure Delete: " + mappingDetailMasterArrayList.get(position).mt_cd); //"The data you entered does not exist on the server !!!");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new Cancel_mapping().execute(webUrl+"ActualWO/Cancel_mapping?wmmid="+ mappingDetailMasterArrayList.get(position).wmmid);
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

    private void inputnum(final int pos) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MappingDetailActivity.this);
        View viewInflated = LayoutInflater.from(MappingDetailActivity.this).inflate(R.layout.number_input_layout, null);



        builder.setTitle("Input Number Return");
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        builder.setView(viewInflated);

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (input.getText().toString() == null) {
                    input.setText("0");
                } else if (input.getText().toString().length() == 0) {
                    input.setText("0");
                }else if(Integer.parseInt(input.getText().toString())>mappingDetailMasterArrayList.get(pos).getGr_qty()){
                    input.setText(mappingDetailMasterArrayList.get(pos).getGr_qty()+"");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString() == null) {
                    dialog.dismiss();
                } else if (input.getText().toString() == "0") {
                    dialog.dismiss();
                } else if (input.getText().toString().length() == 0) {
                    dialog.dismiss();
                } if (Integer.parseInt(input.getText().toString())>0){

                    new Cancel_mapping().execute(webUrl+"ActualWO/savereturn_lot?soluong=" +
                            input.getText().toString() +
                            "&mt_cd=" +
                            mappingDetailMasterArrayList.get(pos).mt_cd +
                            "&mt_lot=" +
                            Mt_cd);
                    Log.e("return",webUrl+"ActualWO/savereturn_lot?soluong=" +
                            input.getText().toString() +
                            "&mt_cd=" +
                            mappingDetailMasterArrayList.get(pos).mt_cd +
                            "&mt_lot=" +
                            Mt_cd);
                }
                else {
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

    private class Cancel_mapping extends AsyncTask<String, Void, String> {
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

                if (!jsonObject.getBoolean("result")) {
                    dialog.dismiss();
                    AlerError.Baoloi(jsonObject.getString("message"), MappingDetailActivity.this);
                    return;
                }else {
                    startActivity(getIntent());
                    Toast.makeText(MappingDetailActivity.this, "Done", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", MappingDetailActivity.this);
                dialog.dismiss();
            }
        }

    }
}