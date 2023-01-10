package com.example.a12020319_harshul_sharma

class Message {
    var message:String? = null
    var senderId: String? = null

    constructor(){}

    constructor(message: String, senderId:String?) {
        this.message = message
        this.senderId = senderId
    }

}