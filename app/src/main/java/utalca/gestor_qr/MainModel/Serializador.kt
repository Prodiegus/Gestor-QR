package utalca.gestor_qr.MainModel

import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import utalca.gestor_qr.MainModel.QR
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class Serializador(private val context: Context) {
fun guardarQR(qr: QR, filename: String) {
    var counter = 0
    var name = "$filename.qr"
    var file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), name)

    // Verificar si el archivo ya existe
    while (file.exists()) {
        // Si el archivo ya existe, agregar "(counter)" al nombre del archivo
        name = "$filename($counter).qr"
        file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), name)
        counter++
    }

    val fileOutputStream = FileOutputStream(file)
    val objectOutputStream = ObjectOutputStream(fileOutputStream)
    objectOutputStream.writeObject(qr)
    objectOutputStream.close()
    fileOutputStream.close()

    val absolutePath = file.absolutePath

    Log.d("utalca.gestor_qr.MainModel.Serializador", "QR guardado en $absolutePath")
}

    fun cargarQR(filename: String): QR? {
        val fileInputStream = context.openFileInput(filename)
        val objectInputStream = ObjectInputStream(fileInputStream)
        val qr = objectInputStream.readObject() as? QR
        objectInputStream.close()
        fileInputStream.close()
        return qr
    }

fun cargarQR(): List<QR> {
    val qrList = mutableListOf<QR>()
    val files = context.fileList()

    for (filename in files) {
        val qr = cargarQR(filename)
        if (qr != null) {
            qrList.add(qr)
        }
    }

    return qrList
}
}