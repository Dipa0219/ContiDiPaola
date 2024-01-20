function PersonalInfoPage(user){
    let self=this
    let personalInformationDiv= document.getElementById("personalInformationDiv")
    let updatePersonalInformationSubmit= document.getElementById("updatePersonalInformationSubmit")

    //initialize all label
    let nameUser= document.getElementById("nameUser")
    let surnameUser = document.getElementById("surnameUser")
    let birthdateUser = document.getElementById("birthdateUser")
    let usernameUser = document.getElementById("usernameUser")
    let emailUser = document.getElementById("emailUser")

    this.openPage=function (){
        personalInformationDiv.style.display=""
        makeCall("GET","GetUser",null,
            function (x){
                if (x.readyState === XMLHttpRequest.DONE) {
                    var message = x.responseText;
                    switch (x.status) {
                        case 200:
                            message=JSON.parse(message)
                            self.updatePersonalInfo(message)
                            break;
                        default:
                            pageOrchestrator.showError(message);
                            break;
                    }
                }
            })
    }

    this.updatePersonalInfo=function (info){
        nameUser.innerHTML="Name: "+ info.name
        surnameUser.innerHTML="Surname: " + info.surname
        birthdateUser.innerHTML="Birthdate :" + info.birthdate
        usernameUser.innerHTML="Username: " + info.username
        emailUser.innerHTML="Email: " + info.email
    }

    this.hide=function (){
        personalInformationDiv.style.display="none"
    }
}
