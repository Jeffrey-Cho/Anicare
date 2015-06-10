package sep.software.anicare.activity;

import de.greenrobot.event.EventBus;
import sep.software.anicare.AniCareException;
import sep.software.anicare.R;
import sep.software.anicare.fragment.AniCareFragment;
import sep.software.anicare.fragment.CareHistoryFragment;
import sep.software.anicare.fragment.ListFriendFragment;
import sep.software.anicare.fragment.MakeFriendFragment;
import sep.software.anicare.fragment.MessageBoxFragment;
import sep.software.anicare.fragment.SettingFragment;
import sep.software.anicare.model.AniCareMessage;
import sep.software.anicare.model.AniCarePet;
import sep.software.anicare.util.AniCareLogger;
import sep.software.anicare.view.CircleImageView;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;

public class MainActivity extends AniCareActivity {

    private final static String TAG = MainActivity.class.getSimpleName();

    private DrawerLayout mDrawerLayout;
    private LinearLayout mLinearLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mPlanetTitles;

    private CircleImageView mProfileImage;
    private TextView mPetName;

    // update the menu_main_ content by replacing fragments
    Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlanetTitles = getResources().getStringArray(R.array.anicare_menu);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLinearLayout = (LinearLayout) findViewById(R.id.left_drawer);
        mDrawerList = (ListView) findViewById(R.id.drawer_list);
        mProfileImage  = (CircleImageView) findViewById(R.id.drawer_profile);
        mPetName = (TextView) findViewById(R.id.pet_name);


        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        // set a custom shadow that overlays the menu_main_ content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(mThisActivity.getResources().getDrawable(R.drawable.custom_action_bar));

        mTitle = getTitle();
        mDrawerTitle = "Select Menu";

        AniCarePet myPet = mAniCareService.getCurrentPet();
        if (myPet != null) {
//            Picasso.with(mThisActivity).invalidate(mAniCareService.getPetImageUrl(myPet.getImageURL()));
            myPet.setImageURL(myPet.getId());
            mAniCareService.setPetImageInto(myPet, mProfileImage);
            mPetName.setText(myPet.getName());
        }

        if (savedInstanceState == null) {
            selectItem(0);
        }

        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }
        boolean checkedMessageBox = mAniCareService.getFlag("didCheckedMessageBox");
        if (mAniCareService.hasNotResolvedMessage() && !checkedMessageBox) {
//        if (false) {
            mAniCareService.saveFlag("didCheckedMessageBox", true);
            mDrawerList.setItemChecked(2, true);
            setTitle(mPlanetTitles[2]);
            mDrawerLayout.closeDrawer(mLinearLayout);

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, new MessageBoxFragment()
                    , MessageBoxFragment.class.getSimpleName()).commit();

        }
        AniCareLogger.log(mThisUser);
    }

    public void onEvent(Exception exception){
        AniCarePet myPet = mAniCareService.getCurrentPet();
        if (myPet == null) return;
        mPetName.setText(myPet.getName());
        mAniCareService.setPetImageInto(myPet, mProfileImage);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkSettings();
    }

    private void checkSettings() {
        if (!mAniCareService.isUserSet()) {
//        if (false) {
        //if (true) {
            Intent intent = new Intent();
            intent.setClass(this, UserSettingActivity.class);
            startActivity(intent);
            return;
        }

        if (!mAniCareService.isPetSet()) {
//        if (false) {
            Intent intent = new Intent();
            intent.setClass(this, PetSettingActivity.class);
            startActivity(intent);
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mLinearLayout);
        menu.findItem(R.id.action_menu0).setVisible(!drawerOpen);
        menu.findItem(R.id.action_menu1).setVisible(!drawerOpen);
        menu.findItem(R.id.action_menu2).setVisible(!drawerOpen);
        menu.findItem(R.id.action_menu3).setVisible(!drawerOpen);
        menu.findItem(R.id.action_test).setVisible(!drawerOpen);

        AniCareFragment myFragment = (AniCareFragment)getFragmentManager().findFragmentByTag(ListFriendFragment.class.getSimpleName());

        if (myFragment != null) {
            menu.findItem(R.id.action_menu0).setVisible(myFragment.isVisible());
            menu.findItem(R.id.action_menu1).setVisible(myFragment.isVisible());
            menu.findItem(R.id.action_menu2).setVisible(myFragment.isVisible());
            menu.findItem(R.id.action_menu3).setVisible(myFragment.isVisible());
            menu.findItem(R.id.action_test).setVisible(myFragment.isVisible());
        } else {
            menu.findItem(R.id.action_menu0).setVisible(false);
            menu.findItem(R.id.action_menu1).setVisible(false);
            menu.findItem(R.id.action_menu2).setVisible(false);
            menu.findItem(R.id.action_menu3).setVisible(false);
            menu.findItem(R.id.action_test).setVisible(false);
        }


        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        ListFriendFragment listFrag = (ListFriendFragment)fragment;
        switch(item.getItemId()) {
            case R.id.action_menu0:
                listFrag.refreshList(3);
                return true;
            case R.id.action_menu1:
                listFrag.refreshList(0);
                return true;
            case R.id.action_menu2:
                listFrag.refreshList(1);
                return true;
            case R.id.action_menu3:
                listFrag.refreshList(2);
                return true;
            case R.id.action_test:
                Intent intent = new Intent();
                intent.setClass(mThisActivity, TestActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {

        String tag = "";
        switch (position) {
            case 0:
                fragment = new ListFriendFragment();
                break;
            case 1:
                fragment = new MakeFriendFragment();
                break;
            case 2:
                fragment = new MessageBoxFragment();
                break;
            case 3:
                fragment = new CareHistoryFragment();
                break;
            case 4:
                fragment = new SettingFragment();
                break;
            default:
                break;
        }

//        Bundle args = new Bundle();
//        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
//        fragment.setArguments(args);
        tag = fragment.getClass().getSimpleName();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, tag).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mLinearLayout);
    }

    public ListView getDrawer() {
        return this.mDrawerList;
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void onEvent(AniCareMessage msg) {
        int position = mDrawerList.getCheckedItemPosition();
        if (position == 2) {
            String tag = MessageBoxFragment.class.getSimpleName();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, new MessageBoxFragment(), tag).commit();
        }
    }

}