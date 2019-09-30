package com.azhar.madrep.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.azhar.madrep.Model.CBObat;
import com.azhar.madrep.R;

import java.util.ArrayList;
import java.util.List;

public class CBObatAdapter extends ArrayAdapter<CBObat> {
    private Context mContext;
    private ArrayList<CBObat> listState;
    private CBObatAdapter CBObatAdapter;
    private boolean isFromView = false;
    private String selectedObat = null;

    public CBObatAdapter(Context context, int resource, List<CBObat> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.listState = (ArrayList<CBObat>) objects;
        this.CBObatAdapter = this;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView,
                              ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.select_obat, null);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView
                    .findViewById(R.id.text);
            holder.mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTextView.setText(listState.get(position).getTitle());
        isFromView = true;
        holder.mCheckBox.setChecked(listState.get(position).isSelected());
        isFromView = false;

        if ((position == 0)) {
            holder.mCheckBox.setVisibility(View.INVISIBLE);
        } else {
            holder.mCheckBox.setVisibility(View.VISIBLE);
        }
        holder.mCheckBox.setTag(position);
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int getPosition = (Integer) buttonView.getTag();

                if (!isFromView) {
                    listState.get(position).setSelected(isChecked);

                }
            }
        });

        return convertView;
    }

    private void getSelected() {
        for (int i = 0; i<listState.size();i++){
            if (listState.get(i).isSelected() == true){
                if (selectedObat == null){
                    selectedObat= ""+listState.get(i).getTitle();
                }
                else{
                    selectedObat = selectedObat+", "+listState.get(i).getTitle();
                }
            }
        }
    }
    public String getSelectedObat(){
        selectedObat = null;
        getSelected();
        return selectedObat;
    }

    private class ViewHolder {
        private TextView mTextView;
        private CheckBox mCheckBox;
    }

}
