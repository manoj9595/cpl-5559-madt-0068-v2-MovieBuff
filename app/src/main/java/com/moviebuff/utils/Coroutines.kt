package com.moviebuff.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

object Coroutines {

    fun main(work: suspend (()-> Unit)) = CoroutineScope(Dispatchers.Main  +Job()).launch { work() }

    fun io(work: suspend (()-> Unit)) = CoroutineScope(Dispatchers.IO).launch { work() }

}