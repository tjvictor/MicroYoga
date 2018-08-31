function getTeachers(){
    callAjax('/websiteService/getTeachers', '', 'getTeachersCallback', '', '', '', '');
}
function getTeachersCallback(data){
    if (data.status == "ok" && data.callBackData.length > 0) {
        var template = '';
        for (var i = 0; i < data.callBackData.length; i++) {
            var teacher = data.callBackData[i];
            template += '<a data-id="' + teacher.id + '" href="javascript:void(0);" class="weui-media-box weui-media-box_appmsg yoga-open-popup">';
            template += '   <div class="weui-media-box__hd"><img class="weui-media-box__thumb picture-radius-30px" src="' + teacher.avatar + '" alt=""></div>';
            template += '   <div class="weui-media-box__bd">';
            template += '       <h4 class="weui-media-box__title">' + teacher.name + '</h4>';
            template += '       <p class="weui-media-box__desc">个人介绍: ' + teacher.brief + '</p>';
            template += '   </div>';
            template += '</a>';

            $('.weui-panel__bd').data(teacher.id, teacher.introduction);
        }

        $('.weui-panel__bd').html(template);
    }else {
        $('.weui-panel__bd').html('<p style="padding:0.1rem;color:red;">暂时没有任何老师介绍。</p>');
    }
}