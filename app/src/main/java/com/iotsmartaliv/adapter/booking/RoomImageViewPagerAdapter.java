    package com.iotsmartaliv.adapter.booking;

    import android.content.Context;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import com.bumptech.glide.Glide;
    import com.iotsmartaliv.R;
    import com.iotsmartaliv.utils.Util;

    public class RoomImageViewPagerAdapter extends RecyclerView.Adapter<RoomImageViewPagerAdapter.ImageViewHolder> {

        private int[] imageList;
        private Context context;

        public RoomImageViewPagerAdapter(Context context, int[] imageList) {
            this.context = context;
            this.imageList = imageList;
        }

        @NonNull
        @Override
        public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_image_item, parent, false);
            return new ImageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
            int image = imageList[position];
            // Load image using Glide or any other method
            Glide.with(context).load(image).placeholder(R.mipmap.ic_room).into(holder.imageView);
            Util.setBrightness(holder.imageView, 2.0f);
        }

        @Override
        public int getItemCount() {
            return imageList.length;
        }

        public static class ImageViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public ImageViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.img_roomBooked);
            }
        }
    }