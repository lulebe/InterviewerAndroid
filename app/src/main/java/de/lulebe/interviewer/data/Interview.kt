package de.lulebe.interviewer.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Bundle
import java.util.*

@Entity(
        tableName = "interviews"
)
data class Interview (
        @PrimaryKey var id: UUID,
        var name: String,
        var colorScheme: ColorScheme
) {
    companion object {
        fun fromBundle(bundle: Bundle) = Interview(
                UUID.fromString(bundle.getString("id")),
                bundle.getString("name"),
                ColorScheme.values()[bundle.getInt("colorScheme")]
        )
    }

    fun toBundle() : Bundle {
        val bundle = Bundle()
        bundle.putString("id", id.toString())
        bundle.putString("name", name)
        bundle.putInt("colorScheme", colorScheme.ordinal)
        return bundle
    }
}