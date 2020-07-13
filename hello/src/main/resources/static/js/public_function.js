function pageJump(pageName) {
    window.location.href = "/pageJump?pageName="+pageName;
}

function requestJump(request) {
    window.location.href = request;
}

function register1() {
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    var secondPassword = document.getElementById("secondPassword").value;
    var phone = document.getElementById("phone").value;
    var email = document.getElementById("email").value;
    if(!username){
        alert("请先输入用户名！");
        return;
    }
    if(!password){
        alert("请输入密码！");
        return;

    }if(!secondPassword){
        alert("请输入确认密码！");
        return;
    }else{
        if(password.indexOf(secondPassword)==-1){
            alert('两次输入密码不一致！');
            return;
        }
    }
    var registerInfo = "username,"+username+"|password,"+password+"|phone,"+phone+"|email,"+email;
    updateContent("registerInfo",registerInfo);
    pageJump('register_2');

}

function requestAndParamJump(request) {
    window.location.href = request+"?username=123456";
}

var interestLimited = 10;
//设置最多保存10个兴趣

function postToPage(object) {
    alert("跳转到其它平台");
    var info = object.getAttribute("value").split("-");
    var tid = info[0];
    var url = info[1];
    var username = getContent("username");
    if(!username){
        //保存当前页面的提交数据
        var requestUrl = "/main"+"|username,"+""+"|url,"+url+"|tid,"+tid;
        updateContent("requestUrl",requestUrl);
        username = null;
    }
    var f=document.createElement("form");
    f.action = '/main';
    f.target = '_blank';
    f.method = 'post';
    f.innerHTML='<input type="hidden" name="username" value="'+username+'">'+
        '<input type="hidden" name="url" value="'+url+'">'+
        '<input type="hidden" name="tid" value="'+tid+'">';
    document.body.appendChild(f);
    f.submit();
}

function back() {
    alert("返回");
    var requestUrl = getContent("requestUrl");
    if(requestUrl) {
        var param = requestUrl.split("|");
        var action = param[0];
        alert(action);
        var f = document.createElement("form");
        f.action = param[0];
        var inputStr = "";
        f.target = '_parent';//关键
        f.method = 'post';
        for (var i = 1; i < param.length; ++i) {
            var inputParam = param[i].split(",");
            var inputName = inputParam[0];
            var inputValue = inputParam[1];
            if (inputName.indexOf("username") != -1 && getContent("username")) {
                //说明已被找到
                inputValue = getContent("username");
            }
            inputStr += '<input type="hidden" name="' + inputName + '" value="' + inputValue + '">';
        }
        f.innerHTML = inputStr;
        document.body.appendChild(f);
        f.submit();
    }
}



function test(object) {
    //变色
    var status = object.getAttribute("value");
    var interest = document.getElementById("interestSum");
    var interestSum = parseInt(interest.value);
    if(status == '1'){
        //取消
        if(interestSum -1 < 0) alert("至少选择1个兴趣！");
        else{
            object.setAttribute("class","layui-btn layui-btn-primary layui-btn-radius");
            object.setAttribute("value","0");
            interest.setAttribute("value",interestSum-1);
        }

    }else if(status == '0'){
        //确认
        if(interestSum+1 > interestLimited) alert("至多选择10个兴趣！");
        else{
            object.setAttribute("class","layui-btn layui-btn-radius");
            object.setAttribute("value","1");
            interest.setAttribute("value",interestSum+1);
        }

    }
}

function classNameSearch(object) {
    var className = object.innerText;
    var f=document.createElement("form");
    f.action = '/classNameSearch';
    if(!getContent("username")){
        updateContent("requestUrl",'/classNameSearch'+'|'+'className,'+className);
    }
    f.target = '_blank';
    f.method = 'post';
    f.innerHTML='<input type="hidden" name="className" value="'+className+'">';
    document.body.appendChild(f);
    f.submit();
}

