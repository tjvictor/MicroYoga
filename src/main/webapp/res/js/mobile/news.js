function getAllNewsBrief(){
    callAjax('/websiteService/getAllNewsBrief', '', 'getAllNewsBriefCallback', '', '', '', '');
}
function getAllNewsBriefCallback(data){
    if (data.status == "ok" && data.callBackData.length > 0) {
        var template = '';
        for (var i = 0; i < data.callBackData.length; i++) {
            var news = data.callBackData[i];
            template += '<a data-news-id="'+news.id+'" href="javascript:void(0);" class="weui-media-box weui-media-box_appmsg yoga-open-popup">';
            template += '   <div class="weui-media-box__hd"><img class="weui-media-box__thumb picture-radius-30px" src="' + news.img + '" alt=""></div>';
            template += '   <div class="weui-media-box__bd">';
            template += '       <p class="weui-media-box__desc">' + news.title + '</p>';
            template += '   </div>';
            template += '</a>';
        }

        $('.weui-panel__bd').html(template);
    }else {
        $('.weui-panel__bd').html('<p style="padding:0.1rem;color:red;">暂时没有任何头条。</p>');
    }
}

function newsOpenPopup(data){
    if(data.status == "ok"){
        $('#full-popup-container .yoga-popup-container-text').html(data.callBackData.content);
        $('#full-popup-container').popup();
    }
}