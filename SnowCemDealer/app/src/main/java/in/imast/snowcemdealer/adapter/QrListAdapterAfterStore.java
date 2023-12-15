package in.imast.snowcemdealer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.imast.snowcemdealer.R;
import in.imast.snowcemdealer.model.QrCodeModal;

public class QrListAdapterAfterStore extends RecyclerView.Adapter<QrListAdapterAfterStore.RecylerviewViewholder> {

    Context context;
    private ArrayList<QrCodeModal> leadListModalArrayList;

    public QrListAdapterAfterStore(Context mcontext, ArrayList<QrCodeModal> leadListModalArrayList) {
        context = mcontext;
        this.leadListModalArrayList = leadListModalArrayList;
    }

    @NonNull
    @Override
    public RecylerviewViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.qr_list_after_success, parent, false);
        return new RecylerviewViewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecylerviewViewholder holder, int position) {


        if (leadListModalArrayList.get(position).getStatus().equals("error")) {
            holder.points.setVisibility(View.GONE);
            holder.status.setText(leadListModalArrayList.get(position).getStatus());
            holder.status.setTextColor(Color.parseColor("#ff0303"));
            holder.message.setText(leadListModalArrayList.get(position).getMessage());
            holder.qr_code.setText(leadListModalArrayList.get(position).getCouponCode());
        } else {
            holder.points.setVisibility(View.VISIBLE);
            holder.status.setText(leadListModalArrayList.get(position).getStatus());
            holder.status.setTextColor(Color.parseColor("#4BB543"));
            holder.points.setText("" + leadListModalArrayList.get(position).getPoints());
            holder.qr_code.setText(leadListModalArrayList.get(position).getCouponCode());

            Log.e("points>>","" + leadListModalArrayList.get(position).getPoints());
        }


    }

    @Override
    public int getItemCount() {
        return leadListModalArrayList.size();
    }

    public static class RecylerviewViewholder extends RecyclerView.ViewHolder {

        private final TextView qr_code;
        private final TextView status;
        private final TextView message;
        private final TextView points;


        public RecylerviewViewholder(@NonNull View itemView) {
            super(itemView);
            qr_code = itemView.findViewById(R.id.couponCode);
            status = itemView.findViewById(R.id.status);
            message = itemView.findViewById(R.id.errorMessage);
            points = itemView.findViewById(R.id.points);
        }
    }
}
