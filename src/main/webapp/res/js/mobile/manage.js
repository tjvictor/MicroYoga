//Member start
function memberInit(){
    $("#join").calendar({
        dateFormat: 'yyyy-mm-dd'
    });
    $("#expire").calendar({
        dateFormat: 'yyyy-mm-dd'
    });

    $('.open_member_new_dialog').on('click', function (){
        $('.weui-input').each(function() {
            $(this).val('');
        });
        $('.weui-textarea').val('');
        $('.add_member_btn').css('display', 'block');
        $('#full-popup-container').popup();
    });

    $('.add_member_btn').on('click', function() {
        var name = $.trim($('#name').val());
        var pwd = $.trim($('#password').val());
        var sex = $.trim($('#sex').val());
        var tel = $.trim($('#tel').val());
        var weChat = $.trim($('#weChat').val());
        var join = $.trim($('#join').val());
        var expire = $.trim($('#expire').val());
        var fee = $.trim($('#fee').val());
        var remark = $.trim($('#remark').val());

        if (name === '' || password === '' || tel === '') {
            $.alert("姓名，密码，电话不能为空!", "警告！");
            return;
        }

        var postValue = {
            "name": name,
            "sex": sex,
            "tel": tel,
            "pwd": pwd,
            "weChat": weChat,
            "joinDate": join,
            "expireDate": expire,
            "fee": fee,
            "remark": remark
        };

        callAjax('/websiteService/addMember', '', 'addMemberCallback', '', 'POST', postValue, '');
    });

    callAjax('/websiteService/getMembers', '', 'getMembersCallback', '', '', '', '');
}
function getMembersCallback(data) {
    if (data.status == "ok") {
        $('.memberView').removeData();
        var template = '';
        var members = data.callBackData;
        for (var i = 0; i < members.length; i++) {
            var member = members[i];
            template += generateMember(member);
            $('.memberView').data(member.id, member);
        }

        $('.memberView').html(template);
        $('.weui-cell_swiped').swipeout();
        $('.yoga-open-popup').on('click', function() {
            $('.add_member_btn').css('display', 'none');
            var id = $(this).data('id');
            var data = $('.memberView').data(id);

            $('#name').val(data.name);
            $('#password').val(data.password);
            $('#sex').val(data.sex);
            $('#tel').val(data.tel);
            $('#weChat').val(data.weChat);
            $('#join').val(data.joinDate);
            $('#expire').val(data.expireDate);
            $('#fee').val(data.fee);
            $('#remark').val(data.remark);

            $('#full-popup-container').popup();
        });
    }
}

function addMemberCallback(data) {
    if (data.status == "ok") {
        var member = data.callBackData;
        var template = generateMember(member);
        $('.memberView').data(member.id, member);
        $('.memberView').prepend(template);
        $('.weui-cell_swiped').swipeout();
        $.alert("添加成功", "提示");
        $.closePopup();
        $("#add_swiped").swipeout('close');
    }
}

function generateMember(member) {
    var template = '';
    template += '<div class="weui-cell weui-cell_swiped">';
    template += '   <div data-id="' + member.id + '" class="weui-cell__bd yoga-open-popup">';
    template += '       <div class="weui-cell">';
    template += '           <div class="weui-cell__bd">';
    template += '               <div class="left" style="width:80px;margin-right:10px;">' + member.name + '</div><div class="left" style="width:100px">' + member.tel + '</div><div class="right">' + member.expireDate + ' 到期</div><div class="clear"></div>';
    template += '           </div>';
    template += '       </div>';
    template += '   </div>';
    template += '   <div class="weui-cell__ft">';
    template += '       <a id="' + member.id + '" onclick="deleteMember(this);" class="weui-swiped-btn weui-swiped-btn_warn delete-swipeout" href="javascript:">删除</a>';
    template += '   </div>';
    template += '</div>';
    return template;
}

function deleteMember(obj) {
    var id = $(obj).attr('id');
    $.confirm("您确定要删除吗?", "确认删除?",
    function() {
        callAjax('/websiteService/deleteMember', '', 'deleteMemberCallback', '', '', 'id=' + id, '');
    },
    function() {
        $('.memberView').removeData(id);
        $(obj).parents('.weui-cell').swipeout('close');
    });
}
function deleteMemberCallback(data) {
    if (data.status == "ok") {
        $('#' + data.callBackData).parents('.weui-cell').remove();
    }
}
//Member end

