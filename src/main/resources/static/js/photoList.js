
$(window).scroll(function () {
    if ($(window).scrollTop()+$(window).height()+30> $(document).height()) {

        $.ajax({
            url: '/photoScrollDown',
            type: 'GET',
            dataType: 'JSON',

            success: function (data) {

                var photoList = data.photoList;
                var infiniteList = '\n';

                $.each(photoList, function (index, value) {
                    var price=value.customerPrice;

                    infiniteList = infiniteList
                        + '<div class="col-6 col-md-3 product-item-wrap"><article class="production-item"><a class="production-item__overlay" href="/photodetail/photoBoardNo=' + value.photoBoardNo
                        + '"></a><div class="production-item-image production-item__image">'
                        + '<img class="image" alt="" src="/uploadImg' + value.photoImg1 + '"></div>'
                        + '</div></article></div>'

                });

                $('#autoScroll').append(infiniteList);
            },

            error: function () {
                alert('더이상의 사진이 존재하지 않습니다.');
            }
        })
    }
});
