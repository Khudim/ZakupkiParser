/**
 * Created by Beaver on 12.03.2017.
 */
var editor2; // use a global for the submit and return data rendering in the examples

$(document).ready(function() {
   editor2 = new $.fn.DataTable.Editor( {
        "ajax": "/admin/editUser",
        "table": "#usersTable",
        "idSrc":  'code',
        "fields": [ {
            "label": "code:",
            "name": "code"
        },  {
            "label": "password:",
            "name": "password"
        }, {
            "label": "email:",
            "name": "email"
        }, {
            "label": "role:",
            "name": "role"
        }
        ]
    } );

    // Activate an inline edit on click of a table cell
    $('#usersTable').on( 'click', 'tbody td:not(:first-child)', function (e) {
        editor2.inline( this );
    } );


    $('#usersTable').DataTable( {
        dom: "Bfrtip",
        ajax: {
            url: "/admin/getAllUsers",
            type: "POST"
        },
        serverSide: true,
        columns: [
            {
                data: null,
                defaultContent: '',
                className: 'select-checkbox',
                orderable: false
            },
            { data: "code" },
            { data: "email" },
            { data: "role" }
        ],
        select: {
            style:    'os',
            selector: 'td:first-child'
        },
        buttons: [
            { extend: "create", editor: editor2 },
            { extend: "edit",   editor: editor2 },
            { extend: "remove", editor: editor2 }
        ]
    } );
} );