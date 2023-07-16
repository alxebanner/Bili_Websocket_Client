$(function () {

$("#setup_table td").change(function(){
    var danmuFont = $("#danmuFont").val();
    var danmuColor = $("#danmuColor").val();
    var userNameFont = $("#userNameFont").val();
    var userNameColor = $("#userNameColor").val();
    var face_backgroundUrl = $("#face_backgroundUrl").val();
    var username_backgroundUrl = $("#username_backgroundUrl").val();
    var commonDanmu_backgroundUrl = $("#commonDanmu_backgroundUrl").val();
    var fleetDanmu_backgroundUrl = $("#fleetDanmu_backgroundUrl").val();
    var pendant_backgroundUrl = $("#pendant_backgroundUrl").val();

    var face_Top_backgroundUrl = $("#face_Top_backgroundUrl").val();
    var face_top_Width = $("#face_top_Width").val();
    var face_top_Height = $("#face_top_Height").val();
    var face_top_top = $("#face_top_top").val();
    var face_top_left = $("#face_top_left").val();
    var face_common_Width = $("#face_common_Width").val();
    var face_common_Height = $("#face_common_Height").val();
    var face_common_top = $("#face_common_top").val();
    var face_common_left = $("#face_common_left").val();
    var user_width = $("#user_width").val();
    var user_height = $("#user_height").val();
    var user_border_radius = $("#user_border_radius").val();

    var user_top = $("#user_top").val();
    var user_left = $("#user_left").val();


     <!--设置用户头像框->
                $(".userFace").css({
                "width": user_width + "px",
                "height": user_height + "px",
                "margin-top": user_top + "px",
                "margin-left": user_left + "px",
                "border-radius": user_border_radius + "px",
                });





                <!--设置普通用户头像框->
                $(".face_common").attr("src", face_backgroundUrl);

                $(".face_common").css({
                "width": face_common_Width + "px",
                "height": face_common_Height + "px",
                "top": face_common_top + "px",
                "left": face_common_left + "px",
                });

                <!--设置舰长用户头像框->
                $(".face_top").attr("src", face_Top_backgroundUrl);

                $(".face_top").css({
                "width": face_top_Width + "px",
                "height": face_top_Height + "px",
                "top": face_top_top + "px",
                "left": face_top_left + "px",
                });

                <!--            设置用户名文字 大小     背景图片-->
                $(".danmu_name_border").css({
                "font-size": userNameFont + "px", "color": userNameColor, "background-image": "url(" +  username_backgroundUrl  +")",
                });

                $(".danmu_wenzi>span").css({
                "font-size": danmuFont + "px", "color": danmuColor
                });
});

$("#userNameColor").change(function(){
    $("#userNameCurrentColor").css({
        "background": $("#userNameColor").val()
    });
});

$("#danmuColor").change(function(){
    $("#danmuCurrentColor").css({
        "background": $("#danmuColor").val()
    });
});

})

//页面加载后执行
window.onload = function () {
    // 获取基础配置
    getSet();

    // 设置弹幕网站
    let strUrl = $(location).attr('href') + 'danmu.html';
    $("#url").val(strUrl);
    // 设置除首页外 不可见
//    $("#myContent").children().hide();
//    $("#index").show();
}

