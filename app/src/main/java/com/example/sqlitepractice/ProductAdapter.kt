package com.example.sqlitepractice



import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView



class ProductAdapter(context: Context, products: List<Product>) : ArrayAdapter<Product>(context, 0, products) {

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val product = getItem(position)!!
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.product_item, parent, false)

        val textViewName: TextView = view.findViewById(R.id.text1)
        val textViewWeight: TextView = view.findViewById(R.id.text2)
        val textViewPrice: TextView = view.findViewById(R.id.text3)

        textViewName.text = product.name
        textViewWeight.text = "Вес: ${product.weight} кг"
        textViewPrice.text = "Цена: ${product.price} руб."

        return view
    }
}

