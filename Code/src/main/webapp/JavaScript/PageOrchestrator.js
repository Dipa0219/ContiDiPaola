{
    //obtains from the session all the useful user information
    var user = JSON.parse(sessionStorage.getItem('user'));

    //creates the page orchestrator
    var pageOrchestrator = new PageOrchestrator();// main controller
    var self= this;

    /*checks if the session is still active
    * in case if it isn't it shows the HomePage*/
    window.addEventListener("load", () => {
        if (user.id == null) {
            window.location.href = "HomePage.html";
        } else {
            //pageOrchestrator.start(); // initialize the components
        } // display initial content
    }, false);

    //It manages the interaction between all the different page
    function PageOrchestrator(){
        //initialize generic useful element
        let goodMorningUser = document.getElementById("goodMorningUser");

        //initialize header elements
        let goodMorningUserDD = document.getElementById("goodMorningUserDD")
        let goToShowPersonalInformation = document.getElementById("goToShowPersonalInformation")
        let goToUserHomePage =document.getElementById("goToUserHomePage")
        let logoutButton = document.getElementById("logoutButton")

        //initialize error element
        let error= document.getElementById("error")
        let errormessage= document.getElementById("errormessage")
        let rollback =document.getElementById("rollback")
        let errorFlag=0

        //initialiaze all the pages
        let personalHomePage = new PersonalHomePage(user)
        let tournamentPage = new TournamentPage(user)
        let battlePage= new BattlePage(user)
        let personalInfoPage = new PersonalInfoPage(user)
        let actualPage= personalHomePage

        //add first generale elements
        goodMorningUser.innerHTML="Goodmorning, " + user.username + "!"
        goodMorningUserDD.innerHTML= "Goodmorning, " + user.username
        goToUserHomePage.addEventListener("click", (e) => {
            actualPage.hide()
            if (errorFlag){
                error.style.display="none"
            }
            personalHomePage.openPage()
            actualPage= personalHomePage
        }, false);
        goToShowPersonalInformation.addEventListener("click",(e) =>{
            actualPage.hide()
            if (errorFlag){
                error.style.display="none"
            }
            personalInfoPage.openPage()
            actualPage=personalInfoPage
        },false)
        logoutButton.addEventListener("click",(e) => {
            actualPage.hide()
            if(errorFlag){
                error.style.display="none"
            }
            this.logout()
        })

        //Calls the function to open the first page
        personalHomePage.openPage()

        this.logout = function (){
            makeCall("POST", "Logout", null,
            function (x){
                    if (x.readyState === XMLHttpRequest.DONE) {
                        let message = x.responseText;
                        switch (x.status) {
                            case 200:
                                sessionStorage.removeItem("user")
                                window.location.href = "HomePage.html";
                                break;
                            default:
                                pageOrchestrator.showError(message);
                                break;
                        }
                    }
                }
            )
        }

        this.showPersonalHomePage= function (){
            actualPage.hide()
            if(errorFlag){
                error.style.display="none"
            }
            actualPage=personalHomePage
        }
        /*This function is used to show the tournament page
        * It updates the actual and then calls the openpage function*/
        this.showTournamentPage = function (id) {
            tournamentPage.openPage(id)
            actualPage = tournamentPage
        };

        /*This function is used to show the battle page
        * It updates the actual and then calls the openpage function*/
        this.showBattlePage = function (id) {
            battlePage.openPage(id)
            actualPage = battlePage
        };

        /*This function is used to show the error page
        * In particular it hides the actual page, and the shows the error message
        * that has just received with a button that allows to come back to the previous page*/
        //TODO try to fix button so that he receives only one call when an error occurs in two sequential MakeCall, if we don't find a solution give only the possibility to come back to the PersonalHomePage
        this.showError=function (message){
            actualPage.hide()
            errorFlag=1
            error.style.display=""
            errormessage.innerHTML=message
            rollback.addEventListener("click",(e)=>{
                    errorFlag=0
                    error.style.display="none"
                    actualPage.openPage()
                }
            )
        }
    }
}