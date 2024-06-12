package utalca.gestor_qr.MainViews

import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import utalca.gestor_qr.MainModel.QR
import utalca.gestor_qr.R
class VistaQr : Fragment() {
    private var qr: QR? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            qr = it.getSerializable("qr") as QR?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_vista_qr, container, false)

        // Find the ImageView within the inflated layout
        val imageView = view.findViewById<ImageView>(R.id.QRView)

        // Get the drawable and color
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.qrcode_www_figma_com__1_)
        val color = ContextCompat.getColor(requireContext(), R.color.primary)

        // Modify the drawable color and set it to the ImageView
        drawable?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        imageView.setImageDrawable(drawable)

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