//notification start
function notificationInit(){
    $('.open_notification_new_dialog').on('click', function (){
        $('.weui-input').each(function() {
            $(this).val('');
        });
        $('.weui-textarea').val('');
        $('.add_notification_btn').css('display', 'block');
        $('#full-popup-container').popup();
    });

    $('.add_notification_btn').on('click', function() {
        var title = $.trim($('#title').val());
        var content = $.trim($('#content').val());
        if (title === '' || content === '') {
            $.alert("标题或是内容不能为空!", "警告！");
            return;
        }

        var postValue = {
            "title": title,
            "content": content,
        };

        callAjax('/websiteService/addNotification', '', 'addNotificationCallback', '', 'POST', postValue, '');
    });

    callAjax('/websiteService/getNotifications', '', 'getNotificationsCallback', '', '', '', '');
}
function getNotificationsCallback(data) {
    if (data.status == "ok") {
        $('.notificationView').removeData();
        var template = '';
        var notifications = data.callBackData;
        for (var i = 0; i < notifications.length; i++) {
            var notification = notifications[i];
            template += generateNotification(notification);
            $('.notificationView').data(notification.id, notification);
        }

        $('.notificationView').html(template);
        $('.weui-cell_swiped').swipeout();
        $('.yoga-open-popup').on('click', function() {
            $('.add_notification_btn').css('display', 'none');
            var id = $(this).data('id');
            var data = $('.notificationView').data(id);

            $('#title').val(data.title);
            $('#content').val(data.content);

            $('#full-popup-container').popup();
        });
    }
}

function addNotificationCallback(data) {
    if (data.status == "ok") {
        var notification = data.callBackData;
        var template = generateNotification(notification);
        $('.notificationView').data(notification.id, notification);
        $('.notificationView').prepend(template);
        $('.weui-cell_swiped').swipeout();
        $.alert("添加成功", "提示");
        $.closePopup();
        $("#add_swiped").swipeout('close');
    }
}

function generateNotification(notification) {
    var template = '';
    template += '<div class="weui-cell weui-cell_swiped">';
    template += '   <div data-id="' + notification.id + '" class="weui-cell__bd yoga-open-popup">';
    template += '       <div class="weui-cell">';
    template += '           <div class="weui-cell__bd">';
    template += '               <div class="left auto-clip" style="width:70%;padding-right:10px;">' + notification.title + '</div><div class="right">' + notification.date + '</div><div class="clear"></div>';
    template += '           </div>';
    template += '       </div>';
    template += '   </div>';
    template += '   <div class="weui-cell__ft">';
    template += '       <a id="' + notification.id + '" onclick="deleteNotification(this);" class="weui-swiped-btn weui-swiped-btn_warn delete-swipeout" href="javascript:">删除</a>';
    template += '   </div>';
    template += '</div>';
    return template;
}

function deleteNotification(obj) {
    var id = $(obj).attr('id');
    $.confirm("您确定要删除吗?", "确认删除?",
    function() {
        callAjax('/websiteService/deleteNotification', '', 'deleteNotificationCallback', '', '', 'id=' + id, '');
    },
    function() {
        $('.notificationView').removeData(id);
        $(obj).parents('.weui-cell').swipeout('close');
    });
}
function deleteNotificationCallback(data) {
    if (data.status == "ok") {
        $('#' + data.callBackData).parents('.weui-cell').remove();
    }
}
//notification end

