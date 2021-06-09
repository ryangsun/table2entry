# 将Mysql建表语句转义为实体

## 包引用
```
<dependency>
    <groupId>com.bumao.model</groupId>
    <artifactId>table2entry</artifactId>
    <version>0.0.1</version>
</dependency>
```

## 使用
```
String sql = "CREATE TABLE `card` (\n" +
        "  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'PK',\n" +
        "  `batch_id` int(11) NOT NULL COMMENT '所属批次ID',\n" +
        "  `denomination` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '面额',\n" +
        "  `card_no` varchar(20) NOT NULL COMMENT '实体卡号',\n" +
        "  `card_pass` varchar(20) NOT NULL COMMENT '实体密码',\n" +
        "  `status` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '状态，0=未核销 1=已核销',\n" +
        "  `active_time` datetime DEFAULT NULL COMMENT '核销核销时间',\n" +
        "  `active_userid` varchar(32) DEFAULT NULL COMMENT '核销用户ID',\n" +
        "  `active_mobile` varchar(20) DEFAULT NULL COMMENT '核销时用户的手机号码',\n" +
        "  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
        "  `stop_time` datetime NOT NULL COMMENT '结束核销时间',\n" +
        "  `active_openid` varchar(64) DEFAULT NULL COMMENT '用户openid',\n" +
        "  PRIMARY KEY (`id`) USING BTREE,\n" +
        "  KEY `batch_id` (`batch_id`) USING BTREE,\n" +
        "  KEY `status` (`status`) USING BTREE\n" +
        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='卡券批次表';" +
        "select * from card;";
ToEntry toEntry = new ToEntry();
List<TableEntryVo> tableEntryVos = toEntry.getEntry(sql);
log.info("tableEntryVos={}", JSONArray.toJSON(tableEntryVos));
```
- 可同时转义多条建表语句，请用“;”隔开。
- 会忽略建表语句以外的sql。
