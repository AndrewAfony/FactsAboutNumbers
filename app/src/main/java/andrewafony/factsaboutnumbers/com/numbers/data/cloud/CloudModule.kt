package andrewafony.factsaboutnumbers.com.numbers.data.cloud

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

interface CloudModule {

    fun <T> service(clazz: Class<T>): T

    abstract class Abstract: CloudModule {

        protected abstract val logLevel: HttpLoggingInterceptor.Level
        protected abstract val baseUrl: String

        override fun <T> service(clazz: Class<T>): T {
            val interceptor = HttpLoggingInterceptor().apply { setLevel(logLevel) }

            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()

            return retrofit.create(clazz)
        }
    }

    class Debug: Abstract() {
        override val logLevel: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY
        override val baseUrl: String = "http://numbersapi.com/"
    }

    class Release : Abstract() {
        override val logLevel: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.NONE
        override val baseUrl: String = "http://numbersapi.com/"
    }

}