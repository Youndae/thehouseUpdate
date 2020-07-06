


$('#getTP').text(getTP());
$('#paymentTP').text(getTP());

function getTP() {
    console.log("getTP실행");
    var tPrice = new Array();
    var TP = 0;
    $('.totalCost').each(function () {
        tPrice.push($(this).attr('price-total'));
    });
    //console.log("tPrice :: "+tPrice);

    for (var m = 0; m < tPrice.length; m++) {
        console.log(Number(tPrice[m]));
        TP += Number(tPrice[m]);
    }
    console.log("결과 : " + TP);
    return TP;

}

function kakaoSubmit() {

    var adr = $('#order_recipient').val();
    var man = $('#order_received_at').val();
    if (adr == '' || man == '') {
        alert("제대로 입력해주세요");
    } else {
        document.getElementById('edit_order').submit();
    }
}

var date = new Date();
var year = date.getFullYear();
var month = new String(date.getMonth() + 1);
var day = new String(date.getDate());
var dateResult = year+'-'+month+'-'+day;
// 한자리수일 경우 0을 채워준다.
if (month.length == 1) {
    month = "0" + month;
}
if (day.length == 1) {
    day = "0" + day;
}
console.log("날짜 : " + year + month + day);

var IMP = window.IMP;

function requestPay() {

    var adr = $('#order_recipient').val();
    var man = $('#order_received_at').val();
    if (adr == '' || man == '') {
        alert("제대로 입력해주세요");
    } else {


        IMP.init("imp76163221"); // "imp00000000" 대신 발급받은 "가맹점 식별코드"를 사용합니다.

        console.log("이름 테스트 : " + $('#productNameTest').text() + " 외");
        console.log("이메일 테스트 : " + $('#order_payer_email').val() + " 이름 : " + $('#order_payer_name').val()
            + " 집코드 : " + $('#order_received_zip_code').val() + " 번호 : " + $('#order_payer_phone_number').val());
        console.log("test : " + $('#productNo').val() + "memId : " + $('#memberId').val() + "colorName :" + $('#colorName').val()
            + "orderQty" + $('#orderQty').val() + "totalPrice" + $('#totalPrice').val() + "recipient : " + $('#order_recipient').val()
            + "recieved_at : " + $('#order_received_at').val())
        //IMP.request_pay(param, callback) 호출
        var price = $('#paymentTP').text();
        console.log("실험 : " + $('#order_payer_email').val() + $('#order_payer_name').text() + $('#order_payer_phone_number').val()
            + $('#order_received_at').text());
        price = Number(price);
        IMP.request_pay({ // param
            pg: "inicis",
            pay_method: "card",
            merchant_uid: 'merchant_' + new Date().getTime(),
            name: $('#productNameTest').text() + " 외",
            amount: 100,
            buyer_email: $('#order_payer_email').val(),
            buyer_name: $('#order_payer_name').val(),
            buyer_tel: $('#order_payer_phone_number').val(),
            buyer_addr: $('#order_received_at').val(),
            buyer_postcode: $('#order_received_zip_code').val()
        }, function (rsp) { // callback
            if (rsp.success) {
                var now = new Date();
                var nowTime = now.getFullYear();
                nowTime += '-' + (now.getMonth() + 1);
                nowTime += '-' + now.getDate();
                $.ajax({
                    url: '/paymentCom',
                    type: "POST",

                    data: {
                        orderNo: null,
                        productNo: $('#productNo').val(),
                        memberId: $('#memberId').val(),
                        colorName: $('#colorName').val(),
                        orderQty: $('#orderQty').val(),
                        totalPrice: price,
                        recipient: $('#order_recipient').val(),
                        orderAddr: $('#order_received_at').val(),
                        orderDate: dateResult,
                        payCode: "2",
                        orderCode: "10"
                    },
                    success: function () {
                        alert("결제가 완료되었습니다.");
                        location.href = "/member/order_list";
                    },
                    error: function () {
                        alert('ajax 실패')
                    }

                })
            } else {
                alert("결제에 실패하였습니다. 에러 내용: " + rsp.error_msg);
            }
        });
    }
}
function execPostCode() {
    new daum.Postcode({
        oncomplete: function (data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 도로명 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var fullRoadAddr = data.roadAddress; // 도로명 주소 변수
            var extraRoadAddr = ''; // 도로명 조합형 주소 변수

            // 법정동명이 있을 경우 추가한다. (법정리는 제외)
            // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
            if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                extraRoadAddr += data.bname;
            }
            // 건물명이 있고, 공동주택일 경우 추가한다.
            if (data.buildingName !== '' && data.apartment === 'Y') {
                extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
            }
            // 도로명, 지번 조합형 주소가 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
            if (extraRoadAddr !== '') {
                extraRoadAddr = ' (' + extraRoadAddr + ')';
            }
            // 도로명, 지번 주소의 유무에 따라 해당 조합형 주소를 추가한다.
            if (fullRoadAddr !== '') {
                fullRoadAddr += extraRoadAddr;
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            console.log(data.zonecode);
            console.log(fullRoadAddr);


            $("[name=zipCode]").val(data.zonecode);
            $("[name=receivedAt]").val(fullRoadAddr);

            //document.getElementById('signUpUserPostNo').value = data.zonecode; //5자리 새우편번호 사용
            //document.getElementById('signUpUserCompanyAddress').value = fullRoadAddr;
            //document.getElementById('signUpUserCompanyAddressDetail').value = data.jibunAddress;
            window.close();
        }
    }).open();
}

