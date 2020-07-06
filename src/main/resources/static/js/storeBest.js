$(document).ready(function() {
    $('.ranking-type-filter__item--left').click(function () {
        $('.ranking-type-filter__item--right').removeClass('right--active')
        $('.ranking-type-filter__item--left').addClass('left--active');
    });
    $('.ranking-type-filter__item--right').click(function () {
        $('.ranking-type-filter__item--left').removeClass('left--active');
        $('.ranking-type-filter__item--right').addClass('right--active');
    });

    var rank=new Array();
    $('.ranking-product-item__number').each(function(){
        rank.push($(this).attr('data-rank'));
    });

    for(var i=0;i<rank.length;i++){
        switch(rank[i]){
            case "1" :{
                $('div[name=a1]').addClass('ranking-product-item__number__gold-wrap');
                $('span[name=b1]').addClass('ranking-product-item__number__gold');
                break;
            }
            case "2" :{
                $('div[name=a2]').addClass('ranking-product-item__number__silver-wrap');
                $('span[name=b2]').addClass('ranking-product-item__number__silver');
                break;
            }
            case "3" : {
                $('div[name=a3]').addClass('ranking-product-item__number__bronze-wrap');
                $('span[name=b3]').addClass('ranking-product-item__number__bronze');
                break;
            }
        }
    }
});
