package com.bit.house.service;

import com.bit.house.domain.CategoryVO;
import com.bit.house.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public  List<CategoryVO> searchCategory() {

        return categoryMapper.searchCategory();
    }

}
