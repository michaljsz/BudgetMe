package com.michalj.bugdetme;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ExpensesListFragment extends Fragment {

    private final String[] from = new String[] { DatabaseHelper.DATE, DatabaseHelper.AMOUNT,
            DatabaseHelper.TYPE, DatabaseHelper.DESCRIPTION};
    private final int[] to = new int[] { R.id.dateDisplay, R.id.amountDisplay, R.id.typeIconDisplay,
            R.id.descriptionDisplay};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_expenses_list, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        DBManager dbManager = new DBManager(getActivity());
        dbManager.open();
        Cursor cursor = dbManager.fetch();
        ListView listView = view.findViewById(R.id.expanses_list);
        listView.setEmptyView(view.findViewById(R.id.empty));
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), R.layout.activity_view_record, cursor, from, to, 0);
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            public boolean setViewValue(View aView, Cursor aCursor, int aColumnIndex) {
                if (aColumnIndex == 2) {
                    double amountInZl = Double.parseDouble(aCursor.getString(aColumnIndex))/100;
                    TextView textView = (TextView) aView;
                    textView.setText(String.format("%.2f",amountInZl) + " zl");
                    return true;
                }
                if (aColumnIndex == 3) {
                    ImageView img = (ImageView) aView;
                    String type = aCursor.getString(aColumnIndex);
                    if (type.equalsIgnoreCase("fmcg")) {
                        img.setImageResource(R.drawable.fmcg);
                    } else if (type.equalsIgnoreCase("Utilities")) {
                        img.setImageResource(R.drawable.utilities);
                    } else if (type.equalsIgnoreCase("transport")) {
                        img.setImageResource(R.drawable.car);
                    } else if (type.equalsIgnoreCase("kids")) {
                        img.setImageResource(R.drawable.kids);
                    } else if (type.equalsIgnoreCase("leisure")) {
                            img.setImageResource(R.drawable.leisure);
                    } else if (type.equalsIgnoreCase("health")) {
                        img.setImageResource(R.drawable.health);
                    } else if (type.equalsIgnoreCase("clothes")) {
                        img.setImageResource(R.drawable.clothes);
                    }
                    return true;
                }
                return false;
            }
        });
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
    }
}
