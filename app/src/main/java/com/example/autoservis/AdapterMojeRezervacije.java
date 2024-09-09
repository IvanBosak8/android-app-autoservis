package com.example.autoservis;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import models.Rezervacija;

public class AdapterMojeRezervacije extends RecyclerView.Adapter<AdapterMojeRezervacije.ViewHolder> {

    private List<Rezervacija> rezervacijaList;
    private OnItemClickLisener lisener;

    public void setOnClickLisener(OnItemClickLisener lisener) {
        this.lisener=lisener;
    }

    public interface OnItemClickLisener{
        void OnItemClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView danRezervacije;

        public ViewHolder(@NonNull View itemView, OnItemClickLisener lisener) {
            super(itemView);
            danRezervacije=itemView.findViewById(R.id.danRezervacije);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (lisener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            lisener.OnItemClick(position);
                        }
                    }
                }
            });

        }
    }

    public AdapterMojeRezervacije(List<Rezervacija> rezervacijaList){
        this.rezervacijaList=rezervacijaList;
    }
    @NonNull
    @Override
    public AdapterMojeRezervacije.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rezervacija_items_recycle_view,parent,false);
        ViewHolder viewHolder=new ViewHolder(view,lisener);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMojeRezervacije.ViewHolder holder, int position) {
        Rezervacija trenutnaRezervacija=rezervacijaList.get(position);

        holder.danRezervacije.setText(trenutnaRezervacija.getDan());
    }

    @Override
    public int getItemCount() {
        return rezervacijaList.size();
    }
}
