$( document ).ready(function() {
    $(document).foundation();

    $(".todo-delete-link").click(function (e) {
        e.preventDefault();
        var todoId = $(this).attr("href");
        console.log(todoId);

        x0p('Are you sure you want to delete this todo item', null, 'warning').then(
            function(data) {
                if(data.button == 'warning') {
                    $("#todo-delete-form-" + todoId).submit();
                }
                if(data.button == 'cancel') {
                    x0p('Canceled',
                        'todo item deletion canceled',
                        'error', false);
                }
            });
    })

});

