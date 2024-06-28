package utalca.gestor_qr.MainModel

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class Serializador(private val context: Context) {
    @SuppressLint("LongLogTag")
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
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), filename)
            if (!file.exists()) {
                Log.d("Serializador", "El archivo $filename no existe")
                return null
            }
            val fileInputStream = FileInputStream(file)
            val objectInputStream = ObjectInputStream(fileInputStream)
            val qr = objectInputStream.readObject() as? QR
            objectInputStream.close()
            fileInputStream.close()
            return qr
        }

        fun cargarQR(): List<QR> {
            val qrList = mutableListOf<QR>()
            val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            val files = directory?.listFiles()

            files?.forEach { file ->
                val qr = cargarQR(file.name)
                if (qr != null) {
                    qrList.add(qr)
                }
            }
            Log.d("Serializador", "QRs cargados: $qrList")
            return qrList
        }

        fun serializarQRToFile(qr: QR, filename: String): File {
            val file = File(context.cacheDir, filename)
            val fileOutputStream = FileOutputStream(file)
            val objectOutputStream = ObjectOutputStream(fileOutputStream)
            objectOutputStream.writeObject(qr)
            objectOutputStream.close()
            return file
        }

        fun outputFileToQR(file: File): QR? {
            val fileInputStream = FileInputStream(file)
            val objectInputStream = ObjectInputStream(fileInputStream)
            val qr = objectInputStream.readObject() as? QR
            objectInputStream.close()
            fileInputStream.close()
            return qr
        }

        fun cargarQR(inputStream: InputStream): QR? {
            return try {
                val objectInputStream = ObjectInputStream(inputStream)
                val qr = objectInputStream.readObject() as QR
                objectInputStream.close()
                qr
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
}