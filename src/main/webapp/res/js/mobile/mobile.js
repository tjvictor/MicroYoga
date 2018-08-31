function callAjax(url, iTarget, iCallBack, iCallBackParam, iPost, iParams, iLoading) {
    callAjax(url, iTarget, iCallBack, iCallBackParam, iPost, iParams, iLoading, true);
};

function callAjax(url, iTarget, iCallBack, iCallBackParam, iPost, iParams, iLoading, async) {
    var aPost = iPost ? 'POST': 'GET';
    var aParams = iParams ? iParams: '';
    var aTarget = iTarget ? '#' + iTarget: iTarget;
    $(iLoading).css('display', 'block');
    $.ajax({
        async: async,
        type: aPost,
        url: url,
        data: aParams,
        contentType: "application/x-www-form-urlencoded; charset=utf-8",
        success: function(data, textStatus, jqXHR) {
            if (aTarget) {
                $(aTarget).html(data);
            }
            if (iCallBack) {
                if (iCallBackParam) {
                    eval(iCallBack)(iCallBackParam, data);
                } else {
                    eval(iCallBack)(data);
                }
            }
        },
        error: function(xhr, textStatus) {
},
        complete: function(data) {
            $(iLoading).css('display', 'none');
        }
    });
}

$('.yoga-login-button').on('click', function(){
    var name = $('.yoga-login-username').val();
    var pwd = $('.yoga-login-password').val();
    if($.trim(name) === ''){
        $.alert("手机不能为空！", "警告！");
    }
    if($.trim(pwd) === ''){
        $.alert("密码不能为空！", "警告！");
    }
    var param = "tel="+name+"&password="+pwd;

    callAjax('/websiteService/login', '', 'loginCallback', '', '', param, '');
});
function loginCallback(data){
    if (data.status == "ok" && data.callBackData){
        if(data.callBackData.outOfDate){
            $.alert("会员已经过期！！！","提示");
            return;
        }
        Cookies.set("meet-yoga-user", data.callBackData, { expires: 1 });
        $.alert("登录成功！！！","提示");
        $('#tabbar .weui-tab__bd-item--content').load('mobileView/home.html');
        close_login();
    }else{
        $.alert("手机号码不存在或是密码不正确！请重新登录", "警告！");
    }
}
function open_login(){
    $('.yoga-login-username').val('');
    $('.yoga-login-password').val('');
    $('#loginBar').addClass('weui-tab__bd-item--active');
    $('#tabbar').removeClass('weui-tab__bd-item--active');
}
function close_login(){
    $('#tabbar').addClass('weui-tab__bd-item--active');
    $('#loginBar').removeClass('weui-tab__bd-item--active');
}

    function checkUser(){
    var user = Cookies.get('meet-yoga-user');
    if(!user){
        open_login();
        return false;
    }
    return $.parseJSON(user);
}

function logout(){
    Cookies.remove("meet-yoga-user");
    $.alert("注销成功！！！","提示");
    checkUser();
}