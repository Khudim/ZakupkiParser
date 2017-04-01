package com.khudim.dao.docs;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Beaver.
 */
public class DocumentsFields {

    public final static String CONTENT = "content";
    public final static String PRICE = "price";
    public final static String DATE = "creationDate";
    public final static String REGION = "region";
    public final static String URL = "url";

    public final static Map<Integer, String> columns = new HashMap<Integer, String>() {{
        columns.put(0, CONTENT);
        columns.put(1, PRICE);
        columns.put(2, DATE);
        columns.put(3, REGION);
        columns.put(4, URL);
    }};
}

