package com.example.photo;
import org.junit.Test;
import static org.junit.Assert.*;
import com.example.photo.fileChecker;


import java.io.File;
import java.util.Date;

public class fileCheckerTest {

    @Test
    public void fileChecker_working() throws Exception {
        File ret;
        File f = new File("C:/","Dummy_timestamp_dummy_caption_60_65");
        Date minDate = new Date(Long.MIN_VALUE);;
        Date maxDate = new Date(Long.MAX_VALUE);;
        String keywords = "";
        int longmin = 50;
        int longmax = 55;
        int latmin = 100;
        int latmax = 105;
        ret = fileChecker.checkFile(f, minDate,  maxDate, keywords,
                longmin,  longmax,  latmin,  latmax);
        assertEquals(ret, null);

        f = new File("C:/","Dummy_timestamp_dummy_caption_60_65");
        minDate = new Date(Long.MIN_VALUE);;
        maxDate = new Date(Long.MAX_VALUE);;
        keywords = "";
        longmin = 60;
        longmax = 70;
        latmin = 55;
        latmax = 65;
        ret = fileChecker.checkFile(f, minDate,  maxDate, keywords,
                longmin,  longmax,  latmin,  latmax);
        assertEquals(ret, f);
    }
}
