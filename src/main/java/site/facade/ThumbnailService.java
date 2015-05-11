package site.facade;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;

import org.springframework.stereotype.Service;

@Service(ThumbnailService.NAME)
public class ThumbnailService {
	public static final String NAME = "ThumbnailService";

	public ThumbnailService() {
	}

	public byte[] thumbImage(byte[] initialImageInBytes, int widthParam, int heigthParam) {
//		InputStream in = new ByteArrayInputStream(initialImageInBytes);
//		byte [] readyImageInBytes = null;
//		try {
//			BufferedImage tempImage = ImageIO.read(in);
//			BufferedImage readyImage = null;
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			readyImage = Thumbnails.of(tempImage).size(widthParam, heigthParam)
//					.outputFormat("jpg").outputQuality(0.8).asBufferedImage();
//			ImageIO.write(readyImage, "jpg", baos);
//			baos.flush();
//			readyImageInBytes = baos.toByteArray();
//			baos.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return initialImageInBytes;
	}
}

