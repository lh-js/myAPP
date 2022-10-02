package com.example.myapp.util;

import com.github.pagehelper.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryResult<T> {

    private int totalPages;
    private long totalRows;
    private List<T> resultList;

    public QueryResult(Page page) {
        this.totalPages = page.getPages();
        this.totalRows = page.getTotal();
        this.resultList = page.getResult();
    }
}
