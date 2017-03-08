package com.khudim.dao;

import com.khudim.dao.docs.Documents;

import java.util.List;

/**
 * Created by Beaver.
 */
public class DataTableObject {

    private int draw;

    private long recordsTotal;

    private long recordsFiltered;

    private List<Documents> data;

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public long getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public long getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public List<Documents> getData() {
        return data;
    }

    public void setData(List<Documents> data) {
        this.data = data;
    }



}