package sep.software.anicare.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sep.software.anicare.R;
import sep.software.anicare.activity.MainActivity;
import sep.software.anicare.interfaces.EntityCallback;
import sep.software.anicare.model.AniCarePet;
import sep.software.anicare.util.AniCareLogger;
import sep.software.anicare.view.AniCareButton;


public class MakeFriendFragment extends AniCareFragment implements AdapterView.OnItemSelectedListener{

    private static final String TAG = MakeFriendFragment.class.getSimpleName();

    private TextView fromDate;
    private TextView toDate;
    private int mYear;
    private int mMonth;
    private int mDay;
    private AniCareButton confirm;
    private AniCareButton cancel;
    private DatePickerDialog dialog;

    private Spinner petCategory;
    private RadioGroup petSize;

    private AniCarePet.Category category;
    private AniCarePet.Size size;

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

            //return date2.after(date1);
            if (date1.equals(date2)) {
                return true;
            } else if (date2.after(date1)) {
                return true;
            }
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
        petCategory = (Spinner) rootView.findViewById(R.id.pet_setting_category);
        petSize = (RadioGroup) rootView.findViewById(R.id.pet_size);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mThisActivity, R.array.pet_category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        petCategory.setAdapter(adapter);
        petCategory.setOnItemSelectedListener(this);


        // buttons
        confirm = (AniCareButton)rootView.findViewById(R.id.confirm);
        cancel =  (AniCareButton)rootView.findViewById(R.id.cancel);

        switch(mThisPet.getRawCategory()) {
            case 0: // dog
                petCategory.setSelection(0);
                break;
            case 1: // cat
                petCategory.setSelection(1);
                break;
            case 2: // birds
                petCategory.setSelection(2);
                break;
            case 3: // etc
                petCategory.setSelection(3);
                break;
        }

        switch(mThisPet.getRawSize()) {
            case 3: // big
                petSize.check(R.id.pet_size_large);
                break;
            case 2:
                petSize.check(R.id.pet_size_medium);
                break;
            case 1: // small
                petSize.check(R.id.pet_size_small);
                break;
        }




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

                if (checkValidDate()) {

                    size = checkSize();

                    String from = parseDateTime(fromDate.getText().toString());
                    String to = parseDateTime(toDate.getText().toString());

                    AniCarePet pet = mThisPet;
                    pet.setStartDate(from);
                    pet.setEndDate(to);
                    pet.setCategory(category);
                    pet.setSize(size);
                    pet.setImageURL(pet.getId());
                    pet.setAddress1(mThisUser.getAddress1());
                    pet.setAddress2(mThisUser.getAddress2());
                    pet.setAddress3(mThisUser.getAddress3());
                    mAppContext.showProgressDialog(mThisActivity);
                    Picasso.with(mThisActivity).invalidate(mAniCareService.getPetImageUrl(pet.getId()));
                    mAniCareService.makeFriend(pet, new EntityCallback<AniCarePet>() {
                        @Override
                        public void onCompleted(AniCarePet entity) {
                            mAppContext.dismissProgressDialog();
                            //Toast t = Toast.makeText(mThisActivity, "Complete make friend with S: "+ size.getValue() +"C: " +category.getValue(), Toast.LENGTH_LONG); // for debugging
                            Toast t = Toast.makeText(mThisActivity, "Complete", Toast.LENGTH_LONG); // for debugging
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                        }
                    });
                } else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(mThisActivity);
                    builder1.setTitle("Alert");
                    builder1.setMessage("Please check your date");
                    builder1.setCancelable(true);
                    builder1.setNeutralButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // return to list Friends

                mAniCareService.removeFriend(mThisPet, new EntityCallback<Boolean>() {
                    @Override
                    public void onCompleted(Boolean entity) {
                        if (entity) {
                            Fragment fragment = new ListFriendFragment();
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

                            // update selected item and title, then close the drawer

                            ((MainActivity) mThisActivity).getDrawer().setItemChecked(0, true);
                            mThisActivity.getActionBar().setTitle(getResources().getStringArray(R.array.anicare_menu)[0]);
                        } else {
                            new AlertDialog.Builder(mThisActivity)
                                    .setMessage("Removing friend failed. Try again.")
                                    .setPositiveButton("ok", null)
                                    .show();
                        }

                    }
                });



            }
        });


        return rootView;

    }

    private AniCarePet.Size checkSize() {
        switch(petSize.getCheckedRadioButtonId()) {
            case R.id.pet_size_large:
                size = AniCarePet.Size.BIG;
                break;
            case R.id.pet_size_medium:
                size = AniCarePet.Size.MIDDLE;
                break;
            case R.id.pet_size_small:
                size = AniCarePet.Size.SMALL;
                break;
            default:
                size = AniCarePet.Size.MIDDLE;
                break;
        }

        return size;
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getSelectedItemPosition()) {
            case 0:
                category = AniCarePet.Category.DOG;
                break;
            case 1:
                category = AniCarePet.Category.CAT;
                break;
            case 2:
                category = AniCarePet.Category.BIRD;
                break;
            case 3:
                category = AniCarePet.Category.ETC;
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        category = AniCarePet.Category.DOG;
    }
}
