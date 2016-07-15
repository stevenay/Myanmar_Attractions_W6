package me.naylinaung.myanmar_attractions_w6.fragments;


import android.content.DialogInterface;
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
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import me.naylinaung.myanmar_attractions_w6.MyanmarAttractionsApp;
import me.naylinaung.myanmar_attractions_w6.R;
import me.naylinaung.myanmar_attractions_w6.data.models.UserModel;
import me.naylinaung.myanmar_attractions_w6.data.persistence.AttractionsContract;
import me.naylinaung.myanmar_attractions_w6.data.vos.UserVO;
import me.naylinaung.myanmar_attractions_w6.events.DataEvent;
import me.naylinaung.myanmar_attractions_w6.utils.MyAlertDialog;
import me.naylinaung.myanmar_attractions_w6.utils.MyanmarAttractionsConstants;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    //region Members Variable Declaration
    @BindView(R.id.btn_register)
    Button btnRegister;

    @BindView(R.id.et_username)
    EditText etUserName;

    @BindView(R.id.et_email)
    EditText etUserEmail;

    @BindView(R.id.et_password)
    EditText etPassword;

    @BindView(R.id.et_dateofbirth)
    EditText etDateofBirth;

    @BindView(R.id.spn_country)
    Spinner spnCountry;
    //endregion

    //region Factory Method
    public static RegisterFragment newInstance()
    {
        return new RegisterFragment();
    }
    //endregion

    //region Life Cycle Methods
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, v);

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
    @OnClick(R.id.btn_register)
    public void btnRegisterClicked(Button button) {
        String name = etUserName.getText().toString();
        String email = etUserEmail.getText().toString();
        String password = etPassword.getText().toString();
        String dateOfBirth = etDateofBirth.getText().toString();
        String country = spnCountry.getSelectedItem().toString();

        if (!this.checkValidation(name, email, password, dateOfBirth, country)) return;

        getLoaderManager().restartLoader(MyanmarAttractionsConstants.USER_REGISTER_LOADER, null, this);
        UserModel.getInstance().postRegister(name, email, password, dateOfBirth, country);
    }

    public void onEventMainThread(DataEvent.RegisterErrorEvent event) {
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
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            UserVO user = UserVO.parseFromCursor(data);
            getActivity().finish();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    //endregion

    //region Utility Helper Methods
    private boolean checkValidation(String name, String email, String password, String dateOfBirth, String countryOfOrigin) {
        AlertDialog alertDialog = MyAlertDialog.createAlertDialog(getActivity(), "Alert");
        String errorMessage = "";

        if (TextUtils.isEmpty(name)) {
            errorMessage = "Name is required. \n";
        }
        if (TextUtils.isEmpty(email)) {
            errorMessage += "Email is required. \n";
        }
        if (TextUtils.isEmpty(password)) {
            errorMessage += "Password is required. \n";
        }
        if (TextUtils.isEmpty(dateOfBirth)) {
            errorMessage += "Date of Birth is required. \n";
        }
        if (TextUtils.isEmpty(countryOfOrigin)) {
            errorMessage += "Country is required. \n";
        }

        if (!errorMessage.isEmpty()) {
            alertDialog.setMessage(errorMessage);
            alertDialog.show();
            return false;
        }

        return true;
    }
    //endregion

}
