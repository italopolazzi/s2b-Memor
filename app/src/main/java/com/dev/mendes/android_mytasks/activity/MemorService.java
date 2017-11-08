package com.dev.mendes.android_mytasks.activity;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.dev.mendes.android_mytasks.adapter.TaskListAdapter;
import com.dev.mendes.android_mytasks.dataBase.DataBaseControl;
import com.google.android.gms.maps.model.LatLng;
/*
import com.dev.mendes.android_mytasks.activity.MapsActivity;
import com.google.android.gms.maps.model.LatLng;
*/

/**
 * Created by Luke on 08/11/2017.
 */

public class MemorService extends IntentService {

    private double valor = 1000 * 1000; //em metros

    private Context context;
    TaskListAdapter adapter;

    public MemorService(String name, Context context) {
        super(name);

        this.context = context;

//        adapter = new ServiceAdapter(context,this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
/*
        while (temItemNaLista()) {
            synchronized (this) {

                try {

                    self.update();

                    for (item i : Lista) {
                        if (calculaDistancia(self.getLat(), self.getLng(), i.lat, i.lng) < valor) {
                            postNotificantion(i);
                        }

                        if (dataHoje == i.data()) {
                            postNotification(i);
                        }
                    }

                    //fará uma pausa de 10 minutos antes de realizar otura verificação na lista
                    long pauseTime = System.currentTimeMillis() + 600*1000;
                    wait(pauseTime - System.currentTimeMillis());

                } catch (Exception e) {

                }



            }
        }
*/
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
        DataBaseControl db = new DataBaseControl(context);

        if (true){
            return  true;
        } else {
            return false;
        }
    }

    protected static class self {
        static LatLng myLocation;

        public static void update(){
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

}