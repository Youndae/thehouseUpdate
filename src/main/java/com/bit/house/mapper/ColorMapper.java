package com.bit.house.mapper;

import com.bit.house.domain.ColorVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ColorMapper {

    List<ColorVO> searchColor();

}
