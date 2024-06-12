package utalca.gestor_qr

import Serializador
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import utalca.gestor_qr.MainModel.QR
import utalca.gestor_qr.MainViews.VistaQr

class VerQR : AppCompatActivity() {
    // En tu actividad VerQR
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ver_qr)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val qr = intent.getSerializableExtra("qr") as QR
        if (qr == null) {
            initMainActivity()
            return
        }
        val fragment = VistaQr.newInstance(qr)
        supportFragmentManager.beginTransaction().replace(R.id.frame_QR_View, fragment).commit()
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val serializador = Serializador(this)
        serializador.guardarQR(qr!!, qr!!.getNombre()!!)

       val toolbarTitle = findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = qr.getNombre()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                initMainActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}