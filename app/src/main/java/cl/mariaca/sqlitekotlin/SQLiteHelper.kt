package cl.mariaca.sqlitekotlin

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.lang.Exception

class SQLiteHelper(context:Context):SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "Contactos"
        private const val TABLE_CONTACTOS = "table_contactos"
        private const val ID = "id"
        private const val NAME = "name"
        private const val EMAIL = "email"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE IF NOT EXISTS "+ TABLE_CONTACTOS +"("
                + ID + " INTEGER PRIMARY KEY,"
                + NAME + " TEXT,"
                + EMAIL + " TEXT)")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTOS")
        onCreate(db)
    }

    fun agregarContacto (std: ContactosModelo):Long{
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, std.id)
        contentValues.put(NAME, std.name)
        contentValues.put(EMAIL, std.email)

        val success = db.insert(TABLE_CONTACTOS, null, contentValues)
        db.close()
        return  success
    }

    fun listarContactos ():ArrayList<ContactosModelo>{

        val listaContactos: ArrayList<ContactosModelo> = ArrayList()
        val query = "SELECT * FROM $TABLE_CONTACTOS"

        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(query,null)
        }catch (e: Exception){
            e.printStackTrace()
            db.execSQL(query)
            return ArrayList()
        }

        var id: Int
        var name: String
        var email: String

        if (cursor.moveToFirst()){
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                email = cursor.getString(cursor.getColumnIndexOrThrow("email"))

                val contacto = ContactosModelo(id=id, name=name, email=email)
                listaContactos.add(contacto)
            }while (cursor.moveToNext())
        }
        return listaContactos
    }

    fun updateContacto (std: ContactosModelo): Int{
        val db = this.writableDatabase
        val data = ContentValues()
        data.put(ID, std.id)
        data.put(NAME, std.name)
        data.put(EMAIL, std.email)

        val success = db.update(TABLE_CONTACTOS, data, " id="+std.id, null)
        db.close()
        return success
    }

    fun deleteContacto (id:Int): Int{
        val db = this.writableDatabase
        val data = ContentValues()
        data.put(ID, id)
        val success = db.delete(TABLE_CONTACTOS,"id=$id",null)
        db.close()
        return success
    }
}