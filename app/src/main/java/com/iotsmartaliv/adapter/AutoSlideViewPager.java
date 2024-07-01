package com.iotsmartaliv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;

import com.iotsmartaliv.R;

import java.util.ArrayList;

public class AutoSlideViewPager extends PagerAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ImageView imageView;
    private ArrayList<Integer> list;

    public AutoSlideViewPager(Context context, ArrayList<Integer> itemList, boolean isInfinite) {
//        super(context, itemList, isInfinite);
        this.context = context;
        this.list = itemList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = inflater.inflate(R.layout.auto_slide_item, container, false);
        ImageView ivPagerImage = itemView.findViewById(R.id.iv_item);
        ivPagerImage.setImageResource(list.get(position));
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
  /*  @Override
    protected void bindView(View view, int i, int i1) {
        if (view != null) {
            imageView = view.findViewById(R.id.iv_item);
            imageView.setImageResource(list.get(i));
        }
    }

    @Override
    protected View inflateView(int i, ViewGroup viewGroup, int i1) {
        return inflater.inflate(R.layout.auto_slide_item, viewGroup, false);
    }

    public ImageView getImageView() {
        return imageView;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
*/
}