var localStorage = window.localStorage;
//定义全局变量u
//设置缓存
function storeContent(key, value) {
    var v = value;
    //是对象转成JSON，不是直接作为值存入内存
    if (typeof v == 'object') {
        v = JSON.stringify(v);
        v = 'obj-' + v;
    } else {
        v = 'str-' + v;
    }
    var localStorage = window.localStorage;
    if (localStorage ) {
        localStorage .setItem(key, v);
    }
};
//获取缓存
function getContent(key) {
    var localStorage = window.localStorage;
    if (localStorage ){
        var v = localStorage.getItem(key);
        if (!v) {
            return;
        }
        if (v.indexOf('obj-') === 0) {
            v = v.slice(4);
            return JSON.parse(v);
        } else if (v.indexOf('str-') == 0) {
            return v.slice(4);
        }
    }
};

function removeContent() {
    var localStorage = window.localStorage;
    if (localStorage ){
        localStorage.clear();
    }
}

function updateContent(key,value) {
    var localStorage = window.localStorage;
    if (localStorage ){
        //删除
        localStorage.removeItem(key);
        storeContent(key,value);
    }
}

function index() {
    var username = getContent("username");
    if(!username){
        updateContent("requestUrl","/index|username,"+"");
        username = "";
    }
    var f=document.createElement("form");
    f.action = '/index';
    f.target = '_parent';//关键
    f.method = 'post';
    f.innerHTML = '<input type="hidden" name="username" value="'+username+'">';
    document.body.appendChild(f);
    f.submit();
}



//加载浏览器缓存中保存的用户数据
function loadUserInfo(){
    document.getElementById("username").setAttribute("value",getContent("username"));
    var usernameDom = document.getElementById("username");
    var username = usernameDom.value;
    var loginDom = document.getElementById("login-model");
    var registerDom = document.getElementById("register-model");
    var userInfoDom = document.getElementById("userInfo");
    if(username == null || username.length == 0){
        //说明未点击登录按钮
        var webUsername = getContent("username");
        alert("当前浏览器缓存:"+webUsername);
        if(webUsername == null || webUsername.length==0){
            //浏览器缓存中也无用户信息
            userInfoDom.innerHTML = '';
        }else{
            //有用户登录信息
            usernameDom.setAttribute("value",webUsername);
            loginDom.innerHTML = '';
            registerDom.innerHTML = '';
            document.getElementById("userInfo-username").setAttribute("value",webUsername);
        }

    }else{
        //存储用户数据到浏览器缓存中
        storeContent("username",username);
        usernameDom.setAttribute("value",username);
        loginDom.innerHTML = '';
        registerDom.innerHTML = '';
        document.getElementById("userInfo-username").setAttribute("value",username);
    }
}
//进入用户管理
function personPage() {
    if(!getContent("username")){
        alert("请先登录！");
        //不允许进入用户管理页面
        //刷新当前页
        location.reload();
        return;
    }else{
        var f=document.createElement("form");
        f.action = '/person';
        f.target = '_blank';//关键
        f.method = 'post';
        document.body.appendChild(f);
        f.submit();
    }
}


//退出登录
function exit() {
    alert("退出登录");
    //清除用户的登录账号信息
    removeContent("username");
    //返回首页
    index();
    //禁止页面回退
    //window.opener=null;window.open('','_self');window.close();
}


