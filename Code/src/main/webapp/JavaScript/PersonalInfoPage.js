function PersonalInfoPage(user){
    let self=this
    let personalInformationDiv= document.getElementById("personalInformationDiv")

    this.openPage=function (){
        personalInformationDiv.style.display=""
    }

    this.hide=function (){
        personalInformationDiv.style.display="none"
    }
}
