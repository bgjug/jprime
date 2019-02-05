package site.facade;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import site.app.Application;

import java.awt.image.BufferedImage;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class ThumbnailServiceTest {

    @Autowired
    @Qualifier(ThumbnailService.NAME)
    private ThumbnailService thumbnailService;


    @Test
    public void testFitToRatioBigger() {
        BufferedImage testImage = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_RGB);
        BufferedImage resultImage =
                        thumbnailService.rescale(testImage, 280, 326, ThumbnailService.ResizeType.FIT_TO_RATIO);
        Assert.assertEquals(280, resultImage.getWidth());
        Assert.assertEquals(326, resultImage.getHeight());
    }

    @Test
    public void testFitToRatioExact() {
        BufferedImage testImage = new BufferedImage(280, 326, BufferedImage.TYPE_INT_RGB);
        BufferedImage resultImage =
                        thumbnailService.rescale(testImage, 280, 326, ThumbnailService.ResizeType.FIT_TO_RATIO);
        Assert.assertEquals(280, resultImage.getWidth());
        Assert.assertEquals(326, resultImage.getHeight());
    }

    @Test
    public void testFitToRatioSmaller() {
        BufferedImage testImage = new BufferedImage(150, 180, BufferedImage.TYPE_INT_RGB);
        BufferedImage resultImage =
                        thumbnailService.rescale(testImage, 280, 326, ThumbnailService.ResizeType.FIT_TO_RATIO);
        Assert.assertEquals(280, resultImage.getWidth());
        Assert.assertEquals(326, resultImage.getHeight());
    }

    @Test
    public void testFitToHeightBigger() {
        BufferedImage testImage = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_RGB);
        BufferedImage resultImage =
                        thumbnailService.rescale(testImage, 180, 64, ThumbnailService.ResizeType.FIT_TO_HEIGHT);
        Assert.assertEquals(64,resultImage.getHeight());
    }

    @Test
    public void testFitToHeightExact() {
        BufferedImage testImage = new BufferedImage(180, 64, BufferedImage.TYPE_INT_RGB);
        BufferedImage resultImage =
                        thumbnailService.rescale(testImage, 180, 64, ThumbnailService.ResizeType.FIT_TO_HEIGHT);
        Assert.assertEquals(64,resultImage.getHeight());
        Assert.assertEquals(180,resultImage.getWidth());
    }

    @Test
    public void testFitToHeightSmaller() {
        BufferedImage testImage = new BufferedImage(90, 32, BufferedImage.TYPE_INT_RGB);
        BufferedImage resultImage =
                        thumbnailService.rescale(testImage, 180, 64, ThumbnailService.ResizeType.FIT_TO_HEIGHT);
        Assert.assertEquals(testImage.getHeight(),resultImage.getHeight());
        Assert.assertEquals(testImage.getWidth(),resultImage.getWidth());
    }

}
