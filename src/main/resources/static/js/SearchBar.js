$(document).ready(function(){
    $(function () {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        $(document).ajaxSend(function (e, xhr, options) {
            xhr.setRequestHeader(header, token); });
    });

    var searchString;
    $("#searchBar").keyup(function (){
        searchString = $("#searchBar").val().replace(/\s+/g, '-');
        console.log(searchString);
        $("#bottomOuterBorder").load("/ajax/updateHome?searchString=" + searchString);
    });

    $("#search").click(function (){
        searchString = $("#searchBar").val().replace(/\s+/g, '-');
        console.log(searchString);
        $("#bottomOuterBorder").load("/ajax/updateHome?searchString=" + searchString);
    });


});