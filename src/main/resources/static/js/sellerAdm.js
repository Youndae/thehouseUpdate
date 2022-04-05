function apply(idx){
    var sellerName = idx;
    location.href = "/applyProc" + sellerName;
};

function sellerProduct(idx){
    var sellerName = idx;
    location.href = "/allSellerProduct" + sellerName;
};

$(function(){
    $("#sellerDetail").click(function(){
        var sellerName = $("#seller").val();

        document.detailSeller.sellerName.value=sellerName;

        $("#detailSeller").submit();
    });
});

$(function(){
    $("#productAll").click(function(){
        var sellerName = $("#seller").val();

        document.allProduct.sellerProduct.value=sellerName;

        $("#allProduct").submit();
    });
});

$(function(){
    $("#apply").click(function(){
        var sellerName = $("#name").val();

        document.proc.sellerName.value=sellerName;

        $("#proc").submit();
    });
});