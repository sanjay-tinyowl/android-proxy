package com.lechucksoftware.proxy.proxysettings.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.lechucksoftware.proxy.proxysettings.R;
import com.lechucksoftware.proxy.proxysettings.db.TagEntity;
import com.shouldit.proxy.lib.utils.ProxyUtils;
import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Marco on 08/12/13.
 */
public class InputBypass extends LinearLayout
{
    private LinearLayout bypassContainer;
    private TextView titleTextView;
    private String title;
    private boolean fullsize;
    private boolean readonly;
    private List<String> exclusionList;

    public InputBypass(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        readStyleParameters(context,attrs);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.input_bypass, this);

        if (v != null)
        {
            titleTextView = (TextView) v.findViewById(R.id.field_title);
            bypassContainer = (LinearLayout) v.findViewById(R.id.bypass_container);

            refreshUI();
        }
    }

    private void refreshUI()
    {
        if (!TextUtils.isEmpty(title))
        {
            titleTextView.setText(title.toUpperCase());
        }

        bypassContainer.removeAllViews();
        if (exclusionList != null && exclusionList.size() > 0)
        {
            for(String bypass : exclusionList)
            {
                InputField i = new InputField(getContext());
                i.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        ((InputField)view).setEnabled(true);
                    }
                });

                i.setReadonly(true);
                i.setValue(bypass);
                bypassContainer.addView(i);
            }
        }

        // Always add the new empty field
        InputField i = new InputField(getContext());
        i.setHint("Add bypass address");
        i.setReadonly(false);
        bypassContainer.addView(i);
    }

    protected void readStyleParameters(Context context, AttributeSet attributeSet)
    {
        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.InputFieldTags);

        try
        {
            title = a.getString(R.styleable.InputField_title);
            fullsize = a.getBoolean(R.styleable.InputField_fullsize, false);
            readonly = a.getBoolean(R.styleable.InputField_readonly, false);
        }
        finally
        {
            a.recycle();
        }
    }

    public void setExclusionString(String exclusionString)
    {
        if (TextUtils.isEmpty(exclusionString))
        {

        }
        else
        {
            exclusionList = new ArrayList<String>();
            exclusionList.addAll(Arrays.asList(ProxyUtils.parseExclusionList(exclusionString)));
        }

        refreshUI();
    }
}
