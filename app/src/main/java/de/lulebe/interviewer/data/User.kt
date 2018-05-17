package de.lulebe.interviewer.data

import android.content.Context
import java.util.*


data class User(
        val id: UUID,
        val name: String,
        val connected: Boolean
) {

    companion object {

        private const val defaultUUID = "6ec1ea1b-22b9-47f4-80b6-e958ccbe1b06"
        private const val defaultName = "local"

        fun loadUser(ctx: Context) : User {
            val sp = ctx.getSharedPreferences("user", Context.MODE_PRIVATE)
            return User(
                    UUID.fromString(sp.getString("id", defaultUUID)),
                    sp.getString("name", defaultName ),
                    sp.getBoolean("connected", false)
            )
        }

    }

    fun save(ctx: Context) {
        val editor = ctx.getSharedPreferences("user", Context.MODE_PRIVATE).edit()
        editor.putString("id", id.toString())
        editor.putString("name", name)
        editor.putBoolean("connected", connected)
        editor.apply()
    }

}