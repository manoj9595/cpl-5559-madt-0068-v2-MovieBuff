package com.moviebuff.utils

import java.io.IOException
import java.lang.NullPointerException

class ApiException(message : String) : IOException(message)
class NoInternetException(message : String) : IOException(message)
class NoResponseBodyException(message : String?) : NullPointerException(message) //Head Call