package de.lulebe.interviewer.helpers

import android.os.Bundle

fun listToBundle(list: List<Bundle>) : Bundle {
    val bundle = Bundle()
    list.forEachIndexed { i, b ->
        bundle.putBundle(i.toString(), b)
    }
    return bundle
}

fun bundleToList(bundle: Bundle) : List<Bundle> {
    return bundle.keySet().map { it.toInt() }.sorted().map { it.toString() }.map {
        bundle.getBundle(it)
    }
}