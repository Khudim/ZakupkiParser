/**
 * Created by Beaver on 12.03.2017.
 */
var usersEditor; // use a global for the submit and return data rendering in the examples

$(document).ready(function() {
   usersEditor = new $.fn.DataTable.Editor( {
        ajax: "/admin/editUser",
        table: "#usersTable",
        idSrc:  'code',
        fields: [ {
            label: "code:",
            name: "code"
        },  {
            label: "password:",
            name: "password"
        }, {
            label: "email:",
            name: "email"
        }, {
            label: "role:",
            name: "role",
            type: "select",
            options: [ "ADMIN", "USER" ],
            def: "USER"
        }
        ]
    } );

   var usersTable = $('#usersTable').DataTable( {
        lengthChange: false,
        ajax: {
            url: "/admin/getAllUsers",
            type: "POST"
        },
        serverSide: true,
        columns: [
            { data: "code" },
            { data: "email" },
            { data: "role" }
        ],
        select: true
    } );

    new $.fn.DataTable.Buttons( usersTable, [
        { extend: "create", editor: usersEditor },
        { extend: "edit",   editor: usersEditor },
        { extend: "remove", editor: usersEditor }
    ] );

    usersTable.buttons().container()
        .appendTo( $('div.eight.column:eq(0)', usersTable.table().container()) );
} );