$(document).ready(function(){
    $(function () {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content"); $(document).ajaxSend(function (e, xhr, options) {
            xhr.setRequestHeader(header, token); });
    });
});

$(function (){

    const ticketEventSource = new EventSource("http://localhost:8080/sse/ticket-emitter");

    ticketEventSource.onopen = function () {
        console.log('connection is established');
    };

    ticketEventSource.onmessage = function () {
        $("#bottomOuterBorder").load("/sse/reload-dashboard");
    }

});