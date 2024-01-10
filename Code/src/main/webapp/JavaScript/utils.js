function makeCall(method, url, formElement, cback) {
    let req = new XMLHttpRequest(); // visible by closure
    req.onreadystatechange = function () {
        cback(req);
    }; // closure
    req.open(method, url);
    if (formElement !== null) {
        var formData = new FormData(formElement)
        req.send(formData)
    } else {
        req.send();
    }
}