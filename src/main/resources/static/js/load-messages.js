$(function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
});

function loadMessages() {
    console.log("ready to load messages");
    $.ajax({
        type: "GET",
        url: "/ticket/load-messages?id=" + ticket.ticketID,
        success: function (data) {
            $("#messageBox").empty();
            for (message of data) {
                if (message.messageType === "STATUS") {
                    $("#messageBox").append(createStatus(message));
                } else if(message.messageType === "STATUS_CHANGE") {
                    $("#messageBox").append(createStatusChange(message));
                } else if (message.user.userId == user.userId) {
                    $("#messageBox").append(createMessage(message, true));
                } else {
                    $("#messageBox").append(createMessage(message, false));
                }
            }
        }
    });
}

function createMessage(message, self) {
    var date = new Date(message.date);
    if (self) {
        message = "<div class=\"container\">\n" +
            "    <div id=\"top-2\" class=\"row justify-content-end\">\n" +
            "        <div class=\"col-7 message-box\">\n" +
            "            <div class=\"row\">\n" +
            "                <div id=\"text-wrapper-2\" class=\"col\">\n" +
            "                    <div id=\"username-2\" class=\"row\">\n" +
            "                        <div class=\"col name\">\n" +
            "                            "+message.user.username+"\n" +
            "                        </div>\n" +
            "                    </div>\n" +
            "                    <div id=\"message-2\" class=\"row break-text\">\n" +
            "                        <div class=\"col\">\n" +
            "                            "+message.message+"\n" +
            "                        </div>\n" +
            "                    </div>\n" +
            "                    <div id=\"date-2\" class=\"row text-muted\">\n" +
            "                        <div class=\"col\">\n" +
            "                            "+ date.getHours()+":"+date.getMinutes()+"\n" +
            "                        </div>\n" +
            "                    </div>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</div>";
    } else {
        message = "<div class=\"container\">\n" +
            "    <div id=\"top-3\" class=\"row justify-content-start\">\n" +
            "        <div class=\"col-7 message-box-other\">\n" +
            "            <div class=\"row\">\n" +
            "                <div id=\"text-wrapper-3\" class=\"col\">\n" +
            "                    <div id=\"username\" class=\"row\">\n" +
            "                        <div class=\"col name\">\n" +
            "                            "+message.user.username+"\n" +
            "                        </div>\n" +
            "                    </div>\n" +
            "                    <div id=\"message-3\" class=\"row break-text\">\n" +
            "                        <div class=\"col\">\n" +
            "                            "+message.message+"\n" +
            "                        </div>\n" +
            "                    </div>\n" +
            "                    <div id=\"date-3\" class=\"row text-muted\">\n" +
            "                        <div class=\"col text-align-end\">\n" +
            "                            "+ date.getHours()+":"+date.getMinutes()+"\n" +
            "                        </div>\n" +
            "                    </div>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</div>"
    }
    return message;
}

function createStatus(message) {
    var date = new Date(message.date);
    var formatter = new Intl.DateTimeFormat('de-DE', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
    });
    var dateString = formatter.format(date);

    message = "<div class=\"container\">\n" +
        "            <div class=\"row justify-content-center\">\n" +
        "                <div class=\"col-4 chat-status\">\n" +
        "                    <div class=\"text-center\">" + message.message + " am " + dateString +"</div>\n" +
        "                </div>\n" +
        "            </div>\n" +
        "        </div>";
    return message;
}

function createStatusChange(message) {
    var date = new Date(message.date);
    var formatter = new Intl.DateTimeFormat('de-DE', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
    });
    var dateString = formatter.format(date);

    if(message.message === "IN_BEARBEITUNG") {
        message = "<div class=\"container\">\n" +
            "            <div class=\"row justify-content-center\">\n" +
            "                <div class=\"col-4 chat-status-progress\">\n" +
            "                    <div class=\"text-center\">Hallo " + message.ticket.user.username + "! Dein Ticket ist nun in Bearbeitung. ("+ dateString +")</div>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "        </div>";
    } else if(message.message === "OFFEN") {
        message = "<div class=\"container\">\n" +
            "            <div class=\"row justify-content-center\">\n" +
            "                <div class=\"col-4 chat-status-open\">\n" +
            "                    <div class=\"text-center\">Hallo " + message.ticket.user.username + "! Der Ticketstaus hat sich zu \"Offen\" geändert. (" + dateString +")</div>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "        </div>";
    } else {
        message = "<div class=\"container\">\n" +
            "            <div class=\"row justify-content-center\">\n" +
            "                <div class=\"col-4 chat-status-done\">\n" +
            "                    <div class=\"text-center\">Hallo " + message.ticket.user.username + "! Dein Ticket ist nun geschlossen. ("+ dateString +")</div>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "        </div>";
    }
    return message;
}