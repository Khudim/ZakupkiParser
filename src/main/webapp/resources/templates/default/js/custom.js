$(document).ready(function () {

    // Activated the table
    var tableClient = $('#tableClient').DataTable({
        "processing": true,
        "serverSide": true,
        "ajax": {
            "url": "/getAllDocuments",
            "type": "POST"
        },
            "columns": [
                { "data": "guid" },
                { "data": "documentType" },
                { "data": "creationDate" },
                { "data": "region" },
                { "data": "url" },
                { "data": "price" }
            ]
    });


    $("#pickerDateBirth").datetimepicker({
        format: 'DD/MM/YYYY'
    });


    $(window).load(function () {

    });

    $("#buttonSearch").click(function () {
        tableClient.clear().draw();
        tableClient.ajax.reload();

    });
    /*
     $("#buttonInsert").click(function(){
     $(this).callAjax("insertClient", "");

     $(".form-control").val("");

     });*/

    /*  $("#buttonDelete").click(function(){

     var valuesChecked = $("#tableClient input[type='checkbox']:checkbox:checked").map(
     function () {
     return this.value;
     }).get().join(",");

     $(this).callAjax("deleteClient", valuesChecked);

     });*/
    /*
     $.fn.callAjax = function( method, checkeds ){
     $.ajax({
     type: "POST",
     url: "/" + method,
     dataType: "json",
     timeout : 100000,
     data: { guid: $("#guid").val(), url: $("#url").val(), checked: checkeds },

     success: function(data){
     tableClient.clear().draw();
     tableClient.ajax.reload();
     },
     error: function(e){
     alert("ERROR: ", e);
     }
     });
     }*/

});