package com.example.autoservis;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import models.Recenzija;

public class AdapterRecenzije extends RecyclerView.Adapter<AdapterRecenzije.AdapterRecenzijeViewHolder> {

    private List<Recenzija> recenzijaList;

    private OnItemClickLisener lisener;

    public AdapterRecenzije(List<Recenzija> recenzijeList) {
        this.recenzijaList=recenzijeList;
    }

    public void setOnItemClickLisener(OnItemClickLisener lisener){this.lisener=lisener;}

    public interface OnItemClickLisener{
        void OnItemClick(int position);

    }

    public static class AdapterRecenzijeViewHolder extends RecyclerView.ViewHolder{
        public TextView ime;

        public AdapterRecenzijeViewHolder(@NonNull View itemView, OnItemClickLisener lisener){
            super(itemView);
            ime=itemView.findViewById(R.id.ime);
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


    @NonNull
    @Override
    public AdapterRecenzijeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recenzije_items_recycle_view,parent,false);
        AdapterRecenzije.AdapterRecenzijeViewHolder viewHolder= new AdapterRecenzijeViewHolder(view,lisener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecenzije.AdapterRecenzijeViewHolder holder, int position) {
        Recenzija trenutnaRecenzija = recenzijaList.get(position);
        holder.ime.setText(String.format("%s %s",trenutnaRecenzija.getUser().getIme(),trenutnaRecenzija.getUser().getPrezime()));
    }

    @Override
    public int getItemCount() {
        return recenzijaList.size();
    }


}
