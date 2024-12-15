package site.facade;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;

@Service(ThumbnailService.NAME)
public class ThumbnailService {

    public enum ResizeType {
        FIT_TO_HEIGHT,
        //		FIT_TO_WIDTH, //not yet used
        FIT_TO_RATIO
    }

    public static final String NAME = "ThumbnailService";

    public ThumbnailService() {
    }

    public BufferedImage rescale(BufferedImage image, int newWidth, int newHeight, ResizeType resizeType) {
        BufferedImage rescaled = null;
        if (resizeType.equals(ResizeType.FIT_TO_HEIGHT)) {
            if(image.getHeight() < newHeight){
                return image;
            }
            rescaled = Scalr.resize(image, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, newWidth,
                                    newHeight);
        } else if (resizeType.equals(ResizeType.FIT_TO_RATIO)) {
            rescaled =  image;
            if(rescaled.getWidth() > newWidth){
                rescaled = Scalr.resize(image, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_WIDTH, newWidth, newHeight);
            }
            if(rescaled.getHeight() > newHeight){
                rescaled = Scalr.resize(image, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_HEIGHT, newWidth, newHeight);
            }
            rescaled = myPad(rescaled, newWidth, newHeight);
        }
        return rescaled;
    }

    public BufferedImage myPad(BufferedImage image, int width, int height) {
        BufferedImage pad = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = pad.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, pad.getWidth(), pad.getHeight());
        int outLineI = (width - image.getWidth()) / 2;
        int outLineJ = (height - image.getHeight()) / 2;
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                pad.setRGB(outLineI + i, outLineJ + j, image.getRGB(i, j));
            }
        }

        return pad;
    }

    public byte[] thumbImage(byte[] initialImageInBytes, int widthParam, int heigthParam, ResizeType resizeType) {

        byte[] readyImageInBytes = null;
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(initialImageInBytes);
            BufferedImage inImage = ImageIO.read(in);
            BufferedImage outImage = rescale(inImage, widthParam, heigthParam, resizeType);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(outImage, "jpg", out);
            out.flush();
            readyImageInBytes = out.toByteArray();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readyImageInBytes;

    }
}

