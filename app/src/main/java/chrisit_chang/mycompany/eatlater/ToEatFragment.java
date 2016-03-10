package chrisit_chang.mycompany.eatlater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class ToEatFragment extends ListFragment {

    public static final String SHOWING_UPDATING_ID = "chrisit_chang.myCompany.eatLater.RestaurantId";

    private static final String TAG = "ToEatFragment";

    protected static final int MENU_BUTTON_1 = Menu.FIRST;
    protected static final int MENU_BUTTON_2 = Menu.FIRST + 1;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");

        //new DAO
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //final initialization
        //set ContextMenu For Delete operation
        registerForContextMenu(getListView());
    }


    //handle the ListItem is being clicked
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        //從選取的id經過adapter取出restaurant物件
        Restaurant restaurant = (Restaurant) this.getListAdapter().getItem(position);

        //start intent
        Intent intent = new Intent(getActivity(),ShowingActivity.class);
        long restaurantId = restaurant.getId();
        intent.putExtra(SHOWING_UPDATING_ID, restaurantId);
        startActivityForResult(intent, MainActivity.REQUEST_ID_SHOWING_ACTIVITY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
//        for (Fragment fragment : getChildFragmentManager().getFragments()) {
//            fragment.onActivityResult(requestCode, resultCode, data);
//        }
        //TODO 新增之後需要重載

        Log.d(TAG, "get in to fragment onActivityResult");
        Log.d(TAG, "requestCode=" + requestCode);
        Log.d(TAG, "resultCode=" + resultCode);

        switch (requestCode)
        {
            //show and update operation
            case MainActivity.REQUEST_ID_SHOWING_ACTIVITY:
                if(resultCode == Activity.RESULT_OK) {
                    updateListView();
                    Log.d(TAG, "come in and Activity.RESULT_OK=" + Activity.RESULT_OK);
                    Log.d(TAG, "REQUEST_ID_SHOWING_ACTIVITY!!");
                    Toast toast = Toast. makeText(getActivity() , "notifyDataSetChanged called", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast. makeText(getActivity() , "in REQUEST_ID_SHOWING_ACTIVITY cancel", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
            //add operation
            case MainActivity.REQUEST_ID_ADDING_ACTIVITY:
                if(resultCode == Activity.RESULT_OK) {
                    updateListView();
                    Log.d(TAG, "come in and Activity.RESULT_OK=" + Activity.RESULT_OK);
                    Log.d(TAG, "REQUEST_ID_ADDING_ACTIVITY!!");
                    Toast toast = Toast. makeText(getActivity() , "notifyDataSetChanged called", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast. makeText(getActivity() , "in REQUEST_ID_ADDING_ACTIVITY cancel", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;

        }

        Log.d(TAG, "fragment onActivityResult end");
    }

    //set ContextMenu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        menu.setHeaderTitle("Further operations");	//設定長按選單的表頭
        menu.add(0, MENU_BUTTON_1, 0, "Delete");
        menu.add(0, MENU_BUTTON_2, 0, "Back");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        //取得user選取資訊 (item on long press operation)
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

        switch(item.getItemId()) {
            case MENU_BUTTON_1:
                //從選取的id經過adapter取出restaurant物件
                Restaurant restaurant = (Restaurant) this.getListAdapter().getItem(info.position);

                //new DAO and delete
                RestaurantDAO restaurantDAO = new RestaurantDAO(getActivity());
                restaurantDAO.delete(restaurant.getId());

                Toast toast = Toast. makeText(getActivity() , "Restaurant is deleted", Toast.LENGTH_SHORT);
                toast.show();
                break;
            case MENU_BUTTON_2:
                //just back to MainActivity
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    public void updateListView() {
        BaseAdapter baseAdapter =(BaseAdapter) this.getListAdapter();
        baseAdapter.notifyDataSetChanged();
    }
}
