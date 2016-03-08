package chrisit_chang.mycompany.eatlater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


public class RestaurantAdapter extends ArrayAdapter<Restaurant> {

    // 畫面資源編號  layout_file
    private int resource;
    // 包裝的記事資料
    private List<Restaurant> restaurants;

    public RestaurantAdapter(Context context, int resource, List<Restaurant> restaurants) {
        super(context, resource, restaurants);
        this.resource = resource;
        this.restaurants = restaurants;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LinearLayout restaurantView;
        // 讀取目前位置的記事物件
        final Restaurant restaurant = getItem(position);

        if (convertView == null) {
            // 建立項目畫面元件
            restaurantView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li = (LayoutInflater)
                    getContext().getSystemService(inflater);
            li.inflate(resource, restaurantView, true);
        }
        else {
            restaurantView = (LinearLayout) convertView;
        }

        // 讀取標題
        TextView titleView = (TextView) restaurantView.findViewById(R.id.title_text);

        // 設定標題
        titleView.setText(restaurant.getTitle());

        return restaurantView;
    }

    // 設定指定編號的記事資料
    public void set(int index, Restaurant restaurant) {
        if (index >= 0 && index < restaurants.size()) {
            restaurants.set(index, restaurant);
            notifyDataSetChanged();
        }
    }

    // 讀取指定編號的記事資料
    public Restaurant get(int index) {
        return restaurants.get(index);
    }

}