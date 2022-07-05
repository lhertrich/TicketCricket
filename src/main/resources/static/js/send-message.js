$(function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
});

$(document).ready(function () {
    console.log("ready");
    $("#text-input-bar").submit(function (e) {
        console.log("submit");
        e.preventDefault();
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
        $.ajax({
            type: "POST",
            url: "/ticket/send-message?id=" + ticket.ticketID,
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify(data),
            success: function(data) {
                console.log(data);
                $("#sendBar").val("");
                $("#messageBox").append(createMessage(data, true));
            },
            error: function() {
                console.log("error");
            }
        });
    });
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