function getSet() {
$.ajax({
            url: '../getSet',
            data: {
            },
            async: false,
            cache: false,
            type: 'GET',
            dataType: 'json',
            success: function (data) {
                var roomid = data.data.roomid
                var user_top = data.data.user_top;
                var user_left = data.data.user_left;
                $("#onoffswitch1").prop("checked",isAnchorLot);
                $("#onoffswitch2").prop("checked",isSilverGift);
                $("#onoffswitch3").prop("checked",isSystemEmoji);
                $("#onoffswitch4").prop("checked",isEnterMessage);
                $("#onoffswitch5").prop("checked",isAttentionMessage);
                $("#room").val(roomid)
                if(roomid === '' || roomid === 0 ) {
                    console.log('房间号为空');
                } else {
                    $("#set").click();
                }
                var isAnchorLot = data.data.isAnchorLot;
                var isSilverGift = data.data.isSilverGift;
                var isSystemEmoji = data.data.isSystemEmoji;

                var isEnterMessage = data.data.isEnterMessage;
                var isAttentionMessage = data.data.isAttentionMessage;
                var isShareMessage = data.data.isShareMessage;
                var isLikeMessage = data.data.isLikeMessage;
                var isLikeNumMessage = data.data.isLikeNumMessage;
                var minGoldPrice = data.data.minGoldPrice;
                var danmuHeight = data.data.danmuHeight;
                var danmuFont = data.data.danmuFont;
                var danmuColor = data.data.danmuColor;
                var userNameFont = data.data.userNameFont;
                var userNameColor = data.data.userNameColor;
                var face_backgroundUrl = data.data.face_backgroundUrl;
                var username_backgroundUrl = data.data.username_backgroundUrl;
                var commonDanmu_backgroundUrl = data.data.commonDanmu_backgroundUrl;
                var fleetDanmu_backgroundUrl = data.data.fleetDanmu_backgroundUrl;
                var pendant_backgroundUrl = data.data.pendant_backgroundUrl;
                var face_Top_backgroundUrl = data.data.face_Top_backgroundUrl;
                var face_top_Width = data.data.face_top_Width;
                var face_top_Height = data.data.face_top_Height;
                var face_top_top = data.data.face_top_top;
                var face_top_left = data.data.face_top_left;
                var face_common_Width = data.data.face_common_Width;
                var face_common_Height = data.data.face_common_Height;
                var face_common_top = data.data.face_common_top;
                var face_common_left = data.data.face_common_left;
                var user_width = data.data.user_width;
                var user_height = data.data.user_height;
                var user_border_radius = data.data.user_border_radius;
                $("#onoffswitch6").prop("checked",isShareMessage);
                $("#onoffswitch7").prop("checked",isLikeMessage);
                $("#onoffswitch8").prop("checked",isLikeNumMessage);
                $("#minGoldPrice").val(minGoldPrice);
                $("#danmuHeight").val(danmuHeight);
                $("#danmuFont").val(danmuFont);
                $("#danmuColor").val(danmuColor);

                $("#danmuCurrentColor").css({
                    "background": $("#danmuColor").val()
                });

                $("#userNameFont").val(userNameFont);
                $("#userNameColor").val(userNameColor);

                $("#userNameCurrentColor").css({
                    "background": $("#userNameColor").val()
                });

                $("#face_backgroundUrl").val(face_backgroundUrl);

                $("#username_backgroundUrl").val(username_backgroundUrl);
                $("#commonDanmu_backgroundUrl").val(commonDanmu_backgroundUrl);
                $("#fleetDanmu_backgroundUrl").val(fleetDanmu_backgroundUrl);
                $("#pendant_backgroundUrl").val(pendant_backgroundUrl);
                $("#face_Top_backgroundUrl").val(face_Top_backgroundUrl);
                $("#face_top_Width").val(face_top_Width);
                $("#face_top_Height").val(face_top_Height);
                $("#face_top_top").val(face_top_top);
                $("#face_top_left").val(face_top_left);
                $("#face_common_Width").val(face_common_Width);
                $("#face_common_Height").val(face_common_Height);
                $("#face_common_top").val(face_common_top);
                $("#face_common_left").val(face_common_left);
                $("#user_width").val(user_width);
                $("#user_height").val(user_height);
                $("#user_border_radius").val(user_border_radius);
                $("#user_top").val(user_top);
                $("#user_left").val(user_left);
                setDemo(data);
             }
        })
}



//$(document).on('click', '#test', function () {
//    console.log("点击按钮");
//});

// 连接房间
$(document).on('click', '#set', function () {
    $.ajax({
                url: '../connectRoom',
                data: {
                roomId:  $("#room").val()
                },
                async: false,
                cache: false,
                type: 'POST',
                dataType: 'json',
                success: function (data) {
                       $.ajax({
                       url: '../getUserInfo',
                       data: {
                       roomId:  $("#room").val()
                       },
                async: false,
                cache: false,
                type: 'GET',
                dataType: 'json',
                success: function (data) {
                    setUserInfo(data.data);
                 }
            })
            }
        });
});

$(document).on('click', '#active', function () {
    $("#myContent").children().hide();
    $("#index").show();
});

$(document).on('click', '#debug', function () {
    $("#myContent").children().hide();
    $("#danmuDebug").show();
});


$(document).on('click', '#enter', function () {
    var newWeb = window.open('_blank');
    newWeb.location= $("#url").val();

});



