package com.szh.tricount.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.szh.tricount.BrowseActivity;
import com.szh.tricount.MainActivity;
import com.szh.tricount.R;
import com.szh.tricount.SettingActivity;
import com.szh.tricount.customview.DrawView;
import com.szh.tricount.datas.DataList;
import com.szh.tricount.listener.RemoveRadioCheckedChangeListener;
import com.szh.tricount.utils.ObjectSerializeUtil;
import com.szh.tricount.utils.ToastUtil;

/**
 * Created by szh on 2017/2/17.
 */
public class LeftFragment extends Fragment implements View.OnClickListener {

    private Button equilateralButton;
    private Button isocelesButton;
    private Button rectangularButton;
    private Button squareButton;
    private RadioGroup radioRemoveGroup;
    private TextView setting;

    private TextView collection;

    private TextView browse;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.left_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initEvents();
    }

    private void initView(View view) {
        radioRemoveGroup = (RadioGroup) view.findViewById(R.id.radio_remove_group);
        equilateralButton = (Button) view.findViewById(R.id.button_equilateral);
        isocelesButton = (Button) view.findViewById(R.id.button_isosceles);
        rectangularButton = (Button) view.findViewById(R.id.button_rectangular);
        squareButton = (Button) view.findViewById(R.id.square);
        setting = (TextView) view.findViewById(R.id.set);
        collection = (TextView) view.findViewById(R.id.collection);
        browse = (TextView) view.findViewById(R.id.browse);
    }

    private void initEvents() {
        radioRemoveGroup.setOnCheckedChangeListener(new RemoveRadioCheckedChangeListener());
        equilateralButton.setOnClickListener(this);
        isocelesButton.setOnClickListener(this);
        rectangularButton.setOnClickListener(this);
        squareButton.setOnClickListener(this);
        setting.setOnClickListener(this);
        collection.setOnClickListener(this);
        browse.setOnClickListener(this);
    }

    public TextView getCollection() {
        return collection;
    }

    @Override
    public void onClick(View v) {
        DrawView drawView = MainActivity.getDrawView();
        switch (v.getId()) {
            case R.id.button_equilateral:
                if (DataList.getLines().size() != 0) {
                    toastAndClose();
                    return;
                }
                ((MainActivity) getActivity()).getDrawerLayout().closeDrawer(Gravity.LEFT);
                drawView.initDrawEquilateral(drawView.getLeft(), drawView.getTop(), drawView.getRight(), drawView.getBottom());
                break;
            case R.id.button_isosceles:
                if (DataList.getLines().size() != 0) {
                    toastAndClose();
                    return;
                }
                ((MainActivity) getActivity()).getDrawerLayout().closeDrawer(Gravity.LEFT);
                drawView.initDrawIsosceles(drawView.getLeft(), drawView.getTop(), drawView.getRight(), drawView.getBottom());
                break;
            case R.id.button_rectangular:
                if (DataList.getLines().size() != 0) {
                    toastAndClose();
                    return;
                }
                ((MainActivity) getActivity()).getDrawerLayout().closeDrawer(Gravity.LEFT);
                drawView.initDrawRectangular(drawView.getLeft(), drawView.getTop(), drawView.getRight(), drawView.getBottom());
                break;
            case R.id.square:
                if (DataList.getLines().size() != 0) {
                    toastAndClose();
                    return;
                }
                ((MainActivity) getActivity()).getDrawerLayout().closeDrawer(Gravity.LEFT);
                drawView.initDrawSquare(drawView.getLeft(), drawView.getTop(), drawView.getRight(), drawView.getBottom());
                break;
            case R.id.set:
                startActivity(new Intent(getContext(), SettingActivity.class));
                getActivity().overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
                break;
            case R.id.collection:
                ObjectSerializeUtil.serializeList(getContext());
                collection.setEnabled(false);
                break;
            case R.id.browse:
                startActivity(new Intent(getContext(), BrowseActivity.class));
                getActivity().overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
                break;
        }
    }

    private void toastAndClose() {
        ToastUtil.toast(this.getContext(), R.string.requestClear);
        ((MainActivity) getActivity()).getDrawerLayout().closeDrawer(Gravity.LEFT);
    }
}
