package com.khudim.parser;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beaver.
 */
@Component
@EnableAsync
@EnableScheduling
@PropertySource(value = {"classpath:application.properties"})
public class FtpParser {

    private String login = "fz223free";
    private String ipAddress = "31.173.38.245";
    private String baseWorkDir = "/out";

    private String tempDir;

    private final List<String> excludeName = new ArrayList<>();

    @Autowired
    public FtpParser(@Value("${parser.tempFolder}") String tempDir) {
        this.tempDir = tempDir;
        excludeName.add("Rejection");
    }

    @Async
    @Scheduled(cron = "${scheduler.ftpParser.cron}")
    public void downloadFiles() {
        System.out.println("Start download" + tempDir);
        recursiveCheck(baseWorkDir);
        System.out.println("im done parse ftp");
    }

    public void recursiveCheck(String path) {
        FTPClient client = new FTPClient();
        try {
            client.connect(ipAddress);
            client.login(login, login);
            FTPFile[] files = client.listFiles(path);
            for (FTPFile ftpFile : files) {
                if (ftpFile.isDirectory()) {
                    recursiveCheck(path + "/" + ftpFile.getName());
                } else if (isCorrect(ftpFile)) {
                    retrieveThisFile(path, client, ftpFile);
                }
            }
        } catch (IOException e) {
            System.out.println("oops start again");
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
            System.err.println(e.getMessage() + " logout");
            //DO_NOTHING
        }
    }

    private boolean isCorrect(FTPFile ftpFile) {
        return ftpFile.isFile()
                && isNotEmpty(ftpFile)
                && !isExcludeName(ftpFile.getName())
                && isFirstDownload(ftpFile);
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
            System.err.println(e.getMessage());
        }
        if (file.length() + 100 < size) {
            System.out.println("Не докачался файл " + ftpFile.getName() + " " + file.length() + " меньше " + size);
            file.delete();
            //   retrieveThisFile(path, client, ftpFile);
        }
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
