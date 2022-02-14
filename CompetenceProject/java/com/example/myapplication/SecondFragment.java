package com.example.myapplication;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.myapplication.databinding.FragmentSecondBinding;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

        /////////////////////////////////////////
///     //     OKNO DODAWANIA - FRAGMENT 2     //    ///
        /////////////////////////////////////////

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    //////////////////////////////////////////////////
    private int nCounter = 1;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    String todayDate = sdf.format(new Date());
    String date = todayDate;
    Database database;
    Item item;

    /////////////////////////////////////////////////
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }
            ////////////////////////////////
    ///     //     POLA EDYCYJNE          //    ///
            ////////////////////////////////

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.productTextInput.setHint("Produkt");
        binding.productTextInput.setHintTextColor(Color.BLACK);

                ////////////////////////////////
        ///     //     KALENDARZ              //    ///
                ////////////////////////////////

        binding.Date.setText(todayDate);

        binding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                binding.Date.setTextColor(Color.BLACK);
                date = dayOfMonth + "/" + (month + 1) + "/" + year;
                binding.Date.setText(date);
            }
        });
                //////////////////////////////////////
        ///     //     PRZYCISKI DO ZMIANY          //    ///
                //            ILOSCI                //
                //////////////////////////////////////

        binding.ButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nCounter < 99) nCounter++;
                binding.TextAmount.setText(Integer.toString(nCounter));
            }
        });

        binding.ButtonSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nCounter > 1) nCounter--;
                binding.TextAmount.setText(Integer.toString(nCounter));
            }
        });

                ////////////////////////////////
        ///     //     SPINNER KATEGORII      //    ///
                ////////////////////////////////

        binding.spinnerKategorie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("Kategoria " + binding.spinnerKategorie.getSelectedItem());

                String kategoria = binding.spinnerKategorie.getSelectedItem().toString();
                String[] resources = new String[0];

                switch (kategoria) {
                    case "Nabiał":
                        resources = getResources().getStringArray(R.array.Nabiał);
                        break;

                    case "Pieczywo":
                        resources = getResources().getStringArray(R.array.Pieczywo);
                        break;

                    case "Mięso":
                        resources = getResources().getStringArray(R.array.Mięso);
                        break;

                    case "Napoje":
                        resources = getResources().getStringArray(R.array.Napoje);
                        break;

                    case "Słodycze":
                        resources = getResources().getStringArray(R.array.Słodycze);
                        break;

                    case "Sosy i dodatki":
                        resources = getResources().getStringArray(R.array.Sosy_i_dodatki);
                        break;

                    case "Kawy herbaty kakao":
                        resources = getResources().getStringArray(R.array.Kawy_herbaty_kakao);
                        break;

                    case "Produkty sypkie":
                        resources = getResources().getStringArray(R.array.Produkty_sypkie);
                        break;

                    case "Lody mrożonki":
                        resources = getResources().getStringArray(R.array.Lody_mrożonki);
                        break;

                    case "Alkohole":
                        resources = getResources().getStringArray(R.array.Alkohole);
                        break;

                    case "Przekąski":
                        resources = getResources().getStringArray(R.array.Przekąski);
                        break;
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, resources);
                binding.spinnerPodkategoria.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

            //////////////////////////////////////
    ///     // GUZIK OD ZATWIERDZENIA PRODUKTU: //   ///
            //////////////////////////////////////

        binding.additonButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view)
            {

                boolean checkTextInput = checkIfTextInputOfProductIsCorrect();
                if( !checkTextInput) { return; }

                MainActivity activity = (MainActivity) getActivity();
                database = activity.getDatabase();
                database.getEveryone();

                Date date1= null;
                try {
                    date1 = new SimpleDateFormat("dd/MM/yyyy").parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                boolean checkDateInput = checkIfDateInputIsCorrect(date1);
                if( !checkDateInput) { return; }

                // Sprawdz czy produkt istnieje:
                int checker = database.checkIfItemAlreadyExists( binding.productTextInput.getText().toString(),
                        binding.spinnerPodkategoria.getSelectedItem().toString(),date1 );

                if( checker > 0 )
                {
                    database.updateExistingProduct(binding.productTextInput.getText().toString(),
                            binding.spinnerPodkategoria.getSelectedItem().toString(),date1, nCounter);
                    NavHostFragment.findNavController(SecondFragment.this)
                            .navigate(R.id.action_SecondFragment_to_FirstFragment);
                    return;
                }
                item = new Item(binding.productTextInput.getText().toString(),binding.spinnerPodkategoria.getSelectedItem().toString(),
                        binding.spinnerKategorie.getSelectedItem().toString(),nCounter,date1);
                database.addProduct(item);

                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });


        ///////////////////////////////

    }

    // Sprawdzenie TextInputu Produktu:
    boolean checkIfTextInputOfProductIsCorrect()
    {

        if(binding.productTextInput.getText().toString().contains("\n") || binding.productTextInput.getText().toString().contains("'")
                || binding.productTextInput.getText().toString().contains("\""))
        {
            binding.productTextInput.getText().clear();
            binding.productTextInput.setHint("Niepoprawna nazwa produktu!");
            binding.productTextInput.setHintTextColor(Color.RED);

            return false;
        }
        if(binding.productTextInput.getText().length() == 0){
            binding.productTextInput.setHint("Wprowadź nazwę produktu!");
            binding.productTextInput.setHintTextColor(Color.RED);

            return false;
        }

        return true;

    }

    // Sprawdzenie daty:
    boolean checkIfDateInputIsCorrect(Date date)
    {
        Date now = new Date();
        Date tomorrow = new Date(now.getTime() - (1000 * 60 * 60 * 24));
        if(date.before(tomorrow)){
            binding.Date.setText("Zła data!");
            binding.Date.setTextColor(Color.RED);

            return false;
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}