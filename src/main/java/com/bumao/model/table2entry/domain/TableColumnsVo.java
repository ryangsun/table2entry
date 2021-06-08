package com.bumao.model.table2entry.domain;

import lombok.Data;

@Data
public class TableColumnsVo {
    private String columnName;
    private String columnDataType;
    private String columnDefault;
    private String columnCommit;
}
