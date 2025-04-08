import com.example.LogManager;
import com.itextpdf.text.DocumentException;
import org.junit.jupiter.api.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import com.example.ChartCreator;

import static org.junit.jupiter.api.Assertions.*;

class ExportsTest {

    private ChartCreator chartCreator;
    private LogManager testManager = new LogManager();

    @BeforeEach
    void setUp() {
        chartCreator = new ChartCreator(testManager); // If you don't need LogManager for PDF
    }

    @Test
    void testSaveChartAsPdfCreatesFile() throws IOException, DocumentException {
        // Create a dummy BufferedImage (simulates a chart)
        BufferedImage dummyImage = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);

        // Redirect output to a temp file
        File tempPdf = File.createTempFile("test_chart", ".pdf");
        tempPdf.deleteOnExit(); // Clean up after test

        // Replace file path writing in saveChartAsPdf
        chartCreator.saveChartAsPdfToFile(dummyImage, tempPdf.getAbsolutePath());

        // Check if the file exists and is not empty
        assertTrue(tempPdf.exists());
        assertTrue(tempPdf.length() > 0);
    }

    @Test
    void testSaveChartAsPdfWithNullImage() {
        assertThrows(NullPointerException.class, () -> {
            chartCreator.saveChartAsPdf(null,null);
        });
    }
}
