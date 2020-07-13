function pageJump(url){
    alert(url)
    var data = {type:1};
    $.ajax({
        type : "post",
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
}


function addCollection(object) {
    var username = getContent("username");
    if(!username){
        alert("请先登录！");
        //刷新当前页
        location.reload();
        return;
    }
    var info = object.getAttribute("value").split("-");
    var tid = info[0];
    var url = info[1];
    var f=document.createElement("form");
    f.action = '/addCollection';
    f.target = '_parent';
    f.method = 'post';
    f.innerHTML='<input type="hidden" name="username" value="'+username+'">'+
        '<input type="hidden" name="url" value="'+url+'">'+
        '<input type="hidden" name="tid" value="'+tid+'">';
    document.body.appendChild(f);
    f.submit();
}

