import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class SearchNumberTest {

    SearchNumber searchNumber = new SearchNumber();


    @Before
    public void init() {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "12");
    }


    @Test
    public void ReadFromFile() {

        List<CustomNumberEntity> data = searchNumber
                .readFromFile(System.getProperty("user.dir") + "/testData.json");
        assertEquals("[" + "CustomNumberEntity [number=67]" + ", "
                        + "CustomNumberEntity [number=45]" + ", "
                        + "CustomNumberEntity [number=45]" + ", "
                        + "CustomNumberEntity [number=-3]" + ", "
                        + "CustomNumberEntity [number=12]" + ", "
                        + "CustomNumberEntity [number=100]" + ", "
                        + "CustomNumberEntity [number=3]" + "]",
                data.toString());
    }


    @Test
    public void jsonFileDoesnotExist() {
        List<CustomNumberEntity> data = searchNumber
                .readFromFile(System.getProperty("user.dir") + "/FileTestNoExist.txt");
        assertEquals(0, data.size());
    }

    @Test
    public void readingCorruptJson() {
        List<CustomNumberEntity> data = searchNumber
                .readFromFile(System.getProperty("user.dir") + "/CorruptJson.json");
        assertEquals(0, data.size());
    }

    @Test
    public void searchNumberExists() {
        List<CustomNumberEntity> data  = searchNumber
                .readFromFile(System.getProperty("user.dir") + "/testData.json");
        assertEquals(true, searchNumber.contains(100, data));
    }

    @Test
    public void searchNumberDoesnotExists() {
        List<CustomNumberEntity> data = searchNumber
                .readFromFile(System.getProperty("user.dir") + "/testData.json");
        assertEquals(false, searchNumber.contains(200, data));
    }

}