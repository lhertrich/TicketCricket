$(document).ready(function(){
    $("#notificationUser").click(function (){
        $("#notificationUser").load("/ajax/set-notificationsRead");
    });

    $("#notificationAdmin").click(function (){
        $("#notificationAdmin").load("/ajax/set-notificationsRead");
    });
});