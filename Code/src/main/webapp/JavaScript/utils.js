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

/**
 * Open modal page.
 *
 * @param type name of modal page to open.
 */
function openModal(type) {
    var modal = document.getElementById(type + 'Modal');
    modal.style.display = 'flex';
}

/**
 * Close modal page.
 */
function closeModal() {
    var modals = document.querySelectorAll('.modal');
    modals.forEach(function(modal) {
        modal.style.display = 'none';
    });
}

/**
 * Clear all form fields.
 *
 * @param type name of form to clear.
 */
function clearForm(type){
    document.getElementById(type).reset();
}