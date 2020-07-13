

$("#personInfo").click(function(){
    alert("ok")
    var url = "/personInfo?username=123456";
    var data = {type:1};
    $.ajax({
        type : "get",
        async : false,  //同步请求
        url : url,
        data : data,
        timeout:1000,
        success:function(dates){
            //alert(dates);
            $("#main").html(dates);//要刷新的div
        },
        error: function() {
            // alert("失败，请稍后再试！");
        }
    });
});
function loadPerson() {
    var username = getContent("username");
    if(!username){
        index();
        alert("请先登录！");
        //关闭当前页面
    }
}

function person(request){
    var username = getContent("username");
    if(!username){
        index();
        alert("请先登录！");
        //关闭当前页面
    }else {
        var url = request + "?username="+username;
        var data = {type: 1};
        $.ajax({
            type: "post",
            async: false,  //同步请求
            url: url,
            data: data,
            timeout: 1000,
            success: function (dates) {
                //alert(dates);
                $("#main").html(dates);//要刷新的div
            },
            error: function () {
                // alert("失败，请稍后再试！");
            }
        });
    }
}

function submitRequest() {
    var username = getContent("username");
    if(!username){
        index();
        alert("请先登录！");
        //关闭当前页面
    }
    var username = document.getElementById("username").value;
    var name = document.getElementById("name").value;
    var phone = document.getElementById("phone").value;
    var email = document.getElementById("email").value;
    var f=document.createElement("form");
    f.action = '/updatePersonInfo';
    f.target = '_parent';//关键
    f.method = 'post';
    f.innerHTML='<input type="hidden" name="username" value="'+username+'">'+
        '<input type="hidden" name="name" value="'+name+'">'+
        '<input type="hidden" name="email" value="'+email+'">'+
        '<input type="hidden" name="phone" value="'+phone+'">';
    document.body.appendChild(f);
    f.submit();


}

function updatePassword() {
    var username = getContent("username");
    if(!username){
        index();
        alert("请先登录！");
        //关闭当前页面
    }
    var username = document.getElementById("username").value;
    var password = document.getElementById("prePassword").value;
    if(password.length == 0){
        alert("请先输入旧密码！");
        return;
    }
    var newPassword = document.getElementById("newPassword").value;
    if(newPassword.length == 0){
        alert("请输入新密码！");
        return;
    }else{
        if(newPassword==password){
            alert("新密码不能与旧密码相同！");
            return;
        }
    }
    var secondNewPassword = document.getElementById("secondNewPassword").value;
    if(secondNewPassword.length ==0){
        alert("请再次输入新密码！");
        return;
    }else{
        if(newPassword!=secondNewPassword){
            alert("输入新密码不一致！");
            return;
        }
    }
    var f=document.createElement("form");
    alert(username+password+newPassword+secondNewPassword);
    f.action = '/updatePassword';
    f.target = '_parent';//关键
    f.method = 'post';
    f.innerHTML='<input type="hidden" name="username" value="'+username+'">'+
        '<input type="hidden" name="prePassword" value="'+password+'">'+
        '<input type="hidden" name="newPassword" value="'+newPassword+'">';
    document.body.appendChild(f);
    f.submit();
}


