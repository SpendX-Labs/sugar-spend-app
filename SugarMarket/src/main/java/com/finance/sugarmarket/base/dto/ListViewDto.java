package com.finance.sugarmarket.base.dto;

import java.util.List;

public class ListViewDto<T> {
    private List<T> data;
    private long total;
    private long offset;
    private int limit;
    private String message;

    public ListViewDto(List<T> data, long total, long offset, int limit) {
        super();
        this.data = data;
        this.total = total;
        this.offset = offset;
        this.limit = limit;
    }

    public ListViewDto(String message) {
        super();
        this.message = message;
    }

    public ListViewDto() {
        super();
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
