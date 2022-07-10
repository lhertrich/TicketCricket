var stompClient = null;

$(document).ready(function () {
    var socket = new SockJS('/fallback');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/chat?id=' + ticket.ticketID, function (message) {
            displayMessage(message);
            updateScroll();
        });
        stompClient.subscribe('/topic/status?id=' + ticket.ticketID, function (message) {
            displayStatus(message);
            updateScroll();
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

function displayStatus(websocketMessage) {
    var message = JSON.parse(websocketMessage.body);
    if (message.user.userId == user.userId) {
        $("#messageBox").append(createStatus(message));
    } else {
        $("#messageBox").append(createStatus(message));
    }
}

function sendMessage() {
    var messageData = $("#sendBar").val();
    var dateData = new Date();
    var userData = user;
    var ticketData = ticket;
    var data = {
        "message": messageData,
        "messageType": "CHAT",
        "date": dateData,
        "user": userData,
        "ticket": ticketData
    };
    stompClient.send("/websocket/chat?id=" + ticket.ticketID, {}, JSON.stringify(data));
}

function sendStatusRequest() {
    var messageData = "Status angefragt";
    var dateData = new Date();
    var userData = user;
    var ticketData = ticket;
    var data = {
        "message": messageData,
        "messageType": "STATUS",
        "date": dateData,
        "user": userData,
        "ticket": ticketData
    };
    stompClient.send("/websocket/status?id=" + ticket.ticketID, {}, JSON.stringify(data));
}

$(function() {
    $("#text-input-bar").submit(function (e) {
        e.preventDefault();
        sendMessage();
        $("#sendBar").val("");
    });
    $("#request-status").submit(function(e) {
        e.preventDefault();
        sendStatusRequest();
        $("#request-status-submit").prop("disabled", true);
    })
});

function updateScroll() {
    var element = document.getElementById("messageBox");
    element.scrollTop = element.scrollHeight;
}