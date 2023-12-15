package in.imast.snowcemdealer.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

import in.imast.snowcemdealer.Connection.EmployeInterface;
import in.imast.snowcemdealer.R;
import in.imast.snowcemdealer.model.CustomerModal;

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.RecylerviewViewholder> {
    private final EmployeInterface buttonListener;
    private EmployeInterface listener;
    Context context;
    private ArrayList<CustomerModal> arrayList;
    private ArrayList<CustomerModal> searchmerchantListBeanArrayList;
    private String customer_id;
    private String firm_name;
    private String customer_mobile;
    private String customer_first_name;
    private String customer_last_name;
    private String customer_address;

    public CustomerListAdapter(Context mcontext, ArrayList<CustomerModal> arrayList, EmployeInterface buttonListener) {
        context = mcontext;
        this.arrayList = arrayList;
        this.buttonListener = buttonListener;

        this.searchmerchantListBeanArrayList = new ArrayList<>();
        searchmerchantListBeanArrayList.addAll(arrayList);
    }

    public void filter(String charText) {
        Log.e("charText", charText);

        if (charText != null) {
            arrayList.clear();

            if (charText.isEmpty()) {
                arrayList.addAll(searchmerchantListBeanArrayList);
                Log.e("array_list1>>", "" + arrayList.size());
                notifyDataSetChanged();
            } else {

                charText = charText.toLowerCase(Locale.getDefault());

                for (CustomerModal wp : searchmerchantListBeanArrayList) {

                    try {
                        if (wp.getFirm_name().toLowerCase().startsWith(charText)) {
                            arrayList.add(wp);
                        }
                    } catch (Exception e3) {

                        e3.printStackTrace();
                    }

                    notifyDataSetChanged();
                }
            }
        }
    }

    @NonNull
    @Override
    public RecylerviewViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.spinner_adapter, parent, false);
        return new RecylerviewViewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecylerviewViewholder holder, int position) {
        final RecylerviewViewholder recylerviewViewholder = (RecylerviewViewholder) holder;
        holder.tvTime.setText(arrayList.get(position).getFirm_name()+" "+ "("+arrayList.get(position).getCustomer_mobile()+")");

        holder.text_view1.setOnClickListener(v -> {
            customer_id = arrayList.get(position).getCustomerId();
            firm_name = arrayList.get(position).getFirm_name();
            customer_mobile = arrayList.get(position).getCustomer_mobile();
            customer_first_name = arrayList.get(position).getCustomer_first_name();
            customer_last_name = arrayList.get(position).getCustomer_last_name();
            customer_address = arrayList.get(position).getCustomer_address();
            ///impliment by sagar///

            synchronized (this) {
                arrayList.clear();
            }
            arrayList.addAll(searchmerchantListBeanArrayList);

            /*StaticSharedpreference.saveInfo("state_name", customer_name, context);
            StaticSharedpreference.saveInfo("state_id", state_idd, context);*/

            buttonListener.foo(customer_id, firm_name,customer_mobile,customer_first_name,customer_last_name,customer_address);
            //dialog.dismiss();
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class RecylerviewViewholder extends RecyclerView.ViewHolder {
        RelativeLayout text_view1;
        TextView tvTime;

        public RecylerviewViewholder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.text_view);
            text_view1 = itemView.findViewById(R.id.text_view1);
        }
    }
}
