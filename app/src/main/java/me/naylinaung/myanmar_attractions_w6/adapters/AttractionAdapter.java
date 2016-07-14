package me.naylinaung.myanmar_attractions_w6.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import me.naylinaung.myanmar_attractions_w6.MyanmarAttractionsApp;
import me.naylinaung.myanmar_attractions_w6.R;
import me.naylinaung.myanmar_attractions_w6.data.vos.AttractionVO;
import me.naylinaung.myanmar_attractions_w6.views.holders.AttractionViewHolder;

/**
 * Created by NayLinAung on 7/13/2016.
 */
public class AttractionAdapter extends RecyclerView.Adapter<AttractionViewHolder> {

    private LayoutInflater mInflater;
    private List<AttractionVO> mAttractionList;
    private AttractionViewHolder.ControllerAttractionItem mControllerAttractionItem;

    public AttractionAdapter(List<AttractionVO> attractionList, AttractionViewHolder.ControllerAttractionItem controllerAttractionItem) {
        mInflater = LayoutInflater.from(MyanmarAttractionsApp.getContext());
        mAttractionList = attractionList;
        mControllerAttractionItem = controllerAttractionItem;
    }

    @Override
    public AttractionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.view_item_attraction, parent, false);
        return new AttractionViewHolder(itemView, mControllerAttractionItem);
    }

    @Override
    public void onBindViewHolder(AttractionViewHolder holder, int position) {
        holder.bindData(mAttractionList.get(position));
    }

    @Override
    public int getItemCount() {
        return mAttractionList.size();
    }

    public void setNewData(List<AttractionVO> newData) {
        mAttractionList = newData;
        notifyDataSetChanged();
    }
}
