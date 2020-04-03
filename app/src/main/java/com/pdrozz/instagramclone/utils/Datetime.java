package com.pdrozz.instagramclone.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Datetime {

    public static String getDateToday(){
        DateFormat format=new SimpleDateFormat("MM/dd/yyyy");
        Date date=new Date();
        return format.format(date);
    }

}
