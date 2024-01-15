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
        let goToUserHomePage =document.getElementById("goToUserHomePage")

        let personalHomePage = new PersonalHomePage(user);
        let tournamentPage = new TournamentPage(user);
        let actualPage= personalHomePage
        goodMorningUser.innerHTML="Goodmorning, " + user.username + "!"
        goToUserHomePage.addEventListener("click", (e) => {
            actualPage.hide()
            personalHomePage.openPage()
            actualPage= personalHomePage
        }, false);
        personalHomePage.openPage()

        this.showBattlePage = function (id) {
            tournamentPage.openPage(id)
            actualPage = tournamentPage
        };
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