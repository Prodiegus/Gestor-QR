package utalca.gestor_qr

import Serializador
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import utalca.gestor_qr.MainModel.QR
import utalca.gestor_qr.MainViews.VistaQr

class VerQR : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
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

        val qr = intent.getSerializableExtra("qr")
        if (qr == null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            Log.d("VerQR", "MainActivity iniciada correctamente")
            finish()
            return
        }
        val qrAsQR = qr as QR
        val fragment = VistaQr.newInstance(qrAsQR)
        supportFragmentManager.beginTransaction().replace(R.id.frame_QR_View, fragment).commit()
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_arrow)

        val serializador = Serializador(this)
        serializador.guardarQR(qrAsQR, qrAsQR.getNombre()!!)

        val toolbarTitle = findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = qrAsQR.getNombre()

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
        finish()
    }
}