package com.bumao.model.table2entry;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlKey;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlPrimaryKey;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.util.JdbcConstants;
import com.bumao.model.table2entry.domain.TableColumnsVo;
import com.bumao.model.table2entry.domain.TableEntryVo;
import com.bumao.model.table2entry.vistor.MysqlCreateColumnVisitor;
import com.bumao.model.table2entry.vistor.MysqlCreateTableVisitor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ToEntry {
    public List<TableEntryVo> getEntry(String sql){
        String dbType = JdbcConstants.MYSQL;
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);

        //解析出的独立语句的个数
        List<TableEntryVo> tableEntryVos  =  new ArrayList<TableEntryVo>();
        if(stmtList==null||stmtList.size()<1){
            return tableEntryVos;
        }
        for (int i = 0; i < stmtList.size(); i++) {
            SQLStatement stmt = stmtList.get(i);
            stmt.toLowerCaseString();
            if(stmt instanceof MySqlCreateTableStatement){
//                log.info("{} is MySqlCreateTableStatement-------",i);
                TableEntryVo vo = this.parseCreateTable((MySqlCreateTableStatement)stmt) ;
                vo.setSqlLowCase(stmt.toLowerCaseString());
                tableEntryVos.add(vo);
            }
//            else if(stmt instanceof SQLSelectStatement){
//                log.info("{} is SQLSelectStatement",i);
//            }else{
//                log.info("{} 未知的stmt类型",i);
//                log.info("stmt.getClass()={}",stmt.getClass());
//            }
        }
        return tableEntryVos;
    }

    private TableEntryVo parseCreateTable(MySqlCreateTableStatement createTableStmt){
        TableEntryVo vo = new TableEntryVo();

        MysqlCreateTableVisitor visitor = new MysqlCreateTableVisitor();
        createTableStmt.accept(visitor);

        vo.setTableName(visitor.getTableName());
        vo.setTableCharset(visitor.getTableCharset());
        vo.setTableEngine(visitor.getTableEngine());
        vo.setTableCommit(visitor.getTableCommit());

        //内容属性
        List<SQLTableElement> sqlTableElementList = createTableStmt.getTableElementList();
        if(sqlTableElementList==null||sqlTableElementList.size()<1){
            return vo;
        }
        List<TableColumnsVo> tableColumnsVoList  =  new ArrayList<TableColumnsVo>();
        int i=0;
        for(SQLTableElement sqlTableElement:sqlTableElementList){
            i++;
            if(sqlTableElement instanceof SQLColumnDefinition) {
                SQLColumnDefinition sqlColumnDefinition = (SQLColumnDefinition) sqlTableElement;
                MysqlCreateColumnVisitor columnVisitor = new MysqlCreateColumnVisitor();
                sqlColumnDefinition.accept(columnVisitor);
                TableColumnsVo cvo = columnVisitor.getTableColumnsVo();
                tableColumnsVoList.add(cvo);
            }else if(sqlTableElement instanceof MySqlPrimaryKey){
                MySqlPrimaryKey mySqlPrimaryKey  = (MySqlPrimaryKey) sqlTableElement;
                log.info("{}- 主键={},类型={},字段={},备注={}"
                        ,i
                        ,mySqlPrimaryKey.getName()
                        ,mySqlPrimaryKey.getIndexType()
                        ,this.parseColumns(mySqlPrimaryKey.getColumns())
                        ,mySqlPrimaryKey.getComment()
                );
            }else if(sqlTableElement instanceof MySqlKey){
                MySqlKey mySqlKey = (MySqlKey) sqlTableElement;
                log.info("{}- 索引={},类型={},字段={},备注={}"
                        ,i
                        ,mySqlKey.getName()
                        ,mySqlKey.getIndexType()
                        ,this.parseColumns(mySqlKey.getColumns())
                        ,mySqlKey.getComment()
                );
//				SQLASTVisitor v = new SQLASTOutputVisitor();
//				mySqlKey.accept(SQLASTVisitor v);
            }
        }
        vo.setColumns(tableColumnsVoList);
        return vo;
    }
    private List<SQLExpr> parseColumns(List<SQLSelectOrderByItem>  sqlSelectOrderByItems){
        List<SQLExpr> retList = new ArrayList<SQLExpr>();
        for(SQLSelectOrderByItem  items :sqlSelectOrderByItems){
            SQLExpr expr = items.getExpr();
            retList.add(expr);
        }
        return retList;
    }
}
