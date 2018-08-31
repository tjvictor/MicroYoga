function getVideos(){
    callAjax('/websiteService/getVideos', '', 'getVideosCallback', '', '', '', '');
}
function getVideosCallback(data){
    if (data.status == "ok" && data.callBackData.length > 0) {
        var template = '';
        for (var i = 0; i < data.callBackData.length; i++) {
            var video = data.callBackData[i];
            template += '<a data-video-path="' + video.videoPath + '" data-video-type="' + video.type + '" href="javascript:void(0);" class="weui-media-box weui-media-box_appmsg yoga-open-video">';
            template += '   <div class="weui-media-box__hd"><img class="weui-media-box__thumb picture-radius-30px" src="' + video.imgPath + '" alt=""></div>';
            template += '   <div class="weui-media-box__bd">';
            template += '       <h4 class="weui-media-box__title">' + video.name + '</h4>';
            template += '       <p class="weui-media-box__desc">' + video.brief + '</p>';
            template += '   </div>';
            template += '</a>';
        }

        $('.weui-panel__bd').html(template);
    }else {
        $('.weui-panel__bd').html('<p style="padding:0.1rem;color:red;">暂时没有任何视频。</p>');
    }
}