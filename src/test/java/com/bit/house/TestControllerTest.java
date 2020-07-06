package com.bit.house;

import com.bit.house.domain.AllMemberVO;
import com.bit.house.mapper.AllMemberMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class TestControllerTest {

    @Autowired
    AllMemberMapper allMemberMapper;

    @Test
    public void test() {
        AllMemberVO allMemberVO = allMemberMapper.read("kim123");

        log.info(String.valueOf(allMemberVO));
    }
}
