package andrewafony.factsaboutnumbers.com.main.presentation

import andrewafony.factsaboutnumbers.com.numbers.presentation.Communication
import andrewafony.factsaboutnumbers.com.numbers.presentation.Mapper

interface NavigationCommunication {

    interface Observe : Communication.Observe<NavigationStrategy>

    interface Mutate : Mapper.Unit<NavigationStrategy>

    interface Mutable: Observe, Mutate

    class Base : Communication.Ui<NavigationStrategy>(SingleLiveEvent()), Mutable
}