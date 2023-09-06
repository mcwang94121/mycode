package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.DemoDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * demoMapper
 *
 * @author mc
 * @date 2022年12月19日
 */
@Mapper
public interface DemoMapper extends BaseMapper<DemoDO> {
}
