package com.hncs.world.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hncs.world.pojo.dto.SaveRecordDto;
import com.hncs.world.pojo.entity.Purchased;
import com.hncs.world.pojo.entity.Read;
import com.hncs.world.pojo.vo.ReadRecordVo;
import com.hncs.world.service.ReadService;
import com.hncs.world.mapper.ReadMapper;
import org.springframework.stereotype.Service;

/**
* @author 24774
* @description 针对表【read(用户阅读记录表)】的数据库操作Service实现
* @createDate 2025-10-16 10:18:38
*/
@Service
public class ReadServiceImpl extends ServiceImpl<ReadMapper, Read>
    implements ReadService{


    @Override
    public ReadRecordVo getBookReadRecord(String token) {
        // 查询阅读记录
        QueryWrapper<Read> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("token", token);
        Read read = this.getOne(queryWrapper);

        //没有阅读记录
        if(read==null){
            return null;
        }

        //转换VO返回
        ReadRecordVo readRecordVo = new ReadRecordVo();
        readRecordVo.setBookName(read.getBookName());
        readRecordVo.setBookLink(read.getBookLink());
        readRecordVo.setChapterLink(read.getChapterLink());
        readRecordVo.setNumber(read.getNumber());

        return readRecordVo;
    }

    @Override
    public Integer saveBookReadRecord(String token, SaveRecordDto saveRecordDto) {
        // 查询记录
        QueryWrapper<Read> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("token", token);
        Read read = this.getOne(queryWrapper);

        if(read==null){
            // 新增
            Read newRead = new Read();
            newRead.setToken(token);
            newRead.setBookName(saveRecordDto.getBookName());
            newRead.setBookLink(saveRecordDto.getBookLink());
            newRead.setChapterLink(saveRecordDto.getChapterLink());
            newRead.setNumber(saveRecordDto.getNumber());
            return this.save(newRead)?1:0;
        }

        // 更新
        read.setToken(token);
        read.setBookName(saveRecordDto.getBookName());
        read.setBookLink(saveRecordDto.getBookLink());
        read.setChapterLink(saveRecordDto.getChapterLink());
        read.setNumber(saveRecordDto.getNumber());
        return this.updateById(read)?1:0;
    }
}




