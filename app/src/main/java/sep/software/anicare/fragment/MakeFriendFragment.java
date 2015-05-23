package sep.software.anicare.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

import sep.software.anicare.R;


public class MakeFriendFragment extends AniCareFragment {

    private static final String TAG = MakeFriendFragment.class.getSimpleName();

    private TextView fromDate;
    private TextView toDate;
    private int mYear;
    private int mMonth;
    private int mDay;

    private DatePickerDialog.OnDateSetListener fromDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            Log.d(TAG, "onDateSet");
            mYear=year;
            mMonth=monthOfYear;
            mDay=dayOfMonth;
            setFromDate();
        }
    };

    private DatePickerDialog.OnDateSetListener toDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear=year;
            mMonth=monthOfYear;
            mDay=dayOfMonth;
            setToDate();
        }
    };



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_make_friend, container, false);

        fromDate = (TextView)rootView.findViewById(R.id.from_period);
        toDate = (TextView)rootView.findViewById(R.id.to_period);

        fromDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG,"fromDate onClick");
                showFromDialog();
            }
        });

        toDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG,"toDate onClick");
                showToDialog();
            }
        });

        final Calendar mCalendar = Calendar.getInstance();

        mYear = mCalendar.get(Calendar.YEAR);
        mMonth= mCalendar.get(Calendar.MONTH);
        mDay  = mCalendar.get(Calendar.DAY_OF_MONTH);

        return rootView;

    }

    protected void showFromDialog() {
        Log.d(TAG,"showFromDialog");
        DatePickerDialog dialog = new DatePickerDialog(mThisActivity, fromDateSetListener, mYear, mMonth, mDay);
        dialog.show();
    }

    protected void showToDialog() {
        Log.d(TAG,"showToDialog");
        DatePickerDialog dialog = new DatePickerDialog(mThisActivity, toDateSetListener, mYear, mMonth, mDay);
        dialog.show();
    }

    private void setFromDate() {
        Log.d(TAG,"setFromDate");
        fromDate.setText(
                new StringBuilder()
                        .append(mYear).append("-")
                        .append(mMonth + 1).append("-")
                        .append(mDay).append(" ")
            );
    }

    private void setToDate() {
        toDate.setText(
                new StringBuilder()
                        .append(mYear).append("-")
                        .append(mMonth + 1).append("-")
                        .append(mDay).append(" ")
        );
    }

}
