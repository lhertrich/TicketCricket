$(function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content"); $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token); });
});

$(document).ready(function(){
    var searchString;
    $("#searchBar").keyup(function (){
        searchString = $("#searchBar").val();
        console.log(searchString);
        $("#bottomOuterBorder").load("/ajax/updateHome?searchString=" + searchString);
    });
});