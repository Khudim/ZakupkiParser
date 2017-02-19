package com.khudim.helpers;

import com.khudim.document.IParsedDocument;
import org.apache.derby.iapi.services.io.ArrayInputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Beaver.
 */
public class ParseHelper {

    public static byte[] serializeObject(IParsedDocument parsedDocument) {
        byte[] bytes = new byte[0];
        try (ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
             ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            outputStream.writeObject(parsedDocument);
            bytes = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }


    public static IParsedDocument backDocumentFromBytes(byte[] bytes) {
        IParsedDocument document = null;
        try (ObjectInputStream inputStream = new ObjectInputStream(new ArrayInputStream(bytes))) {
            document = (IParsedDocument) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return document;
    }

    public static long parseDocumentsDate(String date){
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        long time = 0;
        try {
            Date parseDate = f.parse(date);
            time =  parseDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }


    public static boolean isLastDayModified(Path path) {
        return path.toFile().lastModified() > System.currentTimeMillis() - 60 * 60 * 1000 * 24;
    }
}
