package com.example.teslamarket

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.IOException

class Adding : AppCompatActivity() {

    private lateinit var productNameBox: EditText
    private lateinit var productPriceBox: EditText
    private lateinit var productDescriptionBox: EditText
    private lateinit var nameEdit: EditText
    private lateinit var productkolisestvo: EditText
    private var imageUri: Uri? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        productNameBox = findViewById(R.id.productName)
        productPriceBox = findViewById(R.id.productCost)
        productDescriptionBox = findViewById(R.id.productOpinie)
        nameEdit = findViewById(R.id.productNameEdit)
        productkolisestvo = findViewById(R.id.productKolishestvo)



        nameEdit.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                EditProduct()
                true
            } else {
                false
            }
        }
        val addProduct: Button = findViewById(R.id.BtnAddPriduct)
        addProduct.setOnClickListener{
            addProduct()
        }

        val BtnEdit: Button = findViewById(R.id.btnEditPriduct)
        BtnEdit.setOnClickListener{
            SaveEdit()

        }
        val BtnDelete: Button = findViewById(R.id.btnDeletePriduct)
        BtnDelete.setOnClickListener{
            deleteProduct()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        try {
            startActivity(Intent(this, Glavn::class.java))
            finish()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun readJsonFromFile(fileName: String): JSONObject {
        val file = File(filesDir, fileName)
        return if (file.exists()) {
            file.bufferedReader().use { JSONObject(it.readText()) }
        } else {
            JSONObject("{\"product\":[]}")
        }
    }

    private fun writeJsonToFile(fileName: String, jsonObject: JSONObject) {
        try {
            openFileOutput(fileName, Context.MODE_PRIVATE).use {
                it.write(jsonObject.toString().toByteArray())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Ошибка при сохранении продукта", Toast.LENGTH_SHORT).show()
        }
    }


    private fun productExists(productsArray: JSONArray, name: String): Boolean {
        for (i in 0 until productsArray.length()) {
            val existingName = productsArray.getJSONObject(i).getString("name")
            if (existingName == name) {
                Toast.makeText(this, "Продукт с таким именем уже существует", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return false
    }



    private fun EditProduct() {

        val nameEdit = nameEdit.text.toString()


        val fileName = "data.json"
        val jsonObject = readJsonFromFile(fileName)
        val productsArray = jsonObject.getJSONArray("product")

        for (i in 0 until productsArray.length()) {
            val existingName = productsArray.getJSONObject(i).getString("name")
            val existingCost = productsArray.getJSONObject(i).getString("price")
            val existingDescription = productsArray.getJSONObject(i).getString("description")
            val existingkolvo = productsArray.getJSONObject(i).getString("kolvo")
            if (existingName == nameEdit) {
                productNameBox.setText(existingName)
                productPriceBox.setText(existingCost)
                productDescriptionBox.setText(existingDescription)
                productkolisestvo.setText(existingkolvo)


            }



        }

    }
    private fun SaveEdit() {
        val name = productNameBox.text.toString()
        val price = productPriceBox.text.toString().toDoubleOrNull() ?: 0.0
        val description = productDescriptionBox.text.toString()
        val nameEdit = nameEdit.text.toString()
        val kolvo = productkolisestvo.text.toString()

        val fileName = "data.json"
        val jsonObject = readJsonFromFile(fileName)
        val productsArray = jsonObject.getJSONArray("product")

        for (i in 0 until productsArray.length()) {
            val existingProduct = productsArray.getJSONObject(i)
            if (existingProduct.getString("name") == nameEdit) {
                existingProduct.put("name", name)
                existingProduct.put("price", price)
                existingProduct.put("description", description)
                existingProduct.put("kolvo", kolvo)
                break
            }

        }

        writeJsonToFile(fileName, jsonObject)

        Toast.makeText(this, "Продукт $name успешно обновлен", Toast.LENGTH_SHORT).show()
    }
    private fun deleteProduct() {

        val nameEdit = nameEdit.text.toString()

        val fileName = "data.json"
        val jsonObject = readJsonFromFile(fileName)
        val productsArray = jsonObject.getJSONArray("product")

        for (i in 0 until productsArray.length()) {
            val existingProduct = productsArray.getJSONObject(i)
            if (existingProduct.getString("name") == nameEdit) {
                productsArray.remove(i)
                break
            }
        }

        writeJsonToFile(fileName, jsonObject)

        Toast.makeText(this, "Продукт $nameEdit успешно удален", Toast.LENGTH_SHORT).show()
    }


    private fun addProduct() {
        val name = productNameBox.text.toString()
        val price = productPriceBox.text.toString().toDoubleOrNull() ?: 0.0
        val description = productDescriptionBox.text.toString()
        val koliestvo = productkolisestvo.text.toString()

        val fileName = "data.json"
        val jsonObject = readJsonFromFile(fileName)
        val productsArray = jsonObject.getJSONArray("product")

        if (!productExists(productsArray, name)) {
            val newProduct = JSONObject().apply {
                put("name", name)
                put("price", price)
                put("description", description)
                put("image", imageUri.toString())
                put("kolvo", koliestvo)
            }

            productsArray.put(newProduct)
            jsonObject.put("product", productsArray)

            writeJsonToFile(fileName, jsonObject)

            Toast.makeText(this, "Продукт $name успешно добавлен", Toast.LENGTH_SHORT).show()
        }
    }
}
