package andrewafony.factsaboutnumbers.com.numbers.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

interface Communication {

    interface Observe<T> {

        fun observe(owner: LifecycleOwner, observer: Observer<T>)
    }

    interface Mutable<T> : Observe<T>, Mapper.Unit<T>

    abstract class Abstract<T>(
        protected val liveData: MutableLiveData<T>
    ) : Mutable<T> {
        override fun observe(owner: LifecycleOwner, observer: Observer<T>) {
            liveData.observe(owner, observer)
        }
    }

    abstract class Ui<T: Any>(liveData: MutableLiveData<T> = MutableLiveData()) : Abstract<T>(liveData) {
        override fun map(source: T) {
            liveData.value = source
        }
    }

    abstract class Post<T: Any>(liveData: MutableLiveData<T> = MutableLiveData()) : Abstract<T>(liveData) {
        override fun map(source: T) = liveData.postValue(source)
    }
}