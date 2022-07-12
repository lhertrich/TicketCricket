
    function loadDraft() {
    var selectedDraft = $("#selectDraft").val();
    $("#sendBar").val(selectedDraft);
}
    function removeDraft() {
        var inputString = $("#selectDraft").val();
        let inputObject = {
            message: inputString
        }
        $.ajax({
            type: "POST",
            url: "/ticket/remove-draft?id="+ ticket.ticketID,
            dataType: 'text',
            contentType: 'application/json',
            data: JSON.stringify(inputObject),
            success: function (data) {
                $("#dropdown-parent-outer").html(data);
            }
        });
    }
    function addDraft(){
    var inputString = $("#sendBar").val();
    let inputObject = {
        message: inputString
    }
    $.ajax({
        type: "POST",
        url: "/ticket/add-draft?id="+ ticket.ticketID,
        dataType: 'text',
        contentType: 'application/json',
        data: JSON.stringify(inputObject),
        success: function (data) {
           $("#dropdown-parent-outer").html(data);
           console.log("success");
        }
    });
}
