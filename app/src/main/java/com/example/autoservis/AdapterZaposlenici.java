package com.example.autoservis;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import models.Zaposlenik;

public class AdapterZaposlenici extends RecyclerView.Adapter<AdapterZaposlenici.ViewHoleder> {

    private List<Zaposlenik> zaposlenikList;
    private OnItemClickLisener lisener;

    public void setOnClickLisener(OnItemClickLisener lisener) {
        this.lisener=lisener;
    }


    public interface OnItemClickLisener{
        void onItemClick(int position);
    }



    public AdapterZaposlenici(List<Zaposlenik> zaposlenikList) {
        this.zaposlenikList=zaposlenikList;
    }

    @NonNull
    @Override
    public ViewHoleder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.zaposlenici_items_recycle_view,parent,false);
        ViewHoleder viewHoleder= new ViewHoleder(view,lisener);
        return viewHoleder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoleder holder, int position) {
        Zaposlenik trenutniZaposlenik= zaposlenikList.get(position);
        holder.ime.setText(String.format("%s %s",trenutniZaposlenik.getIme(),trenutniZaposlenik.getPrezime()));

    }


    @Override
    public int getItemCount() {
        return zaposlenikList.size();
    }

    public static class ViewHoleder extends RecyclerView.ViewHolder {
        public TextView ime;
        public TextView prezime;
        public ViewHoleder(@NonNull View itemView, OnItemClickLisener lisener) {
            super(itemView);
            ime=itemView.findViewById(R.id.ime);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (lisener!=null){
                        int position=getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION){
                            lisener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
