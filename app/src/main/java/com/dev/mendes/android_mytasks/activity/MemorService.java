package com.dev.mendes.android_mytasks.activity;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.Toast;

import com.dev.mendes.android_mytasks.R;
import com.dev.mendes.android_mytasks.adapter.TaskListAdapter;
import com.dev.mendes.android_mytasks.dataBase.DataBaseControl;
import com.dev.mendes.android_mytasks.fragment.TaskListFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
/*
import com.dev.mendes.android_mytasks.activity.MapsActivity;
import com.google.android.gms.maps.model.LatLng;
*/

/**
 * Created by Luke on 08/11/2017.
 */

public class MemorService extends IntentService {

    private int raio = 1000;
    private double valor = raio * 1000; //em metros
    private Cursor sCursor;
    private DataBaseControl db;

    private Context context;

    public MemorService(String name, Context context) {
        super(name);

        this.context = context;

        db = new DataBaseControl(context);
        sCursor = db.loadTasks();


    }

    @Override
    protected void onHandleIntent(Intent intent) {

        NotificationInterface notif = new NotificationInterface(getApplicationContext(), TaskListFragment.class, R.drawable.ic_launcher, "Mais um Titulo Teste", "Um texto qualquer, só clica aqui.");

        while (temItemNaLista(context)) synchronized (this) {

            try {

                Self.update();

                ArrayList<Item> lista = getEach();

                for (Item i : lista) {
                    if (calculaDistancia(Self.getLat(), Self.getLng(), i.lat, i.lng) < valor && i.check != 0) {

                        notif.setModel(getApplicationContext(), TaskListFragment.class, R.drawable.ic_launcher, i.title, i.text);
                        notif.setNotifID(i.id);
                        notif.postNotification();


                    }

                    //fará uma pausa de 10 minutos antes de realizar otura verificação na lista
                    int time = 10; // em segundos
                    long pauseTime = System.currentTimeMillis() + time * 1000;
                    wait(pauseTime - System.currentTimeMillis());

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }

        }

    }

    private double calculaDistancia(double lat1, double lng1, double lat2, double lng2) {
        //double earthRadius = 3958.75;//miles
        double earthRadius = 6371;//kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;

        return dist * 1000; //em metros
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    protected boolean temItemNaLista(Context context) {
        sCursor = db.loadTasks();

        if (sCursor != null) {
            return true;
        } else {
            return false;
        }
    }

    protected static class Self {
        static LatLng myLocation;

        public static void update() {
            MapsActivity maps = new MapsActivity();
            myLocation = maps.getMyLocation();
        }

        public static double getLat() {
            return myLocation.latitude;
        }

        public static double getLng() {
            return myLocation.longitude;
        }
    }

    protected static class Item {
        int id, check;
        double lat, lng;
        String title, text;

    }

    private ArrayList<Item> getEach() {
        ArrayList<Item> mArrayList = new ArrayList<>();
        for (sCursor.moveToFirst(); !sCursor.isAfterLast(); sCursor.moveToNext()) {
            // The Cursor is now set to the right position
            Item i = new Item();
            i.id = sCursor.getColumnIndex("_id");
            i.check = sCursor.getColumnIndex("CHECK");
            i.title = sCursor.getString(sCursor.getColumnIndex("NOME"));
            i.text = sCursor.getString(sCursor.getColumnIndex("NOTE"));
            String taskCordenates[] = sCursor.getString(sCursor.getColumnIndex("COORDINATES")).split("|");
            i.lat = Double.parseDouble(taskCordenates[0]);
            i.lng = Double.parseDouble(taskCordenates[1]);
            mArrayList.add(i);
        }

        return mArrayList;
    }

    public int getRaio() {
        return raio;
    }

    public void setRaio(int raio) {
        this.raio = raio;
    }

}