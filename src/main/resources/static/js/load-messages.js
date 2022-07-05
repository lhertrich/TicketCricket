$(function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
});

$(document).ready(function () {
    console.log("ready to load messages");
    setInterval(function () {
        $.ajax({
            type: "GET",
            url: "/ticket/load-messages?id=" + ticket.ticketID,
            success: function (data) {
                $("#messageBox").empty();
                for (message of data) {
                    if (message.user.userId == user.userId) {
                        $("#messageBox").append(createMessage(message, true));
                    } else {
                        $("#messageBox").append(createMessage(message, false));
                    }
                }
                messages = data;
            }
        })

    }, 100);
});

function createMessage(message, self) {
    var message;
    var date = new Date(message.date);
    if (self) {
        message = "<div class=\"row justify-content-end\">\n" +
            "            <div class=\"col-6 float-right chat-message-self\">\n" +
            "                <div class=\"row\">\n" +
            "                    <div class=\"row\">\n" +
            "                        <div class=\"col\">" + message.message + "</div>\n" +
            "                    </div>\n" +
            "                    <div class=\"row\">\n" +
            "                        <div class=\"col-6 align-self-start text-muted\">\n" +
            "                            "+ message.user.username +"\n" +
            "                        </div>\n" +
            "                        <div class=\"col-6 align-self-end text-muted\" style=\"text-align: end\">\n" +
            "                            "+ date.getHours() + ":" + date.getMinutes() + "\n" +
            "                        </div>\n" +
            "                    </div>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "        </div>";
    } else {
        message = "<div class=\"row justify-content-start\">\n" +
            "            <div class=\"col-6 float-right chat-message-other\">\n" +
            "                <div class=\"row\">\n" +
            "                    <div class=\"row\">\n" +
            "                        <div class=\"col\">" + message.message + "</div>\n" +
            "                    </div>\n" +
            "                    <div class=\"row\">\n" +
            "                        <div class=\"col-6 align-self-start text-muted\">\n" +
            "                            "+ message.user.username +"\n" +
            "                        </div>\n" +
            "                        <div class=\"col-6 align-self-end text-muted\" style=\"text-align: end\">\n" +
            "                            "+ date.getHours() + ":" + date.getMinutes() + "\n" +
            "                        </div>\n" +
            "                    </div>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "        </div>";
    }
    return message;
}
