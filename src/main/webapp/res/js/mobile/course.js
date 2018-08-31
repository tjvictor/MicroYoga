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
    var user = checkUser();
    if(user){
        callAjax('/websiteService/getOneWeekScheduledCourses', '', 'getOneWeekScheduledCoursesCallback', '', '', 'memberId='+user.id, '');
    }
}
function getOneWeekScheduledCoursesCallback(data){
    if (data.status == "ok" && data.callBackData.length > 0) {
        var scheduleWeeks = data.callBackData;
        for(var i = 0 ; i < scheduleWeeks.length ; i++){
            var item = scheduleWeeks[i];
            var template = '';
            $('.weui-navbar a:eq('+i+')').html('<p>'+item.shortDate+'</p><p>'+item.weekName+'</p>');
            for(var courseIndex = 0 ; courseIndex < item.scheduleExts.length ; courseIndex++){
                var scheduleExt = item.scheduleExts[courseIndex];
                template += '<div class="weui-media-box weui-media-box_appmsg">';
                template += '   <div class="weui-media-box__hd"><img class="weui-media-box__thumb picture-radius-30px" src="'+scheduleExt.courseAvatar+'" alt=""></div>';
                template += '   <div class="weui-media-box__bd">';
                template += '       <div class="weui-media-box__bd weui-media-box__title">'+scheduleExt.courseName+'</div>';
                template += '       <div class="weui-media-box_appmsg">';
                template += '           <div style="width:200px;">';
                template += '               <span class="text-font-size-14px" style="margin-right:5px;">难度</span>';

                for(var rating = 0 ; rating < scheduleExt.courseRating ; rating++){
                    template += '           <img src="res/img/star_16.png">';
                }

                template += '           </div>';

                if(scheduleExt.ordered){
                    template += '       <div class="weui-media-box__bd text-align-right"><a href="javascript:;" id="btn_'+scheduleExt.id+'" data-order-id="'+scheduleExt.orderId+'" data-schedule-id="'+scheduleExt.id+'" class="weui-btn weui-btn_mini weui-btn_warn cancel-button">取消</a></div>';
                }else{
                    if(scheduleExt.residualCount > 0){
                        template += '   <div class="weui-media-box__bd text-align-right"><a href="javascript:;" id="btn_'+scheduleExt.id+'" data-order-id="" data-schedule-id="'+scheduleExt.id+'" class="weui-btn weui-btn_mini weui-btn_primary order-button">预约</a></div>';
                    }else{
                        template += '   <div class="weui-media-box__bd text-align-right"><a href="javascript:;" class="weui-btn weui-btn_mini weui-btn_disabled weui-btn_warn">人满</a></div>';
                    }
                }

                template += '           </div>';
                template += '           <div class="weui-media-box__title text-font-size-14px">任教老师: '+scheduleExt.teacherName+'</div>';
                template += '           <div class="weui-media-box__title">';
                template += '               <div class="weui-media-box_appmsg"><div class="weui-media-box__bd text-font-size-14px">时间 '+scheduleExt.startTime+' - '+scheduleExt.endTime+'</div>';
                template += '               <div id="residual_'+scheduleExt.id+'" data-residual-count="'+scheduleExt.residualCount+'" class="weui-media-box__bd text-font-size-14px text-align-right">还可预约 '+scheduleExt.residualCount+' 人</div>';
                template += '           </div>';
                template += '       </div>';
                template += '   </div>';
                template += '</div>';
            }

            if(template !== ''){
                $('#tab'+i+' .weui-panel__bd').html(template);
            }else{
                $('#tab'+i+' .weui-panel__bd').html('<p style="padding:0.1rem;color:red;">今日无课程</p>');
            }
        }
    }
}

function orderCourse(scheduleId){
    var param = "scheduleId="+scheduleId+"&memberId=8025e911-91b3-498a-9989-0713b50f96ce";
    callAjax('/websiteService/insertOrder', '', 'insertOrderCallback', scheduleId, '', param, '');
}
function insertOrderCallback(scheduleId, data){
    if (data.status == "ok"){
        $.alert("预约成功", "提示");
        $('#btn_'+scheduleId).removeClass('weui-btn_primary').addClass('weui-btn_warn');
        $('#btn_'+scheduleId).removeClass('order-button').addClass('cancel-button');
        $('#btn_'+scheduleId).data('order-id',data.callBackData);
        $('#btn_'+scheduleId).text('取消');
        var count = parseInt($('#residual_'+scheduleId).data('residual-count'));
        count--;
        $('#residual_'+scheduleId).data('residual-count', count);
        $('#residual_'+scheduleId).html('还可预约 '+count+' 人');
    }
}

function cancelCourse(orderId, scheduleId){
    callAjax('/websiteService/deleteOrder', '', 'deleteOrderCallback', scheduleId, '', 'orderId='+orderId, '');
}
function deleteOrderCallback(scheduleId, data){
    if (data.status == "ok"){
        $.alert("取消成功", "提示");
        $('#btn_'+scheduleId).removeClass('weui-btn_warn').addClass('weui-btn_primary');
        $('#btn_'+scheduleId).removeClass('cancel-button').addClass('order-button');
        $('#btn_'+scheduleId).text('预约');
        var count = parseInt($('#residual_'+scheduleId).data('residual-count'));
        count++;
        $('#residual_'+scheduleId).data('residual-count', count);
        $('#residual_'+scheduleId).html('还可预约 '+count+' 人');
    }
}

