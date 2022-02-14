package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Database extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "products.db";
    private static final int DATABASE_VERSION = 1;

    ArrayList<String> categories;
    ArrayList<String> alkohole;
    ArrayList<String> kawy_herbaty_kakao;
    ArrayList<String> lody_mrozonki;
    ArrayList<String> mieso;
    ArrayList<String> nabial;
    ArrayList<String> napoje;
    ArrayList<String> pieczywo;
    ArrayList<String> produkty_sypkie;
    ArrayList<String> przekaski;
    ArrayList<String> sosy_i_dodatki;
    ArrayList<String> slodycze;

    /////////////////////////////////////
    ///          KONSTRUKTOR          ///
    ////////////////////////////////////

    public Database(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM CATEGORIES";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);

        if (icount < 2)
        {
            databaseSetUp();

            upgradeCategories();

            upgradeSubcategories();
        }

    }

    ///////////////////////////////////////////
    ///          TWORZENIE TABEL            ///
    ///////////////////////////////////////////

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String query = "CREATE TABLE CATEGORIES (ID INTEGER PRIMARY KEY AUTOINCREMENT, CATEGORY_NAME TEXT)";
        db.execSQL(query);
        String query1 = "CREATE TABLE SUBCATEGORIES (ID INTEGER PRIMARY KEY AUTOINCREMENT, CATEGORY_ID REFERENCES CATEGORIES(ID), SUBCATEGORY_NAME TEXT)";
        db.execSQL(query1);
        String query2 = "CREATE TABLE PRODUCTS (ID INTEGER PRIMARY KEY AUTOINCREMENT, SUBCATEGORY_ID REFERENCES SUBCATEGORY(ID), PRODUCT_NAME TEXT, EXPIRY_DATE DATE, AMOUNT INTEGER)";
        db.execSQL(query2);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }

    ///////////////////////////////////////////
    ///            CZYSZCZENIE TABEL        ///
    ///////////////////////////////////////////
    public void databaseSetUp()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        /////////////
        // CLEAR : //
        /////////////

        String query = "DROP TABLE IF EXISTS CATEGORIES";
        db.execSQL(query);

        query = "DROP TABLE IF EXISTS SUBCATEGORIES";
        db.execSQL(query);

        query = "DROP TABLE IF EXISTS PRODUCTS";
        db.execSQL(query);

        //////////////
        // SET NEW: //
        //////////////

        query = "CREATE TABLE CATEGORIES (ID INTEGER PRIMARY KEY AUTOINCREMENT, CATEGORY_NAME TEXT)";
        db.execSQL(query);

        query = "CREATE TABLE SUBCATEGORIES (ID INTEGER PRIMARY KEY AUTOINCREMENT, CATEGORY_ID REFERENCES CATEGORIES(ID), SUBCATEGORY_NAME TEXT)";
        db.execSQL(query);

        query = "CREATE TABLE PRODUCTS (ID INTEGER PRIMARY KEY AUTOINCREMENT, SUBCATEGORY_ID REFERENCES SUBCATEGORY(ID), PRODUCT_NAME TEXT, EXPIRY_DATE DATE, AMOUNT INTEGER)";
        db.execSQL(query);
    }

    ////////////////////////////////////////////////
    ///          TWORZENIE KATEGORII            ///
    ///            I PODKATEGORII              ///
    /////////////////////////////////////////////

    public void upgradeCategories()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        //////////////////////////////////////////////
        //              KATEGORIE                   //
        //////////////////////////////////////////////

        categories = new ArrayList<>();
        categories.add("Nabiał");
        categories.add("Pieczywo");
        categories.add("Mięso");
        categories.add("Napoje");
        categories.add("Słodycze");
        categories.add("Sosy i dodatki");
        categories.add("Kawy herbaty kakao");
        categories.add("Produkty sypkie");
        categories.add("Lody mrożonki");
        categories.add("Alkohole");
        categories.add("Przekąski");
        Collections.sort(categories);

        for (String cat : categories)
        {
            contentValues.put("CATEGORY_NAME", cat);
            db.insert("CATEGORIES", null, contentValues);
        }

    }

    public void upgradeSubcategories()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        //////////////////////////////////////////////
        //          A L K O H O L E                 //
        //////////////////////////////////////////////

        alkohole = new ArrayList<>();
        alkohole.add("Piwo");
        alkohole.add("Wino");
        alkohole.add("Wódka");
        alkohole.add("Cydr");
        alkohole.add("Nalewki");
        alkohole.add("Rum");
        alkohole.add("Szampan");
        Collections.sort(alkohole);

        for (String subcat : alkohole)
        {
            contentValues.put("SUBCATEGORY_NAME", subcat);
            contentValues.put("CATEGORY_ID", categories.indexOf("Alkohole") + 1);
            db.insert("SUBCATEGORIES", null, contentValues);
        }

        //////////////////////////////////////////////
        //          KAWY HERBATY KAKAO              //
        //////////////////////////////////////////////

        kawy_herbaty_kakao = new ArrayList<>();
        kawy_herbaty_kakao.add("Kakao");
        kawy_herbaty_kakao.add("Herbata");
        kawy_herbaty_kakao.add("Kawa");
        kawy_herbaty_kakao.add("Czekolada pitna");
        Collections.sort(kawy_herbaty_kakao);

        for (String subcat : kawy_herbaty_kakao)
        {
            contentValues.put("SUBCATEGORY_NAME", subcat);
            contentValues.put("CATEGORY_ID", categories.indexOf("Kawy herbaty kakao") + 1);
            db.insert("SUBCATEGORIES", null, contentValues);
        }


        //////////////////////////////////////////////
        //          LODY MROZONKI                   //
        //////////////////////////////////////////////

        lody_mrozonki = new ArrayList<>();
        lody_mrozonki.add("Lody");
        lody_mrozonki.add("Mięso");
        lody_mrozonki.add("Ryby");
        lody_mrozonki.add("Pizza");
        lody_mrozonki.add("Zapiekanka");
        lody_mrozonki.add("Mrożone warzywa");
        Collections.sort(lody_mrozonki);

        for (String subcat : lody_mrozonki)
        {
            contentValues.put("SUBCATEGORY_NAME", subcat);
            contentValues.put("CATEGORY_ID", categories.indexOf("Lody mrożonki") + 1);
            db.insert("SUBCATEGORIES", null, contentValues);
        }


        //////////////////////////////////////////////
        //          M I E S O                       //
        //////////////////////////////////////////////

        mieso = new ArrayList<>();
        mieso.add("Szynka");
        mieso.add("Schab");
        mieso.add("Parówki");
        mieso.add("Kabanosy");
        mieso.add("Kiełbasa");

        Collections.sort(mieso);

        for (String subcat : mieso)
        {
            contentValues.put("SUBCATEGORY_NAME", subcat);
            contentValues.put("CATEGORY_ID", categories.indexOf("Mięso") + 1);
            db.insert("SUBCATEGORIES", null, contentValues);
        }

        //////////////////////////////////////////////
        //          N A B I A L                     //
        //////////////////////////////////////////////

        nabial = new ArrayList<>();
        nabial.add("Jogurt");
        nabial.add("Mleko");
        nabial.add("Ser");
        nabial.add("Kefir");
        nabial.add("Śmietana");
        nabial.add("Maślanka");
        Collections.sort(nabial);

        for (String subcat : nabial)
        {
            contentValues.put("SUBCATEGORY_NAME", subcat);
            contentValues.put("CATEGORY_ID", categories.indexOf("Nabiał") + 1);
            db.insert("SUBCATEGORIES", null, contentValues);
        }

        //////////////////////////////////////////////
        //          N A P O J E                     //
        //////////////////////////////////////////////

        napoje = new ArrayList<>();
        napoje.add("Woda");
        napoje.add("Sok");
        napoje.add("Nektary");
        napoje.add("Napoje gazowane");
        napoje.add("Energetyki");
        napoje.add("Napoje izotoniczne");
        napoje.add("Napoje bezalkoholowe");
        Collections.sort(napoje);

        for (String subcat : napoje)
        {
            contentValues.put("SUBCATEGORY_NAME", subcat);
            contentValues.put("CATEGORY_ID", categories.indexOf("Napoje") + 1);
            db.insert("SUBCATEGORIES", null, contentValues);
        }

        //////////////////////////////////////////////
        //          P I E C Z Y W O                 //
        //////////////////////////////////////////////

        pieczywo = new ArrayList<>();
        pieczywo.add("Chleb");
        pieczywo.add("Kajzerka");
        pieczywo.add("Bagietka");
        pieczywo.add("Rogalik");
        pieczywo.add("Chleb tostowy");
        Collections.sort(pieczywo);

        for (String subcat : pieczywo)
        {
            contentValues.put("SUBCATEGORY_NAME", subcat);
            contentValues.put("CATEGORY_ID", categories.indexOf("Pieczywo") + 1);
            db.insert("SUBCATEGORIES", null, contentValues);
        }

        //////////////////////////////////////////////
        //          PRODUKTY SYPKIE                 //
        //////////////////////////////////////////////

        produkty_sypkie = new ArrayList<>();
        produkty_sypkie.add("Kasza");
        produkty_sypkie.add("Makaron");
        produkty_sypkie.add("Ryż");
        produkty_sypkie.add("Płatki śniadaniowe");
        produkty_sypkie.add("Mąka");
        Collections.sort(produkty_sypkie);

        for (String subcat : produkty_sypkie)
        {
            contentValues.put("SUBCATEGORY_NAME", subcat);
            contentValues.put("CATEGORY_ID", categories.indexOf("Produkty sypkie") + 1);
            db.insert("SUBCATEGORIES", null, contentValues);
        }

        //////////////////////////////////////////////
        //          PRZEKASKI                       //
        //////////////////////////////////////////////

        przekaski = new ArrayList<>();
        przekaski.add("Czipsy");
        przekaski.add("Paluszki");
        przekaski.add("Orzeszki");
        przekaski.add("Precle");
        Collections.sort(przekaski);

        for (String subcat : przekaski)
        {
            contentValues.put("SUBCATEGORY_NAME", subcat);
            contentValues.put("CATEGORY_ID", categories.indexOf("Przekąski") + 1);
            db.insert("SUBCATEGORIES", null, contentValues);
        }

        //////////////////////////////////////////////
        //          SOSY I DODATKI                  //
        //////////////////////////////////////////////

        sosy_i_dodatki = new ArrayList<>();
        sosy_i_dodatki.add("Majonez");
        sosy_i_dodatki.add("Keczup");
        sosy_i_dodatki.add("Sos czosnkowy");
        sosy_i_dodatki.add("Sos tysiąca wysp");
        sosy_i_dodatki.add("Chrzan");
        sosy_i_dodatki.add("Musztarda");
        Collections.sort(sosy_i_dodatki);

        for (String subcat : sosy_i_dodatki)
        {
            contentValues.put("SUBCATEGORY_NAME", subcat);
            contentValues.put("CATEGORY_ID", categories.indexOf("Sosy i dodatki") + 1);
            db.insert("SUBCATEGORIES", null, contentValues);
        }

        //////////////////////////////////////////////
        //               SLODYCZE                   //
        //////////////////////////////////////////////

        slodycze = new ArrayList<>();
        slodycze.add("Czekolada");
        slodycze.add("Ciastka");
        slodycze.add("Cukierki");
        slodycze.add("Lizak");
        slodycze.add("Batonik");
        Collections.sort(slodycze);

        for (String subcat : slodycze)
        {
            contentValues.put("SUBCATEGORY_NAME", subcat);
            contentValues.put("CATEGORY_ID", categories.indexOf("Słodycze") + 1);
            db.insert("SUBCATEGORIES", null, contentValues);
        }
    }


    ///////////////////////////////////////////////
    ///            WYSWIETLENIE TABEL           ///
    ///////////////////////////////////////////////

    public void getEveryone()
    {
        String queryString = "SELECT * FROM PRODUCTS";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst())
        {
            do
            {
                System.out.println(cursor.getString(2));

            }
            while (cursor.moveToNext());
        }
    }


    ///////////////////////////////////////////////
    ///            DODANIE PRODUKTU             ///
    ///////////////////////////////////////////////

    public void addProduct(Item product)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String query = "SELECT ID FROM SUBCATEGORIES WHERE SUBCATEGORY_NAME LIKE '" + product.getSubCategory() + "'";
        Cursor getSubcategoryId = db.rawQuery(query, null);
        int subID = 0;
        if (getSubcategoryId != null && getSubcategoryId.moveToFirst())
        {
            subID = getSubcategoryId.getInt(0);
        }

        contentValues.put("SUBCATEGORY_ID", subID);
        contentValues.put("PRODUCT_NAME", product.getName());
        contentValues.put("EXPIRY_DATE", product.getDate().toString());
        contentValues.put("AMOUNT", product.getAmount());
        db.insert("PRODUCTS", null, contentValues);

        getEveryone();
    }
    public ArrayList<String> getList()
    {
        String countQuery = "SELECT  * FROM PRODUCTS  ORDER BY 'EXPIRY_DATE'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        Cursor cursorSubCat;

        String sub_name = "";
        String name = "";
        Date date;
        int amount;

        ArrayList<String> productsList = new ArrayList<String>();
        TreeMap<Date, List<String>> DateMap = new TreeMap<Date, List<String>>();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if (cursor != null && cursor.moveToFirst())
        {
            do
            {
                name = cursor.getString(2);
                date = new Date(cursor.getString(3));
                amount = cursor.getInt(4);
                int subCatID = cursor.getInt(1);

                countQuery = "SELECT  * FROM SUBCATEGORIES WHERE ID = '" + subCatID + "'";
                cursorSubCat = db.rawQuery(countQuery, null);
                if (cursorSubCat != null && cursorSubCat.moveToFirst())
                {
                    sub_name = cursorSubCat.getString(2);
                }
                if(DateMap.containsKey(date))
                {
                    DateMap.get(date).add("\t" + sub_name + " " + name + "\n\tIlość: " + amount+"\n\tData: " + sdf.format(date));
                }
                else
                {
                    ArrayList<String> record = new ArrayList<>();
                    record.add("\t" + sub_name + " " + name + "\n\tIlość: " + amount + "\n\tData: " + sdf.format(date));
                    DateMap.put(date, record);
                }

            } while (cursor.moveToNext());
        }
        for(Map.Entry<Date, List<String>> entry:DateMap.entrySet())
        {
            for(int i=0; i <entry.getValue().size(); i++)
            {
                productsList.add(entry.getValue().get(i));
            }
        }
        return productsList;
    }

    public ArrayList<String> getProducts()
    {
        String countQuery = "SELECT  * FROM PRODUCTS";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        Cursor cursorSubCat;
        Cursor cursorCat;

        String cat_name = "";
        String sub_name = "";
        String name = "";
        Date date;
        int amount;

        ArrayList<String> productsList = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if (cursor != null && cursor.moveToFirst())
        {
            do
            {
                name = cursor.getString(2);
                date = new Date(cursor.getString(3));
                amount = cursor.getInt(4);
                int subCatID = cursor.getInt(1);

                countQuery = "SELECT  * FROM SUBCATEGORIES WHERE ID = '" + subCatID + "'";
                cursorSubCat = db.rawQuery(countQuery, null);
                if (cursorSubCat != null && cursorSubCat.moveToFirst())
                {
                    sub_name = cursorSubCat.getString(2);
                    int catID = cursorSubCat.getInt(1);

                    countQuery = "SELECT  * FROM CATEGORIES WHERE ID = '" + catID + "'";
                    cursorCat = db.rawQuery(countQuery, null);
                    if (cursorCat != null && cursorCat.moveToFirst())
                    {
                        cat_name = cursorCat.getString(1);
                    }
                }
                productsList.add(cat_name + "\t" + sub_name + "\t" + name + "\t" + amount + "\t" + sdf.format(date));
            } while (cursor.moveToNext());
        }
        return productsList;
    }


    public Integer deleteNote(String name, String stringDate) throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = sdf.parse(stringDate);
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("PRODUCTS", "PRODUCT_NAME=? AND EXPIRY_DATE=?", new String[]{name, String.valueOf(date)});
    }


    int getSubcategoryId(String subcategory)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT ID FROM SUBCATEGORIES WHERE SUBCATEGORY_NAME LIKE '" + subcategory + "'";
        Cursor getSubcategoryId = db.rawQuery(query, null);
        int subcategoryId = 0;
        if (getSubcategoryId != null && getSubcategoryId.moveToFirst())
        {
            subcategoryId = getSubcategoryId.getInt(0);
        }

        return subcategoryId;
    }


    int checkIfItemAlreadyExists(String name, String subcategory, Date date)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        // Find subcategory:
        int subcategoryId = getSubcategoryId(subcategory);

        // Find product from subcategory:
        String query = "SELECT ID FROM PRODUCTS WHERE PRODUCT_NAME LIKE '" + name + "' AND EXPIRY_DATE LIKE '" + date + "' AND SUBCATEGORY_ID LIKE '" + subcategoryId + "'";
        Cursor getProductId = db.rawQuery(query, null);
        int productId = 0;
        if (getProductId != null && getProductId.moveToFirst())
        {
            productId = getProductId.getInt(0);
        }

        return productId;
    }

    int updateExistingProduct(String name, String subcategory, Date date, int amount)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // Find subcategory:
        int subcategoryId = getSubcategoryId(subcategory);
        // Find amount of product in subcategory:
        String query = "SELECT AMOUNT FROM PRODUCTS WHERE PRODUCT_NAME LIKE '" + name + "' AND EXPIRY_DATE LIKE '" + date + "' AND SUBCATEGORY_ID LIKE '" + subcategoryId + "'";
        Cursor getAmount = db.rawQuery(query, null);
        int productAmount = 0;
        if (getAmount != null && getAmount.moveToFirst())
        {
            productAmount = getAmount.getInt(0);
        }
        if ( productAmount == 1 && amount == -1)
            return 0;
        // Update database:
        contentValues.put("AMOUNT", productAmount + amount);
        db.update("PRODUCTS",contentValues,"SUBCATEGORY_ID="+subcategoryId+" AND PRODUCT_NAME=? AND EXPIRY_DATE=?",
                new String[]{name, String.valueOf(date)});

        return productAmount + amount;

    }

}