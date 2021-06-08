package com.bumao.model.table2entry.vistor;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;
import com.bumao.model.table2entry.utils.Cleaner;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Data
public class MysqlCreateTableVisitor extends MySqlASTVisitorAdapter {
    private String tableName = null;
    private String tableCommit = null;
    private String tableEngine = null;
    private String tableCharset = null;
    public boolean visit(MySqlCreateTableStatement x) {
        this.tableName = Cleaner.clean(x.getName().toString() );
        this.tableCommit = Cleaner.clean(x.getComment().toString() );

        Map<String, SQLObject> tmap = x.getTableOptions();
        if(tmap.get("ENGINE")!=null){
            this.tableEngine = Cleaner.clean(tmap.get("ENGINE").toString() );
        }
        if(tmap.get("CHARSET")!=null){
            this.tableCharset = Cleaner.clean(tmap.get("CHARSET").toString() );
        }
        return true;
    }
}
