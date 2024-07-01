package com.iotsmartaliv.adapter;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.iotsmartaliv.R;
import com.iotsmartaliv.model.Images;

import java.util.ArrayList;

/**
 * This adapter class is used for offers .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class OffersDetailsPagerAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    private ArrayList<Images> imageBean;

    public OffersDetailsPagerAdapter(Context context, ArrayList<Images> imageBean) {
        this.context = context;
        this.imageBean = imageBean;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return imageBean.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.pager_img, container, false);
        ImageView ivPagerImage = itemView.findViewById(R.id.pagerimage);
        ivPagerImage.setImageResource(imageBean.get(position).getImages());
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
