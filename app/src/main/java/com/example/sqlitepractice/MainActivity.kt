package com.example.sqlitepractice


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var editTextProductName: EditText
    private lateinit var editTextWeight: EditText
    private lateinit var editTextPrice: EditText
    private lateinit var buttonSave: Button
    private lateinit var listViewProducts: ListView

    private lateinit var productDatabaseHelper: ProductDatabaseHelper
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        editTextProductName = findViewById(R.id.editTextProductName)
        editTextWeight = findViewById(R.id.editTextWeight)
        editTextPrice = findViewById(R.id.editTextPrice)
        buttonSave = findViewById(R.id.buttonSave)
        listViewProducts = findViewById(R.id.listViewProducts)

        setSupportActionBar(toolbar)

        productDatabaseHelper = ProductDatabaseHelper(this)
        productAdapter = ProductAdapter(this, productDatabaseHelper.getAllProducts())
        listViewProducts.adapter = productAdapter

        buttonSave.setOnClickListener {
            saveProduct()
        }

        listViewProducts.setOnItemClickListener { _, _, position, _ ->
            val product = productAdapter.getItem(position)
            if (product != null) {
                showDeleteDialog(product)
            }
        }
    }

    private fun saveProduct() {
        val name = editTextProductName.text.toString()
        val weight = editTextWeight.text.toString().toDoubleOrNull()
        val price = editTextPrice.text.toString().toDoubleOrNull()

        if (name.isNotBlank() && weight != null && price != null) {
            productDatabaseHelper.addProduct(name, weight, price)
            editTextProductName.text.clear()
            editTextWeight.text.clear()
            editTextPrice.text.clear()
            updateListView()
        }
    }

    private fun updateListView() {
        productAdapter.clear()
        productAdapter.addAll(productDatabaseHelper.getAllProducts())
        productAdapter.notifyDataSetChanged()
    }

    private fun showDeleteDialog(product: Product) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.delete_change))
            .setMessage(getString(R.string.are_you_sure))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                productDatabaseHelper.deleteProduct(product.id)
                updateListView()
            }
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setNeutralButton(getString(R.string.change)) { _, _ ->
                val intent = Intent(this, EditProductActivity::class.java)
                intent.putExtra("id", product.id)
                startActivity(intent)

            }
            .show()
    }

    override fun onResume() {
        super.onResume()
        updateListView()
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