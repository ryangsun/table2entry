package com.bumao.model.table2entry.vistor;

import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;
import com.bumao.model.table2entry.domain.TableColumnsVo;
import com.bumao.model.table2entry.utils.Cleaner;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class MysqlCreateColumnVisitor extends MySqlASTVisitorAdapter {
    private TableColumnsVo tableColumnsVo  = null;

    public boolean visit(SQLColumnDefinition x) {
        this.tableColumnsVo = new TableColumnsVo();
        if(x.getName()!=null) {
            this.tableColumnsVo.setColumnName(Cleaner.clean(x.getName().toString()) );
        }
        if(x.getDefaultExpr()!=null) {
            this.tableColumnsVo.setColumnDefault(Cleaner.clean(x.getDefaultExpr().toString()) );
        }
        if(x.getDataType()!=null) {
            this.tableColumnsVo.setColumnDataType(Cleaner.clean(x.getDataType().toString()) );
        }
        if(x.getComment()!=null) {
            this.tableColumnsVo.setColumnCommit(Cleaner.clean(x.getComment().toString()) );
        }

        return true;
    }
}
