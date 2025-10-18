package com.example.testflow.core.mvi

/**
 * Interface that intent receivers implement specifying a specific intent class
 */
interface IntentReceiver<I : Any> {

    fun receiveIntent(intent: I)
}
