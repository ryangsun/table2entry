package com.bumao.model.table2entry.domain;

import lombok.Data;

import java.util.List;

@Data
public class TableEntryVo {
    private String sqlLowCase;
    private String tableName;
    private String tableCommit;
    private String tableEngine;
    private String tableCharset;
    List<TableColumnsVo> Columns;
}
