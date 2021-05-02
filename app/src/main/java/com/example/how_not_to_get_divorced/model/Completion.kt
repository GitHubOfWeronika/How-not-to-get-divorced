package com.example.how_not_to_get_divorced.model;

enum class Completion(val id: Int) {
    DONE(1),
    FAILED(2),
    WAITING(3);

    companion object {
        fun get(i : Int): Completion {
            return when(i){
                1 -> DONE
                2 -> FAILED
                else -> WAITING
            }
        }
    }
}
