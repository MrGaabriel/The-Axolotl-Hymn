package me.mrgaabriel.axolotlhymn.data

data class HymnConfig(val clientToken: String = "Token do Bot",
                      val clientId: String = "ID do bot",
                      val owners: List<String> = listOf("IDs dos Donos, que poderão usar comandos especiais"))