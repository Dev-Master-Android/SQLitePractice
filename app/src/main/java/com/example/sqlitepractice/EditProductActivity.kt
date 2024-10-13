package com.example.sqlitepractice

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class EditProductActivity : AppCompatActivity() {
    private lateinit var productDatabaseHelper: ProductDatabaseHelper
    private lateinit var name: EditText
    private lateinit var weight: EditText
    private lateinit var price: EditText
    private lateinit var saveButton: Button
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)
        productDatabaseHelper = ProductDatabaseHelper(this)
        toolbar = findViewById(R.id.toolbarEditProduct)
        name = findViewById(R.id.name)
        weight = findViewById(R.id.weight)
        price = findViewById(R.id.price)
        saveButton = findViewById(R.id.buttonUpdate)
        setSupportActionBar(toolbar)
        val id = intent.getIntExtra("id", -1)
        val product = productDatabaseHelper.getAllProducts().find { it.id == id }
        product?.let {
            name.setText(it.name)
            weight.setText(it.weight.toString())
            price.setText(it.price.toString())
        }

        saveButton.setOnClickListener {
            val updatedName = name.text.toString()
            val updatedWeight = weight.text.toString().toDoubleOrNull()
            val updatedPrice = price.text.toString().toDoubleOrNull()
            if (updatedName.isNotBlank() && updatedWeight != null && updatedPrice != null) {
                productDatabaseHelper.updateProduct(id, updatedName, updatedWeight, updatedPrice)
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_exit -> {
                finishAffinity()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}

