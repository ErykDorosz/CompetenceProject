package com.example.myapplication;

import com.example.myapplication.databinding.FragmentThirdBinding;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ThirdFragment extends Fragment
{
    private FragmentThirdBinding binding;
    ArrayList<String> itemsList = new ArrayList<String>();
    java.text.SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    LinearLayout linearLayout;
    Database database;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    )
    {

        binding = FragmentThirdBinding.inflate(inflater, container, false);
        linearLayout = binding.getRoot().findViewById(R.id.linearLayoutId);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        database = activity.getDatabase();
        database.getEveryone();
        itemsList = new ArrayList<>();

        try
        {
            initListData();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }

    private void padd()
    {
        TextView padding = new TextView(getContext());
        padding.setBackgroundColor(Color.GRAY);
        padding.setTextSize(1);
        padding.setText("                                ");
        linearLayout.addView(padding);
    }

    private void initListData() throws ParseException
    {
        itemsList.clear();
        itemsList = database.getList();

        TextView title = new TextView(getContext());
        title.setBackgroundColor(Color.WHITE);
        title.setTextSize(26);
        title.setTextColor(Color.BLACK);
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/sanserifbold.ttf");
        title.setTypeface(type);
        title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        if( itemsList.size() > 0)
        {
            title.setText("\nLISTA PRODUKTÓW:\n");
            linearLayout.addView(title);
            padd();
            for( String item:itemsList)
            {
                setTextStyle(item);
            }
        }
        else
        {
            title.setText("\nLISTA JEST PUSTA!\n");
            linearLayout.addView(title);
            padd();
        }

    }
    void setTextColor(Date date, TextView textView)
    {
        // Date difference:
        Date todayDate = new Date();

        long difference_In_Time
                = date.getTime() - todayDate.getTime();
        long difference_In_Days
                = (difference_In_Time
                / (1000 * 60 * 60 * 24))
                % 365;

        if( difference_In_Days > 7)
        {
            textView.setTextColor(Color.rgb(0,200,0));
        }
        else if( difference_In_Days > 3)
        {
            textView.setTextColor(Color.rgb(255,140,0));
        }
        else
        {
            textView.setTextColor(Color.RED);
        }
    }

    private void setTextStyle(String item) throws ParseException
    {
        TextView textView = new TextView(getContext());
        textView.setBackgroundColor(Color.WHITE);
        textView.setTextSize(24);
        Date date = getDate(item);
        setTextColor(date, textView);
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/sanserifbold.ttf");
        textView.setTypeface(type);
        textView.setText(item);

        linearLayout.addView(textView);
        padd();

    }

    private Date getDate(String item) throws ParseException
    {
        String[] products = item.split("Data: ");
        return sdf.parse(products[1]);
    }

}