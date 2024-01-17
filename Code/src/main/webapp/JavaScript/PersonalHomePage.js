/*Function to organize the personal home page which is the one shown once you made the login
* It contains all the tournament created by the educator/interesting for the student
* and the possibility for the educator of creating a new tournament*/
function PersonalHomePage(user) {
    //Element initialization
    //General usage
    var self = this;

    //Button div initialization
    let homePageButton = document.getElementById("homePageDiv");

    //Tournament Table Div reference
    let tournamentTableDiv = document.getElementById("tournamentTableDiv");
    //Tournament Table reference
    let tournamentTable= document.getElementById("tournamentTable")
    //Create Tournament Button reference
    let createTournamentButton = document.getElementById("createTournamentSubmit")
    //Tournament Registration Deadline Input reference
    let registrationDeadlineInput = document.getElementById("tournamentRegistrationDeadlineInput")


    //Function that perform the creation of a tournament
    function creationTournament(form){
        //Post in CreateTournament servlet
        if (form.checkValidity()) {
            makeCall("POST", 'CreateTournament', form,
                function (x) {
                    if (x.readyState === XMLHttpRequest.DONE) {
                        //server return message
                        var message = x.responseText;
                        switch (x.status){
                            case 200: //OK
                                closeModal();
                                location.reload();
                                break;
                            case 400: //BAD REQUEST
                                document.getElementById("errormessageNewTournament").textContent = message;
                                break;
                            case 401: //UNAUTHORIZED
                                document.getElementById("errormessageNewTournament").textContent = message;
                                break;
                            case 409: //CONFLICT
                                document.getElementById("errormessageNewTournament").textContent = message;
                                break;
                            case 500: //INTERNAL SERVER ERROR
                                document.getElementById("errormessageNewTournament").textContent = message;
                                break;
                        }
                    }
                })
        }
        else {
            form.reportValidity();
        }
        clearForm("createTournamentForm")
    }

    //Adding the click listener to the button
    createTournamentButton.addEventListener('click', (e) =>{
        //Create Tournament Form reference
        var form = e.target.closest("form")
        creationTournament(form)
    })

    //Adding the keypress listener to the input field
    registrationDeadlineInput.addEventListener('keypress', (e) =>{
        if (e.key === 'Enter') {
            //Create Tournament Form reference
            var form = e.target.closest("form")
            creationTournament(form)
        }
    })


    /*This is the method used to open the personal home page
    * First it decides if the create tournament button must be shown
    * Then with the makeCall function obtains all the necessary tournament
    * and then updates the tournament table*/
    this.openPage = function () {
        if (user.role !== 1) {
            homePageButton.style.display = "";
        }
        tournamentTableDiv.style.display = ""
        makeCall("GET","ShowTournament",null, function(x) {
                if (x.readyState === XMLHttpRequest.DONE) {
                    var message = x.responseText;
                    switch (x.status) {
                        case 200:
                            message=JSON.parse(message)
                            self.updateTournamentTable(message)
                            break;
                        default:
                            pageOrchestrator.showError(message);
                            break;
                    }
                }
            }
        )
    };

    /*This function is used to update the tournament table
    * It obtains in input the list of tournaments and add a new row in the
    * table for each tournament contained in the list*/
    this.updateTournamentTable = function (tournaments){
        tournamentTable.innerHTML=""
        tournaments.forEach(function (tournament){
            let tr= document.createElement("tr");
            let td= document.createElement("td");
            let anchor = document.createElement("a");
            td.appendChild(anchor);
            let linkText = document.createTextNode(tournament.id);
            anchor.appendChild(linkText);
            anchor.addEventListener("click", (e) => {
                self.hide()
                pageOrchestrator.showTournamentPage(tournament.id)
            }, false);
            anchor.href = "#";
            tr.appendChild(td);
            tournamentTable.appendChild(tr);
            td= document.createElement("td");
            td.innerHTML= tournament.name;
            tr.appendChild(td);
            td= document.createElement("td");
            if(tournament.creatorUsername !== user.username){
                td.innerHTML= tournament.creatorUsername;
            }
            else
                td.innerHTML= "ME"
            tr.appendChild(td);
        })
    }

    /*This function is used to hide all the element contained in this page*/
    this.hide = function (){
        homePageButton.style.display="none"
        tournamentTableDiv.style.display="none"
    }
}