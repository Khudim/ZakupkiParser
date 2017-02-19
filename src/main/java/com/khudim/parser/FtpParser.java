package com.khudim.parser;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Beaver.
 */
public class FtpParser {

    private String login = "fz223free";
    private String ipAddress = "31.173.38.245";
    private String baseWorkDir = "/out";

    @Autowired
    private XmlParser xmlParser;

    private String tempDir;
    private final static ExecutorService executorService = Executors.newFixedThreadPool(5);
    private final static List<Future<?>> futures = new CopyOnWriteArrayList<>();
    private final static List<String> excludeName = new ArrayList<>();


    public void downloadFiles()  {
        System.out.println("Start download" + tempDir);
        excludeName.add("Rejection");
        recursiveCheck(baseWorkDir);
        for (Future<?> future : futures) {
            try{
                try {
                    future.get(50, TimeUnit.SECONDS);
                } catch (ExecutionException | TimeoutException e) {
                    e.printStackTrace();
                }
            }catch (InterruptedException e ){
                System.out.println("Can't wait" + e);
            }
        }
        System.out.println("Start parse Xml");
        xmlParser.findDocuments(tempDir);
    }

    public void recursiveCheck(String path) {
        FTPClient client = new FTPClient();
        try {
            client.connect(ipAddress);
            client.login(login, login);
            FTPFile[] files = client.listFiles(path);
            for (FTPFile ftpFile : files) {
                if (ftpFile.isDirectory()) {
                    futures.add(executorService.submit(() -> recursiveCheck(path + "/" + ftpFile.getName())));
                } else if (isCorrect(ftpFile)) {
                    retrieveThisFile(path, client, ftpFile);
                }
            }
        } catch (IOException e) {
            recursiveCheck(path);
        } finally {
            logoutAndClose(client);
        }
    }

    private void logoutAndClose(FTPClient client) {
        try {
            client.logout();
            client.disconnect();
        } catch (IOException e) {
            //DO_NOTHING
        }
    }

    private boolean isCorrect(FTPFile ftpFile) {
        return ftpFile.isFile()
                && isTodayFile(ftpFile)
                && isNotEmpty(ftpFile)
                && !isExcludeName(ftpFile.getName())
                && isFirstDownload(ftpFile);
    }

    private boolean isTodayFile(FTPFile ftpFile) {
        return System.currentTimeMillis() - ftpFile.getTimestamp().getTimeInMillis() < 60 * 60 * 24 * 2000;
    }

    private boolean isFirstDownload(FTPFile ftpFile) {
        File file = new File(tempDir + ftpFile.getName());
        return !file.exists() || file.length() == 0;
    }

    private boolean isExcludeName(String name) {
        return excludeName.stream().anyMatch(name::contains);
    }

    private boolean isNotEmpty(FTPFile ftpFile) {
        return ftpFile.getSize() > 400;
    }

    private void retrieveThisFile(String path, FTPClient client, FTPFile ftpFile) {
        long size = ftpFile.getSize();
        File file = new File(tempDir + ftpFile.getName());
        try (OutputStream outputStream = new FileOutputStream(new File(tempDir + ftpFile.getName()))) {
            client.retrieveFile(path + "/" + ftpFile.getName(), outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (file.length() < size) {
            System.out.println("Не докачался файл " + ftpFile.getName() + " " + file.length() + " меньше " + size);
            retrieveThisFile(path, client, ftpFile);
        }
    }

    public String getTempDir() {
        return tempDir;
    }

    public void setTempDir(String tempDir) {
       this.tempDir = tempDir;
    }
}
