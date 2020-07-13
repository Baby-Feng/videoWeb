// 提交搜索表单
function search() {
    var form = document.getElementById("search");
    form.submit()
    if(!getContent("username")){
        var searchInput = document.getElementById("searchInput").value;
        var requestUrl = "/search|search,"+searchInput;
        updateContent("requestUrl",requestUrl);
    }
}

function getResourceListByPageNum(pagenum){
    var content = document.getElementById("content");
    alert(content.getAttribute("th:each"));
    var startIndex = (pagenum-1)*20;
    var endIndex = pagenum * 20;
    var value = "resource:${resourceList.subList("+startIndex+","+endIndex+")}";
    content.setAttribute("th:each",value);
    alert(content.getAttribute("th:each"));
    window.location.replace(location.href)
    var result = resourceList;
}

$("#waitWork").click(function(){
    var url = "请求地址";
    var data = {type:1};
    $.ajax({
        type : "get",
        async : false,  //同步请求
        url : url,
        data : data,
        timeout:1000,
        success:function(dates){
            //alert(dates);
            $("#mainContent").html(dates);//要刷新的div
        },
        error: function() {
            // alert("失败，请稍后再试！");
        }
    });
});

function main(tid) {
    alert(tid)
    var form = document.getElementById(tid);
    form.submit();
}

