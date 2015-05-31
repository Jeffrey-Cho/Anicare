package sep.software.anicare.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sep.software.anicare.R;
import sep.software.anicare.interfaces.EntityCallback;
import sep.software.anicare.model.AniCarePet;


public class MakeFriendFragment extends AniCareFragment {

    private static final String TAG = MakeFriendFragment.class.getSimpleName();

    private TextView fromDate;
    private TextView toDate;
    private int mYear;
    private int mMonth;
    private int mDay;
    private Button confirm;
    private Button cancel;
    private DatePickerDialog dialog;

    private DatePickerDialog.OnDateSetListener fromDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
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


    // for checking the valid date
    private boolean checkValidDate() {
        if (fromDate.getText()!=null && toDate.getText() != null
                && fromDate.getTextSize() != 0 && toDate.getTextSize() != 0) {

            SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy-MM-dd");
            Date date1 = new Date();
            Date date2 = new Date();

            try {
                date1 = dateFormat.parse(fromDate.getText().toString());
                date2 = dateFormat.parse(toDate.getText().toString());


            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return date2.after(date1);
        }

        return false;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_make_friend, container, false);

        fromDate = (TextView)rootView.findViewById(R.id.from_period);
        toDate = (TextView)rootView.findViewById(R.id.to_period);


        // buttons
        confirm = (Button)rootView.findViewById(R.id.confirm);
        cancel =  (Button)rootView.findViewById(R.id.cancel);


        // select date event
        fromDate.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_UP:
                        dialog = new DatePickerDialog(mThisActivity, fromDateSetListener, mYear, mMonth, mDay);
                        dialog.show();
                        break;
                    default:
                        return false;

                }
                return true;
            }

        });

        toDate.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_UP:
                        dialog = new DatePickerDialog(mThisActivity, toDateSetListener, mYear, mMonth, mDay);
                        dialog.show();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

        final Calendar mCalendar = Calendar.getInstance();

        mYear = mCalendar.get(Calendar.YEAR);
        mMonth= mCalendar.get(Calendar.MONTH);
        mDay  = mCalendar.get(Calendar.DAY_OF_MONTH);


        // button events
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from = parseDateTime(fromDate.getText().toString());
                String to = parseDateTime(toDate.getText().toString());
                AniCarePet.Category category = AniCarePet.Category.DOG;
                AniCarePet.Size size = AniCarePet.Size.MIDDLE;

                AniCarePet pet = mThisPet;
                pet.setStartDate(from);
                pet.setEndDate(to);
                pet.setCategory(category);
                pet.setSize(size);
                mAppContext.showProgressDialog(mThisActivity);

                Picasso.with(mThisActivity).invalidate(mAniCareService.getPetImageUrl(pet.getId()));
                mAniCareService.makeFriend(pet, new EntityCallback<AniCarePet>() {
                    @Override
                    public void onCompleted(AniCarePet entity) {
                        mAppContext.dismissProgressDialog();
                    }
                });
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do nothing..??
            }
        });


        return rootView;

    }

    private String parseDateTime(String datetime) {

        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return (sd.format(sd.parse(datetime)).replace("-", ""));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
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
