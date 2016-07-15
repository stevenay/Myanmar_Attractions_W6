package me.naylinaung.myanmar_attractions_w6.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import me.naylinaung.myanmar_attractions_w6.MyanmarAttractionsApp;
import me.naylinaung.myanmar_attractions_w6.R;
import me.naylinaung.myanmar_attractions_w6.adapters.AttractionAdapter;
import me.naylinaung.myanmar_attractions_w6.data.models.AttractionModel;
import me.naylinaung.myanmar_attractions_w6.data.persistence.AttractionsContract;
import me.naylinaung.myanmar_attractions_w6.data.vos.AttractionVO;
import me.naylinaung.myanmar_attractions_w6.data.vos.UserVO;
import me.naylinaung.myanmar_attractions_w6.events.DataEvent;
import me.naylinaung.myanmar_attractions_w6.utils.MyanmarAttractionsConstants;
import me.naylinaung.myanmar_attractions_w6.views.holders.AttractionViewHolder;

public class HomeActivity extends AppCompatActivity
        implements AttractionViewHolder.ControllerAttractionItem,
        NavigationView.OnNavigationItemSelectedListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.rv_attractions)
    RecyclerView rvAttractions;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    private View header;
    private Button btnNavToLogin;
    private Button btnNavToRegister;
    private Button btnLogout;

    private AttractionAdapter mAttractionAdapter;
    private String mLoggedInUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Menu leftMenu = navigationView.getMenu();
        navigationView.setNavigationItemSelectedListener(this);
        header = navigationView.getHeaderView(0);

        btnNavToLogin = (Button) header.findViewById(R.id.btn_nav_to_login);
        if (btnNavToLogin != null)
            btnNavToLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Intent intent = AuthenticateActivity.newIntent(AuthenticateActivity.ScreenType.LOGIN_SCREEN);
                    drawerLayout.closeDrawers();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(intent);
                        }
                    }, 100);
                }
            });

        btnNavToRegister = (Button) header.findViewById(R.id.btn_nav_to_register);
        if (btnNavToRegister != null)
            btnNavToRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = AuthenticateActivity.newIntent(AuthenticateActivity.ScreenType.REGISTER_SCREEN);
                    drawerLayout.closeDrawers();
                    startActivity(intent);
                }
            });

        btnLogout = (Button) header.findViewById(R.id.btn_nav_to_logout);
        if (btnLogout != null)
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserVO.deleteUser(mLoggedInUserEmail);
                    bindHeaderData(null);
                }
            });

        List<AttractionVO> attractionList = AttractionModel.getInstance().getAttractionList();
        mAttractionAdapter = new AttractionAdapter(attractionList, this);
        rvAttractions.setAdapter(mAttractionAdapter);

        int gridColumnSpanCount = getResources().getInteger(R.integer.attraction_list_grid);
        rvAttractions.setLayoutManager(new GridLayoutManager(getApplicationContext(), gridColumnSpanCount));

        getSupportLoaderManager().initLoader(MyanmarAttractionsConstants.ATTRACTION_LIST_LOADER, null, this);
        getSupportLoaderManager().initLoader(MyanmarAttractionsConstants.USER_EXISTED_LOADER, null, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onTapAttraction(AttractionVO attraction, ImageView ivAttraction) {
        Intent intent = AttractionDetailActivity.newIntent(attraction.getTitle());
        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                new Pair(ivAttraction, getString(R.string.attraction_list_detail_transition_name)));
        ActivityCompat.startActivity(this, intent, activityOptions.toBundle());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case MyanmarAttractionsConstants.ATTRACTION_LIST_LOADER:
                return new CursorLoader(this,
                        AttractionsContract.AttractionEntry.CONTENT_URI, null, null, null, null);
            case MyanmarAttractionsConstants.USER_EXISTED_LOADER:
                return new CursorLoader(this,
                        AttractionsContract.UserEntry.CONTENT_URI, null, null, null, null);
            default:
                throw new UnsupportedOperationException("Unsupported Loader");
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case MyanmarAttractionsConstants.ATTRACTION_LIST_LOADER:
                List<AttractionVO> attractionList = new ArrayList<>();
                if (data != null && data.moveToFirst()) {
                    do {
                        AttractionVO attraction = AttractionVO.parseFromCursor(data);
                        attraction.setImages(AttractionVO.loadAttractionImagesByTitle(attraction.getTitle()));
                        attractionList.add(attraction);
                    } while (data.moveToNext());
                }

                Log.d(MyanmarAttractionsApp.TAG, "Retrieved attractions : " + attractionList.size());
                mAttractionAdapter.setNewData(attractionList);
                break;

            case MyanmarAttractionsConstants.USER_EXISTED_LOADER:
                if (data != null && data.moveToFirst()) {
                    UserVO user = UserVO.parseFromCursor(data);
                    mLoggedInUserEmail = user.getEmail();
                    bindHeaderData(user);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unsupported Loader");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void bindHeaderData(UserVO userVO) {
        TextView tvEmail = (TextView) header.findViewById(R.id.tv_email);
        TextView tvUserName = (TextView) header.findViewById(R.id.tv_username);
        ImageView ivAvatar = (ImageView) header.findViewById(R.id.iv_avatar);

        if (userVO == null) {
            tvEmail.setText("");
            tvUserName.setText("");

            ivAvatar.setVisibility(View.INVISIBLE);

            btnNavToLogin.setVisibility(View.VISIBLE);
            if (btnNavToRegister != null) btnNavToRegister.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.GONE);
        } else {
            tvEmail.setText(userVO.getEmail());
            tvUserName.setText(userVO.getName());

            ivAvatar.setVisibility(View.VISIBLE);
            ivAvatar.setImageResource(R.mipmap.ic_launcher);

            btnNavToLogin.setVisibility(View.GONE);
            if (btnNavToRegister != null) btnNavToRegister.setVisibility(View.GONE);
            btnLogout.setVisibility(View.VISIBLE);
        }
    }

    //region Menu and Navigation
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem menuItem) {
        menuItem.setChecked(true);
        drawerLayout.closeDrawers();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (menuItem.getItemId()) {
                    case R.id.left_menu_myanmar_attraction:
                        //navigateToJoke(jokeIndex);
                        break;
                }
            }
        }, 100); //to close drawer smoothly.

        return true;
    }
    //endregion
}
