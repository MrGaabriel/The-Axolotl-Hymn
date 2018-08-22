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

    var about = "Eu sou uma pessoa legal! Sabia que você pode mudar este texto usando \"t!sobremim\"?"
}