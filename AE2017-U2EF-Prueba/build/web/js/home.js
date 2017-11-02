/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(function () {
    //how to use jQueryValidate
    $("#form-register").validate({
        rules: {
            nombre: {//name del input
                //reglas para el rolname
                required: true
            },
            email: {
                required: true
            },
            password: {
                required: true
            },
            passwordConfirm: {
                required: true
            }
        },
        messages: {
            nombre: {
                required: "<strong>*Este campo es obligatorio</strong>"
            },
            email: {
                required: "<strong>*Este campo es obligatorio</strong>"
            },
            password: {
                required: "<strong>*Este campo es obligatorio</strong>"
            },
            passwordConfirm: {
                required: "<strong>*Este campo es obligatorio</strong>"
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
            //console.log("Hey hey hey algo bien siempre al 100 pal dj everyday..");
            newPerson();
            return false; //evito que haga el submit. 
        }
    });
});

var tabla = $('#tb-usuarios').DataTable({
    ajax: {
        url: "ConsultaUsuarios",
        dataSrc: function (json) {
            //return $.parseJSON(json);
            return json;
        }
    },
    columns: [
        {
            data: "usuarioid"
        },
        {
            data: "nombre"
        },
        {
            data: "email"
        },
        {
            data: function (row) {
                let cad = "";
                if (row["activo"]) {
                    cad = "<buton class='btn btn-success btn-sm' onclick='actualizar(" + row["usuarioid"] + ")'>ACTIVO</button>"
                } else {
                    cad = "<buton class='btn btn-danger btn-sm'  onclick='actualizar(" + row["usuarioid"] + ")'>INACTIVO</button>"
                }
                return cad;
            }
        }
    ]
});

function newPerson() {
    if ($("#password").val() === $("#passwordConfirm").val()) {
        $.ajax({
            url: "CrearUsuario",
            type: "post",
            data: $("#form-register").serialize()
        }).done(function (data) {
            console.log(data);
            switch (data.code) {
                case 200:
                    swal("INFORMACIÓN!", data.message, "success");
                    tabla.ajax.reload();
                    break;
                case 502:
                    swal("ATENCION!", data.message, "error");
                default:
                    swal("ATENCION!", data.message, "error");
            }
        }).fail(function (data) {
            console.log(data);
            swal("INFORMACION!", "Oops! Es que no me tienen paciencia", "error");
        });
    } else {
        swal("ATENCION!", "Las contraseñas no coinciden", "error");
    }

}

function actualizar(usuarioid) {
    $.ajax({
        url: "ActualizarUsuario",
        type: "post",
        data: {
            usuarioid: usuarioid,
        }
    }).done(function (data) {
        console.log(data);
        switch (data.code) {
            case 200:
                tabla.ajax.reload();
                break;
            case 400:
                swal("ATENCION!", data.message, "warning");
                break;
            case 409:
                swal("ATENCION!", data.message, "warning");
                break;
            default:
                console.log("esta vaina");
                swal("ATENCION!", data.message, "error");
        }
    }).fail(function (data) {
        console.log(data);
        swal("INFORMACION!", "Oops! Es que no me tienen paciencia", "error");
    });
}
