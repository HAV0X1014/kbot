package org.kbot

import org.json.JSONObject

/**
 * @author surge
 * @since 28/03/2023
 */
object Storage {

    val COUNTRY_OF_THE_DAY: Pair<Long, String>

    init {
        val json = JSONObject(this.javaClass.getResourceAsStream("/countries.json")!!.reader().readText())

        COUNTRY_OF_THE_DAY = Pair(System.currentTimeMillis(), json.getJSONObject(json.keySet().random()).getString("name"))
    }

}