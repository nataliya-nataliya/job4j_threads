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

    public void speedLimit(byte[] dataBuffer, FileOutputStream fileOutputStream, int totalBytesRead) {
        long start = System.nanoTime();
        try {
            fileOutputStream.write(dataBuffer, 0, totalBytesRead);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        long finish = System.nanoTime();
        long diff = finish - start;
        if ((long) speed * 1000000000L < diff) {
            try {
                Thread.sleep(1000L * diff / speed);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void run() {
        URL fileUrl;
        try {
            fileUrl = new URL(url);
            String fileName = Paths.get(fileUrl.getPath()).getFileName().toString();
            try (BufferedInputStream in = new BufferedInputStream(fileUrl.openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
                byte[] dataBuffer = new byte[1024];
                int totalBytesRead = 0;
                int bytesRead;
                while (totalBytesRead < 1024 && (bytesRead = in.read(dataBuffer, totalBytesRead,
                        Math.min(1024 - totalBytesRead, dataBuffer.length - totalBytesRead))) != -1) {
                    totalBytesRead += bytesRead;
                    if (totalBytesRead == 1024) {
                        speedLimit(dataBuffer, fileOutputStream, totalBytesRead);
                        totalBytesRead = 0;
                    }
                }
                if (totalBytesRead > 0) {
                    speedLimit(dataBuffer, fileOutputStream, totalBytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
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
