
$(function () {
    $("#form-login").validate({
        rules: {
            email: {
                required: true
            },
            password: {
                required: true
            }
        },
        messages: {
            email: {
                required: "<strong>*El nombre del rol es obligatorio</strong>"
            },
            password: {
                required: "<strong>*Este campo Salario es obliatorio</strong>"
            }
        },
        highlight: function (element) {    //todos los eementos de color rojo
            //el elemento que este más cerca de form-group le ponga la clase de error
            $(element).closest(".form-group").addClass("has-error");
        },
        unhighlight: function (element) {  //quitar el color rojo
            $(element).closest(".form-group").removeClass("has-error");
        },

        //elementos para poner el mensaje
        errorElement: "p", //para ponerlo en un <p>
        errorClass: "text-danger",
        errorPlacement: function (error, element) {
            error.insertAfter(element.parent());    //insertar despues del padre
        },
        //cuando todo esta bien
        submitHandler: function (form) {
            checkLogin();
            return false; //evito que haga el submit. 
        }
    });


}); //function()

function checkLogin() {
    $.ajax({
        url: "LoginUsuario",
        type: "post",
        data: {
            email: $("#email").val(),
            password: $("#password").val()
        }
    }).done(function (data) {
        console.log(data);
        switch (data.code) {
            case 200:
                window.location.replace("home.html");
                break;
            case 400:
                swal("ATENCION!", data.message, "warning");
                break;
            case 409:
                swal("ATENCION!", data.message, "warning");
                break;
            default:
                swal("ATENCION!", data.message, "error");
        }
    }).fail(function (data) {
        console.log(data);
        swal("INFORMACION!","Oops! Es que no me tienen paciencia","error");
    });
}