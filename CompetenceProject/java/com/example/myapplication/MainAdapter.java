package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainAdapter extends BaseExpandableListAdapter
{

    Context context;
    List<String> listGroup;
    HashMap<String, List<String>> listItem;
    MainActivity activity;

    public MainAdapter(Context context, List<String> listGroup, HashMap<String, List<String>> listItem, MainActivity activity)
    {
        this.context = context;
        this.listGroup = listGroup;
        this.listItem = listItem;
        this.activity = activity;
    }

    @Override
    public int getGroupCount()
    {
        return listGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return this.listItem.get(this.listGroup.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return this.listGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return this.listItem.get(this.listGroup.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        String group = (String) getGroup(groupPosition);
        if (convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }

        TextView textView = convertView.findViewById(R.id.list_parent);
        textView.setText(group);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        String child = (String) getChild(groupPosition, childPosition);
        if (convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }
        TextView textView = convertView.findViewById(R.id.list_child);


        // Ustawienie koloru tekstu w liscie:

        textView.setText(child);
        String[] strings = child.split("\n");
        String[] data = strings[1].split(" ");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try
        {
            date = sdf.parse(data[2]);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        setTextColor(date, textView);

        // Delete button:
        ImageView delete = convertView.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Czy chcesz usunąć ten produkt?");
                builder.setCancelable(true);
                builder.setPositiveButton("TAK", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                        List<String> child = listItem.get(listGroup.get(groupPosition));
                        String[] strings = child.get(childPosition).split("\n");
                        String[] datasplit = strings[1].split(" ");

                        Database db = activity.getDatabase();
                        try
                        {
                            db.deleteNote(strings[0], datasplit[2]);
                        }
                        catch (ParseException e)
                        {
                            e.printStackTrace();
                        }
                        child.remove(childPosition);
                        if (child.size() == 0)
                            listGroup.remove(groupPosition);

                        if(listGroup.isEmpty()){
                            AlertDialog.Builder builderInfo = new AlertDialog.Builder(context);
                            builderInfo.setMessage("Wszystkie produkty zostały usunięte!");
                            builderInfo.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builderInfo.show();
                        }

                        notifyDataSetChanged();

                    }
                });
                builder.setNegativeButton("NIE", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        });

        /////////////////////////////               + / - buttons:

        // Additon button:
        ImageView addButton = convertView.findViewById(R.id.buttonAddOnList);
        addButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                List<String> child = listItem.get(listGroup.get(groupPosition));
                String[] strings = child.get(childPosition).split("\n");
                String[] data = strings[1].split(" ");
                String[] amount = strings[2].split(" ");
                String subcategory = (String) getGroup(groupPosition);
                String name = strings[0];
                System.out.println(name + " " + subcategory + " " + data[2] + " " + amount[1]);

                Database db = activity.getDatabase();

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date date = null;
                try
                {
                    date = sdf.parse(data[2]);
                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }

                int amountInt = db.updateExistingProduct(name, subcategory, date, 1);
                String refreshedProduct = name + "\nTermin ważności: " + data[2] + "\nIlość: " + amountInt;
                child.set(childPosition, refreshedProduct);
                notifyDataSetChanged();
            }

        });

        // Subtarction button:
        ImageView subButton = convertView.findViewById(R.id.buttonSubOnList);
        subButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                List<String> child = listItem.get(listGroup.get(groupPosition));
                String[] strings = child.get(childPosition).split("\n");
                String[] data = strings[1].split(" ");
                String[] amount = strings[2].split(" ");
                String subcategory = (String) getGroup(groupPosition);
                String name = strings[0];
                System.out.println(name + " " + subcategory + " " + data[2] + " " + amount[1]);

                Database db = activity.getDatabase();

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date date = null;
                try
                {
                    date = sdf.parse(data[2]);
                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }

                int amountInt = db.updateExistingProduct(name, subcategory, date, -1);
                if (amountInt != 0)
                {
                    String refreshedProduct = name + "\nTermin ważności: " + data[2] + "\nIlość: " + amountInt;
                    child.set(childPosition, refreshedProduct);
                    notifyDataSetChanged();
                }

            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
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
}
