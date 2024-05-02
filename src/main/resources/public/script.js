// 获取要操作的元素
let conf = document.getElementById("confCard");
let ping = document.getElementById("pingCard");
let confSpan = document.getElementById("confSpan");
let pingSpan = document.getElementById("pingSpan");
let pointPic = document.getElementById("pointPic");
let frontCard = document.getElementById("frontCard");
let backCard = document.getElementById("backCard");
let loader = document.getElementById("loader");

let localIp = document.getElementById("localIp");
let localMac = document.getElementById("localMac");
let routerMac = document.getElementById("routerMac");
let dstIp = document.getElementById("dstIp");
let routerIp = document.getElementById("routerIp");
let ip =  document.getElementById("pingIp");
let pingTime =  document.getElementById("pingTime");
let pingTTL =  document.getElementById("pingTTL");

//切换卡片监听事件
window.onload = function () {
    confSpan.addEventListener('click', () => {
        ping.style.visibility = 'hidden';
        conf.style.visibility = 'visible';
        pointPic.style.left = '36%';
        pointPic.style.filter = 'none';
        backCard.style.transform = 'rotateY(180deg)';
        frontCard.style.transform = 'none';
    })
    pingSpan.addEventListener('click', () => {
        conf.style.visibility = 'hidden';
        ping.style.visibility = 'visible';
        pointPic.style.left = '61.8%';
        pointPic.style.filter = 'invert(1)';
        frontCard.style.transform = 'rotateY(180deg)';
        backCard.style.transform = 'none';
    })

}
function getRouterMac() {
    var xmlHttp = new XMLHttpRequest();
    loader.style.visibility = 'visible';
    xmlHttp.open("GET", "http://127.0.0.1:8080/getLocalInfo", true);
    xmlHttp.send();
    xmlHttp.onreadystatechange = function () {
        if (xmlHttp.readyState === 4 && xmlHttp.status === 200) {
            var res = JSON.parse(xmlHttp.responseText);
            if (res.code === 102) {
                localIp.innerHTML = "本机IP：" + res.data.localIP;
                localMac.innerHTML = "本机MAC：" + res.data.localMac;
                sendArp();
            } else {
                localIp.innerHTML = "获取失败：" + res.code;
                loader.style.visibility = 'hidden';
            }
        } else {
            localIp.innerHTML = "请求发送失败";
            loader.style.visibility = 'hidden';
        }
    }
}

function sendArp(){
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open("POST","http://127.0.0.1:8080/getRouterMac",true);
    xmlHttp.setRequestHeader("Content-Type", "application/json");
    xmlHttp.send(JSON.stringify({
        dstIp: routerIp.value,
        srcIp: localIp.innerHTML.slice(5),
        srcMac: localMac.innerHTML.slice(6)
    }));
    xmlHttp.onreadystatechange = function (){
        if (xmlHttp.readyState === 4 && xmlHttp.status === 200) {
            var res = JSON.parse(xmlHttp.responseText);
            if (res.code === 101) {
                routerMac.innerHTML ="路由MAC："+res.data;
                loader.style.visibility = 'hidden';
            } else {
                routerMac.innerHTML = "获取失败：" + res.code;
                loader.style.visibility = 'hidden';
            }
        } else {
            routerMac.innerHTML = "请求发送失败";
            loader.style.visibility = 'hidden';
        }
    }
}

function sendIcmp(){
    loader.style.visibility = 'visible';
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open("POST","http://127.0.0.1:8080/pingIp",true);
    xmlHttp.setRequestHeader("Content-Type", "application/json");
    xmlHttp.send(JSON.stringify({
        srcIp: localIp.innerHTML.slice(5),
        srcMac: localMac.innerHTML.slice(6),
        dstIp: dstIp.value,
        dstMac:routerMac.innerHTML.slice(6)
    }));
    xmlHttp.onreadystatechange = function (){
        if (xmlHttp.readyState === 4 && xmlHttp.status === 200) {
            var res = JSON.parse(xmlHttp.responseText);
            if (res.code === 103) {
                ip.innerHTML ="响应ip："+res.data.ip;
                pingTime.innerHTML ="响应时间："+ res.data.pingTime+" ms";
                pingTTL.innerHTML = "TTL："+res.data.ttl;
                loader.style.visibility = 'hidden';
            } else {
                ip.innerHTML = "获取失败：" + res.code;
                loader.style.visibility = 'hidden';
            }
        } else {
            ip.innerHTML = "请求发送失败";
            loader.style.visibility = 'hidden';
        }
    }
}

function check(str){
    var regex = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
    console.log(regex.test(str));
}





