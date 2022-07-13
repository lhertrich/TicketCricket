$(document).ready(function(){
    $(function () {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        $(document).ajaxSend(function (e, xhr, options) {
            xhr.setRequestHeader(header, token); });
    });

    var searchString;
    $("#searchBar").keyup(function (){
        searchString = $("#searchBar").val().toLowerCase().replace(/\s+/g, '');
        $("#bottomOuterBorder").load("/ajax/updateTicketerstellung?searchString=" + searchString);
    });

    $("#search").click(function (){
        searchString = $("#searchBar").val();
        $("#bottomOuterBorder").load("/ajax/updateTicketerstellung?searchString=" + searchString);
    });


});