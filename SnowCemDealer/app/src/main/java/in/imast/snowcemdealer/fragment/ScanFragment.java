package in.imast.snowcemdealer.fragment;

import static in.imast.snowcemdealer.activity.ActivityNewEntry.codeArray;
import static in.imast.snowcemdealer.activity.ActivityNewEntry.codeArray1_Update;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.imast.snowcemdealer.Connection.APIResultLitener;
import in.imast.snowcemdealer.Connection.ApiClient;
import in.imast.snowcemdealer.Connection.EmployeInterface;
import in.imast.snowcemdealer.R;
import in.imast.snowcemdealer.activity.ActivityNewEntry;
import in.imast.snowcemdealer.activity.MainActivity;
import in.imast.snowcemdealer.adapter.CustomerListAdapter;
import in.imast.snowcemdealer.databinding.FragmentScanBinding;
import in.imast.snowcemdealer.helper.DialogClass;
import in.imast.snowcemdealer.helper.StaticSharedpreference;
import in.imast.snowcemdealer.model.CustomerModal;
import retrofit2.Response;

public class ScanFragment extends Fragment {


    View view;
    FragmentScanBinding fragmentScanBinding;
    private Dialog dialog1;
    private Dialog dialog;

    private RecyclerView recyclerView;

    private CustomerListAdapter customerListAdapter;
    private ArrayList<CustomerModal> customerModalArrayList;

    private String coustumer_idd = "";
    private CustomerModal customerModal;
    private DialogClass dialogClass;

    private static final int CAMERA_PERMISSION_CODE = 100;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentScanBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_scan, container, false);
        view = fragmentScanBinding.getRoot();
        dialogClass = new DialogClass();

        dialog1 = new Dialog(getContext());
        initView();

        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);

        return view;
    }

    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(getContext(), permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
        } else {
            Toast.makeText(getContext(), "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }


    EmployeInterface buttonListener = new EmployeInterface() {

        @Override
        public void foo(String customer_id, String firm_name, String customer_mobile, String customer_first_name, String customer_last_name, String customer_address) {

            dialog1.dismiss();
            fragmentScanBinding.selectCustomerTv.setVisibility(View.GONE);
            fragmentScanBinding.linearcustomerdetails.setVisibility(View.VISIBLE);

            fragmentScanBinding.customerTypetv.setText(firm_name);
            coustumer_idd = customer_id;
            fragmentScanBinding.firmNameTv.setText(firm_name);
            fragmentScanBinding.customerMobileTv.setText("( " + customer_mobile + " )");
            fragmentScanBinding.customerNameTv.setText(customer_first_name + " " + customer_last_name);
            fragmentScanBinding.addressTv.setText(customer_address);

            StaticSharedpreference.saveInfo("CustomerIdScan", coustumer_idd, getContext());

        }
    };

    private void initView() {

        GetCustomerScan();

        fragmentScanBinding.proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!coustumer_idd.equals("")) {

                    codeArray.clear();
                    codeArray1_Update.clear();

                    startActivity(new Intent(getContext(), ActivityNewEntry.class));
                    /*ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE},

                            1);*/

                } else {
                    Toast.makeText(getContext(), "Please Select Customer", Toast.LENGTH_SHORT).show();
                }


            }
        });

        fragmentScanBinding.selectCustomerType.setOnClickListener(v -> {
            dialog1.setContentView(R.layout.search_state);
            dialog1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog1.setCancelable(false);
            dialog1.getWindow().getAttributes().windowAnimations = R.style.animation;

            LinearLayout okay_text = dialog1.findViewById(R.id.closed);
            EditText search_et_home = dialog1.findViewById(R.id.search_et_home);

            recyclerView = dialog1.findViewById(R.id.recyclerView);

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            customerListAdapter = new CustomerListAdapter(getActivity(), customerModalArrayList, buttonListener);
            recyclerView.setAdapter(customerListAdapter);

            search_et_home.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    customerListAdapter.filter(s.toString());
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            okay_text.setOnClickListener(v1 -> {
                dialog1.dismiss();
            });

            if (customerModalArrayList != null) {
                dialog1.show();
            }

        });


    }


    private void GetCustomerScan() {
        if (customerModalArrayList != null) {
            customerModalArrayList.clear();
        }
        dialog = dialogClass.progresesDialog(getActivity());
        Map<String, String> queryParams = new HashMap<String, String>();
        Log.e("queryParams>>", "" + queryParams);
        Log.e("token>>>", StaticSharedpreference.getInfo("AccessToken", getActivity()));

        ApiClient.getCustomerScan(StaticSharedpreference.getInfo("AccessToken", getActivity()), new APIResultLitener<JsonObject>() {
            @Override
            public void onAPIResult(Response<JsonObject> response, String errorMessage) {
                Log.e("response>>", "" + response);
                try {

                    String status = String.valueOf(response.body().get("status"));

                    Log.e("current_date>>", status);

                    String status1 = status.replace("\"", "");

                    if (status1.equals("success")) {

                        fragmentScanBinding.relativeError.setVisibility(View.GONE);
                        dialog.dismiss();
                        customerModalArrayList = new ArrayList<CustomerModal>();

                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response.body()));
                            JSONArray data = jsonObject.getJSONArray("data");

                            Log.e("data>>", "" + data);

                            for (int j = 0; j < data.length(); j++) {
                                JSONObject data_state = data.getJSONObject(j);
                                String customer_id = data_state.getString("customer_id");
                                String firm_name = data_state.getString("firm_name");
                                String customer_mobile = data_state.getString("customer_mobile");
                                String customer_first_name = data_state.getString("customer_first_name");
                                String customer_last_name = data_state.getString("customer_last_name");
                                String customer_address = data_state.getString("customer_address");


                                customerModal = new CustomerModal();
                                customerModal.setCustomerId(customer_id);
                                customerModal.setFirm_name(firm_name);
                                customerModal.setCustomer_mobile(customer_mobile);
                                customerModal.setCustomer_first_name(customer_first_name);
                                customerModal.setCustomer_last_name(customer_last_name);
                                customerModal.setCustomer_address(customer_address);

                                customerModalArrayList.add(customerModal);
                            }

                            Log.e("StateModalArrayList>>>", "" + customerModalArrayList);


                        } catch (JSONException e) {
                            dialog.dismiss();
                            ErrorDailoge();
                            fragmentScanBinding.rlMain.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    } else {
                        dialog.dismiss();
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.errorBody().string());
                            dialogClass.alertDialog(jsonObject.getString("status"), jsonObject.getString("message")
                                    , getActivity(), false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), "r" + e, Toast.LENGTH_SHORT).show();
                    Log.e("e>>>>>", "" + e);
                    dialog.dismiss();
                    fragmentScanBinding.rlMain.setVisibility(View.GONE);
                    ErrorDailoge();
                    e.printStackTrace();
                }
            }
        }, getActivity());
    }

    private void ErrorDailoge() {
        fragmentScanBinding.relativeError.setVisibility(View.VISIBLE);
        fragmentScanBinding.tvNoInternet.setText("Server Error");
        fragmentScanBinding.rlMain.setVisibility(View.GONE);

        fragmentScanBinding.btnTryAgain.setOnClickListener(v -> {
            fragmentScanBinding.rlMain.setVisibility(View.VISIBLE);
            fragmentScanBinding.relativeError.setVisibility(View.GONE);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Camera Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /*@Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                        perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                ) {
                    startActivity(new Intent(getContext(), ActivityNewEntry.class));                 // All Permissions Granted

                } else {

                    // Permission Denied
                }

            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }*/


}
