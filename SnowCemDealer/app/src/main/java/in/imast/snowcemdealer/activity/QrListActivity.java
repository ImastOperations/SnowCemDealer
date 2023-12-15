package in.imast.snowcemdealer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import in.imast.snowcemdealer.Connection.APIResultLitener;
import in.imast.snowcemdealer.Connection.ApiClient;
import in.imast.snowcemdealer.R;
import in.imast.snowcemdealer.adapter.QrListAdapter;
import in.imast.snowcemdealer.adapter.QrListAdapterAfterStore;
import in.imast.snowcemdealer.databinding.ActivityQrListBinding;
import in.imast.snowcemdealer.helper.DialogClass;
import in.imast.snowcemdealer.helper.StaticSharedpreference;
import in.imast.snowcemdealer.helper.Utilities;
import in.imast.snowcemdealer.model.QrCodeModal;
import retrofit2.Response;

public class QrListActivity extends AppCompatActivity {

    private ActivityQrListBinding binding;
    Utilities utilities;
    Dialog dialog;
    DialogClass dialogClass;

    int arrCount = 0;
    int totalPoints = 0;
    public static ArrayList<String> arrEntryId = new ArrayList<>();

    ArrayList<String> numbersList;

    private ArrayList<QrCodeModal> qrCodeModalArrayList;
    private QrCodeModal qrCodeModal;
    private String finalStringlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qr_list);

        utilities = new Utilities(this);
        dialogClass = new DialogClass();

        numbersList = (ArrayList<String>) getIntent().getSerializableExtra("QrCodeArray");


        Log.e("numbersList", "" + numbersList);

        QrListAdapter adapter1 = new QrListAdapter(QrListActivity.this, numbersList);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(QrListActivity.this));
        binding.recyclerView.setAdapter(adapter1);
        adapter1.notifyDataSetChanged();


        initView();


    }


    private void initView() {

        binding.cardSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                coupenCodeStore();

            }
        });

        binding.cardOkay.setOnClickListener( view -> {
            numbersList.clear();
            finish();
        });

    }


    private void coupenCodeStore() {


        if (!utilities.isOnline())
            return;

        dialog = dialogClass.progresesDialog(this);

        finalStringlist = Arrays.toString(numbersList.toArray()).replace("[", "").replace("]", "");

        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("qr_code", finalStringlist);
        queryParams.put("customer_id",  StaticSharedpreference.getInfo("CustomerIdScan", this));

        Log.e("Query>>", "" + queryParams);


        ApiClient.couponCodeStore(StaticSharedpreference.getInfo("AccessToken", this), queryParams, new APIResultLitener<JsonObject>() {

            @Override
            public void onAPIResult(Response<JsonObject> response, String errorMessage) {
                Log.e("response>>>", "" + response.body());
                if (response != null && errorMessage == null) {

                    if (response.code() == 200) {
                        dialog.dismiss();

                        String status = String.valueOf(response.body().get("status").getAsString());


                        if (status.equals("success")) {

                            int total_points = response.body().get("totalPoint").getAsInt();


                            Toast.makeText(QrListActivity.this, "Submitted Successfully", Toast.LENGTH_SHORT).show();

                            dialogClass.scanDialogAfterSubmit(QrListActivity.this, total_points);

                            qrCodeModalArrayList = new ArrayList<QrCodeModal>();

                            try {
                                JSONObject jsonObject = new JSONObject(String.valueOf(response.body()));
                                JSONArray data = jsonObject.getJSONArray("data");

                                Log.e("data>>", "" + data);

                                for (int j = 0; j < data.length(); j++) {
                                    JSONObject data_state = data.getJSONObject(j);
                                    String data_status = data_state.getString("status");
                                    String message = data_state.getString("message");
                                    String couponCode = data_state.getString("couponCode");

                                    int points = data_state.getInt("points");


                                    qrCodeModal = new QrCodeModal();
                                    qrCodeModal.setStatus(data_status);
                                    qrCodeModal.setMessage(message);
                                    qrCodeModal.setCouponCode(couponCode);
                                    qrCodeModal.setPoints(points);


                                    qrCodeModalArrayList.add(qrCodeModal);
                                }

                                binding.recyclerView.setVisibility(View.GONE);
                                binding.recyclVAfterSubmit.setVisibility(View.VISIBLE);

                                QrListAdapterAfterStore adapter1 = new QrListAdapterAfterStore(QrListActivity.this, qrCodeModalArrayList);
                                binding.recyclVAfterSubmit.setHasFixedSize(true);
                                binding.recyclVAfterSubmit.setLayoutManager(new LinearLayoutManager(QrListActivity.this));
                                binding.recyclVAfterSubmit.setAdapter(adapter1);
                                adapter1.notifyDataSetChanged();

                                binding.cardSubmit.setVisibility(View.GONE);
                                binding.cardOkay.setVisibility(View.VISIBLE);


                                Log.e("StateModalArrayList>>>", "" + qrCodeModalArrayList);


                            } catch (JSONException e) {
                                dialog.dismiss();

                                e.printStackTrace();
                            }

                        }


                    } else {
                        dialog.dismiss();

                        JSONObject jsonObject = null;
                        try {
                            //jsonObject = new JSONObject(response.errorBody().string());
                            alertDialogScan(response.body().get("status").getAsString(), response.body().get("message").getAsString()
                                    , QrListActivity.this, false);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                } else {

                }

            }
        }, this);


    }


    public void alertDialogScan(String title, String description, final Activity activity, final boolean isFinishActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(description)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (isFinishActivity) {
                            startActivity(new Intent(QrListActivity.this, MainActivity.class)
                                    .putExtra("status", ""));
                            finishAffinity();
                        }
                    }
                });

        AlertDialog alert = builder.create();
        alert.setTitle(title);
        alert.show();
    }


    public static void alertDialog(String title, String description, final Activity activity, final boolean isFinishActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(description)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (isFinishActivity)
                            activity.finish();
                    }
                });
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle(title);
        alert.show();
    }


}