package com.example.mmsapp.ui.home.Composite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mmsapp.AlerError.AlerError;
import com.example.mmsapp.ui.home.Composite.Divide.DivideActivity;

import com.example.mmsapp.ui.home.Manufacturing.ManufacturingActivity;
import com.example.mmsapp.ui.home.Mapping.MappingActivity;
import com.example.mmsapp.R;
import com.example.mmsapp.Url;
import com.example.mmsapp.ui.home.ActualWO.HomeFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.OnBoomListener;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.mmsapp.Url.NoiDung_Tu_URL;

public class CompositeActivity extends AppCompatActivity {
    String webUrl = Url.webUrl;
    private ProgressDialog dialog;
    ArrayList<CompositeMaster> compositeMasterArrayList;
    TextView nodata;
    RecyclerView recyclerView;
    CompositeAdapter compositeAdapter;
    public static String id_actual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_composite);
        setTitle("Composite");
        dialog = new ProgressDialog(this);
        nodata = findViewById(R.id.nodata);
        recyclerView = findViewById(R.id.recyclerView);
        nodata.setVisibility(View.GONE);
        Bundle bundle = getIntent().getExtras();
        id_actual = ManufacturingActivity.id_actual;
        //build bombutton
        BoomMenuButton bmb4 = (BoomMenuButton) findViewById(R.id.bmb4);
        bmb4.addBuilder(new HamButton.Builder()
                .normalImageRes(R.drawable.ic_mold)
                .normalTextRes(R.string.Mold)
                .subNormalTextRes(R.string.addModeinhere));
        bmb4.addBuilder(new HamButton.Builder()
                .normalImageRes(R.drawable.ic_staff)
                .normalTextRes(R.string.Worker)
                .subNormalTextRes(R.string.movingpositionofmachinery));
        bmb4.addBuilder(new HamButton.Builder()
                .normalImageRes(R.drawable.ic_machine)
                .normalTextRes(R.string.Machine)
                .subNormalTextRes(R.string.ConfirmMoving));
        bmb4.setOnBoomListener(new OnBoomListener() {
            @Override
            public void onClicked(int index, BoomButton boomButton) {
                switch (index) {
                    case 0:
                        Intent intent = new Intent(CompositeActivity.this, AddmoldActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(CompositeActivity.this, WorkerActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(CompositeActivity.this, MachineActivity.class);
                        startActivity(intent2);
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

        BoomMenuButton mapping = (BoomMenuButton) findViewById(R.id.mapping);

        if (ManufacturingActivity.style_name.equals("STA")) {
            mapping.setButtonEnum(ButtonEnum.Ham);
            mapping.setButtonPlaceEnum(ButtonPlaceEnum.HAM_2);
            mapping.setPiecePlaceEnum(PiecePlaceEnum.DOT_2_1);
            mapping.addBuilder(new HamButton.Builder()
                    .normalImageRes(R.drawable.ic_mapping)
                    .normalTextRes(R.string.Mappiing)
                    .subNormalTextRes(R.string.CompositeMaterialMapping));
            mapping.addBuilder(new HamButton.Builder()
                    .normalImageRes(R.drawable.ic_div)
                    .normalTextRes(R.string.Divide)
                    .subNormalTextRes(R.string.DivideMTNo));
        } else {
            mapping.setButtonEnum(ButtonEnum.Ham);
            mapping.setButtonPlaceEnum(ButtonPlaceEnum.HAM_1);
            mapping.setPiecePlaceEnum(PiecePlaceEnum.DOT_1);
            mapping.addBuilder(new HamButton.Builder()
                    .normalImageRes(R.drawable.ic_mapping)
                    .normalTextRes(R.string.Mappiing)
                    .subNormalTextRes(R.string.CompositeMaterialMapping));
        }

        mapping.setOnBoomListener(new OnBoomListener() {
            @Override
            public void onClicked(int index, BoomButton boomButton) {
                switch (index) {
                    case 0:
                        Intent intent = new Intent(CompositeActivity.this, MappingActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(CompositeActivity.this, DivideActivity.class);
                        startActivity(intent1);
                        break;
//                    case 2:
//                        Intent intent2 = new Intent(CompositeActivity.this,MachineActivity.class);
//                        startActivity(intent2);
//                        break;
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
    }

    private void getData() {
        new getData().execute(webUrl + "ActualWO/Getinfo_mc_wk_mold?id_actual=" + id_actual);
        Log.e("getData", webUrl + "ActualWO/Getinfo_mc_wk_mold?id_actual=" + id_actual);
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
            compositeMasterArrayList = new ArrayList<>();
            String code, start_dt, end_dt, type, name;

            try {

                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("data");

                if (jsonArray.length() == 0) {
                    dialog.dismiss();
                    nodata.setVisibility(View.VISIBLE);
                    return;
                }

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject objectRow = jsonArray.getJSONObject(i);
                    code = objectRow.getString("code").replace("null", "");
                    start_dt = objectRow.getString("start_dt").replace("null", "");
                    end_dt = objectRow.getString("end_dt").replace("null", "");
                    type = objectRow.getString("type").replace("null", "");
                    name = objectRow.getString("name").replace("null", "");

                    compositeMasterArrayList.add(new CompositeMaster(code, start_dt, end_dt, type, i + 1 + "", name));
                }
                dialog.dismiss();
                buildrecyc();
            } catch (JSONException e) {
                e.printStackTrace();
                nodata.setVisibility(View.VISIBLE);
                AlerError.Baoloi("Could not connect to server", CompositeActivity.this);
                dialog.dismiss();
            }
        }

    }

    private void buildrecyc() {
        nodata.setVisibility(View.GONE);

        final LinearLayoutManager mLayoutManager;
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(CompositeActivity.this);
        compositeAdapter = new CompositeAdapter(compositeMasterArrayList);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(compositeAdapter);
        compositeAdapter.setOnItemClickListener(new CompositeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                switch (compositeMasterArrayList.get(position).getType()) {
                    case "machine":

                        break;
                    case "worker":

                        break;
                    case "mold":
                       // openpopupmold(position,compositeMasterArrayList.get(position).start_dt,compositeMasterArrayList.get(position).end_dt);
                        break;
                    default:
                        Toast.makeText(CompositeActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openpopupmold(int position, String start_dt, String end_dt) {
        final Dialog dialog = new Dialog(CompositeActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        View dialogView = LayoutInflater.from(CompositeActivity.this).inflate(R.layout.change_mold, null);
        dialog.setCancelable(false);
        dialog.setContentView(dialogView);
        final TextView ngaystart, giostart;
        ngaystart = dialog.findViewById(R.id.ngaystart);
        giostart = dialog.findViewById(R.id.giostart);

        final TextView ngayend, gioend;
        ngayend = dialog.findViewById(R.id.ngayend);
        gioend = dialog.findViewById(R.id.gioend);

        Toast.makeText(this, start_dt.length()+"      "+end_dt.length(), Toast.LENGTH_SHORT).show();
        String yy,MM,dd,hh,mm,ss,yye,MMe,dde,hhe,mme,sse;
        if (start_dt.length()==19) {
             yy = start_dt.substring(0, 4);
             MM = start_dt.substring(5, 7);
            dd = start_dt.substring(8, 10);
            hh = start_dt.substring(11, 13);
            mm = start_dt.substring(14, 16);
            ss = start_dt.substring(17, 19);
        }else {
            AlerError.Baoloi("Format date incorrect.", CompositeActivity.this);
            return;
        }
        if (end_dt.length()==19) {
            yye = end_dt.substring(0, 4);
            MMe = end_dt.substring(5, 7);
            dde = end_dt.substring(8, 10);
            hhe = end_dt.substring(11, 13);
            mme = end_dt.substring(14, 16);
            sse = end_dt.substring(17, 19);
        }else {
            AlerError.Baoloi("Format date incorrect.", CompositeActivity.this);
            return;
        }
     //   http://messhinsungcntvina.com:83/ActualWO/Modifyprocessmachine_unit?mc_no=HJ&pmid=71&use_yn=Y&start=2020-10-24+16%3A56%3A19&id_actual=136&end=2020-10-24+20%3A00%3A00&remark=

      //  http://messhinsungcntvina.com:83/ActualWO/Modifyprocess_unitstaff?psid=61&staff_tp=SLT&staff_id=90192&use_yn=Y&start=2020-10-24+18%3A20%3A45&end=2020-10-24+20%3A00%3A00



        ngaystart.setText(yy+"-"+MM+"-"+dd);
        ngayend.setText(yye+"-"+MMe+"-"+dde);
        giostart.setText(hh+":"+mm+":"+ss);
        gioend.setText(hhe+":"+mme+":"+sse);

        ngaystart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogNgay(ngaystart);
            }
        });
        giostart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialoggio(giostart);
            }
        });
        ngayend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogNgay(ngayend);
            }
        });
        gioend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialoggio(gioend);
            }
        });

        dialog.findViewById(R.id.btclose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    private void dialogNgay(final TextView ngaystart) {
        Calendar c = Calendar.getInstance();
        int selectedYear = c.get(Calendar.YEAR);
        int selectedMonth = c.get(Calendar.MONTH);
        int selectedDayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {

                ngaystart.setText(String.format("%02d",dayOfMonth) + "-" + (String.format("%02d",monthOfYear + 1)) + "-" + year);
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                dateSetListener, selectedYear, selectedMonth, selectedDayOfMonth);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void dialoggio(final TextView ngaystart) {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                ngaystart.setText(String.format("%02d",hourOfDay) + "-" + (String.format("%02d",minute))+"-00");
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar,timeSetListener,12,59,true);
        timePickerDialog.show();
    }

    @Override
    protected void onResume() {
        getData();
        super.onResume();
    }
}