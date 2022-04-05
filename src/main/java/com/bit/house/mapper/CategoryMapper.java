package com.bit.house.mapper;

import com.bit.house.domain.CategoryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    List<CategoryVO> searchCategory();

}
