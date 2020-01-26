package com.example.photo.DataStorage;

public class LocalStorage implements IDataStore {
    String state_;
    @Override
    public void saveState(String state) { state_ = state;}

    @Override
    public String getState() {return state_;}

}
