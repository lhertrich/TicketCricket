$(document).ready(function(){
    $(function () {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content"); $(document).ajaxSend(function (e, xhr, options) {
            xhr.setRequestHeader(header, token); });
    });

    //Attributes
    let searchString;
    let filterString = "";
    let sortString;

    if(searchString != "" )

    function getDataAndSend(){
        searchString = $("#searchBar").val().replace(/\s+/g, '');
        filterString = "";
        $(":checkbox").each(function(){
            if($(this).is(":checked")){
                filterString += $(this).val();
            }
        });
        if(($("#userSelection").length)){
            filterString = filterString + $("#userSelection").val();
        } else {
            filterString = filterString + "noUser";
        }
        sortString = $("#sortAttribute").val();
        let selectionObject = {
            searchString: searchString,
            filterString: filterString,
            sortString: sortString
        }
        $.ajax({
            type: "POST",
            url: "/ajax/updateDashboard",
            dataType: "text",
            contentType: "application/json",
            data: JSON.stringify(selectionObject),
            success: function (result){
                $("#bottomOuterBorder").html(result);
            }
        })
    }

    //Searchbar
    $("#searchBar").keyup(function (){
        getDataAndSend();
    });

    $("#search").click(function (){
        getDataAndSend();
    });

    //Filterbuttons
    $("#filterSubmitButton").click(function (){
        getDataAndSend();
    });

    $("#filterResetButton").click(function (){
        filterString = "";
        $(":checkbox").each(function (){
            $(this).prop('checked', false);
        });
        if(($("#userSelection").length)){

            $("#userSelection").val('noUser').change();
        }
        getDataAndSend();
    });

    //Sortbuttons
    $("#sortSubmitButton").click(function (){
        getDataAndSend();
    });

    $("#sortResetButton").click(function (){
        sortString = "";
        $("#sortAttribute").val('').change();
        getDataAndSend();
    });
});