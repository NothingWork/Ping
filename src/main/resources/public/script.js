// 获取要操作的元素
let conf = document.getElementById("confCard");
let ping = document.getElementById("pingCard");
let confSpan = document.getElementById("confSpan");
let pingSpan = document.getElementById("pingSpan");
let pointPic = document.getElementById("pointPic");
let frontCard = document.getElementById("frontCard");
let backCard = document.getElementById("backCard");
let loader = document.getElementById("loader");
let tips = document.getElementById("tips");
let plane = document.getElementById("plane");

let localIp = document.getElementById("localIp");
let localMac = document.getElementById("localMac");
let routerMac = document.getElementById("routerMac");
let dstIp = document.getElementById("dstIp");
let routerIp = document.getElementById("routerIp");
let ip = document.getElementById("pingIp");
let pingTime = document.getElementById("pingTime");
let pingTTL = document.getElementById("pingTTL");

//纸飞机
//偏转角度，x偏移，y偏移。x方向加速度，y方向加速度，控制加速度上限
let deg=0,ex=0,ey=0,vx=0,vy=0,count=0;
window.addEventListener('mousemove',(e)=>{
    //监听鼠标相对于纸飞机位置的偏移位置和角度
    ex=e.x-plane.offsetLeft-plane.clientWidth/2;
    ey=e.y-plane.offsetTop-plane.clientHeight/2;
    deg=360*Math.atan(ey/ex)/(2*Math.PI)+45;
    //鼠标相对于纸飞机反方向移动则偏转180度
    if(ex<0){
        deg+=180;
    }
    //加速度上限控制器归零
    count=0;
})
//绘制纸飞机
function draw(){
    //设定纸飞机的坐标及其偏转角度
    plane.style.transform='rotate('+deg+'deg)';
    if(count<100){
        //鼠标移动越快，纸飞机加速度越大，100ms后加速度到上限
        vx+=ex/100;
        vy+=ey/100
    }
    plane.style.left=vx+'px';
    plane.style.top=vy+'px';
    count++
}
//每毫秒调用一次绘制
setInterval(draw, 1);

//切换卡片监听事件
window.onload = function () {
    confSpan.addEventListener('click', () => {
        //阴影效果
        conf.style.boxShadow = "inset -600px -400px 0 0 #deabd4";
        ping.style.boxShadow = "none";
        //卡片可见性
        ping.style.visibility = 'hidden';
        conf.style.visibility = 'visible';
        //指针移动
        pointPic.style.left = '36%';
        pointPic.style.filter = 'none';
        //信息卡片翻转
        backCard.style.transform = 'rotateY(180deg)';
        frontCard.style.transform = 'none';
    })
    //效果同上
    pingSpan.addEventListener('click', () => {
        ping.style.boxShadow = "inset 600px -400px 0 0 #ab7fe1";
        conf.style.boxShadow = "none";
        conf.style.backgroundColor = "#ab7fe1"

        conf.style.visibility = 'hidden';
        ping.style.visibility = 'visible';

        pointPic.style.left = '61.8%';
        pointPic.style.filter = 'invert(96%) sepia(69%) saturate(7259%) hue-rotate(204deg) brightness(94%) contrast(84%)';

        frontCard.style.transform = 'rotateY(180deg)';
        backCard.style.transform = 'none';
    })

}

//获取本机信息并调用获取路由MAC方法
function getRouterMac() {
    if (checkIp(routerIp.value)) {
        //Loading...
        loader.style.visibility = 'visible';
        //校验通过，发送请求，接受结果
        const xmlHttp = sendRequest("GET", "http://127.0.0.1:8080/getLocalInfo", "");
        xmlHttp.onreadystatechange = function () {
            if (xmlHttp.readyState === 4 && xmlHttp.status === 200) {
                //请求发送成功
                const res = JSON.parse(xmlHttp.responseText);
                //处理结果
                if (res.code === 102) {
                    //获取成功
                    localIp.innerHTML = "本机IP：" + res.data.localIP;
                    localMac.innerHTML = "本机MAC：" + res.data.localMac;
                    //开始获取路由MAC
                    sendArp();
                } else {
                    //获取失败
                    clearCard(frontCard);
                    showAlert(res.code + "本机信息获取失败！")
                }
            }
        }
    }
}

