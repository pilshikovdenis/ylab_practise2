package com.pilshikov.homework3.dated_map;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DatedMapImpl implements DatedMap{
    Map<String, String> mainMap;
    Map<String, Date> dateMap;
    public DatedMapImpl() {
        this.mainMap = new HashMap<>();
        this.dateMap = new HashMap<>();
    }
    @Override
    public void put(String key, String value) {
        this.mainMap.put(key, value);
        this.dateMap.put(key, new Date());
    }

    @Override
    public String get(String key) {
        return this.mainMap.getOrDefault(key, null);
    }

    @Override
    public boolean containsKey(String key) {
        return this.mainMap.containsKey(key);
    }

    @Override
    public void remove(String key) {
        this.mainMap.remove(key);
        this.dateMap.remove(key);
    }

    @Override
    public Set<String> keySet() {
        return this.mainMap.keySet();
    }

    @Override
    public Date getKeyLastInsertionDate(String key) {
        return this.dateMap.getOrDefault(key, null);
    }
}
