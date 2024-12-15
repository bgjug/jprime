package site.facade;

import java.awt.image.BufferedImage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import site.app.Application;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = Application.class)
@WebAppConfiguration
class ThumbnailServiceTest {

    @Autowired
    private ThumbnailService thumbnailService;

    @ParameterizedTest
    @CsvSource({
        "1920, 1080, 280, 326", // Bigger image
        "280, 326, 280, 326",   // Exact dimensions
        "150, 180, 280, 326"    // Smaller image
    })
    void testFitToRatio(int inputWidth, int inputHeight, int expectedWidth, int expectedHeight) {
        BufferedImage testImage = new BufferedImage(inputWidth, inputHeight, BufferedImage.TYPE_INT_RGB);
        BufferedImage resultImage = thumbnailService.rescale(testImage, expectedWidth, expectedHeight,
            ThumbnailService.ResizeType.FIT_TO_RATIO);

        assertEquals(expectedWidth, resultImage.getWidth());
        assertEquals(expectedHeight, resultImage.getHeight());
    }

    @Test
    void testFitToHeightBigger() {
        BufferedImage testImage = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_RGB);
        BufferedImage resultImage =
            thumbnailService.rescale(testImage, 180, 64, ThumbnailService.ResizeType.FIT_TO_HEIGHT);
        assertEquals(64, resultImage.getHeight());
    }

    @Test
    void testFitToHeightExact() {
        BufferedImage testImage = new BufferedImage(180, 64, BufferedImage.TYPE_INT_RGB);
        BufferedImage resultImage =
            thumbnailService.rescale(testImage, 180, 64, ThumbnailService.ResizeType.FIT_TO_HEIGHT);
        assertEquals(64, resultImage.getHeight());
        assertEquals(180, resultImage.getWidth());
    }

    @Test
    void testFitToHeightSmaller() {
        BufferedImage testImage = new BufferedImage(90, 32, BufferedImage.TYPE_INT_RGB);
        BufferedImage resultImage =
            thumbnailService.rescale(testImage, 180, 64, ThumbnailService.ResizeType.FIT_TO_HEIGHT);
        assertEquals(testImage.getHeight(), resultImage.getHeight());
        assertEquals(testImage.getWidth(), resultImage.getWidth());
    }

}
