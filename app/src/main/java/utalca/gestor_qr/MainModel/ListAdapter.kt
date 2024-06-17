package utalca.gestor_qr.MainModel

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import utalca.gestor_qr.R

class ListAdapter(private var myDataset: List<QR>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(qr: QR)
    }

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.item_text)
        val imageView: ImageView = view.findViewById(R.id.item_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lista, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textView.text = myDataset[position].getNombre()
        holder.imageView.setImageBitmap(QR_Generator().generateQRCode(myDataset[position].getUrl(), holder.imageView.context))

        holder.itemView.setOnClickListener {
            listener.onItemClick(myDataset[position])
        }
    }

    override fun getItemCount() = myDataset.size

    fun setList(qrList: List<QR>) {
        myDataset = qrList
        notifyDataSetChanged()
    }
}