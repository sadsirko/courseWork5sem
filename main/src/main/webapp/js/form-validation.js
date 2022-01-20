
    $("form[name='process']").validate({
        rules: {
            range: {
                required: true
                number: true
                default: 1
            },
            symbolNum: {
                required: false,
                number: true
                default: 55
            },
            stop: {
                required: false,
                number: true,
            },
            startDate: {
                required: true
            },
            endDate: {
                required: true
            }
        },
        messages:
            start_date: {
                required: "Please provide date"
            },
            end_date: {
                required: "Please provide date"
            }
        },
        submitHandler: function(form) {
            form.submit();
        }
    });
});
