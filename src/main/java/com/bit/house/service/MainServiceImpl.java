package com.bit.house.service;

import com.bit.house.domain.productQtyVO;
import com.bit.house.mapper.MainMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MainServiceImpl implements MainService {

    @Autowired(required = false)
    MainMapper mainMapper;

    @Override
    public List<productQtyVO> selectBestList() {
        /*//option테이블에서 productNo와 group by productNo별로 qty sum 가져오기
        List<productQtyVO> qtys=mainMapper.selectTotalQty();
        List<productQtyVO> bestlists=new ArrayList();
        int ranking=1;
        for(productQtyVO qty:qtys){
            //product테이블에서 옵션테이블에서 가져온 productNo의 디테일 가져오기
            productQtyVO bestList=mainMapper.selectDetail(qty.getProductNo());
            //옵션테이블의 productNo, totalQty 넣어주기
            bestList.setProductNo(qty.getProductNo());
            bestList.setTotalQty(qty.getTotalQty());
            bestList.setRanking(ranking++);
            bestlists.add(bestList);
        }*/

        return null;
    }
}
