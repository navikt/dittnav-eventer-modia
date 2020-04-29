package no.nav.personbruker.dittnav.eventer.modia.common.exceptions

open class EventCacheException(message: String, cause: Throwable?) : Exception(message, cause) {

    val context: MutableMap<String, Any> = mutableMapOf()

    fun addContext(key: String, value: Any) {
        context[key] = value
    }

    override fun toString(): String {
        return when (context.isNotEmpty()) {
            true -> super.toString() + ", context: $context"
            false -> super.toString()
        }
    }
}
