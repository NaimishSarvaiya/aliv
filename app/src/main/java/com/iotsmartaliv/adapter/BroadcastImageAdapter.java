package com.iotsmartaliv.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;

import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.DocumentPdfActivity;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BroadcastImageAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ImageView imageView;
    private ArrayList<String> list;

    public BroadcastImageAdapter(Context context, ArrayList<String> itemList, boolean isInfinite) {
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
        View itemView = inflater.inflate(R.layout.broadcast_image_item, container, false);
        RoundedImageView ivPagerImage = itemView.findViewById(R.id.iv_item);

        if (list.get(position).endsWith(".pdf")){
            ivPagerImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.get()
                    .load(R.drawable.pdfimage)
                    .into(ivPagerImage);
            container.addView(itemView);
        }else {
            Picasso.get()
                    .load(list.get(position))
                    .into(ivPagerImage);
            container.addView(itemView);
        }


        ivPagerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (list.get(position).endsWith(".pdf")){
                    ArrayList<String> mUrl = new ArrayList<>();
                    mUrl.add(list.get(position));

                    Intent intent = new Intent(context, DocumentPdfActivity.class);
//                    intent.putExtra("PDFUrl",list.get(position));
                    intent.putStringArrayListExtra("PDFUrl",mUrl);
                    context.startActivity(intent);                }
//            }
        });
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}

