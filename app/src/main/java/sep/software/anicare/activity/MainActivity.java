package sep.software.anicare.activity;


import java.util.ArrayList;

import sep.software.anicare.R;
import sep.software.anicare.adapter.NavDrawerListAdapter;
import sep.software.anicare.view.CircleImageView;
import sep.software.anicare.view.NavDrawerItem;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

	
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private RelativeLayout mDrawerLinear;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	private final String TAG = "saltfactory.net";
	private CircleImageView imageViewprofile;
	private TextView nickName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mTitle = mDrawerTitle = getTitle();
		// load slide menu items
//		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
//		navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

//		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//		mDrawerLinear = (RelativeLayout) findViewById(R.id.left_drawer);
//		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
		
		// ImageView for Kakao profile
//		imageViewprofile = (CircleImageView) findViewById(R.id.sf_imageview_profile);
		
//		nickName = (TextView) findViewById(R.id.text);
		// myimg.setImageResource(R.drawable.sampleprofile);
		// myimg.setImageResource(R.drawable.sampleprofile);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons
				.getResourceId(0, -1)));
		// Find People
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons
				.getResourceId(1, -1)));
		// Photos
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons
				.getResourceId(2, -1)));
		// Communities, Will add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons
				.getResourceId(3, -1)));
		// Find Hospital
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons
				.getResourceId(4, -1)));
		// Setting
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons
				.getResourceId(5, -1)));
		// 게시판
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons
				.getResourceId(6, -1)));

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// display view for selected nav drawer item
//				displayView(position);
			}
		});

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_launcher, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();

			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();

			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		// set color trans whild drawer is open
		mDrawerLayout.setScrimColor(this.getResources().getColor(R.color.drawer_shadow));

		if (savedInstanceState == null) {
			// on first time display view for first nav item
//			displayView(0);
		}
		
	}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
	
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		int id = item.getItemId();
//		// toggle nav drawer on selecting action bar app icon/title
//		if (mDrawerToggle.onOptionsItemSelected(item)) {
//			return true;
//		}
//		FragmentManager fragmentManager = getSupportFragmentManager();
//		switch (id) {
//		case R.id.action_settings:
//			return true;
//			// case R.id.actionbar_call:
//			// // Intent dialer= new Intent(Intent.ACTION_DIAL);
//			// // startActivity(dialer);
//			// Toast.makeText(getApplicationContext(),"Status Clicked",Toast.LENGTH_SHORT).show();
//			// return true;
//		case R.id.board:
//			Fragment board = new FragmentBoard();
//			fragmentManager.beginTransaction()
//					.replace(R.id.frame_container, board).commit();
//			setTitle(navMenuTitles[6]);
//			return true;
//		case R.id.actionbar_torch:
//			Bundle args = new Bundle();
//			args.putString("Menu", "You pressed done button.");
//			Fragment detail = new FragmentFlash();
//			detail.setArguments(args);
//			fragmentManager.beginTransaction()
//					.replace(R.id.frame_container, detail).commit();
//			// Toast.makeText(getApplicationContext(),"Status Clicked",Toast.LENGTH_SHORT).show();
//			setTitle(navMenuTitles[7]);
//			return true;
//		default:
//			return super.onOptionsItemSelected(item);
//		}
//	}
}
