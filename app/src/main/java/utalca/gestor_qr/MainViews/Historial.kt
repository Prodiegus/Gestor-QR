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
import android.widget.PopupWindow
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.textfield.TextInputLayout
import utalca.gestor_qr.MainModel.ListAdapter
import utalca.gestor_qr.MainModel.QR
import utalca.gestor_qr.MainModel.Serializador
import utalca.gestor_qr.R
import utalca.gestor_qr.VerQR

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Historial : Fragment(), ListAdapter.OnItemClickListener {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var myDataset: List<QR>
    private lateinit var adapter: ListAdapter

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

        myDataset = Serializador(requireContext()).cargarQR()

        val recyclerView = view.findViewById<RecyclerView>(R.id.lista_historial)
        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout)
        val searchView = view.findViewById<TextInputLayout>(R.id.search_view)

        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = ListAdapter(myDataset, this)
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
            val radioGroup = popupView.findViewById<RadioGroup>(R.id.radio_group)
            val selectedId = radioGroup.checkedRadioButtonId

            val sortBy = when (selectedId) {
                R.id.orden_nombre_ascendente -> "name_asc"
                R.id.orden_nombre_descendente -> "name_desc"
                R.id.orden_fecha_ascendente -> "date_asc"
                R.id.orden_fecha_descendente -> "date_desc"
                else -> ""
            }

            myDataset = sortList(myDataset, sortBy)
            adapter.setList(myDataset)
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
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com"))
            startActivity(webIntent)
        }
    }

    private fun filter(models: List<QR>, query: String?): List<QR> {
        val lowerCaseQuery = query?.toLowerCase()
        val filteredModelList = ArrayList<QR>()
        for (model in models) {
            val text = model.getNombre()?.toLowerCase()
            if (lowerCaseQuery != null && text != null && text.contains(lowerCaseQuery)) {
                filteredModelList.add(model)
            }
        }
        return filteredModelList
    }

    private fun sortList(models: List<QR>, sortBy: String): List<QR> {
        return when (sortBy) {
            "name_asc" -> models.sortedBy { it.getNombre() ?: "" }
            "name_desc" -> models.sortedByDescending { it.getNombre() ?: "" }
            "date_asc" -> models.sortedBy { it.getDate() }
            "date_desc" -> models.sortedByDescending { it.getDate() }
            else -> models
        }
    }

    companion object {
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
