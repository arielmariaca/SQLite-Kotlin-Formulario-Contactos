package cl.mariaca.sqlitekotlin

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactosAdapter:RecyclerView.Adapter<ContactosAdapter.ContactoViewHolder>() {

    private var contactosLista: ArrayList<ContactosModelo> = ArrayList()
    private var onClickItem: ((ContactosModelo)->Unit)?=null
    private var onClickDeleteItem: ((ContactosModelo)->Unit)?=null

    fun additems (items: ArrayList<ContactosModelo>){
        this.contactosLista = items
        notifyDataSetChanged()
    }

    fun setOnClickItem(callback: (ContactosModelo)->Unit){
        this.onClickItem = callback
    }

    fun setOnClickDeleteItem (callback: (ContactosModelo)->Unit){
        this.onClickDeleteItem = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ContactoViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.card_contactos, parent, false)
    )

    override fun onBindViewHolder(holder: ContactoViewHolder, position: Int) {
        val contacto = contactosLista[position]
        holder.bindView(contacto)
        holder.itemView.setOnClickListener {
            onClickItem?.invoke(contacto)
        }
        holder.btnBorrar.setOnClickListener {
            onClickDeleteItem?.invoke(contacto)
        }
    }

    override fun getItemCount(): Int {
        return contactosLista.size
    }

    inner class ContactoViewHolder(var view: View): RecyclerView.ViewHolder(view){
        private var id = view.findViewById<TextView>(R.id.tvId)
        private var name = view.findViewById<TextView>(R.id.tvName)
        private var email = view.findViewById<TextView>(R.id.tvEmail)
        var btnBorrar = view.findViewById<Button>(R.id.btn_borrar)

        fun bindView(std: ContactosModelo){
            id.text = std.id.toString()
            name.text = std.name
            email.text = std.email
        }
    }
}