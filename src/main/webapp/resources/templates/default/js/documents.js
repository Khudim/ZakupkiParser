$(document).ready(function () {

    // Activated the table
   $('#tableClient').DataTable({
        processing: true,
        serverSide: true,
        order: [[ 1, "desc" ]],
        ajax: {
            url: "/getAllDocuments",
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
     /*       {
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
            },*/
            {
                data: "url", render: function (data, type) {
                if (type === "display") {

                    return $('<a href="#" data-toggle="popover" title="Popover Header" data-content="Some content inside the popover">')
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
});