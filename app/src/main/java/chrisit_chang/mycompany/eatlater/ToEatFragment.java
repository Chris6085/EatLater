package chrisit_chang.mycompany.eatlater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;


public class ToEatFragment extends ListFragment {

    //used by the key of bundle for restaurantId
    public static final String SHOWING_ACTIVITY_RES_ID = "chrisit_chang.myCompany.eatLater.RestaurantId";

    private static final String TAG = "ToEatFragment";

    protected static final int MENU_BUTTON_1 = Menu.FIRST;
    protected static final int MENU_BUTTON_2 = Menu.FIRST + 1;

    // Store instance variables
    private int page;
    private int UNIQUE_FRAGMENT_GROUP_ID;


    // newInstance constructor for creating fragment with arguments
    public static ToEatFragment newInstance(int page) {
        ToEatFragment toEatFragment = new ToEatFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        toEatFragment.setArguments(args);
        return toEatFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        UNIQUE_FRAGMENT_GROUP_ID = page;

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
        return inflater.inflate(R.layout.toeat_fragment, container, false);
//        TextView tvLabel = (TextView) view.findViewById(R.id.textView);
//        tvLabel.setText(page + " -- " + title);

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
        long restaurantId = restaurant.getId();

        //start intent
        Intent intent = new Intent(getActivity(), ShowingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong(SHOWING_ACTIVITY_RES_ID, restaurantId);
        bundle.putInt(MainActivity.CHOOSE_ACTIVITY, MainActivity.REQUEST_UPDATE);

        intent.putExtras(bundle);
        startActivityForResult(intent, MainActivity.REQUEST_ID_SHOWING_ACTIVITY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            //show and update operation
            case MainActivity.REQUEST_ID_SHOWING_ACTIVITY:
                if(resultCode == Activity.RESULT_OK) {

                    //update view with new data after update
                    this.updateListView();
                    Toast toast = Toast. makeText(getActivity() , "update is completed", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
        }
    }

    //set ContextMenu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //設定長按選單的表頭
        menu.setHeaderTitle("Further operations");
        menu.add(UNIQUE_FRAGMENT_GROUP_ID, MENU_BUTTON_1, 0, R.string.delete);
        menu.add(UNIQUE_FRAGMENT_GROUP_ID, MENU_BUTTON_2, 0, R.string.back);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    //長壓提供刪除功能
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        //判斷被長壓的是哪一個fragment中的ContextMenu產生
        if (item.getGroupId() == UNIQUE_FRAGMENT_GROUP_ID) {
            //取得user選取資訊 (item on long press operation)
            AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
            switch(item.getItemId()) {
                //delete option
                case MENU_BUTTON_1:
                    //new DAO
                    RestaurantDAO restaurantDAO = new RestaurantDAO(getActivity());

                    //從選取的id經過adapter取出restaurant物件
                    Restaurant restaurant = (Restaurant) this.getListAdapter().getItem(info.position);
                    restaurantDAO.delete(restaurant.getId());

                    //update view with new data after delete
                    this.updateListView();
                    break;
                //back option
                case MENU_BUTTON_2:
                    //just back to MainActivity
                    break;
                default:
                    break;
            }
        }
        return super.onContextItemSelected(item);
    }

    public void updateListView() {

        //new DAO
        RestaurantDAO restaurantDAO = new RestaurantDAO(getActivity());
        //update
        setListAdapter(new RestaurantAdapter(getActivity(), R.layout.single_restaurant, restaurantDAO.getAll()));

    }
}
