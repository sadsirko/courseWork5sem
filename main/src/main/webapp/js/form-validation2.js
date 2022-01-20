$(function () {
    $("form[name='registration']").validate({
        rules: {
            email: {
                required: true,
                email: true
            },
            password: {
                required: true,
                minlength: 8
            }
        },
        messages: {
            password: {
                required: "Please provide a password",
                minlength: "Your password must be at least 8 characters long"
            },
            email: "Please enter a valid email address"
        },
        submitHandler: function (form) {
            form.submit();
        }
    });

    $("form[name='login']").validate({
        rules: {
            email: {
                required: true,
                email: true
            },
            password: {
                required: true,
                minlength: 8
            }
        },
        messages: {
            password: {
                required: "Please provide a password",
                minlength: "Your password must be at least 8 characters long"
            },
            email: "Please enter a valid email address"
        },
        submitHandler: function (form) {
            form.submit();
        }
    });


    $("form[name='upBalance']").validate({
        rules: {
            upBal: {
                required: true,
                min: 0,
                max: 10000000
            },
        },
        messages: {
            upBalance: "Please enter a valid email address"
        },
        submitHandler: function (form) {
            form.submit();
        }
    });
});
