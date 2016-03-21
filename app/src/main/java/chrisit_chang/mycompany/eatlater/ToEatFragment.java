package chrisit_chang.mycompany.eatlater;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import chrisit_chang.mycompany.eatlater.DB.Restaurant;
import chrisit_chang.mycompany.eatlater.DB.RestaurantDAO;
import chrisit_chang.mycompany.eatlater.util.ItemClickSupport;


public class ToEatFragment extends Fragment {

    ListViewUpdateListener mCallback;

    //used by the key of bundle for restaurantId
    public static final String SHOWING_ACTIVITY_RES_ID
            = "chrisit_chang.myCompany.eatLater.RestaurantId";

    private static final String TAG = "ToEatFragment";

    // Store instance variables
    private int page;
    private int UNIQUE_FRAGMENT_GROUP_ID;

    //DAO
    private RestaurantDAO mRestaurantDAO;

    private static final String KEY_LAYOUT_MANAGER = "layoutManager";

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView mRecyclerView;
    protected RestaurantAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    // Container Activity must implement this interface
    public interface ListViewUpdateListener {
        void eatenFragmentUpdate(Restaurant restaurant);
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
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.toeat_fragment, container, false);
        rootView.setTag(TAG);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.to_eat_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

//        if (savedInstanceState != null) {
//            // Restore saved layout manager type.
//            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
//                    .getSerializable(KEY_LAYOUT_MANAGER);
//        }

        mAdapter = new RestaurantAdapter(getContext(), R.layout.single_restaurant
                , mRestaurantDAO.getAllOfRestaurantsWithFlag(RestaurantDAO.FLAG_NOT_EATEN));
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);

        //set onclick function
        setOnClickFunction();

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            Bundle bundle = data.getExtras();
            //get data from intent
            int position = bundle.getInt(ShowingActivity.ITEM_POSITION);
            int option = bundle.getInt(ShowingActivity.OPTION);
            Restaurant restaurant = (Restaurant) bundle.getSerializable(ShowingActivity.PASSING_RESTAURANT);

            switch (option) {
                case ShowingActivity.OPTION_DELETE:
                    mAdapter.removeRestaurant(position);
                    break;
                case ShowingActivity.OPTION_UPDATE:
                    mAdapter.set(position, restaurant);
                    break;
                case ShowingActivity.OPTION_CHECK:
                    //after check option, toEatFragment should be remove the designed restaurant
                    mAdapter.removeRestaurant(position);
                    mCallback.eatenFragmentUpdate(restaurant);
            }
        }
    }

    public void setOnClickFunction() {
        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                //從選取的id經過adapter取出restaurant物件
                //Restaurant restaurant = (RestaurantAdapter) mRecyclerView.getAdapter().getItem(position);

                Restaurant restaurant = mAdapter.get(position);
                long restaurantId = restaurant.getId();

                //start intent
                Intent intent = new Intent(getContext(), ShowingActivity.class);
                Bundle bundle = new Bundle();

                //update needed vars: action (update), restaurantId and page
                bundle.putLong(SHOWING_ACTIVITY_RES_ID, restaurantId);
                bundle.putInt(MainActivity.WHICH_PAGE, page);
                bundle.putInt(MainActivity.CHOOSE_ACTIVITY, MainActivity.REQUEST_UPDATE);

                //for mAdapter.removeRestaurant(position) use
                //first send to ShowingActivity and take back in ActivityResult
                bundle.putInt(ShowingActivity.ITEM_POSITION, position);

                intent.putExtras(bundle);
                startActivityForResult(intent, MainActivity.REQUEST_ID_SHOWING_ACTIVITY);
            }
        });
    }
}
