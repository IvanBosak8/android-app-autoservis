package com.example.autoservis;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import models.Rezervacija;

public class AdapterNedefinirano extends RecyclerView.Adapter<AdapterNedefinirano.AdapterNedefiniranoViewHolder> {
    private List<Rezervacija> rezervacijaList;
    private AdapterNedefinirano.OnItemClickLisiner lisiner;
    public interface OnItemClickLisiner {
        void onItemClick(int position);
    }
    public void setOnCliclLisener(AdapterNedefinirano.OnItemClickLisiner lisener){
        this.lisiner=lisener;
    }

    public static class AdapterNedefiniranoViewHolder extends RecyclerView.ViewHolder {
        public TextView danRezervacija;
        public TextView imePrezime;
        public AdapterNedefiniranoViewHolder(@NonNull View itemView, OnItemClickLisiner lisiner){
            super(itemView);
            danRezervacija=itemView.findViewById(R.id.danRezervacije);
            imePrezime=itemView.findViewById(R.id.imePrezimeSvi);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (lisiner!=null){
                        int position=getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION){
                            lisiner.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
    public AdapterNedefinirano(List<Rezervacija> rezervacijaList) {
        this.rezervacijaList=rezervacijaList;
    }

    @NonNull
    @Override
    public AdapterNedefiniranoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rezervacija_items_recycle_view_nedefinirano,parent,false);
        AdapterNedefinirano.AdapterNedefiniranoViewHolder viewHolder=new AdapterNedefinirano.AdapterNedefiniranoViewHolder(view,lisiner);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterNedefinirano.AdapterNedefiniranoViewHolder holder, int position) {
        Rezervacija trenutnaRezervacija=rezervacijaList.get(position);
        holder.danRezervacija.setText(String.format("Rezervacija %s",trenutnaRezervacija.getDan()));
        holder.imePrezime.setText(String.format("%s %s",trenutnaRezervacija.getUser().getIme(),trenutnaRezervacija.getUser().getPrezime()));
    }


    @Override
    public int getItemCount() {
        return rezervacijaList.size();
    }

}
