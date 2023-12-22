package com.example.teslamarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject
import javax.xml.transform.Source

class Katolog : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var sourceTextEditText: EditText
    private lateinit var filter: Spinner




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog)


        sourceTextEditText = findViewById(R.id.SourceText)

        filter = findViewById(R.id.filter)
        val words = arrayOf("По увеличению", "По уменьшению")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, words)
        filter.adapter = adapter


        filter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedWord = parent.getItemAtPosition(position).toString()

                if (selectedWord == "По увеличению") {
                    val products = Sord01()
                    productAdapter = ProductAdapter(products)
                    recyclerView.adapter = productAdapter
                } else if (selectedWord == "По уменьшению") {
                    val products = Sord10()
                    productAdapter = ProductAdapter(products)
                    recyclerView.adapter = productAdapter
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }




        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val products = loadProducts()
        productAdapter = ProductAdapter(products)
        recyclerView.adapter = productAdapter

        val Sort01: Button = findViewById(R.id.Btnsort01)
        Sort01.setOnClickListener{
            val products = Sord01()
            productAdapter = ProductAdapter(products)
            recyclerView.adapter = productAdapter
        }
        val Sort10: Button = findViewById(R.id.Btnsort10)
        Sort10.setOnClickListener{
            val products = Sord10()
            productAdapter = ProductAdapter(products)
            recyclerView.adapter = productAdapter
        }

        sourceTextEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                updateProductList(sourceTextEditText.text.toString())
                true
            } else {
                false
            }
        }


    }




    private fun updateProductList(filterText: String) {
        val filteredProductsArray = searchProductsByName(filterText)
        productAdapter = ProductAdapter(filteredProductsArray)
        recyclerView.adapter = productAdapter
    }

    private fun searchProductsByName(name: String): JSONArray {
        val fileName = "data.json"
        val file = getFileStreamPath(fileName)

        return if (file.exists()) {
            val jsonString = file.bufferedReader().use {
                it.readText()
            }

            val jsonObject = JSONObject(jsonString)

            val productsArray = jsonObject.getJSONArray("product")

            val filteredProductsArray = JSONArray()
            for (i in 0 until productsArray.length()) {
                val productObject = productsArray.getJSONObject(i)
                val productName = productObject.getString("name")

                if (productName.startsWith(name, ignoreCase = true)) {
                    filteredProductsArray.put(productObject)
                }
            }

            filteredProductsArray
        } else {
            JSONArray()
        }
    }







    override fun onBackPressed() {
        super.onBackPressed()
        try {
            val intent = Intent(this, Glavn::class.java)
            startActivity(intent)
            finish()

        }
        catch (e: Exception){

        }

    }

    private fun loadProducts(): JSONArray {
        val fileName = "data.json"
        val file = getFileStreamPath(fileName)
        return if (file.exists()) {
            val jsonString = file.bufferedReader().use {
                it.readText()
            }

            val jsonObject = JSONObject(jsonString)
            val productsArray = jsonObject.getJSONArray("product")

            productsArray
        } else {
            JSONArray()
        }
    }



    private fun Sord01(): JSONArray {
        val fileName = "data.json"
        val file = getFileStreamPath(fileName)
        return if (file.exists()) {
            val jsonString = file.bufferedReader().use {
                it.readText()
            }

            val jsonObject = JSONObject(jsonString)

            val productsArray = jsonObject.getJSONArray("product")

            val productList = (0 until productsArray.length()).map {
                val productObject = productsArray.getJSONObject(it)
                val existingPrice = productObject.getString("price")
                val price = existingPrice.toDouble()
                Pair(price, productObject)
            }

            val sortedProductList = productList.sortedBy { it.first }

            val sortedProductsArray = JSONArray()
            sortedProductList.forEach {
                sortedProductsArray.put(it.second)
            }

            sortedProductsArray
        } else {
            JSONArray()
        }
    }
    private fun Sord10(): JSONArray {
        val fileName = "data.json"
        val file = getFileStreamPath(fileName)
        return if (file.exists()) {
            val jsonString = file.bufferedReader().use {
                it.readText()
            }

            val jsonObject = JSONObject(jsonString)

            val productsArray = jsonObject.getJSONArray("product")

            val productList = (0 until productsArray.length()).map {
                val productObject = productsArray.getJSONObject(it)
                val existingPrice = productObject.getString("price")
                val price = existingPrice.toDouble()
                Pair(price, productObject)
            }

            val sortedProductList = productList.sortedByDescending { it.first }

            val sortedProductsArray = JSONArray()
            sortedProductList.forEach {
                sortedProductsArray.put(it.second)
            }

            sortedProductsArray
        } else {
            JSONArray()
        }
    }




}
