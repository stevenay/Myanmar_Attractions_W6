package me.naylinaung.myanmar_attractions_w6.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.naylinaung.myanmar_attractions_w6.MyanmarAttractionsApp;
import me.naylinaung.myanmar_attractions_w6.R;
import me.naylinaung.myanmar_attractions_w6.adapters.AttractionAdapter;
import me.naylinaung.myanmar_attractions_w6.data.models.AttractionModel;
import me.naylinaung.myanmar_attractions_w6.data.persistence.AttractionsContract;
import me.naylinaung.myanmar_attractions_w6.data.vos.AttractionVO;
import me.naylinaung.myanmar_attractions_w6.utils.MyanmarAttractionsConstants;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttractionFragment extends Fragment {

    public AttractionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_attraction, container, false);
        return v;
    }

    public interface ControllerAttractionItem
    {
        public void onTapEvent(AttractionVO attraction, ImageView ivAttractionPhoto);
    }
}
