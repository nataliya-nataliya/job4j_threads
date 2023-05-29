package ru.job4j.net;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

public class Wget implements Runnable {
    private final String url;
    private final int speed;

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    @Override
    public void run() {
        URL fileUrl;
        try {
            fileUrl = new URL(url);
            String fileName = Paths.get(fileUrl.getPath()).getFileName().toString();
            try (BufferedInputStream in = new BufferedInputStream(fileUrl.openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream("wget_download_" + fileName)) {
                byte[] dataBuffer = new byte[1024];
                int bytesRead;
                long start = System.nanoTime();
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    try {
                        fileOutputStream.write(dataBuffer, 0, bytesRead);
                        long finish = System.nanoTime();
                        long diff = finish - start;
                        if ((long) speed * 1000000000L < diff) {
                            try {
                                Thread.sleep(1000L * diff / speed);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        start = System.nanoTime();
                    } catch (IOException | RuntimeException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isValidUrl(String url) {
        boolean isValid;
        try {
            new URL(url);
            isValid = true;
        } catch (MalformedURLException e) {
            isValid = false;
        }
        return isValid;
    }

    public static void validationParameters(String[] args) throws InterruptedException {
        String url = null;
        int speed;
        if (args.length != 2) {
            throw new IllegalArgumentException(String.format("2 parameters are required: url and "
                            + "the number of kilobytes per second separated by a space. You entered %d parameter(s)",
                    args.length));
        }
        if (isValidUrl(args[0])) {
            url = args[0];
        }
        speed = Integer.parseInt(args[1]);
        if (speed <= 0) {
            throw new IllegalArgumentException("Download speed must be greater than 0");
        }
        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        wget.join();
    }

    public static void main(String[] args) throws InterruptedException {
        validationParameters(args);
    }
}
