package utalca.gestor_qr.MainModel

import android.content.Context
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.services.drive.model.File as DriveFile
import com.google.api.client.http.FileContent
import java.io.ByteArrayOutputStream
import java.io.File

class DriveServiceHelper(private val context: Context, account: GoogleSignInAccount) {
    private val driveService: Drive

    init {
        val credential = GoogleAccountCredential.usingOAuth2(
            context, listOf(DriveScopes.DRIVE_FILE)
        )
        credential.selectedAccount = account.account

        driveService = Drive.Builder(
            NetHttpTransport(),
            GsonFactory(),
            credential
        ).setApplicationName("Your App Name").build()
    }

    fun uploadFile(qr: QR, mimeType: String, folderId: String? = null) {
        val serializador = Serializador(context)
        val file = qr.getNombre()?.let { serializador.serializarQRToFile(qr, it) }
        val fileContent = FileContent(mimeType, file)
        val driveFileMetadata = DriveFile().apply {
            name = qr.getNombre()+".qr"
            folderId?.let { parents = listOf(it) }
        }

        val driveFile = driveService.files().create(driveFileMetadata, fileContent)
            .setFields("id")
            .execute()

        file?.delete()
    }

    fun getFile(): List<QR> {
        val result = mutableListOf<QR>()
        val serializador = Serializador(context)

        // List all files in Google Drive
        val files = driveService.files().list()
            .setSpaces("drive")
            .setFields("nextPageToken, files(id, name, mimeType)")
            .execute()
            .files

        // Filter QR files
        val qrFiles = files.filter { it.mimeType == "application/octet-stream" && it.name.endsWith(".qr") }

        // Download and deserialize each QR file
        for (file in qrFiles) {
            val outputStream = ByteArrayOutputStream()
            driveService.files()[file.id].executeMediaAndDownloadTo(outputStream)

            val tempFile = File.createTempFile("temp", ".qr", context.cacheDir)
            tempFile.deleteOnExit()
            tempFile.outputStream().use { outputStream.writeTo(it) }

            val qr = serializador.outputFileToQR(tempFile)

            qr?.let { result.add(it) }
        }

        return result
    }
}
