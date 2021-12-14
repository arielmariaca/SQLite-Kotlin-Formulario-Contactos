package cl.mariaca.sqlitekotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var edName: EditText
    private lateinit var edEmail: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnView: Button
    private lateinit var btnUpdate: Button
    private lateinit var helper: SQLiteHelper

    //Declarar recyclerView y el adapter
    private lateinit var recyclerview: RecyclerView
    private var adapter: ContactosAdapter?=null

    //Variable para realizar el UPDATE
    private var std:ContactosModelo?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initRecyclerview()
        helper = SQLiteHelper(this)

        //Agregar las acciones de los botones
        btnAdd.setOnClickListener {
            addContacto()
        }
        btnView.setOnClickListener {
            verContactos()
        }
        btnUpdate.setOnClickListener {
            modificarContacto()
        }

        //Click en el cada item del recyclerView
        adapter?.setOnClickItem {
            //Toast para ver si puedo recoger el nombre del registro.
            //Toast.makeText(this,it.name,Toast.LENGTH_SHORT).show()
            edName.setText(it.name)
            edEmail.setText(it.email)
            std = it
        }

        adapter?.setOnClickDeleteItem {
            borrarContacto(it.id)
        }

    }

    //Funcion para hacer el update del registro
    private fun modificarContacto() {
        val name = edName.text.toString()
        val email = edEmail.text.toString()

        if (name == std?.name && email == std?.email ){
            Toast.makeText(this, "No hay cambios", Toast.LENGTH_SHORT).show()
            return
        }
        if (std == null) return

        val std = ContactosModelo(id = std!!.id, name = name, email = email)
        val status = helper.updateContacto(std)
        if (status > -1){
            limpiarFormulario()
            verContactos()
        }else{
            Toast.makeText(this,"No se pudo actualizar", Toast.LENGTH_SHORT).show()
        }
    }

    //Fucion para eliminar el contacto
    private fun borrarContacto(id: Int){
        if (id == null) return

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Seguro que desea elimnar?")
        builder.setCancelable(true)
        builder.setPositiveButton("Si"){dialog, _ ->
                helper.deleteContacto(id)
                verContactos()
                dialog.dismiss()
        }
        builder.setNegativeButton("No"){dialog, _ ->
                dialog.dismiss()
        }
        var alert = builder.create()
        alert.show()
    }

    //Funcion para listar los contactos.
    private fun verContactos() {
        val listaContactos = helper.listarContactos()
        //Permite verificar los datos antes de listar
        //Log.e("****Cantidad Datos:", "${listaContactos.size}")
        adapter?.additems(listaContactos)
    }

    //Funcion para agregar un contacto.
    private fun addContacto() {
        val name = edName.text.toString()
        val email = edEmail.text.toString()

        if(name.isEmpty() || email.isEmpty()){
            Toast.makeText(this,"Ingrese Datos",Toast.LENGTH_SHORT).show()
        }else{
            val data = ContactosModelo(name = name, email = email)
            val status = helper.agregarContacto(data)
            //Verificar si se agrego bien los datos
            if(status > -1){
                Toast.makeText(this, "Contacto Agregado", Toast.LENGTH_SHORT).show()
                limpiarFormulario()
                verContactos()
            } else{
                Toast.makeText(this, "Fallo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Funcion para usar el adaptador para armar el recyclerview
    private fun initRecyclerview(){
        recyclerview.layoutManager = LinearLayoutManager(this)
        adapter = ContactosAdapter()
        recyclerview.adapter = adapter
    }

    private fun limpiarFormulario() {
        edName.setText("")
        edEmail.setText("")
        edName.requestFocus()
    }

    //funcion para reconocer los elementos del Layout y cargarlos en variables.
    fun initView(){
        edName = findViewById(R.id.nombre)
        edEmail = findViewById(R.id.correo)
        btnAdd = findViewById(R.id.btn_agregar)
        btnView = findViewById(R.id.btn_ver)
        btnUpdate = findViewById(R.id.btn_actualizar)
        recyclerview = findViewById(R.id.recyclerView)
    }
}