package dev.nelson.mot.main.domain.use_case.settings

import java.lang.RuntimeException

class FileAlreadyExistException(message: String?) : Exception(message) {
}