$(document).ready(function(){
    $(function () {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content"); $(document).ajaxSend(function (e, xhr, options) {
            xhr.setRequestHeader(header, token); });
    });

    //search
    let searchString;
    let filterString = "";
    let sortString;
    $("#searchBar").keyup(function (){
        searchString = $("#searchBar").val().replace(/\s+/g, '');
        $(":checkbox").each(function(){
            if($(this).is(":checked")){
                filterString += $(this).val();
            }
        });
        sortString = $("#sortAttribute").val();
        console.log("searchstring: "+searchString+"; filterstring: "+filterString+"; sortattribute: "+sortString);
    });

    $("#search").click(function (){
        searchString = $("#searchBar").val().replace(/\s+/g, '');
        $("#bottomOuterBorder").load("/ajax/updateHome?searchString=" + searchString);
    });

    $("#filterResetButton").click(function (){

    });

    $("#filterSubmitButton").click(function (){
        let filterString = "";
        $(":checkbox").each(function(){
            if($(this).is(":checked")){
                filterString += $(this).val();
            }
        });
        $("#bottomOuterBorder").load("/ajax/filter?filterString=" + filterString);
    });

    $("#filterResetButton").click(function (){
        $(":checkbox").each(function (){
            $(this).prop('checked', false);
        });
        $("#bottomOuterBorder").load("/ajax/filter?filterString=");
    });
});