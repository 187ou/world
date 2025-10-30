package com.hncs.world.service;

import com.hncs.world.pojo.dto.SaveRecordDto;
import com.hncs.world.pojo.entity.Read;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hncs.world.pojo.vo.ReadRecordVo;

/**
* @author 24774
* @description 针对表【read(用户阅读记录表)】的数据库操作Service
* @createDate 2025-10-16 10:18:38
*/
public interface ReadService extends IService<Read> {

    ReadRecordVo getBookReadRecord(String  token);

    Integer saveBookReadRecord(String  token, SaveRecordDto saveRecordDto);
}
