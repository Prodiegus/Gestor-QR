package utalca.gestor_qr.MainViews

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import utalca.gestor_qr.MainModel.QR_Generator
import utalca.gestor_qr.R

class Crear : Fragment() {
    private lateinit var tituloEditText: EditText
    private lateinit var enlaceEditText: EditText
    private lateinit var crearButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crear, container, false)

        tituloEditText = view.findViewById(R.id.titulo_edit_text)
        enlaceEditText = view.findViewById(R.id.enlace_edit_text)
        crearButton = view.findViewById(R.id.crear_button)

        crearButton.setOnClickListener {
            val titulo = tituloEditText.text.toString()
            val enlace = enlaceEditText.text.toString()
            mostrarPrevisualizacion(titulo, enlace)
        }

        return view
    }

    private fun mostrarPrevisualizacion(titulo: String, enlace: String) {
        val builder = AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.dialog_previsualizacion, null)

        val qrImageView = view.findViewById<ImageView>(R.id.qr_image_view)
        val tituloTextView = view.findViewById<TextView>(R.id.titulo_text_view)
        val enlaceTextView = view.findViewById<TextView>(R.id.enlace_text_view)

        val qrBitmap = QR_Generator().generateQRCode(enlace, requireContext())
        qrImageView.setImageBitmap(qrBitmap)
        tituloTextView.text = titulo
        enlaceTextView.text = enlace

        builder.setView(view)
        builder.setPositiveButton("Aceptar", null)
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }
}
