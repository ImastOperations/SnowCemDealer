package in.imast.snowcemdealer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import in.imast.snowcemdealer.R;

public class QrListAdapter extends RecyclerView.Adapter<QrListAdapter.RecylerviewViewholder> {

    Context context;
    private ArrayList<String> leadListModalArrayList;

    public QrListAdapter(Context mcontext, ArrayList<String> leadListModalArrayList) {
        context = mcontext;
        this.leadListModalArrayList = leadListModalArrayList;
    }

    @NonNull
    @Override
    public RecylerviewViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.qr_list, parent, false);
        return new RecylerviewViewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecylerviewViewholder holder, int position) {
        holder.qr_.setText("" + leadListModalArrayList.get(position));

        holder.qr_delete.setOnClickListener(v -> {
            try {
                leadListModalArrayList.remove(position);
                notifyItemRemoved(position);
            }catch (Exception e){
                e.printStackTrace();
            }

        });
    }

    @Override
    public int getItemCount() {
        return leadListModalArrayList.size();
    }

    public static class RecylerviewViewholder extends RecyclerView.ViewHolder {

        private final TextView qr_;
        private final ImageView qr_delete;

        public RecylerviewViewholder(@NonNull View itemView) {
            super(itemView);
            qr_ = itemView.findViewById(R.id.qr_);
            qr_delete = itemView.findViewById(R.id.qr_delete);
        }
    }
}
