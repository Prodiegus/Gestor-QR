package utalca.gestor_qr.MainViews

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.zxing.ResultPoint
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.CompoundBarcodeView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import utalca.gestor_qr.MainActivity
import utalca.gestor_qr.MainModel.QR
import utalca.gestor_qr.R
import utalca.gestor_qr.VerQR
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Escanear : Fragment() {

    private var LOCATION_PERMISSION_CODE = 1

    private lateinit var barcodeView: CompoundBarcodeView
    private lateinit var abrirScanButton: Button
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var url: String? = null
    private var content: String? = null
    private var qr = QR(url, content, -35.423244, -71.648483)
    private var isScanning = false

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(activity, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(activity, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
            }
            // Finalizar la actividad después de recibir el resultado del escaneo
            activity?.finish()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            content = it.getString(ARG_PARAM1)
            url = it.getString(ARG_PARAM2)
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val permissions = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (ContextCompat.checkSelfPermission(requireContext(), permissions[0]) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(requireContext(), permissions[1]) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions, PERMISSIONS_REQUEST_CODE)
        }

        val view = inflater.inflate(R.layout.fragment_escanear, container, false)

        barcodeView = view.findViewById(R.id.barcode_scanner)
        abrirScanButton = view.findViewById(R.id.abrir_scan_button)

        barcodeView.setStatusText("")

        val callback = object : BarcodeCallback{
            override fun barcodeResult(result: BarcodeResult?) {
                if (result != null) {
                    lifecycleScope.launch {
                        val location = getLocalizacion()
                        val title = getTitulo(result.text)
                        qr =  QR(result.text, title, location!!.latitude, location!!.longitude)
                        Log.d("Escanear", "QR: $qr")
                        abrirScanButton.text = "Abrir Escaneo"
                        abrirScanButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark))
                        abrirScanButton.textSize = 14f
                        abrirScanButton.visibility = View.VISIBLE
                        barcodeView.pause()
                    }
                } else {
                    Toast.makeText(requireContext(), "No se pudo escanear", Toast.LENGTH_SHORT).show()
                }
            }

            override fun possibleResultPoints(resultPoints: List<ResultPoint>) {

            }

        }
        barcodeView.decodeSingle(callback)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        abrirScanButton = view.findViewById(R.id.abrir_scan_button)
        abrirScanButton.setOnClickListener {
            if (qr != null) {

                val permissions = arrayOf(
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
                val allPermissionsGranted = permissions.all {
                    ActivityCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
                }
                Log.d("Escanear", "Permisos: $allPermissionsGranted")
                Log.d("Escanear", "QR: ${qr.toString()}")
                if (!allPermissionsGranted) {
                    requestPermissions(permissions, PERMISSIONS_REQUEST_CODE)
                }else {
                        val intent = Intent(requireContext(), MainActivity::class.java).apply {
                            putExtra("qr", qr)
                        }
                        startActivity(intent)
                        barcodeView.pause()
                        parentFragmentManager.popBackStack()
                        activity?.finish()
                }
            } else {
                Toast.makeText(requireContext(), "No se pudo escanear", Toast.LENGTH_SHORT).show()
            }

            val intentIntegrator = IntentIntegrator.forSupportFragment(this)
            intentIntegrator.setOrientationLocked(false)
            intentIntegrator.setPrompt("")
            intentIntegrator.initiateScan()

            // Iniciar la nueva actividad
            val intent = Intent(requireContext(), VerQR::class.java).apply {
                putExtra("qr", qr)
            }
            startActivity(intent)

            activity?.finish()
        }
}

    private suspend fun getTitulo(url: String): String {
        if (url.isEmpty()) {
            return ""
        }
        if (url === null) {
            return ""
        }
        return withContext(Dispatchers.IO) {
            val doc = Jsoup.connect(url).get()
            val title = doc.title()
            if (title.isNotEmpty()) {
                title
            } else {
                val bodyText = doc.body().text()
                val words = bodyText.split(" ")
                if (words.size >= 2) {
                    words[0] + " " + words[1]
                } else {
                    bodyText
                }
            }
        }
    }

 private suspend fun getLocalizacion(): Location? {
    return withContext(Dispatchers.IO) {
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            val manualLocation = Location("manual")
            manualLocation.latitude = -35.423244
            manualLocation.longitude = -71.648483
            manualLocation
        } else {
            suspendCancellableCoroutine<Location?> { continuation ->
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location ->
                        continuation.resume(location)
                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(exception)
                    }
            }
        }
    }
}

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE -> {
                val allPermissionsGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
                if (!allPermissionsGranted) {
                    Toast.makeText(requireContext(), "Todos los permisos necesarios no fueron concedidos", Toast.LENGTH_SHORT).show()
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }

    override fun onResume() {
        super.onResume()
        barcodeView.resume()
    }

    override fun onPause() {
        super.onPause()
        barcodeView.pause()
    }

    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 0
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Escanear().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}