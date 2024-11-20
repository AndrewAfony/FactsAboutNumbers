package andrewafony.factsaboutnumbers.com.main.presentation

import andrewafony.factsaboutnumbers.com.numbers.presentation.Communication

interface DetailsCommunication: Communication.Mutable<String> {

    class Base: DetailsCommunication, Communication.Post<String>()
}