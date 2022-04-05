package com.bit.house.service;

import com.bit.house.domain.PhotoBoardVO;
import com.bit.house.mapper.PhotoBoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

@Service
public class PhotoBoardServiceImpl implements PhotoBoardService{


    @Autowired(required = false)
    PhotoBoardMapper mapper;

    @Override
    public void photoUpdateProc(PhotoBoardVO photoBoardVO, MultipartHttpServletRequest mtf, HttpServletRequest request) throws Exception{



        //삭제할 이미지가 존재한다면 넘겨서 처리할 수 있도록 구현.
        if(request.getParameterValues("delOldPhotoName") != null){
            deletePhoto(request);
        }

        //기존이미지 + 새로 추가한 이미지의 파일명
        String[] allPhoto = request.getParameterValues("allPhotoName");

        /*
            문제점.
            하나의 row에 1,2,3,4,5 라는 이미지 데이터가 저장되기 때문에 데이터가 삭제되었을 경우에
            컬럼에 빈공간이 생기게 되고 남아있는 이미지가 그 자리를 채워야 한다는 문제점이 있었음.
            그래서 처음에는 삭제될 컬럼과 남게될 컬럼을 구분해 당겨주는 방법을 사용하려 했으나
            그 후에 새로 등록하는 이미지를 컬럼에 넣어줄때 제대로 된 위치를 찾기 어렵다는 문제점이 발생.
            JQuery를 통해 삭제할때마다 삭제하는 이미지명을 input으로 생성해서 받아주도록 구현.(delOldPhotoName)
            그리고 등록 버튼을 눌렀을 때 preview에 존재하는 모든 이미지명을 input으로 생성해서 form submit (allPhotoName)

            이렇게 받아왔으므로 deletePhoto에서는 삭제할 파일명만을 받아 파일만 삭제하도록 구현.
            그리고 allPhotoName을 배열화시키고 이 배열의 길이에서 새로운 파일 개수만큼을 빼준다.
            그렇게 새로 등록되어야 할 파일의 인덱스를 찾고 파일 저장 후 맞는 인덱스에 데이터를 넣어준다.

            여기까지 마무리가 된다면 allPhoto배열에는 기존 파일이 앞쪽에 위치 그리고 새로 등록할 파일이 후순위에 위치하게 된다.

            그리고 이것들을 기존 switch문을 통해 처리해준다.


            아직 더 필요한 구현 코드는 기존 + 새 파일이 5가 되지 않았을때.
            수정 전 기존 이미지가 5개였는데 수정할때 그 이하로 줄였다면 불필요한 데이터가 남게 되기 때문에
            null처리를 할 수 있도록 추가해줘야 한다.

         */


        List<MultipartFile> fileList = mtf.getFiles("files");

        if(fileList.get(0).getSize() != 0){

            System.out.println("new file add");

            int idx = allPhoto.length - fileList.size();

            String filePath = request.getSession().getServletContext().getRealPath("image/board/photoboard/");

            for(MultipartFile mf : fileList){

                StringBuffer sb = new StringBuffer();

                String originFileName = mf.getOriginalFilename();

                String saveName =  sb.append(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()))
                        .append(UUID.randomUUID().toString())
                        .append(originFileName.substring(originFileName.lastIndexOf("."))).toString();

                String saveFile = filePath + saveName;

                allPhoto[idx] = "/board/photoboard/" + saveName;

                mf.transferTo(new File(saveFile));

                idx++;
            }
        }

        int filesSize= allPhoto.length;
        if( filesSize !=0){
            int i=1;
            switch (filesSize ){
                case 5 :
                    photoBoardVO.setPhotoImg5(allPhoto[allPhoto.length - i]);
                    i++;
                case 4 :
                    photoBoardVO.setPhotoImg4(allPhoto[allPhoto.length - i]);
                    i++;
                case 3 :
                    photoBoardVO.setPhotoImg3(allPhoto[allPhoto.length - i]);
                    i++;
                case 2 :
                    photoBoardVO.setPhotoImg2(allPhoto[allPhoto.length - i]);
                    i++;
                case 1 :
                    photoBoardVO.setPhotoImg1(allPhoto[allPhoto.length - i]);
            }
        }

        if(filesSize < 5){
            switch(5 - filesSize){
                case 4 :
                    photoBoardVO.setPhotoImg2(null);
                case 3 :
                    photoBoardVO.setPhotoImg3(null);
                case 2 :
                    photoBoardVO.setPhotoImg4(null);
                case 1 :
                    photoBoardVO.setPhotoImg5(null);
            }
        }

        photoBoardVO.setPhotoContent(request.getParameter("photoContent"));
        photoBoardVO.setAreaCode(request.getParameter("areaCode"));
        photoBoardVO.setHouseCode(request.getParameter("houseCode"));
        photoBoardVO.setStyleCode(request.getParameter("styleCode"));
        photoBoardVO.setPlaceCode(request.getParameter("placeCode"));

        mapper.updatePhoto(photoBoardVO);

    }


    public void deletePhoto(HttpServletRequest request){

        /*
            삭제하는 이미지의 컬럼 번호와 파일명을 받아와서
            파일 삭제 및 DB 데이터 삭제 후
            기존 이미지데이터들을 앞쪽으로 당기는 로직으로 구현.

            반복문으로 삭제 이미지 컬럼과 비교해 삭제하지 않는 번호들을 추출해
            resultNum 이라는 배열에 넣어주고
            resultNum 배열에 있는 값들을 1번 컬럼부터 순서대로 넣어주는 방법을 선택.
            그리고 배열에 있는 값들을 전부 처리했다면 나머지는 null로 비워두도록 구현.
            resultNum의 모든 값이 처리가 끝났다면 0을 넘겨 쿼리문에서 0을 받았을때는
            null로 처리하도록 구현.

            resultNum의 length가 0인 경우는 모든 기존 이미지를 삭제했다는것이기 때문에
            굳이 쿼리문에서 null로 만들 필요가 없다고 판단.
            작동하지 않도록 조건문으로 제어.
            전부 삭제한 경우는 update 해주면서 남은 컬럼을 null로 처리하는 형태로 구현.

         */


        //삭제할 이미지 파일명
        String[] delOldPhotoNames = request.getParameterValues("delOldPhotoName");




        for(int i = 0; i < delOldPhotoNames.length; i++){

            String filePath = request.getSession().getServletContext().getRealPath("image/");

            File file = new File(filePath + delOldPhotoNames[i]);

            if(file.exists()){
                file.delete();
            }
        }

    }
}
