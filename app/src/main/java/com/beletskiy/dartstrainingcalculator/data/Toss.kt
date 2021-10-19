package com.beletskiy.dartstrainingcalculator.data

import android.os.Parcel
import android.os.Parcelable

/** Represents a single throw's result */
data class Toss(
    var number: Int, // we need 'number' for DiffUtil to work
    var counted: Boolean,
    val section: Section,
    val ring: Ring
) : Parcelable {

    val value: Int
        get() = section.value * ring.value

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt() == 1,
        Section.values()[parcel.readInt()],
        Ring.values()[parcel.readInt()]
    )

    override fun toString(): String {
        return "${this.section} ${this.ring} points: ${this.section.value * this.ring.value}"
    }

    enum class Section(val value: Int) {
        MISSED(0),
        ONE(1),
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5),
        SIX(6),
        SEVEN(7),
        EIGHT(8),
        NINE(9),
        TEN(10),
        ELEVEN(11),
        TWELVE(12),
        THIRTEEN(13),
        FOURTEEN(14),
        FIFTEEN(15),
        SIXTEEN(16),
        SEVENTEEN(17),
        EIGHTEEN(18),
        NINETEEN(19),
        TWENTY(20),
        OUTER_BULLSEYE(25),
        INNER_BULLSEYE(50),
    }

    enum class Ring(val value: Int) {
        X1(1),
        X2(2),
        X3(3)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(number)
        parcel.writeInt(if (counted) 1 else 0)
        parcel.writeInt(section.ordinal)
        parcel.writeInt(ring.ordinal)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Toss> {

        override fun createFromParcel(parcel: Parcel): Toss {
            return Toss(parcel)
        }

        override fun newArray(size: Int): Array<Toss?> {
            return arrayOfNulls(size)
        }
    }
}
