package ippon.tech.iotcontroller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.services.iot.model.GroupNameAndArn;
import java.util.List;

// https://developer.android.com/guide/topics/ui/layout/recyclerview
// https://developer.android.com/reference/android/support/v7/recyclerview/extensions/ListAdapter
// https://stackoverflow.com/questions/24471109/recyclerview-onclick
public class ThingGroupAdapter extends RecyclerView.Adapter<ThingGroupAdapter.ViewHolder> {

    private List<GroupNameAndArn> data;

    public ThingGroupAdapter(List<GroupNameAndArn> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(data.get(position).getGroupName());
    }

    @Override
    public int getItemCount() {
        if ( data != null) {
            return data.size();
        } else {
            return 0;
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView;
        public ViewHolder(TextView v) {
            super(v);
            textView = v;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d(this.getClass().toString(), String.valueOf(getLayoutPosition()));

            int position = getLayoutPosition();
            String groupName = data.get(position).getGroupName();

//            Intent intent = new Intent(v.getContext(), ThingDetailsActivity.class);
//            intent.putExtra("Group", groupName);

            Toast.makeText(v.getContext(), groupName, Toast.LENGTH_LONG).show();
        }
    }
}