//获取路由MAC
function sendArp() {
    //校验通过，发送请求，接受结果
    var xmlHttp = sendRequest("POST", "http://127.0.0.1:8080/getRouterMac", JSON.stringify({
        dstIp: routerIp.value,
        srcIp: localIp.innerHTML.slice(5),
        srcMac: localMac.innerHTML.slice(6)
    }));
    xmlHttp.onreadystatechange = function () {
        if (xmlHttp.readyState === 4 && xmlHttp.status === 200) {
            //请求发送成功
            var res = JSON.parse(xmlHttp.responseText);
            //处理结果
            if (res.code === 101) {
                //获取成功
                routerMac.innerHTML = "路由MAC："+res.data;
                showAlert("获取成功！")
            } else {
                //获取失败
                showAlert(res.code + "请确认路由ip填写无误！")
                clearCard(frontCard)
            }
            //Loaded!
            loader.style.visibility = 'hidden';
        }
    }

}

//PING
function sendIcmp() {
    if (pingCheck()) {
        //Loading...
        loader.style.visibility = 'visible';
        //校验通过，发送请求，接受结果
        var xmlHttp = sendRequest("POST", "http://127.0.0.1:8080/pingIp", JSON.stringify({
            srcIp: localIp.innerHTML.slice(5),
            srcMac: localMac.innerHTML.slice(6),
            dstIp: dstIp.value,
            dstMac: routerMac.innerHTML.slice(6)
        }));
        xmlHttp.onreadystatechange = function () {
            if (xmlHttp.readyState === 4 && xmlHttp.status === 200) {
                //请求发送成功
                var res = JSON.parse(xmlHttp.responseText);
                //处理结果
                switch (res.code) {
                    //失败
                    case 402:
                        showAlert(res.code + " 请确认目标填写无误！");
                        clearCard(backCard);
                        break;
                    //超时未响应
                    case 203:
                        showAlert("超时未响应！更换目标或稍后重试");
                        clearCard(backCard);
                        break;
                    //操作成功
                    case 103:
                        ip.innerHTML = "响应ip：" + res.data.ip;
                        pingTime.innerHTML = "响应时间：" + res.data.pingTime + " ms";
                        pingTTL.innerHTML = "TTL：" + res.data.ttl;
                        showAlert("成功收到响应！")
                        break;
                }
                //Loaded!
                loader.style.visibility = 'hidden';
            }
        }

    }
}

//检查ip地址格式
function checkIp(str) {
    //ip地址正则表达式，检验输入是否正确
    var regex = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
    if (!regex.test(str)) {
        //校验未通过,展示提示弹窗,3s后关闭
        showAlert("请检查地址信息是否正确填写！")
        return false;
    } else return true;
}

//在ping操作之前检查是否成功获取路由MAC
function pingCheck() {
    //未获取路由，展示提示弹窗
    if (routerMac.innerHTML === "") {
        showAlert("请先正确完成获取路由MAC操作！")
        return false
    } else return true;
}

//与后端交互,传入地址,类型,数据,返回从后端获取的结果
function sendRequest(type, url, data) {
    const xmlHttp = new XMLHttpRequest();
    xmlHttp.open(type, url, true);
    xmlHttp.setRequestHeader("Content-Type", "application/json");
    xmlHttp.send(data);
    return xmlHttp
}

//展示提示弹窗
function showAlert(text) {
    tips.innerHTML = text
    tips.style.visibility = 'visible'
    //3s后关闭
    setTimeout(function () {
        tips.style.visibility = 'hidden';
    }, 3000)
}

//清除信息卡片内容
function clearCard(card){
        for (let i = 0; i < card.children.length; i++) {
            frontCard.children[i].innerHTML ="";
        }
}





