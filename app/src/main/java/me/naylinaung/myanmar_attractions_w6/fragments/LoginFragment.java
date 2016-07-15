package me.naylinaung.myanmar_attractions_w6.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import me.naylinaung.myanmar_attractions_w6.R;
import me.naylinaung.myanmar_attractions_w6.activities.HomeActivity;
import me.naylinaung.myanmar_attractions_w6.data.models.UserModel;
import me.naylinaung.myanmar_attractions_w6.data.persistence.AttractionsContract;
import me.naylinaung.myanmar_attractions_w6.data.vos.UserVO;
import me.naylinaung.myanmar_attractions_w6.events.DataEvent;
import me.naylinaung.myanmar_attractions_w6.utils.MyAlertDialog;
import me.naylinaung.myanmar_attractions_w6.utils.MyanmarAttractionsConstants;

/**
 * Created by NayLinAung on 7/14/2016.
 */
public class LoginFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    //region Members Variable Declaration
    @BindView(R.id.btn_login)
    Button btnLogin;

    @BindView(R.id.et_email)
    EditText etUserEmail;

    @BindView(R.id.et_password)
    EditText etPassword;

    private String userEmail;

    public static final LoginFragment newInstance() {
        return new LoginFragment();
    }
    //endregion

    //region LifeCycleMethods
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, v);

        userEmail = etUserEmail.getText().toString();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        // for Error Notification
        EventBus eventBus = EventBus.getDefault();
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBus eventBus = EventBus.getDefault();
        eventBus.unregister(this);
    }
    //endregion

    //region Events
    @OnClick(R.id.btn_login)
    public void btnLoginClicked(Button button) {
        userEmail = etUserEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (!this.checkValidation(userEmail, password)) return;

        getLoaderManager().restartLoader(MyanmarAttractionsConstants.USER_LOGIN_LOADER, null, this);
        UserModel.getInstance().postLogin(userEmail, password);
    }

    public void onEventMainThread(DataEvent.LoginErrorEvent event) {
        String errorMessage = event.getErrorMessage();

        AlertDialog alertDialog = MyAlertDialog.createAlertDialog(getActivity(), "Sorry");
        alertDialog.setMessage(errorMessage);
        alertDialog.show();
    }
    //endregion

    //region LoaderManager
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(),
                AttractionsContract.UserEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        // Useless in this API implementation
        // AttractionsContract.UserEntry.buildUserUriWithEmail(this.userEmail),
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            getActivity().finish();
            // This check is useless in the Current API Implementation
            // if (user.getEmail().equals(userEmail)) {
            //    getActivity().finish();
            // }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    //endregion

    //region Utility Helper Methods
    private boolean checkValidation(String email, String password) {
        AlertDialog alertDialog = MyAlertDialog.createAlertDialog(getActivity(), "Alert");
        String errorMessage = "";

        if (TextUtils.isEmpty(email)) {
            errorMessage = "Please enter your email";
        }
        if (TextUtils.isEmpty(password)) {
            errorMessage += (!errorMessage.isEmpty()) ? " and password" : "Please enter your password";
        }

        if (!errorMessage.isEmpty()) {
            errorMessage += ".";
            alertDialog.setMessage(errorMessage);
            alertDialog.show();
            return false;
        }

        return true;
    }


    //endregion
}
