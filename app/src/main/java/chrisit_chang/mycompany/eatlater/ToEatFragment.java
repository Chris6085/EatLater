package chrisit_chang.mycompany.eatlater;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


public class ToEatFragment extends ListFragment {

    public final static String SHOWING_UPDATING_ID = "chrisit_chang.myCompany.eatLater.RestaurantId";

    // Store instance variables
    private String title;
    private int page;



    // newInstance constructor for creating fragment with arguments
    public static ToEatFragment newInstance(int page, String title) {
        ToEatFragment toEatFragment = new ToEatFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        toEatFragment.setArguments(args);
        return toEatFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");

        //建立資料庫物件
        RestaurantDAO restaurantDAO = new RestaurantDAO(getContext());

        if (restaurantDAO.getCount() == 0) {
            restaurantDAO.sample();
        }

        setListAdapter(new RestaurantAdapter(getActivity(), R.layout.single_restaurant, restaurantDAO.getAll()));
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

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        //get item (Restaurant)
        Restaurant restaurant = (Restaurant) this.getListAdapter().getItem(position);

        //start intent
        Intent intent = new Intent(getActivity(),ShowingActivity.class);
        long restaurantId = restaurant.getId();
        intent.putExtra(SHOWING_UPDATING_ID, restaurantId);
        startActivity(intent);

    }
}
