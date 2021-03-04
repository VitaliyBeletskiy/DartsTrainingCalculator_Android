package com.beletskiy.dartstrainingcalculator.data

/// represents a single throw's result
data class Toss(val section: Section, val ring: Ring) {

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
        INNER_BULLSEYE(25),
        OUTER_BULLSEYE(50),
    }

    enum class Ring(val value: Int) {
        X1(1),
        X2(2),
        X3(3)
    }
}
