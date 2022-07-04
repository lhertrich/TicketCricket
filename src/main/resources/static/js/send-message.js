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
                $("#messageBox").append("<div>" + data.message + "</div>");
            },
            error: function() {
                console.log("error");
            }
        });
    });
});
