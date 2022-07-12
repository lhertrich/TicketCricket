var stompClient = null;
var deactivated = false;

function disableChat() {
    $("#sendBar").prop("disabled", true);
    $("#sendBar").val("");
    $("#sendBar").attr("placeholder", "Der Chat ist deaktiviert, da das Ticket erledigt ist.");
    $("#sendButton").prop("disabled", true);
    $("#request-status-submit").prop("disabled", true);
}

function enableChat() {
    $("#sendBar").prop("disabled", false);
    $("#sendBar").attr("placeholder", "Nachricht senden...");
    $("#sendButton").prop("disabled", false);
}

$(document).ready(function () {
    loadMessages();
    if(ticket.status == "ERLEDIGT") {
        deactivated = true;
        disableChat();
    }

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
        stompClient.subscribe('/topic/disabled?id=' + ticket.ticketID, function (message) {
            displayStatus(message);
            disableChat();
            updateScroll();
        });
        stompClient.subscribe('/topic/enabled?id=' + ticket.ticketID, function (message) {
            displayStatus(message);
            enableChat();
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
    var messageData = $("#sendBar").val().trim();
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

function sendDisabledInfo() {
    var messageData = "Chat deaktiviert";
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
    stompClient.send("/websocket/disabled?id=" + ticket.ticketID, {}, JSON.stringify(data));
}

function sendEnabledInfo() {
    var messageData = "Chat aktiviert";
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
    stompClient.send("/websocket/enabled?id=" + ticket.ticketID, {}, JSON.stringify(data));
}

$(function() {
    $("#text-input-bar").submit(function (e) {
        e.preventDefault();
        if($("#sendBar").val().trim() !== "") {
            sendMessage();
        }
        $("#sendBar").val("");
    });
    $("#request-status").submit(function(e) {
        e.preventDefault();
        sendStatusRequest();
        $("#request-status-submit").prop("disabled", true);
    });
    $("#change-status").submit(function() {
        let data = $("#change-status").serializeArray();
        if(data[1].value == "ERLEDIGT" && !deactivated) {
            sendDisabledInfo();
            //currently not needed but maybe in the future
            deactivated = true;
        } else if(deactivated && !(data[1].value == "ERLEDIGT")) {
            sendEnabledInfo();
        }
    });
});



function updateScroll() {
    var element = document.getElementById("messageBox");
    element.scrollTop = element.scrollHeight;
}