package com.example.dailyhelper;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.dailyhelper.utils.DateFormat;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void today_isCorrect() {
        DateFormat dateFormat = new DateFormat();
        assertEquals("12 15", dateFormat.make_MM() + " " + dateFormat.make_dd());
    }
}