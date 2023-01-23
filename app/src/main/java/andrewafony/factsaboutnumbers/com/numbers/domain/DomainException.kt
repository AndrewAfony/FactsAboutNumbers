package andrewafony.factsaboutnumbers.com.numbers.domain

abstract class DomainException : IllegalStateException()

class NoConnectionException(): DomainException()

class ServiceUnavailableException(): DomainException()