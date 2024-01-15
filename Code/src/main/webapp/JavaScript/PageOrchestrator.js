{
    var user = JSON.parse(sessionStorage.getItem('user'));
    var pageOrchestrator = new PageOrchestrator();// main controller
    var self= this;
    window.addEventListener("load", () => {
        if (user == null) {
            window.location.href = "HomePage.html";
        } else {
            //pageOrchestrator.start(); // initialize the components
        } // display initial content
    }, false);

    function PageOrchestrator(){
        let goodMorningUser = document.getElementById("goodMorningUser");

        let personalHomePage = new PersonalHomePage(user);

        goodMorningUser.innerHTML="Goodmorning, " + user.username + "!"
        personalHomePage.openPage()
        /*let homePageButton = document.getElementById("homePageDiv");
        let tournamentPageButton=document.getElementById("tournamentPageDiv")
        let battlePageButton= document.getElementById("battlePageDiv")
        let tournamentInfo = document.getElementById("tournamentInfo")
        let battleInfo = document.getElementById("battleInfo")
        let personalInformationDiv = document.getElementById("personalInformationDiv")
        let tournamentTableDiv= document.getElementById("tournamentTableDiv")
        let battleTableDiv = document.getElementById("battleTableDiv")
        let rankingTableDiv = document.getElementById("rankingTableDiv")
        let notificationTableDiv= document.getElementById("notificationTableDiv")


        if(user.role===1) {
            homePageButton.style.display="none";
        }
        tournamentPageButton.style.display="none"
        battlePageButton.style.display="none"
        tournamentInfo.style.display="none"
        battleInfo.style.display="none"
        personalInformationDiv.style.display="none"
        battleTableDiv.style.display="none"
        rankingTableDiv.style.display="none"
        notificationTableDiv.style.display="none"*/
    }
}