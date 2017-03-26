package com.khudim.dao.docs;

/**
 * Created by Beaver.
 */
public class DocumentsFields {

    public final static String[] columns = new String[5];
    public final static String CONTENT = "content";
    public final static String PRICE = "price";
    public final static String DATE = "creationDate";
    public final static String REGION = "region";
    public final static String URL = "url";

    static {
        columns[0] = CONTENT;
        columns[1] = PRICE;
        columns[2] = DATE;
        columns[3] = REGION;
        columns[4] = URL;
    }

    public static String getColumnName(int columnNumber) {
        if (columnNumber >= 0 && columnNumber < columns.length) {
            return columns[columnNumber];
        }
        return "";
    }
}
