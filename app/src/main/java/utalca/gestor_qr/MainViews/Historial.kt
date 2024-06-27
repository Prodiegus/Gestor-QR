package utalca.gestor_qr.MainViews

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import utalca.gestor_qr.MainModel.ListAdapter
import utalca.gestor_qr.MainModel.QR
import utalca.gestor_qr.MainModel.Serializador
import utalca.gestor_qr.R
import utalca.gestor_qr.VerQR

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Historial.newInstance] factory method to
 * create an instance of this fragment.
 */
class Historial : Fragment(), ListAdapter.OnItemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_historial, container, false)

        var myDataset = Serializador(requireContext()).cargarQR()

        val recyclerView = view.findViewById<RecyclerView>(R.id.lista_historial)
        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout)
        val searchView = view.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.search_view)

        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = ListAdapter(myDataset, this)
        recyclerView.adapter = adapter

        searchView.isHintEnabled = false
        val editText = searchView.editText
        editText?.clearFocus()
        swipeRefreshLayout.setOnRefreshListener {
            myDataset = Serializador(requireContext()).cargarQR()
            adapter.setList(myDataset)
            swipeRefreshLayout.isRefreshing = false
        }
        editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do something before text changes
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val filteredModelList = filter(myDataset, s.toString())
                adapter.setList(filteredModelList)
            }

            override fun afterTextChanged(s: Editable) {
                // Do something after text changes
            }
        })

        val filterButton = view.findViewById<ImageButton>(R.id.search_icon)
        filterButton.setOnClickListener {
            showPopup(view)
        }

        return view
    }

    private fun showPopup(anchorView: View) {
        val popupView = layoutInflater.inflate(R.layout.popup_filter, null)
        val popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)

        val sincronizarButton = popupView.findViewById<Button>(R.id.sincronizar_drive)
        sincronizarButton.setOnClickListener {
            openGoogleDrive()
            popupWindow.dismiss()
        }

        val cancelarButton = popupView.findViewById<Button>(R.id.cancelar_button)
        cancelarButton.setOnClickListener {
            popupWindow.dismiss()
        }

        val aplicarButton = popupView.findViewById<Button>(R.id.aplicar_button)
        aplicarButton.setOnClickListener {
            // Aquí puedes manejar la lógica para aplicar los filtros
            popupWindow.dismiss()
        }

        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0)
    }

    private fun openGoogleDrive() {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://drive.google.com")
            setPackage("com.google.android.apps.docs")
        }
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        } else {
            // Manejar el caso en que Google Drive no esté instalado
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com"))
            startActivity(webIntent)
        }
    }

    private fun filter(models: List<QR>, query: String?): List<QR> {
        val lowerCaseQuery = query?.toLowerCase()

        val filteredModelList = ArrayList<QR>()
        for (model in models) {
            val text = model.getNombre()!!.toLowerCase()
            if (lowerCaseQuery != null && text.contains(lowerCaseQuery)) {
                filteredModelList.add(model)
            }
        }
        return filteredModelList
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Historial.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Historial().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onItemClick(qr: QR) {
        val intent = Intent(requireContext(), VerQR::class.java).apply {
            putExtra("qr", qr)
        }
        startActivity(intent)
        parentFragmentManager.popBackStack()
    }
}
