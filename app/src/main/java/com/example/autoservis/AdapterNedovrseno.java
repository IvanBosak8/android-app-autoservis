package com.example.autoservis;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import models.Rezervacija;

public class AdapterNedovrseno extends RecyclerView.Adapter<AdapterNedovrseno.AdapterNedovrsenoViewHolder> {

    private List <Rezervacija> nedovrsenaRezervacija;
    private OnItemClickLisener lisener;

    public void setOnCliclLisener(OnItemClickLisener lisener) {
        this.lisener=lisener;
    }


    public interface OnItemClickLisener {
        void onItemClick(int position);
    }


    public static class AdapterNedovrsenoViewHolder extends RecyclerView.ViewHolder{

        public TextView danRezervacije;


        public AdapterNedovrsenoViewHolder(@NonNull View itemView, OnItemClickLisener lisiner) {
            super(itemView);
            danRezervacije=itemView.findViewById(R.id.danRezervacije);
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


    public AdapterNedovrseno(List<Rezervacija> nedovrsenaRezervacija) {
        this.nedovrsenaRezervacija =nedovrsenaRezervacija;
    }

    @NonNull
    @Override
    public AdapterNedovrsenoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.rezervacija_items_recycle_view_nedovrseno,parent,false);
        AdapterNedovrseno.AdapterNedovrsenoViewHolder viewHolder=new AdapterNedovrseno.AdapterNedovrsenoViewHolder(v,lisener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterNedovrsenoViewHolder holder, int position) {

        Rezervacija trenutnaRezervacija= nedovrsenaRezervacija.get(position);
        holder.danRezervacije.setText(trenutnaRezervacija.getDan());

    }

    @Override
    public int getItemCount() {
        return nedovrsenaRezervacija.size();
    }

}
