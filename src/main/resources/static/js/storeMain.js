
$(window).scroll(function () {
    if ($(window).scrollTop()+$(window).height()+30> $(document).height()) {

        $.ajax({
            url: '/infiniteScrollDown',
            type: 'GET',
            dataType: 'JSON',

            success: function (data) {

                var mainList = data.mainList;
                var infiniteList = '\n';

                $.each(mainList, function (index, value) {
                    var price=value.customerPrice;

                    infiniteList = infiniteList
                        + '<div class="col-6 col-md-3 product-item-wrap"><article class="production-item"><a class="production-item__overlay" href="/productions/productDetails?productNo=' + value.productNo
                        + '"></a><div class="production-item-image production-item__image">'
                        + '<img class="image" alt="" src="/uploadImg/' + value.productMainImg + '"></div>'
                        + '<div class="production-item__content"><h1 class="production-item__header"><span class="production-item__header__brand">' + value.sellerName + '</span>'
                        + '<span class="production-item__header__name">' + value.productName + '</span></h1>'
                        + '<span class="production-item-price"><span class="production-item-price__price">' + price.toLocaleString() + '</span></span></div></article></div>'

                });

                $('#autoScroll').append(infiniteList);
            },

            error: function () {
                alert('더이상의 상품이 존재하지 않습니다.');
            }
        })
    }
});

function enterChat(formName) {
    window.open("", "new_popup", "scrollbars=no, width=400, height=450, menubar=false, location=false");
    formName.target = "new_popup";
    formName.action = "/chat";
    formName.method = "post";
    formName.submit();
}