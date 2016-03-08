package chrisit_chang.mycompany.eatlater;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class ToEatFragment extends ListFragment {
    // Store instance variables
    private String title;
    private int page;

    //資料庫物件
    private RestaurantDAO restaurantDAO;

    // newInstance constructor for creating fragment with arguments
    public static ToEatFragment newInstance(int page, String title) {
        ToEatFragment fragmentFirst = new ToEatFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
//        Log.d("newInstance", "page=" + page);
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

        //setListAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, WEEK_DATA));

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

        Toast toast = Toast. makeText(getContext() , "position=" + position, Toast.LENGTH_SHORT);
        toast.show();


//        Intent intent = new Intent(getContext(), AddingActivity.class);
//        EditText editText = (EditText) findViewById(R.id.edit_message);
//
//
//
//
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
//        startActivity(intent);
    }
}
