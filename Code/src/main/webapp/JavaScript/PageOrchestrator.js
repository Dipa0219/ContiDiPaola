{
    //var pageOrchestrator = new PageOrchestrator();// main controller
    var user = JSON.parse(sessionStorage.getItem("username"));
    var self= this;
    window.addEventListener("load", () => {
        if (user == null) {
            window.location.href = "HomePage.html";
        } else {
            //pageOrchestrator.start(); // initialize the components
        } // display initial content
    }, false);
}