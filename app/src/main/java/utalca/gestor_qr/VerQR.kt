package utalca.gestor_qr

import utalca.gestor_qr.MainModel.Serializador
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import utalca.gestor_qr.MainModel.QR
import utalca.gestor_qr.MainViews.VistaQr

class VerQR : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ver_qr)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = this.getWindow()
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.primary)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val serializador = Serializador(this)
        val qr: QR? = if (intent.hasExtra("qr")) {
            // Si el Intent contiene un extra "qr", obtenerlo y usarlo
            intent.getSerializableExtra("qr") as? QR
        } else if (intent.data != null) {
            // Si el Intent contiene una URI de datos, deserializar el objeto QR del archivo en esa URI
            val inputStream = contentResolver.openInputStream(intent.data!!)
            serializador.cargarQR(inputStream!!)
        } else {
            null
        }

        if (qr != null) {
            // Si se obtuvo un objeto QR, mostrarlo en la actividad
            val fragment = VistaQr.newInstance(qr)
            supportFragmentManager.beginTransaction().replace(R.id.frame_QR_View, fragment).commit()
            val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.back_arrow)
            val toolbarTitle = findViewById<TextView>(R.id.toolbar_title)
            toolbarTitle.text = qr.getNombre()
        } else {
            // Si no se obtuvo un objeto QR, iniciar MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            Log.d("VerQR", "MainActivity iniciada correctamente")
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
               finish()
                true
            }
            R.id.share -> {
                val qr = intent.getSerializableExtra("qr") as QR
                if (qr != null) {
                    val serializador = Serializador(this)
                    val qrFile = serializador.serializarQRToFile(qr, qr.getNombre()+".qr")
                    val uri = FileProvider.getUriForFile(this,  this.applicationContext.packageName + ".provider", qrFile)
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_STREAM, uri)
                        type = "application/octet-stream"
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    val shareIntent = Intent.createChooser(sendIntent, "Compartir QR")
                    startActivity(shareIntent)
                } else {
                    Toast.makeText(this, "No hay un QR para compartir", Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }
    private fun initMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}