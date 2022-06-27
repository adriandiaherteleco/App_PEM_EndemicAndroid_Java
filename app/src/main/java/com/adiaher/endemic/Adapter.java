package com.adiaher.endemic;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.PlayerViewnHolder> {

    private Context mCtx;
    private List<FloraItem> floraItemList;

    public Adapter(Context mCtx, List<FloraItem> floraItemList) {
        this.mCtx = mCtx;
        this.floraItemList = floraItemList;
    }

    @NonNull
    @Override
    public PlayerViewnHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_flora, null);

        return new PlayerViewnHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewnHolder holder, int position) { // pasar el item seleccionado en el recyclerview a detalle
        FloraItem floraItem = floraItemList.get(position);
        Glide.with(mCtx)
                .load(floraItem.getImagen())
                .into(holder.img);
        holder.tv1.setText(floraItem.getNombre());
        holder.tv2.setText(floraItem.getNombreCientifico());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
                intent.putExtra("itemDetail", floraItem);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() { //tamaño del arrayList floraItemList
        return floraItemList.size();
    }

    static class PlayerViewnHolder extends RecyclerView.ViewHolder {  // presentar en pantalla los datos del item obtenido
        TextView tv1, tv2;
        ImageView img;

        public PlayerViewnHolder(@NonNull View itemView) {
            super(itemView);

            tv1 = itemView.findViewById(R.id.tv1);
            tv2 = itemView.findViewById(R.id.tv2);
            img = itemView.findViewById(R.id.img);
        }
    }
}
