
    /*$(document).ready(function(){

        $(셀렉트박스 option).each(function(){

            if($(this).val()=="${DB값}"){

                $(this).attr("selected","selected"); // attr적용안될경우 prop으로

            }

        });

    });*/





$('#getTP').text( getTP());
$('#paymentTP').text(getTP());

function  getTP() {
    console.log("getTP실행");
    var tPrice=new Array();
    var TP = 0;
    $('.selling-option-item__price__number').each(function(){
        tPrice.push($(this).attr('price-total'));
    });
    //console.log("tPrice :: "+tPrice);

    for(var m = 0; m<tPrice.length; m++){
        console.log(Number(tPrice[m]));
        TP += Number(tPrice[m]);
    }
    console.log("결과 : "+ TP);
    return TP;

}

function checkAll(){
    if( $("#th_checkAll").is(':checked') ){
        $("input[name=innerCheckbox]").prop("checked", true);
    }else{
        $("input[name=innerCheckbox]").prop("checked", false);
    }
}
$("input[name=innerCheckbox]").click(function(){
    $("#th_checkAll").prop("checked", false);
});