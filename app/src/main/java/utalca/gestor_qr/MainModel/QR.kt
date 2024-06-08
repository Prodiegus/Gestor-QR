package utalca.gestor_qr.MainModel

class QR {
    private var url: String? = null
    private var nombre: String? = null
    private var address: String? = null
    constructor(url: String?, nombre: String?, address: String?) {
        this.url = url
        this.nombre = nombre
        this.address = address
    }

    fun getUrl(): String? {
        return url
    }
    fun getNombre(): String? {
        return nombre
    }
    fun getAddress(): String? {
        return address
    }
    fun setUrl(url: String?) {
        this.url = url
    }
    fun setNombre(nombre: String?) {
        this.nombre = nombre
    }
    fun setAddress(address: String?) {
        this.address = address
    }
}