var name = "";
var phoneNum = "";

function InitValue(frm) {

    name = frm.order_payer_name.value; //이름
    phoneNum = frm.order_received_phone_number.value;
}

function ShipToBill(frm) {
    var n = null;
    if (frm.copy_delivery.checked) {

        InitValue(frm);			//현재 텍스트박스와 체크박스의 상태유지

        document.getElementById("order_payer_name").value = document.getElementById("order_recipient").value; // 이름 = 받는사람
        document.getElementById("order_payer_phone_number").value = document.getElementById("order_received_phone_number").value;

    } else {						//체크박스를 해제한다면

        frm.order_payer_name.value = ''; // 이름 = 이름
        frm.order_payer_phone_number.value = '';
    }
    /*if(!frm.copy_delivery.checked){
        document.getElementById("order_recipient").value = '';
    }*/
}
function check() {
    if (fr.zipCode.value == "") { // 지번
        alert("값을 입력해 주세요.1");
        fr.fr.zipCode.value.focus();
        return false;
    } else if (fr.receivedAt.value == "") {	//주소
        alert("값을 입력해 주세요.2");
        fr.fr.receivedAt.value.focus();
        return false;
    } else if (receivedAtDetail == "") {	//주소
        alert("값을 입력해 주세요.3");
        fr.receivedAtDetail.focus();
        return false;
    } else return true;
}

function saveMemberAddr() {
    //document.getElementById('signUpUserPostNo').value = data.zonecode; //5자리 새우편번호 사용
    //document.getElementById('signUpUserCompanyAddress').value = fullRoadAddr;
    //document.getElementById('signUpUserCompanyAddressDetail').value = data.jibunAddress;
    var zipCode1 = document.getElementById('order_received_zip_code').value
    var receivedAt1 = document.getElementById('order_received_at').value
    var receivedAtDetail1 = document.getElementById('order_received_at_detail').value
    var fullAddr;
    console.log(zipCode1 + receivedAt1 + receivedAtDetail1 + "\n" + fullAddr);
    if (zipCode1 == '' || receivedAt1 == '') {
        alert("주소를 입력하세요");
    } else {
        fullAddr = zipCode1 + receivedAt1 + receivedAtDetail1;
        $.ajax({
            url: 'saveAddr',
            type: "POST",
            data: {
                fullAddr: fullAddr
            }, success: function () {
                console.log("성공");
                alert("저장 됐습니다");
            }, error: function () {
                console.log("실패");
            }
        })
    }
}
