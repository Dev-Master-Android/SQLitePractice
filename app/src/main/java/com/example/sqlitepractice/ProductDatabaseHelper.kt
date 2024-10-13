package com.example.sqlitepractice



import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.Cursor

data class Product(val id: Int, val name: String, val weight: Double, val price: Double)


class ProductDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "shopping_cart.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "products"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_WEIGHT = "weight"
        private const val COLUMN_PRICE = "price"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_WEIGHT REAL,
                $COLUMN_PRICE REAL)
        """.trimIndent()
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addProduct(name: String, weight: Double, price: Double) {
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_WEIGHT, weight)
            put(COLUMN_PRICE, price)
        }
        this.writableDatabase.insert(TABLE_NAME, null, values).also {
            this.writableDatabase.close()
        }
    }

    fun updateProduct(id: Int, name: String, weight: Double, price: Double) {
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_WEIGHT, weight)
            put(COLUMN_PRICE, price)
        }
        this.writableDatabase.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(id.toString())).also {
            this.writableDatabase.close()
        }
    }


    fun deleteProduct(id: Int) {
        this.writableDatabase.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString())).also {
            this.writableDatabase.close()
        }
    }

    @SuppressLint("Range")
    fun getAllProducts(): List<Product> {
        val products = mutableListOf<Product>()
        val cursor: Cursor = this.readableDatabase.query(TABLE_NAME, null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                val weight = cursor.getDouble(cursor.getColumnIndex(COLUMN_WEIGHT))
                val price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE))
                products.add(Product(id, name, weight, price))
            } while (cursor.moveToNext())
        }
        cursor.close()
        this.readableDatabase.close()
        return products
    }
}
