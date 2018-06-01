package com.michalj.bugdetme;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), R.style.TimePickerTheme, dateSetListener, year, month, day);
    }

    private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int month, int day) {

                    // Formatting month after it is chosen
                    if ( month < 10 ) {
                        if ( day < 10 ) {
                            ((MainActivity) getActivity()).setChosenData(view.getYear() + "-0" +
                                    (view.getMonth() + 1) + "-0" + view.getDayOfMonth());
                        } else {
                            ((MainActivity) getActivity()).setChosenData(view.getYear() + "-0" +
                                    (view.getMonth() + 1) + "-" + view.getDayOfMonth());
                        }
                    } else {
                        if ( day < 10 ) {
                            ((MainActivity) getActivity()).setChosenData(view.getYear() + "-0" +
                                    (view.getMonth() + 1) + "-0" + view.getDayOfMonth());
                        } else {
                            ((MainActivity) getActivity()).setChosenData(view.getYear() + "-0" +
                                    (view.getMonth() + 1) + "-" + view.getDayOfMonth());
                        }
                    }
                }
            };
}