package utalca.gestor_qr.MainViews

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import utalca.gestor_qr.MainModel.QR
import utalca.gestor_qr.MainModel.QR_Generator
import utalca.gestor_qr.R
class VistaQr : Fragment() {
    private var qr: QR? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            qr = it.getSerializable("qr") as QR?
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_vista_qr, container, false)

        // Find the ImageView within the inflated layout
        val imageView = view.findViewById<ImageView>(R.id.QRView)

        imageView.setImageBitmap(QR_Generator().generateQRCode(qr?.getUrl(), this.requireContext()))

        val pos_label = view.findViewById<TextView>(R.id.pos_label)
        val latitude = qr?.getLatitude()
        val longitude = qr?.getLongitude()

        // Formatea la URL de Google Maps con la latitud y la longitud
        val mapUrl = "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude"

        // Crea un SpannableString con el texto "Abrir Mapa"
        val spannableString = SpannableString("Abrir mapa")

        // Crea un ClickableSpan que abre la URL de Google Maps cuando se hace clic
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mapUrl))
                startActivity(intent)
            }
        }

        // Aplica el ClickableSpan al SpannableString
        spannableString.setSpan(clickableSpan, 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Establece el texto del TextView al SpannableString
        pos_label.text = spannableString

        pos_label.setLinkTextColor(ContextCompat.getColor(requireContext(), R.color.dark))

        // Hacer que la URL sea clickeable
        pos_label.movementMethod = android.text.method.LinkMovementMethod.getInstance()

        val url_label = view.findViewById<TextView>(R.id.url_label)
        url_label.text = qr?.getUrl()

        url_label.movementMethod = android.text.method.LinkMovementMethod.getInstance()
        url_label.setLinkTextColor(ContextCompat.getColor(requireContext(), R.color.dark))

        android.text.util.Linkify.addLinks(url_label, android.text.util.Linkify.WEB_URLS)


        // Return the inflated layout
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(qr: QR) =
            VistaQr().apply {
                arguments = Bundle().apply {
                    putSerializable("qr", qr)
                }
            }
    }
}