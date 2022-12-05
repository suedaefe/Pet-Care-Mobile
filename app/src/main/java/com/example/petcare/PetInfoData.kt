package com.example.petcare


class PetInfoData {

    var petname : String= ""
    var petbreed: String= ""
    var petbday : String= ""
    var petfm: String= ""
    var petweight : String= ""

    constructor(petname :String, petbreed: String, petbday: String, petfm: String, petweight:String){
        this.petname = petname
        this.petbreed= petbreed
        this.petbday= petbday
        this.petfm= petfm
        this.petweight= petweight
    }
}