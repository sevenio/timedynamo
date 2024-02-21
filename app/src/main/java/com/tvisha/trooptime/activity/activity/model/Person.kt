package com.tvisha.trooptime.activity.activity.model

import java.io.Serializable

/**
 * Created by koti on 23/5/17.
 */
class Person(val name: String, val email: String, val userId: String) : Serializable {

    override fun toString(): String {
        return name
    }
}