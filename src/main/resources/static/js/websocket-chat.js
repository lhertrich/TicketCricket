var stompClient = null;

$(document).ready(function () {
    var socket = new SockJS('/fallback');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/chat?id=' + ticket.ticketID, function (message) {
            displayMessage(message);
        });
    });
});

function displayMessage(websocketMessage) {
    var message = JSON.parse(websocketMessage.body)
    if (message.user.userId == user.userId) {
        $("#messageBox").append(createMessage(message, true));
    } else {
        $("#messageBox").append(createMessage(message, false));
    }
}

function sendMessage() {
    var messageData = $("#sendBar").val();
    var dateData = new Date();
    var userData = user;
    var ticketData = ticket;
    var data = {
        "message": messageData,
        "date": dateData,
        "user": userData,
        "ticket": ticketData
    };
    stompClient.send("/websocket/chat?id=" + ticket.ticketID, {}, JSON.stringify(data));
}

$(function() {
    $("#text-input-bar").submit(function (e) {
        e.preventDefault();
        sendMessage();
        $("#sendBar").val("");
    });
})
