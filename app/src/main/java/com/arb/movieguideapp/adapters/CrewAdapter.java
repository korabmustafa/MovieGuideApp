package com.arb.movieguideapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arb.movieguideapp.R;
import com.arb.movieguideapp.models.Crew;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CrewAdapter extends RecyclerView.Adapter<CrewAdapter.MyViewHolder> {

    private List<Crew> mCrew;

    public CrewAdapter(List<Crew> mCrew) {
        this.mCrew = mCrew;
    }

    @NonNull
    @Override
    public CrewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_crew, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CrewAdapter.MyViewHolder holder, int position) {
        Crew crew = mCrew.get(position);
        holder.txtCrew.setText(crew.getName());
        holder.txtJob.setText(crew.getJob());
        Picasso.get()
                .load(crew.getThumbnail())
                .placeholder(R.drawable.ic_baseline_blur_on_24)
                .error(R.drawable.ic_broken_image_black_24dp)
                .into(holder.imgCrew);
    }

    @Override
    public int getItemCount() {
        return null != mCrew ? mCrew.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgCrew;
        private TextView txtCrew;
        private TextView txtJob;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCrew = itemView.findViewById(R.id.txtCrew);
            txtJob = itemView.findViewById(R.id.txtJob);
            imgCrew = itemView.findViewById(R.id.img_crew);
        }
    }
}
