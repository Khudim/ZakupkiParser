$(document).ready(function () {

    // Activated the table
    $('#tableClient').DataTable({
        processing: true,
        serverSide: true,
        order: [[2, "desc"]],
        ajax: {
            url: "/getAllDocuments",
            type: "POST"
        },
        columns: [
            {
                data: "content", render: function (data, type) {
                if (type === "display") {
                    return $('<div class="ui button myBtn" data-text="32">Show modal</div>')
                        .wrap("<div></div>")
                        .parent()
                        .html()
                } else {
                    return data;
                }
            }
            },
            {data: "price", render: $.fn.dataTable.render.number(',', '.', 0, '\u20bd ')},
            {
                data: "creationDate", render: function (data) {
                return new Date(data).toUTCString();
            }
            },
            {data: "region"},
            {
                data: "url", render: function (data, type) {
                if (type === 'display') {
                    return $('<a>')
                        .attr('href', data)
                        .text(data)
                        .wrap('<div></div>')
                        .parent()
                        .html();

                } else {
                    return data;
                }
            }
            }

        ]
    });
    /*
     $(document).ready(function(){
     $("#myBtn").click(function(){
     document.getElementById("tableModal").innerHTML = $(this).attr('data-text');
     $("#myModal").modal();
     });
     });*/
    $('#tableResult').DataTable({
        processing: true,
        serverSide: true,
        order: [[2, "desc"]],
        ajax: {
            url: "/getAllNotificationDocuments",
            type: "POST"
        },
        columns: [
            {data: "price", render: $.fn.dataTable.render.number(',', '.', 0, '\u20bd ')},
            {
                data: "creationDate", render: function (data) {
                return new Date(data).toUTCString();
            }
            },
            {data: "region"},
            {
                data: "url", render: function (data, type) {
                if (type === 'display') {
                    return $('<a>')
                        .attr('href', data)
                        .text(data)
                        .wrap('<div></div>')
                        .parent()
                        .html();

                } else {
                    return data;
                }
            }
            }

        ]
    });

    function filterColumn(i) {
        $('#tableClient').DataTable().column(i).search(
            $('#col' + i + '_filter').val()
        ).draw();
    }

    $(document).ready(function () {
        $('#tableClient').DataTable();
        $('input.column_filter').on('keyup click', function () {
            filterColumn($(this).parents('div').attr('data-column'));
        });
    });

    // $(document).ready(function () {
    /*      $('#myBtn').click(function () {
     document.getElementById("tableModal").innerHTML = $(this).attr('data-text');
     $('.ui.modal').modal('show');
     });*/
    //   });

    /*
     $(document).ready(function(){
     $('[data-toggle="popover"]').popover();
     });
     */

    /*    $('body').on('click', function (e) {
     $('[data-toggle="popover"]').each(function () {
     //the 'is' for buttons that trigger popups
     //the 'has' for icons within a button that triggers a popup
     if (!$(this).is(e.target) && $(this).has(e.target).length === 0 && $('.popover').has(e.target).length === 0) {
     $(this).popover('hide');
     }
     });
     });*/
});

/*
 {
 data: "url", render: function (data, type) {
 if (type === "display") {
 return $('<a href="#" data-toggle="popover" title="Popover Header" data-content="Some content inside the popover">')
 // .attr('href', data)
 .text(data)
 .wrap('<div class="container"></div>')
 .parent()
 .html();
 } else {
 return data;
 }
 }
 }*/
