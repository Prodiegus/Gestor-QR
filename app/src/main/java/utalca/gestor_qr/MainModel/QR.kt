package utalca.gestor_qr.MainModel

import java.io.Serializable

class QR  : Serializable {
    private var url: String? = null
    private var nombre: String? = null
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var date: Long

    constructor(url: String?, nombre: String?, latitude: Double, longitude: Double) {
        this.url = url
        this.nombre = nombre
        this.latitude = latitude
        this.longitude = longitude
        this.date = System.currentTimeMillis()
    }

    fun getUrl(): String? {
        return url
    }
    fun getNombre(): String? {
        return nombre
    }
    fun getLatitude(): Double {
        return latitude
    }
    fun getLongitude(): Double {
        return longitude
    }

    fun getDate(): Long {
        return date
    }

    fun setUrl(url: String?) {
        this.url = url
    }
    fun setNombre(nombre: String?) {
        this.nombre = nombre
    }
    fun setLatitude(latitude: Double) {
        this.latitude = latitude
    }
    fun setLongitude(longitude: Double) {
        this.longitude = longitude
    }

    override fun toString(): String {
        return "QR(url=$url, nombre=$nombre, latitude=$latitude, longitude=$longitude)"
    }
}