// 以下是验证码
!(function(window, document) {
    function GVerify(options) { //创建一个图形验证码对象，接收options对象为参数
        this.options = { //默认options参数值
            id: "", //容器Id
            canvasId: "verifyCanvas", //canvas的ID
            width: "100", //默认canvas宽度
            height: "30", //默认canvas高度
            type: "blend", //图形验证码默认类型blend:数字字母混合类型、number:纯数字、letter:纯字母
            code: ""
        }

        if(Object.prototype.toString.call(options) == "[object Object]"){//判断传入参数类型
            for(var i in options) { //根据传入的参数，修改默认参数值
                this.options[i] = options[i];
            }
        }else{
            this.options.id = options;
        }

        this.options.numArr = "0,1,2,3,4,5,6,7,8,9".split(",");
        this.options.letterArr = getAllLetter();

        this._init();
        this.refresh();
    }

    GVerify.prototype = {
        /**版本号**/
        version: '1.0.0',

        /**初始化方法**/
        _init: function() {
            var con = document.getElementById(this.options.id);
            var canvas = document.createElement("canvas");
            this.options.width = con.offsetWidth > 0 ? con.offsetWidth : "100";
            this.options.height = con.offsetHeight > 0 ? con.offsetHeight : "30";
            canvas.id = this.options.canvasId;
            canvas.width = this.options.width;
            canvas.height = this.options.height;
            canvas.style.cursor = "pointer";
            canvas.innerHTML = "您的浏览器版本不支持canvas";
            con.appendChild(canvas);
            var parent = this;
            canvas.onclick = function(){
                parent.refresh();
            }
        },

        /**生成验证码**/
        refresh: function() {
            this.options.code = "";
            var canvas = document.getElementById(this.options.canvasId);
            if(canvas.getContext) {
                var ctx = canvas.getContext('2d');
            }else{
                return;
            }

            ctx.textBaseline = "middle";

            ctx.fillStyle = randomColor(180, 240);
            ctx.fillRect(0, 0, this.options.width, this.options.height);

            if(this.options.type == "blend") { //判断验证码类型
                var txtArr = this.options.numArr.concat(this.options.letterArr);
            } else if(this.options.type == "number") {
                var txtArr = this.options.numArr;
            } else {
                var txtArr = this.options.letterArr;
            }

            for(var i = 1; i <= 4; i++) {
                var txt = txtArr[randomNum(0, txtArr.length)];
                this.options.code += txt;
                ctx.font = randomNum(this.options.height/2, this.options.height) + 'px SimHei'; //随机生成字体大小
                ctx.fillStyle = randomColor(50, 160); //随机生成字体颜色
                ctx.shadowOffsetX = randomNum(-3, 3);
                ctx.shadowOffsetY = randomNum(-3, 3);
                ctx.shadowBlur = randomNum(-3, 3);
                ctx.shadowColor = "rgba(0, 0, 0, 0.3)";
                var x = this.options.width / 5 * i;
                var y = this.options.height / 2;
                var deg = randomNum(-30, 30);
                /**设置旋转角度和坐标原点**/
                ctx.translate(x, y);
                ctx.rotate(deg * Math.PI / 180);
                ctx.fillText(txt, 0, 0);
                /**恢复旋转角度和坐标原点**/
                ctx.rotate(-deg * Math.PI / 180);
                ctx.translate(-x, -y);
            }
            /**绘制干扰线**/
            for(var i = 0; i < 4; i++) {
                ctx.strokeStyle = randomColor(40, 180);
                ctx.beginPath();
                ctx.moveTo(randomNum(0, this.options.width), randomNum(0, this.options.height));
                ctx.lineTo(randomNum(0, this.options.width), randomNum(0, this.options.height));
                ctx.stroke();
            }
            /**绘制干扰点**/
            for(var i = 0; i < this.options.width/4; i++) {
                ctx.fillStyle = randomColor(0, 255);
                ctx.beginPath();
                ctx.arc(randomNum(0, this.options.width), randomNum(0, this.options.height), 1, 0, 2 * Math.PI);
                ctx.fill();
            }
        },

        /**验证验证码**/
        validate: function(code){
            var code = code.toLowerCase();
            var v_code = this.options.code.toLowerCase();
            console.log(v_code);
            if(code == v_code){
                return true;
            }else{
                this.refresh();
                return false;
            }
        }
    }
    /**生成字母数组**/
    function getAllLetter() {
        var letterStr = "a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z";
        return letterStr.split(",");
    }
    /**生成一个随机数**/
    function randomNum(min, max) {
        return Math.floor(Math.random() * (max - min) + min);
    }
    /**生成一个随机色**/
    function randomColor(min, max) {
        var r = randomNum(min, max);
        var g = randomNum(min, max);
        var b = randomNum(min, max);
        return "rgb(" + r + "," + g + "," + b + ")";
    }
    window.GVerify = GVerify;
})(window, document);

function login(){
    alert("ok");
    var username = document.getElementById("username").value;
    if(username.length==0){
        alert("请输入用户名！");
        return;
    }
    var password = document.getElementById("password").value;
    if(password.length==0){
        alert("请输入密码！");
        return;
    }
    var res = verifyCode.validate(document.getElementById("code_input").value);
    if(!res){
        //验证码错误
        alert("验证码错误");
        return;
    }
    var f=document.createElement("form");
    alert(username+password);
    f.action = '/login';
    f.target = '_blank';//关键
    f.method = 'post';
    f.innerHTML='<input type="hidden" name="username" value="'+username+'">'+
        '<input type="hidden" name="password" value="'+password+'">';
    document.body.appendChild(f);
    f.submit();
}