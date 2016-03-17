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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


public class EatenFragment extends ListFragment {

    //used by the key of bundle for restaurantId
    public static final String SHOWING_ACTIVITY_RES_ID
            = "chrisit_chang.myCompany.eatLater.RestaurantId";

    private static final String TAG = "EatenFragment";

    protected static final int MENU_BUTTON_1 = Menu.FIRST;
    protected static final int MENU_BUTTON_2 = Menu.FIRST + 1;

    // Store instance variables
    private int page;
    private int UNIQUE_FRAGMENT_GROUP_ID;

    //DAO
    private RestaurantDAO mRestaurantDAO;

    // newInstance constructor for creating fragment with arguments
    public static EatenFragment newInstance(int page) {
        EatenFragment eatenFragment = new EatenFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        eatenFragment.setArguments(args);


        return eatenFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        UNIQUE_FRAGMENT_GROUP_ID = page;

        mRestaurantDAO = new RestaurantDAO(getContext());

        if (mRestaurantDAO.getCount() == 0) {
            mRestaurantDAO.sample();
        }

        //get Eaten Data
        setListAdapter(new RestaurantAdapter(getActivity(), R.layout.single_restaurant
                , mRestaurantDAO.getAllOfRestaurantsWithFlag(RestaurantDAO.FLAG_EATEN)));
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_eaten, container, false);
//        TextView tvLabel = (TextView) view.findViewById(R.id.textView);
//        tvLabel.setText(page + " -- " + title);

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //final initialization
        //ContextMenu For Delete operation
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

        //update needed vars: action (update), restaurantId and page
        bundle.putLong(SHOWING_ACTIVITY_RES_ID, restaurantId);
        bundle.putInt(MainActivity.WHICH_PAGE, page);
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
                if (resultCode == Activity.RESULT_OK) {

                    //update view with new data after update
                    this.updateListView();
                    Toast toast = Toast.makeText(getActivity()
                            , "Update is completed", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
            default:
                Toast toast = Toast.makeText(getActivity()
                        , "something is wrong", Toast.LENGTH_SHORT);
                toast.show();
        }
    }

    //set ContextMenu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v
            , ContextMenu.ContextMenuInfo menuInfo) {
        //設定長按選單的表頭
        menu.setHeaderTitle("Further operations");
        menu.add(UNIQUE_FRAGMENT_GROUP_ID, MENU_BUTTON_1, 0, R.string.delete);
        menu.add(UNIQUE_FRAGMENT_GROUP_ID, MENU_BUTTON_2, 0, R.string.back);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    //long press for delete
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        //判斷被長壓的是哪一個fragment中的ContextMenu產生
        if (item.getGroupId() == UNIQUE_FRAGMENT_GROUP_ID) {
            //取得user選取資訊 (item on long press operation)
            AdapterView.AdapterContextMenuInfo info =
                    (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            switch (item.getItemId()) {
                //delete option
                case MENU_BUTTON_1:
                    //從選取的id經過adapter取出restaurant物件
                    Restaurant restaurant = (Restaurant) this.getListAdapter().getItem(info.position);
                    mRestaurantDAO.delete(restaurant.getId());

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
        //update
        setListAdapter(new RestaurantAdapter(getActivity(), R.layout.single_restaurant
                , mRestaurantDAO.getAllOfRestaurantsWithFlag(RestaurantDAO.FLAG_EATEN)));
    }
}
