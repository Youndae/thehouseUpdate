var sock = new SockJS("/ws-stomp");
var ws = Stomp.over(sock);
var reconnect = 0;

var chatId;
var sender;
var enterTime;
var count;
var msg = $('#message').val();

$(document).ready(function () {

    $('#message').keydown(function (key) {
        if (key.keyCode == 13) {
            sendMsg();
        }
    });

});

function sendMsg() {
    if($('#message').val()!=""){
    ws.send("/pub/message", {}, JSON.stringify({
        type: 'TALK',
        chatId: chatId,
        sender: sender,
        msg: $('#message').val(),
        count: count
    }));
    $('#message').val("");
    }
}

function connect() {
    // pub/sub event
    ws.connect({}, function (frame) {
        ws.send("/pub/message", {}, JSON.stringify({
            type: 'ENTER',
            chatId: chatId,
            sender: sender,
            enterTime: enterTime,
        }));

        ws.subscribe("/sub/chat/" + chatId, function (msg) {
            var receive = JSON.parse(msg.body);

            if (receive.type == 'ENTER') {
                if (sender != receive.sender) {
                    //상담원 입장했을때 고객이 보는 시점
                    $('.chat-content').append("<div class='chat-message-group'><div class='chat-messages'><div class='message'>상담원이 입장하였습니다.</div><div class='message'>안녕하세요? 무엇 궁금하신가요?</div></div></div>");
                }
            } else if (receive.type == 'TALK') {
                if (sender == receive.sender) {
                    $('.chat-content').append("<div class='chat-message-group writer-user'><div class='chat-messages'><div class='message'>" + receive.msg + "</div></div></div>")
                        .stop()
                        .animate({scrollTop: $('.chat-content')[0].scrollHeight}, 1000);
                } else {
                    $(".chat-content").append("<div class='chat-message-group'><div class='chat-messages'><div class='message'>" + receive.msg + "</div></div></div>")
                        .stop()
                        .animate({scrollTop: $('.chat-content')[0].scrollHeight}, 1000);
                }
            } else {
                alert("상대방이 채팅방을 나갔습니다. 채팅이 종료됩니다.")
                ws.disconnect();
                window.close();
                opener.location.reload();
            }
        });

    }, function (error) {
        if (reconnect++ <= 5) {
            setTimeout(function () {
                console.log("connection reconnect");
                sock = new SockJS("/ws-stomp");
                ws = Stomp.over(sock);
                connect();
            }, 10 * 1000);
        }
    });
}

connect();

function disconnect() {
    ws.send("/pub/message", {}, JSON.stringify({
        type: 'LEAVE',
        chatId: chatId,
        sender: sender
    }));

    ws.disconnect();
    window.close();
    opener.location.reload();
}