//schedule start
function scheduleInit(){
    $("#start").calendar({
        dateFormat: 'yyyy-mm-dd'
    });
    $("#end").calendar({
        dateFormat: 'yyyy-mm-dd'
    });
    $("#searchDate").calendar({
        dateFormat: 'yyyy-mm-dd',
        onChange: function (p, values, displayValues) {
            var param = "date="+values[0];
            callAjax('/websiteService/getFullScheduleByDate', '', 'getFullScheduleByDateCallback', '', '', param, '');
        }
    });


    $('.open_schedule_new_dialog').on('click', function (){
        $('.weui-input').each(function() {
            $(this).val('');
            $(this).removeData();
        });

        $('.add_schedule_btn').css('display', 'block');
        $('.orderView').css('display', 'none');
        $('#full-popup-container').popup();
    });

    $('.add_schedule_btn').on('click', function() {
        var courseId = $('#course').data('id');
        var teacherId = $('#teacher').data('id');
        var start = $.trim($('#start').val());
        var end = $.trim($('#end').val());
        var capacity = $.trim($('#capacity').val());

        if (courseId === '' || teacherId === '' || start === '' ||
            end === '' || capacity === '') {
            $.alert("所有项均为必填内容!", "警告！");
            return;
        }

        var postValue = {
            "courseId": courseId,
            "teacherId": teacherId,
            "start": start,
            "end": end,
            "capacity": capacity,
        };

        callAjax('/websiteService/addSchedule', '', 'addScheduleCallback', '', 'POST', postValue, '');
    });

    callAjax('/websiteService/getCourses', '', 'getCoursesCallback', '', '', '', '');
    callAjax('/websiteService/getTeachers', '', 'getTeachersCallback', '', '', '', '');
}
function getFullScheduleByDateCallback(data) {
    if (data.status == "ok") {
        $('.scheduleView').removeData();
        var template = '';
        var schedules = data.callBackData;
        for (var i = 0; i < schedules.length; i++) {
            var schedule = schedules[i];
            template += generateSchedule(schedule);
            $('.scheduleView').data(schedule.id, schedule);
        }

        $('.scheduleView').html(template);
        $('.weui-cell_swiped').swipeout();
        $('.yoga-open-popup').on('click', function() {
            $('.add_schedule_btn').css('display', 'none');
            $('.orderView').css('display', 'flex');
            var id = $(this).data('id');
            var data = $('.scheduleView').data(id);

            $('#course').val(data.courseName);
            $('#teacher').val(data.teacherName);
            $('#start').val(data.startTime);
            $('#end').val(data.endTime);
            $('#capacity').val(data.capacity);
            var template = '';
            for(var i = 0 ; i < data.orderList.length ; i++){
                template += '<p>'+data.orderList.memberName+'</p>';
            }
            if(template === ''){
                template = '暂时无人预约';
            }
            $('.orderItem').html(template);

            $('#full-popup-container').popup();
        });
    }
}
function getCoursesCallback(data){
    if (data.status == "ok") {
        var list = [];
        for(var i = 0 ; i < data.callBackData ; i++){
            list.push({title: data.callBackData[i].courseName, value: data.callBackData[i].courseId});
        }

        $('#course').select({
            title: '选择课程',
            items: list
        });
    }
}
function getTeachersCallback(data){
    if (data.status == "ok") {
        var list = [];
        for(var i = 0 ; i < data.callBackData ; i++){
            list.push({title: data.callBackData[i].teacherName, value: data.callBackData[i].teacherId});
        }

        $('#teacher').select({
            title: '选择教练',
            items: list
        });
    }
}


function addScheduleCallback(data) {
    if (data.status == "ok") {
        var schedule = data.callBackData;
        var template = generateSchedule(schedule);
        $('.scheduleView').data(schedule.id, schedule);
        $('.scheduleView').prepend(template);
        $('.weui-cell_swiped').swipeout();
        $.alert("添加成功", "提示");
        $.closePopup();
        $("#add_swiped").swipeout('close');
    }
}

function generateSchedule(schedule) {
    var template = '';
    template += '<div class="weui-cell weui-cell_swiped">';
    template += '   <div data-id="' + schedule.id + '" class="weui-cell__bd yoga-open-popup">';
    template += '       <div class="weui-cell">';
    template += '           <div class="weui-cell__bd">';
    template += '               <div class="left" style="width:120px;margin-right:10px;">' + schedule.startTime + ' - ' + schedule.endTime + '</div><div class="left auto-clip">' + schedule.courseName + '</div><div class="clear"></div>';
    template += '           </div>';
    template += '       </div>';
    template += '   </div>';
    template += '   <div class="weui-cell__ft">';
    template += '       <a id="' + schedule.id + '" onclick="deleteSchedule(this);" class="weui-swiped-btn weui-swiped-btn_warn delete-swipeout" href="javascript:">删除</a>';
    template += '   </div>';
    template += '</div>';
    return template;
}

function deleteSchedule(obj) {
    var id = $(obj).attr('id');
    $.confirm("您确定要删除吗?", "确认删除?",
    function() {
        callAjax('/websiteService/deleteSchedule', '', 'deleteScheduleCallback', '', '', 'id=' + id, '');
    },
    function() {
        $('.scheduleView').removeData(id);
        $(obj).parents('.weui-cell').swipeout('close');
    });
}
function deleteScheduleCallback(data) {
    if (data.status == "ok") {
        $('#' + data.callBackData).parents('.weui-cell').remove();
    }
}
//schedule end