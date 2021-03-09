$(function () {
    $(".password-show").click(function (event) {
        $(this).toggleClass('fa-eye-slash');
        var x = $("#password").attr("type");
        if (x == "password") {
            $("#password").attr("type", "text");
        } else {
            $("#password").attr("type", "password");
        }
    });
})