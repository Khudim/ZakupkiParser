package com.khudim.helpers;

import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Beaver.
 */
public class ParseHelper {

    public static long parseDocumentsDate(String date){
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        long time = 0;
        if(StringUtils.isBlank(date)){
            return System.currentTimeMillis();
        }
        try {
            Date parseDate = f.parse(date);
            time =  parseDate.getTime();
        } catch (ParseException e) {
            System.err.println("wtf "+ date);
        }
        return time;
    }

    public static boolean isLastDayModified(Path path) {
        return path.toFile().lastModified() > System.currentTimeMillis() - 60 * 60 * 1000 * 24 * 10;
    }
}
