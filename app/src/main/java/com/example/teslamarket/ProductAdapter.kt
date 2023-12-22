package com.example.teslamarket
import android.Manifest
import android.content.pm.PackageManager

import android.app.Activity
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class ProductAdapter(private val products: JSONArray) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textProductName: TextView = itemView.findViewById(R.id.textProductName)
        val textProductPrice: TextView = itemView.findViewById(R.id.textProductPrice)
        val textProductDescription: TextView = itemView.findViewById(R.id.textProductDescription)
        val textProductkolvo: TextView = itemView.findViewById(R.id.textProductKolvo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products.getJSONObject(position)

        holder.textProductName.text = "Название: ${product.getString("name")}"
        holder.textProductPrice.text = "Цена: $${product.getDouble("price")}"
        holder.textProductkolvo.text = "Количество: ${product.getString("kolvo")}"
        holder.textProductDescription.text = "Описание: ${product.getString("description")}"
    }

    override fun getItemCount(): Int {
        return products.length()
    }
}