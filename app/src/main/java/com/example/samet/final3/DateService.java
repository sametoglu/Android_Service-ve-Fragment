package com.example.samet.final3;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by samet on 28.05.2018.
 */

public class DateService extends Service {

    private IBinder datebinder = new Datebinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return datebinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public class Datebinder extends Binder {
        public DateService getService(){
            return DateService.this;
        }
    }

    public String getReadableDate(){
        Date now = new Date();
        String pattern = "dd/MM/yyyy HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        return simpleDateFormat.format(now);

    }
}
