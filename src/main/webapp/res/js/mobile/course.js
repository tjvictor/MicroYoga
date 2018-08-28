function getCourses(){
    callAjax('/websiteService/getCourses', '', 'getCoursesCallback', '', '', '', '');
}
function getCoursesCallback(data){
    if (data.status == "ok" && data.callBackData.length > 0) {
        var template = '';
        for (var i = 0; i < data.callBackData.length; i++) {
            var course = data.callBackData[i];
            template += '<a data-id="' + course.id + '" href="javascript:void(0);" class="weui-media-box weui-media-box_appmsg yoga-open-popup">';
            template += '   <div class="weui-media-box__hd"><img class="weui-media-box__thumb picture-radius-30px" src="' + course.avatar + '" alt=""></div>';
            template += '   <div class="weui-media-box__bd">';
            template += '       <h4 class="weui-media-box__title">' + course.name + '</h4>';
            template += '       <p class="weui-media-box__desc">';
            template += '           <span class="text-font-size-14px" style="margin-right:5px;">难度</span>';

            for(var j = 0 ; j < course.rating; j++){
                template += '       <img src="res/img/star_16.png">';
            }

            template += '       </p>';
            template += '   </div>';
            template += '</a>';

            $('.weui-panel__bd').data(course.id, course.introduction);
        }

        $('.weui-panel__bd').html(template);
    }else {
        $('.weui-panel__bd').html('<p style="padding:0.1rem;color:red;">暂时没有任何课程介绍。</p>');
    }
}

function getOneWeekScheduledCourses(){
    callAjax('/websiteService/getOneWeekScheduledCourses', '', 'getOneWeekScheduledCoursesCallback', '', '', '', '');
}
function getOneWeekScheduledCoursesCallback(data){
    if (data.status == "ok" && data.callBackData.length > 0) {

    }
}