var i = 1;

$(window).scroll(function () {

    var isEnd = false;

    if ($(window).scrollTop()+$(window).height()+30> $(document).height()) {

        $.ajax({
            url: '/photoScrollDown',
            type: 'GET',
            dataType: 'JSON',
            data: {idx : i},

            success: function (data) {

                var photoList = data.photoList;
                var infiniteList = '\n';


                $.each(photoList, function (index, value) {

                    infiniteList = infiniteList
                        + '<div class="col-6 col-md-3 product-item-wrap"><article class="production-item"><a class="production-item__overlay" href="/photodetail/photoBoardNo=' + value.photoBoardNo
                        + '"></a><div class="production-item-image production-item__image">'
                        + '<img class="image" alt="" src="/uploadImg' + value.photoImg1 + '"></div>'
                        + '</div></article></div>'

                });

                $('#autoScroll').append(infiniteList);


            },
        })
        i++;
    }

});
