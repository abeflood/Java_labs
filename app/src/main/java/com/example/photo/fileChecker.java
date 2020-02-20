package com.example.photo;

import java.io.File;
import java.util.Date;

public class fileChecker {
    public static File checkFile(File f, Date minDate, Date maxDate, String keywords,
                         int longmin, int longmax, int latmin, int latmax ) {
        File ret;
        String[] split_str = f.getAbsolutePath().split("_");
        int plong = Integer.parseInt(split_str[5]);
        int plat = Integer.parseInt(split_str[4]);
        if (((minDate == null && maxDate == null) || (f.lastModified() >= minDate.getTime()
                && f.lastModified() <= maxDate.getTime())
        ) && (keywords == "" || f.getPath().contains(keywords)) &&
                (plat >= latmin) && (plat <= latmax)
                && (plong >= longmin) && (plong <= longmax))
            ret = f;
        else
            ret = null;
        return ret;
    }
}
