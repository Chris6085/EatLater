package chrisit_chang.mycompany.eatlater;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ToEatFragment extends ListFragment {
    // Store instance variables
    private String title;
    private int page;

    private RestaurantDAO restaurantDAO;

    String[] WEEK_DATA = {"Sun", "Mon", "Tue", "Wed", "Thr", "Fri", "Sat"};
    String[] title_restaurant = {"Sun", "Mon", "Tue", "Wed", "Thr", "Fri", "Sat"};

    // newInstance constructor for creating fragment with arguments
    public static ToEatFragment newInstance(int page, String title) {
        ToEatFragment fragmentFirst = new ToEatFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        Log.d("newInstance", "page=" + page);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");

        //建立資料庫物件
        restaurantDAO = new RestaurantDAO(getContext());

        if (restaurantDAO.getCount() == 0) {
            restaurantDAO.sample();
        }

        List<Restaurant> restaurantList = restaurantDAO.getAll();
        for (int i=0; i<restaurantList.size() ; i++) {

        }


        setListAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, WEEK_DATA));
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.toeat_fragment, container, false);
        TextView tvLabel = (TextView) view.findViewById(R.id.textView);
        tvLabel.setText(page + " -- " + title);
        return view;
    }
}
