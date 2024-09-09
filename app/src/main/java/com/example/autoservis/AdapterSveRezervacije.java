package com.example.autoservis;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import models.Rezervacija;

public class AdapterSveRezervacije extends RecyclerView.Adapter<AdapterSveRezervacije.AdapterSveRezervacijeViewHolder> {
    private List<Rezervacija> rezervacijaList;
    private AdapterSveRezervacije.OnItemClickLisener lisener;

    public interface OnItemClickLisener {
        void onItemClick(int position);
    }

    public void setOnClickListener(AdapterSveRezervacije.OnItemClickLisener lisener) {
        this.lisener = lisener;
    }


        public static class AdapterSveRezervacijeViewHolder extends RecyclerView.ViewHolder{

            public TextView danRezervacije;
            public TextView imePrezime;

            public AdapterSveRezervacijeViewHolder(@NonNull View itemView, OnItemClickLisener lisener){
                super(itemView);
                danRezervacije=itemView.findViewById(R.id.danRezervacije);
                imePrezime=itemView.findViewById(R.id.imePrezimeSvi);
                itemView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        if (lisener!=null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                lisener.onItemClick(position);
                            }
                        }
                }
            });
        }
    }

    public AdapterSveRezervacije(List<Rezervacija> rezervacijaList){
        this.rezervacijaList=rezervacijaList;
    }
    @NonNull
    @Override
    public AdapterSveRezervacije.AdapterSveRezervacijeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rezervacija_items_recycle_view_sve_rezervacije,parent,false);
        AdapterSveRezervacije.AdapterSveRezervacijeViewHolder viewHolder=new  AdapterSveRezervacije.AdapterSveRezervacijeViewHolder(view,lisener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSveRezervacije.AdapterSveRezervacijeViewHolder holder, int position) {
        Rezervacija trenutnaRezervacija=rezervacijaList.get(position);
        holder.danRezervacije.setText(String.format("Servis %s", trenutnaRezervacija.getDan()));
        holder.imePrezime.setText(String.format("%s %s",trenutnaRezervacija.getUser().getIme(),trenutnaRezervacija.getUser().getPrezime()));

    }

    @Override
    public int getItemCount() {
        return rezervacijaList.size();
    }


}
