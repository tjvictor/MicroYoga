function getTop5Notification(){
    callAjax('/websiteService/getNotificationBriefByCount', '', 'getTop5NotificationCallback', '', '', 'topCount=5', '');
}
function getTop5NotificationCallback(data){
    if(data.status == "ok" && data.callBackData.length > 0){
        $('.yoga-home-notification').html('');
        var template = '';
        for(var i=0; i < data.callBackData.length ; i++){
            var item = data.callBackData[i];

            template += '<div class="weui-panel__ft">';
            template += '   <a href="javascript:void(0);" class="weui-cell weui-cell_access weui-cell_link yoga-open-popup" data-notification-id="'+item.id+'" data-target="#notification-full">';
            template += '       <div class="weui-cell__bd auto-clip">'+item.title+'</div>';
            template += '       <span class="weui-cell__ft"></span>';
            template += '   </a>';
            template += '</div>';

        }

        $('.yoga-home-notification').html(template);
    }
}

function notificationOpenPopup(data){
    if(data.status == "ok"){
        $('#full-popup-container .yoga-popup-container-text').html(data.callBackData.content);
        $('#full-popup-container').popup();
    }
}