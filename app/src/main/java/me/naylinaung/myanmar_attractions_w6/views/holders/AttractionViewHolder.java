package me.naylinaung.myanmar_attractions_w6.views.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.naylinaung.myanmar_attractions_w6.R;
import me.naylinaung.myanmar_attractions_w6.data.vos.AttractionVO;
import me.naylinaung.myanmar_attractions_w6.utils.MyanmarAttractionsConstants;

/**
 * Created by NayLinAung on 7/13/2016.
 */
public class AttractionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.tv_attraction_title)
    TextView tvAttractionTitle;

    @BindView(R.id.iv_attraction)
    ImageView ivAttraction;

    @BindView(R.id.tv_attraction_desc)
    TextView tvAttractionDesc;

    private ControllerAttractionItem mController;
    private AttractionVO mAttraction;

    public AttractionViewHolder(View itemView, ControllerAttractionItem controller) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
        mController = controller;
    }

    public void bindData(AttractionVO attraction) {
        mAttraction = attraction;
        tvAttractionTitle.setText(attraction.getTitle());
        tvAttractionDesc.setText(attraction.getDesc());

        String imageUrl = MyanmarAttractionsConstants.IMAGE_ROOT_DIR + attraction.getImages()[0];

        Glide.with(ivAttraction.getContext())
                .load(imageUrl)
                .centerCrop()
                .placeholder(R.drawable.stock_photo_placeholder)
                .error(R.drawable.stock_photo_placeholder)
                .into(ivAttraction);
    }

    @Override
    public void onClick(View view) {
        mController.onTapAttraction(mAttraction, ivAttraction);
    }

    public interface ControllerAttractionItem {
        void onTapAttraction(AttractionVO attraction, ImageView ivAttraction);
    }

}
