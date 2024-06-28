package utalca.gestor_qr.MainViews

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ClickableSpan
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
        val view = inflater.inflate(R.layout.fragment_vista_qr, container, false)

        val imageView = view.findViewById<ImageView>(R.id.QRView)
        imageView.setImageBitmap(QR_Generator().generateQRCode(qr?.getUrl(), this.requireContext()))

        val pos_label = view.findViewById<TextView>(R.id.pos_label)
        val latitude = qr?.getLatitude()
        val longitude = qr?.getLongitude()
        val mapUrl = "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude"
        val spannableString = SpannableString("Abrir mapa")
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mapUrl))
                startActivity(intent)
            }
        }
        spannableString.setSpan(clickableSpan, 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        pos_label.text = spannableString
        pos_label.setLinkTextColor(ContextCompat.getColor(requireContext(), R.color.dark))
        pos_label.movementMethod = android.text.method.LinkMovementMethod.getInstance()

        val url_label = view.findViewById<TextView>(R.id.url_label)
        url_label.text = qr?.getUrl()
        url_label.movementMethod = android.text.method.LinkMovementMethod.getInstance()
        url_label.setLinkTextColor(ContextCompat.getColor(requireContext(), R.color.dark))
        android.text.util.Linkify.addLinks(url_label, android.text.util.Linkify.WEB_URLS)

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
