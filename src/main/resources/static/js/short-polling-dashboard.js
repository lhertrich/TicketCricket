$(function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
});

var tickets = [];
var newTickets = [];

$(document).ready(function getNewTickets() {
    $.ajax({
        type: "GET",
        url: "/ticket/load-tickets",
        success: function (data) {
            tickets = data;
            newTickets = data;
        }
    });

    setInterval(function () {
        $.ajax({
            type: "GET",
            url: "/ticket/load-tickets",
            success: function (data) {
                newTickets = data;
                console.log(_.isEqual(tickets, newTickets) === false);
                if (_.isEqual(tickets, newTickets) === false) {
                    tickets = data;
                    $.ajax({
                        type: "GET",
                        url: "/ajax/reloadDashboard",
                        success: function (data2) {
                            console.log("reloaded");
                            $("#bottomOuterBorder").html(data2);
                        }
                    });
                }
            }
        });
    }, 1000);
});



// function compareTickets(oldTickets, newTickets) {
//     for (newTicket in newTickets) {
//         for (oldTicket in oldTickets) {
//             if (!(_.isEqual(newTicket, oldTicket))) {
//                 return true;
//             }
//         }
//     }
//     return false;
// }
