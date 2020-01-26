package com.example.photo;
import org.junit.Test;
import static org.junit.Assert.*;
import com.example.photo.DataStorage.IDataStore;
import com.example.photo.DataStorage.LocalStorage;

public class LocalStorageTest {
    @Test
    public void localstorage_working() throws Exception {
        IDataStore ls = new LocalStorage();
        ls.saveState("Just Testing");
        assertEquals("Just Testing", ls.getState());
    }
}
