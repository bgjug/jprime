package site.facade;

import java.awt.image.BufferedImage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import site.app.Application;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = Application.class)
@WebAppConfiguration
class ThumbnailServiceTest {

    @Autowired
    @Qualifier(ThumbnailService.NAME)
    private ThumbnailService thumbnailService;


    @Test
    void testFitToRatioBigger() {
        BufferedImage testImage = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_RGB);
        BufferedImage resultImage =
                        thumbnailService.rescale(testImage, 280, 326, ThumbnailService.ResizeType.FIT_TO_RATIO);
        assertEquals(280, resultImage.getWidth());
        assertEquals(326, resultImage.getHeight());
    }

    @Test
    void testFitToRatioExact() {
        BufferedImage testImage = new BufferedImage(280, 326, BufferedImage.TYPE_INT_RGB);
        BufferedImage resultImage =
                        thumbnailService.rescale(testImage, 280, 326, ThumbnailService.ResizeType.FIT_TO_RATIO);
        assertEquals(280, resultImage.getWidth());
        assertEquals(326, resultImage.getHeight());
    }

    @Test
    void testFitToRatioSmaller() {
        BufferedImage testImage = new BufferedImage(150, 180, BufferedImage.TYPE_INT_RGB);
        BufferedImage resultImage =
                        thumbnailService.rescale(testImage, 280, 326, ThumbnailService.ResizeType.FIT_TO_RATIO);
        assertEquals(280, resultImage.getWidth());
        assertEquals(326, resultImage.getHeight());
    }

    @Test
    void testFitToHeightBigger() {
        BufferedImage testImage = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_RGB);
        BufferedImage resultImage =
                        thumbnailService.rescale(testImage, 180, 64, ThumbnailService.ResizeType.FIT_TO_HEIGHT);
        assertEquals(64,resultImage.getHeight());
    }

    @Test
    void testFitToHeightExact() {
        BufferedImage testImage = new BufferedImage(180, 64, BufferedImage.TYPE_INT_RGB);
        BufferedImage resultImage =
                        thumbnailService.rescale(testImage, 180, 64, ThumbnailService.ResizeType.FIT_TO_HEIGHT);
        assertEquals(64,resultImage.getHeight());
        assertEquals(180,resultImage.getWidth());
    }

    @Test
    void testFitToHeightSmaller() {
        BufferedImage testImage = new BufferedImage(90, 32, BufferedImage.TYPE_INT_RGB);
        BufferedImage resultImage =
                        thumbnailService.rescale(testImage, 180, 64, ThumbnailService.ResizeType.FIT_TO_HEIGHT);
        assertEquals(testImage.getHeight(),resultImage.getHeight());
        assertEquals(testImage.getWidth(),resultImage.getWidth());
    }

}
