//쪽지 체크박스 전체 선택
$(function(){
    $("#allcheck").click(function(){
        var chk = $(this).is(":checked");
        if(chk) $(".select_note input").prop('checked', true);
        else $(".select_note input").prop('checked', false);
    });
});

//팔로우버튼
$(function () {
    $("#follow").click(function(){

        var follow = {
            followId: $("#memberId").val(),
        };

        $.ajaxSettings.traditional=true;
        $.ajax({
            type : "post",
            url : "/member/follow",
            data : follow,
            success : function(data){

                location.reload();
            },
        });
    });
});

//팔로우취소버튼
$(function(){
    $("#followcancel").click(function(){

        var follow = {
            followId: $("#memberId").val(),
        };

        $.ajaxSettings.traditional=true;
        $.ajax({
            type : "post",
            url : "/member/cancelFollow",
            data : follow,
            success : function(data){

                location.reload();
            },
        });
    });
});


//프로필페이지에서 쪽지보내기 버튼
$(function(){
    $("#notesend").click(function(){
        var receive = $("#memberId").val();
        document.sending.receiveId.value = receive;

        $("#sending").submit();
    });
});

//프로필페이지 사진 전체보기
$(function(){
    $("#allphoto").click(function(){
        var member = $("#fol").val();
        document.photo.memberId.value = member;


        $("#photo").submit();
    });
});


//프로필페이지 스크랩 전체보기
$(function(){
    $("#allscrap").click(function(){
        var member = $("#fol").val();
        document.scrap.memberId.value = member;


        $("#scrap").submit();
    });
});



//쪽지페이지에서 보내기버튼
$(function() {
    $("#send").click(function () {
        var count = $(".select_note input:checked").length;
        var chk;
        $("input[name=check]:checked").each(function () {
            chk = ($(this).attr("data-sendId"));
        });
        document.sending.receiveId.value = chk;

        if (count == 0) {
            alert("선택된 쪽지가 없습니다.")
        } else {
            if (count > 1) {
                alert("하나만 선택해주세요.")
            } else {

                $("#sending").submit();

            }
        }
    });
});

//쪽지 삭제 버튼
$(function() {
    $("#delete").click(function(){
        var dcount = $(".select_note input:checked").length;
        var dchk=new Array();
        $("input[name=check]:checked").each(function(){
            dchk.push($(this).attr("data-No"));
        });
        console.log(dchk);
        if(dcount == 0){
            alert("선택된 쪽지가 없습니다.")
        }else{
            confirm(dcount+"개의 쪽지를 삭제하시겠습니까?")

            $.ajaxSettings.traditional=true;
            $.ajax({
                url : "/member/deleteNote",
                type : "post",
                data : {msgNum : dchk},
                success : function(data){
                    location.reload(true);
                },
            });
        }
    });
});


//profileInfo 사진 업로드
$(document).ready(function (e){
    $("input[type='file']").change(function(e){

        $('#preview').empty();

        var files = e.target.files;
        var arr =Array.prototype.slice.call(files);

        for(var i=0;i<files.length;i++){
            if(!checkExtension(files[i].name,files[i].size)){
                return false;
            }
        }

        preview(arr);


    });

    function checkExtension(fileName,fileSize){

        var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
        var maxSize = 10485760;

        if(fileSize >= maxSize){
            alert('파일 사이즈 초과');
            $("input[type='file']").val("");
            return false;
        }

        if(regex.test(fileName)){
            alert('업로드 불가능한 파일이 있습니다.');
            $("input[type='file']").val("");
            return false;
        }
        return true;
    }

    function preview(arr){
        arr.forEach(function(f){

            var fileName = f.name;
            if(fileName.length > 10){
                fileName = fileName.substring(0,7)+"...";
            }

            var str = '<div style="display: inline-flex; ">';



            if(f.type.match('image.*')){
                var reader = new FileReader();
                reader.onload = function (e) {
                    str += '<img src="'+e.target.result+'" title="'+f.name+'" width=300 height=300 />';
                    str += '</li></div>';
                    $(str).appendTo('#preview');
                }
                reader.readAsDataURL(f);
            }else{
                str += '<img src="/resources/img/fileImg.png" title="'+f.name+'" width=300 height=300 />';
                $(str).appendTo('#preview');
            }
        });
    }
});

