$(document).ready(function(){

    $(function () {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content"); $(document).ajaxSend(function (e, xhr, options) {
            xhr.setRequestHeader(header, token); });
    });

    $("#notificationUser").click(function (){
        $("#notificationUser").load("/ajax/set-notificationsRead");
    });

    $("#notificationAdmin").click(function (){
        $("#notificationAdmin").load("/ajax/set-notificationsRead");
    });
});