package com.pvk.krishna.sampledemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnLogoutListener;

public class MainFragment extends Fragment {

	protected static final String TAG = MainFragment.class.getName();
	private Button mButtonLogin;
	private Button mButtonLogout;
	private TextView mTextStatus;

	private SimpleFacebook mSimpleFacebook;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSimpleFacebook = SimpleFacebook.getInstance();
	}

	@Override
	public void onResume() {
		super.onResume();
		getActivity().setTitle("Simple Facebook Sample");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main, container, false);

		mButtonLogin = (Button) view.findViewById(R.id.button_login);
		mButtonLogout = (Button) view.findViewById(R.id.button_logout);
		mTextStatus = (TextView) view.findViewById(R.id.text_status);
		Button btnProfileDetails= (Button) view.findViewById(R.id.btn_profile_details);
		btnProfileDetails.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addFragment(new GetProfileFragment());
			}
		});

		setLogin();
		setLogout();

		setUIState();
		return view;
	}

	private void addFragment(Fragment fragment) {
		try {
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.frame_layout, fragment);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
		} catch (Exception e) {
			Log.e(TAG, "Failed to add fragment", e);
		}
	}

	/**
	 * Login example.
	 */
	private void setLogin() {
		// Login listener
		final OnLoginListener onLoginListener = new OnLoginListener() {

			@Override
			public void onFail(String reason) {
				mTextStatus.setText(reason);
				Log.w(TAG, "Failed to login");
			}

			@Override
			public void onException(Throwable throwable) {
				mTextStatus.setText("Exception: " + throwable.getMessage());
				Log.e(TAG, "Bad thing happened", throwable);
			}

			@Override
			public void onThinking() {
				// show progress bar or something to the user while login is
				// happening
				mTextStatus.setText("Thinking...");
			}

			@Override
			public void onLogin() {
				// change the state of the button or do whatever you want
				mTextStatus.setText("Logged in");
				loggedInUIState();
			}

			@Override
			public void onNotAcceptingPermissions(Permission.Type type) {
				mTextStatus.setText("Logged out");
				Toast.makeText(getActivity(), String.format("You didn't accept %s permissions", type.name()), Toast.LENGTH_SHORT).show();
			}
		};

		mButtonLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mSimpleFacebook.login(onLoginListener);
			}
		});
	}

	/**
	 * Logout example
	 */
	private void setLogout() {
		final OnLogoutListener onLogoutListener = new OnLogoutListener() {

			@Override
			public void onFail(String reason) {
				mTextStatus.setText(reason);
				Log.w(TAG, "Failed to login");
			}

			@Override
			public void onException(Throwable throwable) {
				mTextStatus.setText("Exception: " + throwable.getMessage());
				Log.e(TAG, "Bad thing happened", throwable);
			}

			@Override
			public void onThinking() {
				// show progress bar or something to the user while login is
				// happening
				mTextStatus.setText("Thinking...");
			}

			@Override
			public void onLogout() {
				// change the state of the button or do whatever you want
				mTextStatus.setText("Logged out");
				loggedOutUIState();
			}

		};

		mButtonLogout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mSimpleFacebook.logout(onLogoutListener);
			}
		});
	}

	private void setUIState() {
		if (mSimpleFacebook.isLogin()) {
			loggedInUIState();
		} else {
			loggedOutUIState();
		}
	}

	private void loggedInUIState() {
		mButtonLogin.setEnabled(false);
		mButtonLogout.setEnabled(true);
		mTextStatus.setText("Logged in");
	}

	private void loggedOutUIState() {
		mButtonLogin.setEnabled(true);
		mButtonLogout.setEnabled(false);
		mTextStatus.setText("Logged out");
	}
}