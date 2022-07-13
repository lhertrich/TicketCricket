$(document).ready(function(){
    $(function () {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content"); $(document).ajaxSend(function (e, xhr, options) {
            xhr.setRequestHeader(header, token); });
    });



});

$(function (){
    $(function () {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content"); $(document).ajaxSend(function (e, xhr, options) {
            xhr.setRequestHeader(header, token); });
    });

    const eventSource = new EventSource("http://localhost:8080/sse/emitter");

    eventSource.onopen = function () {
        console.log('connection is established');
    };

    eventSource.onmessage = function (event){
        let notification = JSON.parse(event.data);
        console.log(notification);
        $("#notificationAdmin").load("/ajax/load-notifications");
        $("#notificationUser").load("/ajax/load-notifications");
    }
});