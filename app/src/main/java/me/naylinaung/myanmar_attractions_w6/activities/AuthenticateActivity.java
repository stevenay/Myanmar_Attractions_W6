package me.naylinaung.myanmar_attractions_w6.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.naylinaung.myanmar_attractions_w6.MyanmarAttractionsApp;
import me.naylinaung.myanmar_attractions_w6.R;
import me.naylinaung.myanmar_attractions_w6.fragments.LoginFragment;
import me.naylinaung.myanmar_attractions_w6.fragments.RegisterFragment;

public class AuthenticateActivity extends AppCompatActivity {

    @BindView(R.id.tv_screen_title)
    TextView tvTitle;

    public enum ScreenType {
        LOGIN_SCREEN, REGISTER_SCREEN
    }

    private static final String IE_SCREEN_NAME = "IE_SCREEN_NAME";

    public static Intent newIntent(ScreenType screenType) {
        Intent intent = new Intent(MyanmarAttractionsApp.getContext(), AuthenticateActivity.class);
        intent.putExtra(IE_SCREEN_NAME, screenType);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            ScreenType screenName = (ScreenType) getIntent().getSerializableExtra(IE_SCREEN_NAME);

            if (findViewById(R.id.fl_container_2) != null) {
                RegisterFragment fragment = RegisterFragment.newInstance();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_container_2, fragment)
                        .commit();

                screenName = ScreenType.LOGIN_SCREEN;
            }

            Fragment fragment;
            switch (screenName) {
                case LOGIN_SCREEN:
                    fragment = LoginFragment.newInstance();
                    tvTitle.setText("Login");
                    break;
                case REGISTER_SCREEN:
                    fragment = RegisterFragment.newInstance();
                    tvTitle.setText("Register");
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown screen to navigate.");
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit();
        }
    }

}
