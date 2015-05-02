package sep.software.anicare.view;

import sep.software.anicare.activity.AniCareActivity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class AniCareDialogFragment extends DialogFragment {

	public static final String INTENT_KEY = "DIALOG_INTENT_KEY";

	protected AniCareActivity mActivity;
	protected AniCareDialogFragment mThisFragment;

	public AniCareDialogFragment() {
		super();
		mThisFragment = this;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = (AniCareActivity)getActivity();
	}


	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		setStyle(STYLE_NO_TITLE, 0);
		return super.onCreateDialog(savedInstanceState);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().setCanceledOnTouchOutside(true);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
}
