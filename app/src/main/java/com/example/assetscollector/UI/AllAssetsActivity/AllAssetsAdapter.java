package com.example.assetscollector.UI.AllAssetsActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assetscollector.Database.entities.AssetModel;
import com.example.assetscollector.R;

import java.util.List;


public class AllAssetsAdapter extends RecyclerView.Adapter<AllAssetsAdapter.AssetsViewHolder> {
    private List<AssetModel> assetModelList ;

    public  AllAssetsAdapter(List<AssetModel> assetModelList) {
        this.assetModelList = assetModelList;
    }

    @NonNull
    @Override
    public AssetsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AssetsViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.asset_item , parent ,false ));
    }

    @Override
    public void onBindViewHolder(@NonNull AssetsViewHolder holder, int position) {
        holder.setAssetItem(assetModelList.get(position));

    }

    @Override
    public int getItemCount() {
        return assetModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static  class AssetsViewHolder extends RecyclerView.ViewHolder {
        TextView barcodeTxt ,  desTxt , locationTxt , statusTxt ;

        AssetsViewHolder(@NonNull View itemView) {
            super(itemView);
            barcodeTxt = itemView.findViewById(R.id.itemBarcode);
            desTxt = itemView.findViewById(R.id.itemDes);
            locationTxt = itemView.findViewById(R.id.itemLoc);
        }

        void setAssetItem(AssetModel assetModel){
            barcodeTxt.setText(assetModel.getBarcode());
            desTxt.setText(assetModel.getDescription());
            locationTxt.setText(assetModel.getLocation());

        }
    }
}
