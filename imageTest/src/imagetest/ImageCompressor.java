/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagetest;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

/**
 *
 * @author Jolan
 */
public class ImageCompressor {

    private int time;
    private long oldSize;
    private long newSize;

    public boolean compressImage(File f, float cRate, Color c, String maxWidth) {

        try {
            long start = System.currentTimeMillis();
            BufferedImage imageStart = ImageIO.read(f);
            BufferedImage image;
            if (maxWidth.equals("") || maxWidth == null) {
                image = new BufferedImage(imageStart.getWidth(), imageStart.getHeight(), BufferedImage.TYPE_INT_RGB);
                image.createGraphics().drawImage(imageStart, 0, 0, c, null);
            } else {
                double ratio = imageStart.getHeight() / (double)imageStart.getWidth();
      
                long newHeight = Math.round(Integer.parseInt(maxWidth) * ratio);
               
                image = new BufferedImage(Integer.parseInt(maxWidth), (int) newHeight, BufferedImage.TYPE_INT_RGB);
                image.createGraphics().drawImage(imageStart, 0, 0, Integer.parseInt(maxWidth), (int) newHeight, c, null);
            }

            String filename = stripExtension(f.getName());
            File compressedImageFile;
            String foldername = getNewFolderName(f);
            new File(f.getParent() + "\\" + foldername).mkdirs();

            compressedImageFile = new File(f.getParent() + "\\" + foldername + "\\" + filename + ".jpg");
            writeLog("File: " + f.getParent() + "\\" + foldername + "\\" + filename + ".jpg");

            OutputStream os = new FileOutputStream(compressedImageFile);

            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
            ImageWriter writer = writers.next();

            ImageOutputStream ios = ImageIO.createImageOutputStream(os);
            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();

            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);

            param.setCompressionQuality(cRate / 10);
           
            writer.write(null, new IIOImage(image, null, null), param);

            writeLog("From: " + String.valueOf(f.length() / 1024) + " kb");
            writeLog("To: " + String.valueOf(ios.length() / 1024) + " kb");
           
            os.close();
            ios.close();
            writer.dispose();
            long end = System.currentTimeMillis();
            writeLog("Duration: " + String.valueOf(end - start) + " ms");
            time += end - start;

            return true;
        } catch (IOException | NumberFormatException ex) {
            writeLog(ex.getMessage());
            return false;
        }
    }

    static String stripExtension(String str) {
        if (str == null) {
            return null;
        }
        int pos = str.lastIndexOf(".");
        if (pos == -1) {
            return str;
        }
        return str.substring(0, pos);
    }

    public int getTime() {
        return time;
    }

    public long getOldSize() {
        return oldSize;
    }

    public long getNewSize() {
        return newSize;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Image getImage(File f) {
        try {
            BufferedImage image = ImageIO.read(f);
            return SwingFXUtils.toFXImage(image, null);
        } catch (IOException ex) {
            return null;
        }
    }

    public Image getCompressedImage(File f, Color c, float cRate) {
        try {
            long start = System.currentTimeMillis();
            BufferedImage imageStart = ImageIO.read(f);
            BufferedImage image = new BufferedImage(imageStart.getWidth(), imageStart.getHeight(), BufferedImage.TYPE_INT_RGB);
            image.createGraphics().drawImage(imageStart, 0, 0, c, null);

            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
            ImageWriter writer = writers.next();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();

            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);

            param.setCompressionQuality(cRate / 10);

            writer.write(null, new IIOImage(image, null, null), param);

            newSize = ios.length();
            oldSize = f.length();

            ios.close();
            writer.dispose();

            baos.flush();
            byte[] buf = baos.toByteArray();//bestand als bytes
            InputStream in = new ByteArrayInputStream(buf);
            BufferedImage bImageFromConvert = ImageIO.read(in);
            in.close();

            long end = System.currentTimeMillis();
            time += end - start;

            return SwingFXUtils.toFXImage(bImageFromConvert, null);

        } catch (IOException ex) {
            return null;
        }
    }

    public String getNewFolderName(File file) {
        File f;
        String prefix = "compressed_";
        int count = 0;
        String foldername = "compressed";
        f = new File(file.getParent() + "\\" + foldername);
        File pFile = new File(f.getAbsolutePath() + "\\" + file.getName());

        if (pFile.exists() && f.isDirectory()) {
            do {
                count += 1;
                foldername = prefix + String.valueOf(count);
                f = new File(file.getParent());
                pFile = new File(f.getAbsolutePath() + "\\" + foldername + "\\" + file.getName());
            } while (pFile.exists());

            return foldername;
        }
        return foldername;

    }

    public void writeLog(String txt) {
        Platform.runLater(() -> {
            ImageTest.log().log(txt);
        });
    }

}
