package chrisit_chang.mycompany.eatlater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import chrisit_chang.mycompany.eatlater.DB.Restaurant;


public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {
    private static final String TAG = "RestaurantAdapter";
    // 畫面資源編號  layout_file
    private int mResource;

    private final Context mContext;
    // 包裝的記事資料
    private List<Restaurant> mRestaurants;

    public RestaurantAdapter(Context context, int resource, List<Restaurant> restaurants) {
        this.mContext = context;
        this.mResource = resource;
        this.mRestaurants = restaurants;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            mTextView = (TextView) itemView.findViewById(R.id.title_text);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View contactView = LayoutInflater.from(context).inflate(mResource, parent, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Restaurant restaurant = mRestaurants.get(position);
        holder.mTextView.setText(restaurant.getTitle());
    }

    @Override
    public int getItemCount() {
        return mRestaurants == null ? 0 : mRestaurants.size();
    }

    // 設定指定編號的記事資料
    public void set(int index, Restaurant restaurant) {
        if (index >= 0 && index < mRestaurants.size()) {
            mRestaurants.set(index, restaurant);
            notifyDataSetChanged();
        }
    }

    // 讀取指定編號的記事資料
    public Restaurant get(int index) {
        return mRestaurants.get(index);
    }

    public void insertRestaurant(Restaurant restaurant) {

        //int lastIndex = mRestaurants.size();
        if (mRestaurants.add(restaurant)) {
            notifyDataSetChanged();
        }
    }


    public void removeRestaurant(int index) {
        mRestaurants.remove(index);
        notifyItemRemoved(index);
        notifyDataSetChanged();
    }
}