//좋아요버튼
$(function(){
    $("#like").click(function(){

        var BoardNo = {
            photoBoardNo : $("#photoBoardNo").val(),
        }

        console.log(BoardNo);

        $.ajaxSettings.traditional = true;
        $.ajax({
            type: "post",
            url : "/like",
            data : BoardNo,
            success : function(data){

                location.reload();
            },
        });
    });
});

//좋아요취소버튼
$(function(){
    $("#cancellike").click(function(){
        var BoardNo = {
            photoBoardNo: $("#photoBoardNo").val(),
        };
        console.log(BoardNo);

        $.ajaxSettings.traditional = true;
        $.ajax({
            type: "post",
            url: "/nonlike",
            data: BoardNo,
            success : function(data){

                location.reload();
            },
        });
    });
});

$(function(){
    $("#likeNon").click(function(){

        if(confirm("좋아요를 누르려면 로그인을 하셔야합니다.\n로그인하시겠습니까?")){
            location.href="/customLogin";
        }

    })
})

//스크랩버튼
$(function(){
    $("#scrap").click(function(){
        var BoardNo = {
            photoBoardNo : $("#photoBoardNo").val(),
        };
        console.log(BoardNo);

        $.ajaxSettings.traditional = true;
        $.ajax({
            type: "post",
            url: "/scrap",
            data: BoardNo,
            success : function(data){

                location.reload();
            },
        });
    });
});

$(function(){
    $("#cancelscrap").click(function(){
        var BoardNo = {
            photoBoardNo : $("#photoBoardNo").val(),
        };
        console.log(BoardNo);

        $.ajaxSettings.traditional = true;
        $.ajax({
            type: "post",
            url: "/nonscrap",
            data: BoardNo,
            success : function(data){

                location.reload();
            },
        });
    });
});

$(function(){
    $("#scrapNon").click(function(){

        if(confirm("스크랩을 하시려면 로그인을 하셔야합니다.\n로그인하시겠습니까?")){
            location.href="/customLogin";
        }
    })
})

$(function(){
    $("#notesend").click(function(){
        var receive = $("#memberId").val();
        console.log(receive);

        document.sending.receiveId.value=receive;

        $("#sending").submit();
    });

});


var num;

$(document).ready(function(e){
    var len = $(".expert-form-group").length;
    num  = len;

    $("#files").change(function(e){
        var files = e.target.files;
        var arr = Array.prototype.slice.call(files);

        len = $(".expert-form-group").length;

        console.log("files num : " + num);

        if(files.length > 5){
            alert("이미지는 최대 5장까지만 등록이 가능합니다.");
        }else if(5 - len < files.length){
            alert("이미지는 최대 5장까지만 등록이 가능합니다.")
        }else{
            for(var i = 0; i < files.length; i++){
                if(!checkExtension(files[i].name, files[i].size)){
                    return false;
                }
            }
            preview_photoBoard(arr);
        }
    })
});

function preview_photoBoard(arr){
    console.log("preview num : " + num);

    arr.forEach(function(f){
        console.log("preview Each");

        console.log("num UP : " + num);

        var fileName = f.name;

        if(fileName.length > 10){
            fileName = fileName.substring(0, 7) + "...";
        }

        var str = "";

        console.log("BoardPreview N : " + num);

        var reader = new FileReader();

        reader.onload = function(e){
            num++;
            console.log("function n : " + num);
            str = "<div class=\"expert-form-group edit-user-info__form-item__group\" value=\"" + num + "\">" +
                    "<div class=\"expert-form-group__content\">" +
                        "<div class=\"expert-form-group__input\">" +
                            "<div class=\"edit-user-info__form-item__field edit-user-info__form-item__field--profile\">" +
                                "<div id=\"preview_box" + num + "\">" +
                                    "<div class=\"image-single-input-wrap\">" +
                                        "<ul class=\"image-single-input\">" +
                                            "<li class=\"image-single-input__entry\">" +
                                                "<div class=\"preview_img\">" +
                                                    "<img src=\"" + e.target.result + "\"/>" +
                                                "</div>" +
                                            "</li>" +
                                        "</ul>" +
                                    "</div>" +
                                "</div>" +
                                "<button class=\"button button--color-blue button--size-30 button-shape-4 edit-user-info__form-item__delete\" " +
                                        "type=\"button\" value=\"" + num + "\" data=\"" + e.target.result + "\" onclick=\"deletePhoto(this)\">" +
                                    "삭제" +
                                "</button>" +
                            "</div>" +
                        "</div>" +
                    "</div>" +
                "</div>";
            console.log("function end n : " + num);
            $(str).appendTo('.preview_area');
        }
        reader.readAsDataURL(f);
    })
}

$(function(){
    $("#photoinsert").click(function(){
        var area = $("#areaSelect").val();
        var house = $("#houseSelect").val();
        var style = $("#styleSelect").val();
        var place = $("#placeSelect").val();


        document.insertphoto.area.value=area;
        document.insertphoto.style.value=style;
        document.insertphoto.house.value=house;
        document.insertphoto.place.value=place;


        $("#insertphoto").submit();

    });
});

function update(idx) {
    var photoBoardNo = idx;
    location.href = "/photoupdate/" + photoBoardNo;
};

function del(idx){
    var photoBoardNo = idx;
    location.href = "/photodelete/" + photoBoardNo;
};

$(function(){
    $("#photoupdate").click(function(){
        var area = $("#areaSelect").val();
        var house = $("#houseSelect").val();
        var style = $("#styleSelect").val();
        var place = $("#placeSelect").val();

        document.updatephoto.area.value=area;
        document.updatephoto.style.value=style;
        document.updatephoto.house.value=house;
        document.updatephoto.place.value=place;


        console.log("num : " + num);

        for(var i = 1; i <= num; i++){

            var imgNames = $('button[value=' + i + ']').attr('data')

            if(imgNames != undefined){
                var allPhotoName = "<input type=\"hidden\" name=\"allPhotoName\" value=\"" + imgNames + "\">";
                $("#updatephoto").append(allPhotoName);
            }
        }

        $("#updatephoto").submit();
    });
});

$(function(){
    $("#pCommentInsert").click(function(){

        var Content = $("#comment").serialize();

        $.ajaxSettings.traditional = true;
        $.ajax({
            type: "post",
            url: "/insertPhotoComment",
            data: Content,
            success : function(data){
                console.log(data);

                location.reload();
            },
        });
    });
});

$(function(){
    $("#memberprofile").click(function(){
        var userId = $("#memberId").val();

        document.profile.userId.value=userId;

        $("#profile").submit();
    })
})

function deleteOldPhoto(obj){
    var imgNum = obj.attributes['value'].value;
    console.log("del Old imgNum : " + imgNum);

    var imgName = obj.attributes['data'].value;

    var delName = "<input type=\"hidden\" name=\"delOldPhotoName\" value=\"" + imgName + "\">";


    $(".expert-form-group[value="+ imgNum + "]").remove();



    $("#updatephoto").append(delName);
}

function deletePhoto(obj){
    var imgNum =obj.attributes['value'].value;

    console.log("del imgNum : " + imgNum);

    $(".expert-form-group[value="+ imgNum + "]").remove();


}

//이미지 등록

