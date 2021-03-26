package dev.nelson.mot.main.util.extention

import android.os.Parcel

/** Read a boolean from a Parcel (copied from Parcel, where this is @hidden). */
fun Parcel.readBoolean() = readInt() != 0

/** Write a boolean to a Parcel (copied from Parcel, where this is @hidden). */
fun Parcel.writeBoolean(value: Boolean) = writeInt(if (value) 1 else 0)