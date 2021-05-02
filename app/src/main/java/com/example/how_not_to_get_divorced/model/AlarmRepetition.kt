package com.example.how_not_to_get_divorced.model

/**
 * Union of possible types of alarms repetition
 */
sealed class AlarmRepetition {
    /**
     * Class of discrete repetition value [0, 1] - chances to fire on specific weekday
     */
    data class Discrete(
        var monday: Float,
        var tuesday: Float,
        var wednesday: Float,
        var thursday: Float,
        var friday: Float,
        var saturday: Float,
        var sunday: Float
        ) : AlarmRepetition()

    /**
     * Class of continuous repetition value of expected number of firing on specific weekday
     */
    data class Continuous(
        var monday: Float,
        var tuesday: Float,
        var wednesday: Float,
        var thursday: Float,
        var friday: Float,
        var saturday: Float,
        var sunday: Float
    ) : AlarmRepetition()

    fun getExpectedNumberOfTriggers(): Float{
        return when (this) {
            is Discrete -> {
                val sum = monday + tuesday + wednesday + thursday + friday + saturday + sunday
                sum / 7.0F
            }
            is Continuous -> {
                val sum = monday + tuesday + wednesday + thursday + friday + saturday + sunday
                sum / 7.0F
            }
        }
    }

    fun getExpectedTimeBetween(): Float{
        return when (this) {
            is Discrete -> {
                val sum = monday + tuesday + wednesday + thursday + friday + saturday + sunday
                24.0F / (sum / 7.0F)
            }
            is Continuous -> {
                val sum = monday + tuesday + wednesday + thursday + friday + saturday + sunday
                24.0F / (sum / 7.0F)
            }
        }
    }

    fun getType(): String {
        return when (this) {
            is Discrete ->
                "Discrete"
            is Continuous ->
                "Continuous"
        }
    }
}