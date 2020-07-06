package com.bit.house.service;

import com.bit.house.domain.ColorVO;
import com.bit.house.mapper.ColorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColorServiceImpl implements ColorService {

    @Autowired
    ColorMapper colorMapper;

    @Override
    public List<ColorVO> searchColor() {
        return colorMapper.searchColor();
    }
}
