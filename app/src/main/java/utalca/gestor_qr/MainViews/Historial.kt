package utalca.gestor_qr.MainViews

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        val myDataset = Serializador(requireContext()).cargarQR()

        val recyclerView = view.findViewById<RecyclerView>(R.id.lista_historial)
        val searchView = view.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.search_view)

        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = ListAdapter(myDataset, this)
        recyclerView.adapter = adapter

        searchView.isHintEnabled = false
        val editText = searchView.editText
        editText?.clearFocus()
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
        return view
    }

        fun filter(models: List<QR>, query: String?): List<QR> {
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
        // TODO: Rename and change types and number of parameters
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