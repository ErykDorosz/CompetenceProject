package com.example.myapplication;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.myapplication.databinding.FragmentFirstBinding;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;


@RequiresApi(api = Build.VERSION_CODES.N)

        ////////////////////////////////
///     //   OKNO GłÓWNE - FRAGMENT   //    ///
        ////////////////////////////////

public class FirstFragment extends Fragment
{

    private FragmentFirstBinding binding;

    ExpandableListView expandableListView;

    TreeMap<Date, List<Item>> DateMap = new TreeMap<>();

    HashMap<String, List<String>> listItem = new HashMap<>();
    ArrayList<String> listGroup = new ArrayList<>();

    MainAdapter adapter;
    java.text.SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    Database database;




    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    )
    {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

            ////////////////////////////////////////
    ///     //     UTWORZENIE ROZWIJANYCH LIST    //    ///
            ////////////////////////////////////////

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        database = activity.getDatabase();
        database.getEveryone();

        expandableListView = binding.expandableList;
        listGroup = new ArrayList<>();
        listItem = new HashMap<>();
        adapter = new MainAdapter(getActivity(), listGroup, listItem, (MainActivity) getActivity());
        expandableListView.setAdapter(adapter);

        initListData();

        if( listGroup.size() == 0)
        {
            TextView title = new TextView(getContext());
            title.setBackgroundColor(Color.WHITE);
            title.setTextSize(26);
            title.setTextColor(Color.BLACK);
            Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/sanserifbold.ttf");
            title.setTypeface(type);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            title.setPadding(width/8,height/2-100,width/8,0);
            title.setText("Brak dodanych produktów!");
            title.setTextColor(Color.GRAY);
            binding.Lista.addView(title);
        }


                ////////////////////////////////
        ///     //     PRZEJDZ DO DODAWANIA   //    ///
                //        PRODUKTU            //
                ////////////////////////////////

        binding.buttonFirst.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                listItem.clear();
                listGroup.clear();
                DateMap.clear();

                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);

            }
        });

        // buttonListOfProducts
        binding.buttonListOfProducts.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                listItem.clear();
                listGroup.clear();
                DateMap.clear();

                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_ThirdFragment);

            }
        });
    }

            ////////////////////////////////
    ///     //     INICJALIZACJA LISTY    //    ///
            ////////////////////////////////
    private void initListData() {
        listItem.clear();
        listGroup.clear();
        DateMap.clear();
        List<String> records = database.getProducts();
        if (records.size() != 0 && DateMap != null) {
            try {
                for (String record : records) {
                    String[] products = record.split("\t");
                    List<Item> items = new ArrayList<>();
                    Date date = sdf.parse(products[4]);

                    if (DateMap.containsKey(date)) {
                        Objects.requireNonNull(DateMap.get(date)).add(new Item(products[2], products[1], products[0], Integer.parseInt(products[3]), date)); // add to tree map
                    } else {
                        items.add(new Item(products[2], products[1], products[0], Integer.parseInt(products[3]), date));
                        DateMap.put(date, items);
                    }

                }

            } catch (ParseException e) {
                System.out.println("Incorrect date!");
            }


            for (int i = 0; i < DateMap.size(); i++) {
                Object key = DateMap.keySet().toArray()[i];
                for (int j = 0; j < DateMap.get(key).size(); j++) {
                    if (listGroup.contains(DateMap.get(key).get(j).subCategory)) {
                        continue;
                    }
                    listGroup.add(DateMap.get(key).get(j).subCategory);
                }
            }

            for (int category = 0; category < listGroup.size(); category++) {
                List<String> localItemList = new ArrayList<String>();
                for (int i = 0; i < DateMap.size(); i++) {
                    Object key = DateMap.keySet().toArray()[i];
                    for (int j = 0; j < DateMap.get(key).size(); j++) {
                        if (DateMap.get(key).get(j).subCategory.equals(listGroup.get(category))) {
                            String item = DateMap.get(key).get(j).name + "\nTermin ważności: " + sdf.format(DateMap.get(key).get(j).date) + "\nIlość: " + DateMap.get(key).get(j).amount;
                            localItemList.add(item);
                        }
                    }
                }
                listItem.put(listGroup.get(category), localItemList);
            }

            String[] array;
            List<String> list1 = new ArrayList<>();
            array = getResources().getStringArray(R.array.group1);
            list1.addAll(Arrays.asList(array));

            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }

}