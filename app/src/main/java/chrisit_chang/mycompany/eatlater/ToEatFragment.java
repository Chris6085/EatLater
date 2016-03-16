package chrisit_chang.mycompany.eatlater;

import android.app.Activity;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.Toast;


public class ToEatFragment extends ListFragment {

    ListViewUpdateListener mCallback;

    //used by the key of bundle for restaurantId
    public static final String SHOWING_ACTIVITY_RES_ID = "chrisit_chang.myCompany.eatLater.RestaurantId";

    private static final String TAG = "ToEatFragment";

    protected static final int MENU_BUTTON_1 = Menu.FIRST;
    protected static final int MENU_BUTTON_2 = Menu.FIRST + 1;

    // Store instance variables
    private int page;
    private int UNIQUE_FRAGMENT_GROUP_ID;

    //DAO
    private RestaurantDAO mRestaurantDAO;

    // Container Activity must implement this interface
    public interface ListViewUpdateListener {
        public void eatenFragmentUpdate();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (ListViewUpdateListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ListViewUpdateListener");
        }
    }

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

        mRestaurantDAO = new RestaurantDAO(getContext());

        if (mRestaurantDAO.getCount() == 0) {
            mRestaurantDAO.sample();
        }

        //get toEat Data
        setListAdapter(new RestaurantAdapter(getActivity(), R.layout.single_restaurant
                , mRestaurantDAO.getAllOfRestaurantsWithFlag(RestaurantDAO.FLAG_NOT_EATEN)));
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.toeat_fragment, container, false);
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

                    //TODO update another fragment
                    //update view with new data after update
                    this.updateListView();

                    //communicate with EatenFragment by MainActivity implemented interface mCallback
                    mCallback.eatenFragmentUpdate();
                }
                break;
            default:
                Toast toast = Toast.makeText(getActivity()
                        , "requestCode is wrong", Toast.LENGTH_SHORT);
                toast.show();
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
                , mRestaurantDAO.getAllOfRestaurantsWithFlag(RestaurantDAO.FLAG_NOT_EATEN)));
    }
}
