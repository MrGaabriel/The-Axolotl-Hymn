package me.mrgaabriel.axolotlhymn.bson

import org.bson.codecs.pojo.annotations.*

class UserProfile @BsonCreator constructor(
        @BsonProperty("_id")
        _id: String) {

    @BsonProperty("_id")
    val id = _id

    var xp = 0

    var rep = 0
    var lastUsedRep = 0.toLong()

    var money = 0
    var lastDaily = 0.toLong()

    var favColor = "#99AAB5"

    var about = "Eu sou uma pessoa legal! Sabia que você pode mudar este texto usando \"t!sobremim\"?"

    var backgroundUrl = "https://cdn.discordapp.com/attachments/397295975175028736/480380805517017098/Sem_Titulo-1.png"

    var married = false
    var marriedUser = ""
}