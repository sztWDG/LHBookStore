package com.example.handle;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BaseEntityMetaObjectHandle implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "creator", Long.class, StpUtil.getLoginIdAsLong());
//        this.strictInsertFill(metaObject, "modifier", Long.class, StpUtil.getLoginIdAsLong());
//        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
//        this.strictInsertFill(metaObject, "modifyTime", LocalDateTime.class, LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("updateSuccess");
        //BQ:updateFill审计字段过滤器
        this.strictUpdateFill(metaObject, "modifier", Long.class, StpUtil.getLoginIdAsLong());
//        this.strictInsertFill(metaObject, "modifyTime", LocalDateTime.class, LocalDateTime.now());
    }
}
