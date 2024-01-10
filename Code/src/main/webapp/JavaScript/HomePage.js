(function() { // avoid variables ending up in the global scope

    document.getElementById("loginsubmit").addEventListener('click', (e) => {
        var form = e.target.closest("form")
        if (form.checkValidity()) {
            makeCall("POST", 'LoginManager', e.target.closest("form"),
                function(x) {
                    console.log("Entrato")
                    if (x.readyState === XMLHttpRequest.DONE) {
                        var message = x.responseText;
                        switch (x.status) {
                            case 200:
                                sessionStorage.setItem('username',message);
                                window.location.href = "StudentHomePage.html";
                                break;
                            case 400: // bad request
                                document.getElementById("errormessage").textContent = message;
                                break;
                            case 401: // unauthorized
                                document.getElementById("errormessage").textContent = message;
                                break;
                            case 500: // server error
                                document.getElementById("errormessage").textContent = message;
                                break;
                        }
                    }
                }
            );
        } else {
            form.reportValidity();
        }
    });

})();