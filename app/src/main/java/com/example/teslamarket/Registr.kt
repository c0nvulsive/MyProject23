package com.example.teslamarket

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.Writer

class Registr : AppCompatActivity() {

    private var loginBox: EditText? = null
    private var passBox: EditText? = null
    private var fioBox: EditText? = null
    private var emailBox: EditText? = null
    // Метод, вызываемый при создании активн

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registr)

        loginBox = findViewById(R.id.loginRegistr)
        passBox = findViewById(R.id.PasswordRegistr)
        fioBox = findViewById(R.id.FioRegistr)
        emailBox = findViewById(R.id.EmailRegistr)


        val RegButton: TextView = findViewById(R.id.RegistrButton)
        RegButton.setOnClickListener{
            try {
                register()

            }
            catch (e: Exception){

            }
        }
    }
    fun isEmailValid(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches() && email.endsWith("@gmail.com")
    }
    fun isValidFio(string: String): Boolean{
        val words = string.split("  ")

        return  words.size == 3
    }

    fun register() {
        val login = loginBox?.text.toString()
        val pass = passBox?.text.toString()
        val fio = fioBox?.text.toString()
        val email = emailBox?.text.toString()

        val file = File(filesDir, "users.json")

        val jsonString = if (file.exists()) {
            file.bufferedReader().use {
                it.readText()
            }
        } else {
            "{\"users\":[]}"
        }

        val jsonObject = JSONObject(jsonString)

        val usersArray = jsonObject.getJSONArray("users")

        for (i in 0 until usersArray.length()) {
            val userObject = usersArray.getJSONObject(i)
            val existingLogin = userObject.getString("login")
            val existingEmail = userObject.getString("email")
            if (existingLogin == login) {
                Toast.makeText(this, "Пользователь с таким логином уже существует", Toast.LENGTH_SHORT).show()

                    return
            }
            if (existingEmail == email){
                Toast.makeText(this, "Пользователь с таким Email уже существует", Toast.LENGTH_SHORT).show()

                return
            }
        }
        if (isEmailValid(email)) {

        } else {
            Toast.makeText(this, "Email должен заканчиваться на @gmail.com", Toast.LENGTH_SHORT).show()

            return
        }
        if (isValidFio(fio)) {

        } else {
            Toast.makeText(this, "Введите ФИО корректно", Toast.LENGTH_SHORT).show()

            return
        }
        if (fio.any { it.isDigit() }) {
            Toast.makeText(this, "ФИО не должно содержать цифры", Toast.LENGTH_SHORT).show()
            return
        }

        // Создание нового JSON-объекта для нового пользователя
        val newUser = JSONObject().apply {
            put("login", login)
            put("pass", pass)
            put("fio", fio)
            put("email", email)
        }
        // Добавление нового пользователя в массив пользователей
        usersArray.put(newUser)
        // Обновление JSON-объекта с массивом пользователей
        jsonObject.put("users", usersArray)
        try {
            // Запись JSON-данных в файл
            val fileOutputStream = openFileOutput("users.json", Context.MODE_PRIVATE)

            fileOutputStream.write(jsonObject.toString().toByteArray())
            fileOutputStream.close()
            // Вывод уведомления Toast об успешной регистрации
            Toast.makeText(this, "Пользователь $fio успешно  зарегистрирован", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            // Обработка ошибок при записи данных в файл
            e.printStackTrace()
            Toast.makeText(this, "Ошибка при сохранении пользователя",
                Toast.LENGTH_SHORT).show()
        }
        try {

            val writer: Writer = BufferedWriter(FileWriter(file))
            writer.write(jsonObject.toString())
            writer.close()
            // Вывод уведомления Toast об успешной регистрации
            Toast.makeText(this, "Пользователь $fio успешно    зарегистрирован", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            // Обработка ошибок при записи данных в файл
            e.printStackTrace()
            Toast.makeText(this, "Ошибка при сохранении пользователя",
                Toast.LENGTH_SHORT).show()
        }
    }



}