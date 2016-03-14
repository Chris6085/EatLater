package chrisit_chang.mycompany.eatlater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    //for requestCode of startActivityForResult()
    public static final int REQUEST_ID_SHOWING_ACTIVITY = 0;
    public static final int REQUEST_ID_ADDING_ACTIVITY = 1;

    //for operation of user's choice
    public static final String CHOOSE_ACTIVITY = "chrisit_chang.mycompany.eatlater.choose.option";
    public static final int REQUEST_ADD = 0;
    public static final int REQUEST_UPDATE = 1;

    private static final String TAG = "MainActivity";
    TabLayout mTabs;
    MyPagerAdapter MyPagerAdapter;
    ViewPager mVpPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set TabLayout_Tab
        mTabs = (android.support.design.widget.TabLayout) findViewById(R.id.tabs);
        mTabs.addTab(mTabs.newTab().setText("ToEat"));
        mTabs.addTab(mTabs.newTab().setText("Eaten"));

        //set TabLayout_ViewPager
        mVpPager = (ViewPager) findViewById(R.id.vpPager);
        MyPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mVpPager.setAdapter(MyPagerAdapter);
        mVpPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabs));


        //set FloatingActionButton
        //goto Adding Activity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ShowingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(MainActivity.CHOOSE_ACTIVITY, MainActivity.REQUEST_ADD);

                intent.putExtras(bundle);
                startActivityForResult(intent, MainActivity.REQUEST_ID_ADDING_ACTIVITY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            //add operation
            case MainActivity.REQUEST_ID_ADDING_ACTIVITY:
                if(resultCode == Activity.RESULT_OK) {

                    //get PagerAdapter
                    MyPagerAdapter myPagerAdapter = (MyPagerAdapter) mVpPager.getAdapter();
                    if(myPagerAdapter != null) {
                        //get fragment
                        ToEatFragment toEatFragment = (ToEatFragment) myPagerAdapter.getRegisteredFragment(mVpPager.getCurrentItem());

                        //update view with new data after add
                        toEatFragment.updateListView();
                    } else {
//                        Log.d(TAG, "myPagerAdapter == null");
                    }
                } else {
                    Toast toast = Toast. makeText(MainActivity.this , "The draft is dropped", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //implement ViewPagerAdapter
    public static class MyPagerAdapter extends FragmentPagerAdapter {

        private static int NUM_ITEMS = 2;

        //紀錄key to fragment的array
        private SparseArray<Fragment> registeredFragments = new SparseArray<>();

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
//                    Log.d(TAG, "getItem="+ position);
                    return ToEatFragment.newInstance(0);

                case 1: // Fragment # 0 - This will show FirstFragment different title
                    //TODO: implement the eaten fragment and number without hardcode
//                    Log.d(TAG, "getItem="+ position);
                    return ToEatFragment.newInstance(1);
                default:
                    return null;
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //Log.d(TAG, "instantiateItem/+position" + position);
            Object obj = super.instantiateItem(container, position);
            if (obj instanceof Fragment) {
                // record the fragment tag here.
                Fragment f = (Fragment) obj;
                registeredFragments.put(position, f);
            }
            return obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

//        @Override
//        public int getItemPosition(Object object) {
//            return super.getItemPosition(object);
//        }

        //從sparseArray中取出fragment by position(key)
        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }
}