$("#set").click(function(){
console.log("点击按钮")
var str1 = $('input').val();
            $.ajax({
            url: '../connectRoom',
            data: {
            roomId:  $("#room").val()
            },
            async: false,
            cache: false,
            type: 'POST',
            dataType: 'json',
            success: function (data) {
                   $.ajax({
                   url: '../getUserInfo',
                   data: {
                   roomId:  $("#room").val()
                   },
            async: false,
            cache: false,
            type: 'GET',
            dataType: 'json',
            success: function (data) {
                setUserInfo(data.data);
             }
        })
        }
    });
});



function setUserInfo(data) {
    $("#UserInfo").empty()
    var uid = data.uid;
    var roomId = data.roomId;
    var div =
    ' <div class="row">' +
            '<div class="touxiang">' +
<!--          '  <img src="https://i1.hdslb.com/bfs/face/8ed6da664167ca07c1660f77f10f938c39c4a8bb.jpg">' +-->
                ' <a href=" https://space.bilibili.com/' + uid +'" target="_blank">' +
<!--                https://live.bilibili.com/3961583-->
                    '  <img src="' + data.face + '   ">' +
                ' </a>' +
        '</div>' +
        '<div class="row1">' +
         '   <div class="row2">' +
           '      主播名称 '+  data.name     +

           ' </div>' +
            '<div class="row2">' +
             '   开播状态 ' + data.liveStatus +
            '</div>' +
            '<div class="row2">' +
             '   直播标题 ' + data.title +
            '</div>' +
        '</div>' +

        '<div class="fengmian">' +
<!--         '   <img src="http://i0.hdslb.com/bfs/live/user_cover/2b2e2c8d72aa785d6b53e6885be097c9619ac1f1.jpg">' +-->
               ' <a href=" https://live.bilibili.com/' + roomId +'" target="_blank">' +
                    '  <img src="' + data.cover + '   ">' +
                ' </a>' +
         '</div>'+
           '</div>'
    $("#UserInfo").append(div);

<!--userFace-->
    $(".userFace").attr("src", data.face);
}

function setDemo(data) {
                var minGoldPrice = data.data.minGoldPrice;
                var danmuHeight = data.data.danmuHeight;
                var danmuFont = data.data.danmuFont;
                var danmuColor = data.data.danmuColor;
                var userNameFont = data.data.userNameFont;
                var userNameColor = data.data.userNameColor;
                var face_backgroundUrl = data.data.face_backgroundUrl;
                var username_backgroundUrl = data.data.username_backgroundUrl;
                var commonDanmu_backgroundUrl = data.data.commonDanmu_backgroundUrl;
                var fleetDanmu_backgroundUrl = data.data.fleetDanmu_backgroundUrl;
                var pendant_backgroundUrl = data.data.pendant_backgroundUrl;
                var face_Top_backgroundUrl = data.data.face_Top_backgroundUrl;
                var face_top_Width = data.data.face_top_Width;
                var face_top_Height = data.data.face_top_Height;
                var face_top_top = data.data.face_top_top;
                var face_top_left = data.data.face_top_left;
                var face_common_Width = data.data.face_common_Width;
                var face_common_Height = data.data.face_common_Height;
                var face_common_top = data.data.face_common_top;
                var face_common_left = data.data.face_common_left;
                var user_width = data.data.user_width;
                var user_height = data.data.user_height;
                var user_border_radius = data.data.user_border_radius;
                var user_top = data.data.user_top;
                var user_left = data.data.user_left;

                 <!--设置用户头像框->
                $(".userFace").css({
                "width": user_width + "px",
                "height": user_height + "px",
                "margin-top": user_top + "px",
                "margin-left": user_left + "px",
                "border-radius": user_border_radius + "px",
                });

                <!--设置普通用户头像框->
                $(".face_common").attr("src", face_backgroundUrl);

                $(".face_common").css({
                "width": face_common_Width + "px",
                "height": face_common_Height + "px",
                "top": face_common_top + "px",
                "left": face_common_left + "px",
                });

                <!--设置舰长用户头像框->
                $(".face_top").attr("src", face_Top_backgroundUrl);

                $(".face_top").css({
                "width": face_top_Width + "px",
                "height": face_top_Height + "px",
                "top": face_top_top + "px",
                "left": face_top_left + "px",
                });

                <!--            设置用户名文字 大小     背景图片-->
                $(".danmu_name_border").css({
                "font-size": userNameFont + "px", "color": userNameColor, "background-image": "url(" +  username_backgroundUrl  +")",
                });

//                $(".danmu_wenzi>span").css({
//                "font-size": danmuFont + "px", "color": danmuColor
//                });

                $(".danmuContent").css({
                                "font-size": danmuFont + "px", "color": danmuColor
                });
}


