package com.example.autoservis;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import models.Rezervacija;

public class AdapterServis extends RecyclerView.Adapter<AdapterServis.AdapterServisViewHolder> {

    private List<Rezervacija> servisList;

    private OnItemClickLisener lisener;

    public void setOnClickLisener(OnItemClickLisener lisener){this.lisener=lisener;}

    public interface OnItemClickLisener{
        void OnItemClick(int position);
    }

    public static class AdapterServisViewHolder extends RecyclerView.ViewHolder{
        public TextView danRezervacije;
        public TextView imePrezime;

        public AdapterServisViewHolder(@NonNull View itemView, OnItemClickLisener lisener) {
            super(itemView);
            danRezervacije=itemView.findViewById(R.id.danRezervacije);
            imePrezime=itemView.findViewById(R.id.imePrezimeSvi);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(lisener !=null){
                        int position= getAdapterPosition();
                        if(position !=RecyclerView.NO_POSITION){
                            lisener.OnItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public AdapterServis(List<Rezervacija> servisList) {
        this.servisList=servisList;
    }

    @NonNull
    @Override
    public AdapterServisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.servis_items_recycle_view,parent,false);
       AdapterServis.AdapterServisViewHolder viewHolder = new AdapterServisViewHolder(view,lisener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterServisViewHolder holder, int position) {
        Rezervacija trenutnaRezervacija=servisList.get(position);
        holder.danRezervacije.setText(String.format("Rezervacija %s",trenutnaRezervacija.getDan()));
        holder.imePrezime.setText(String.format("%s %s",trenutnaRezervacija.getUser().getIme(),trenutnaRezervacija.getUser().getPrezime()));
    }


    @Override
    public int getItemCount() {
        return servisList.size();
    }
}
