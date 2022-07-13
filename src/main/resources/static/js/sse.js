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

    const eventSource = new EventSource("http://localhost:8080/sse/notification-emitter");

    eventSource.onopen = function () {
        console.log('connection is established');
    };

    eventSource.onmessage = function (event){
        let notification = JSON.parse(event.data);
        console.log(notification);
        $("#notification-div-admin").load("/ajax/load-notifications");
        $("#notification-div-user").load("/ajax/load-notifications");
    }

});