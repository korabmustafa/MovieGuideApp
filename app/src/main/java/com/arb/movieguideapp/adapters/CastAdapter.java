package com.arb.movieguideapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arb.movieguideapp.R;
import com.arb.movieguideapp.models.Cast;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.MyViewHolder> {

    private List<Cast> mCast;

    public CastAdapter(List<Cast> mCast) {
        this.mCast = mCast;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cast, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Cast cast = mCast.get(i);
        myViewHolder.txtCast.setText(cast.getName());
        myViewHolder.txtCharacter.setText(cast.getCharacter());
        Picasso.get()
                .load(cast.getThumbnail())
                .placeholder(R.drawable.ic_baseline_blur_on_24)
                .error(R.drawable.ic_broken_image_black_24dp)
                .into(myViewHolder.imgCast);
    }

    @Override
    public int getItemCount() {
        return null != mCast ? mCast.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgCast;
        private TextView txtCast;
        private TextView txtCharacter;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCast = itemView.findViewById(R.id.txtCast);
            txtCharacter = itemView.findViewById(R.id.txtCharacter);
            imgCast = itemView.findViewById(R.id.img_cast);
        }
    }
}
