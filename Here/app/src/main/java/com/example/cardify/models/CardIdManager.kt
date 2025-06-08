//hasn't used, thus excluded from commit. 10:44 23 May.

package com.example.cardify.models

object CardIdManager {
    private var currentId = 0

    fun getNextId(): String {
        currentId += 1
        return currentId.toString()
    }
}