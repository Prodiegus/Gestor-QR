package utalca.gestor_qr

import android.os.Bundle
import android.widget.RelativeLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import utalca.gestor_qr.MainViews.Crear
import utalca.gestor_qr.MainViews.Escanear
import utalca.gestor_qr.MainViews.Historial

class MainActivity : AppCompatActivity() {

    val escanear = Escanear()
    val crear = Crear()
    val historial = Historial()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val navigationView = findViewById<BottomNavigationView>(R.id.navigation)
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(escanear)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.navigation), { view, insets ->
            val params = view.layoutParams as RelativeLayout.LayoutParams
            params.topMargin = insets.getInsets(WindowInsetsCompat.Type.displayCutout()).top
            view.layoutParams = params
            insets
        })
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.escanear -> {
                loadFragment(escanear)
                return@OnNavigationItemSelectedListener true
            }
            R.id.crear -> {
                loadFragment(crear)
                return@OnNavigationItemSelectedListener true
            }
            R.id.historial -> {
                loadFragment(historial)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

     fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment)
         transaction.commit()
     }
}


