package com.example.teslamarket

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.File

class MainActivity : AppCompatActivity() {
    private var logBox: EditText? = null
    private var passBox: EditText? = null

    // Метод, вызываемый при создании активности
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Установка макета активности из XML-файла ресурсов
        setContentView(R.layout.activity_main)
        // Инициализация переменных EditText
        logBox = findViewById(R.id.logbox)
        passBox = findViewById(R.id.passbox)
        // Инициализация кнопок с использованием их идентификаторов из XML-файла ресурсов

        val start: TextView = findViewById(R.id.RegistrtText)
        start.setOnClickListener {
            try {
                val intent = Intent(this, Registr::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {
            }
        }

        val authButton: TextView = findViewById(R.id.authButton)
        authButton.setOnClickListener {
            try {
                userLogin()
            } catch (e: Exception) {
            }
        }
    }

    // Метод для обработки входа пользователя, вызываемый при нажатии
    fun userLogin() {
        // Проверка наличия данных в полях ввода
        if (logBox == null || passBox == null) {
            // Вывод уведомления Toast о пустых полях
            Toast.makeText(this, "поля пустые", Toast.LENGTH_SHORT).show()
            return
        }
        // Получение введенных логина и пароля
        val login = logBox?.text.toString()
        val pass = passBox?.text.toString()
        // Создание объекта файла для хранения пользовательских данных
        val file = File(filesDir, "users.json")
        // Чтение содержимого файла JSON с пользователями или создание
        val jsonString = if (file.exists()) {
            file.bufferedReader().use {
                it.readText()
            }
        } else {
            "{\"users\":[]}"
        }
        // Создание объекта JSON из строки
        val jsonObject = JSONObject(jsonString)
        // Переменная для отслеживания успешности аутентификации
        var isAuth = false
        // Массив пользователей из JSON-объекта
        val usersArray = jsonObject.getJSONArray("users")
        // Перебор всех пользователей в массиве
        for (i in 0 until usersArray.length()) {
            // Получение JSON-объекта для каждого пользователя
            val userObject = usersArray.getJSONObject(i)
            // Извлечение логина, пароля и ФИО пользователя из JSON-объекта
            val userJSON = userObject.getString("login")
            val passJSON = userObject.getString("pass")
            val fioJSON = userObject.getString("fio")
            val emailJson = userObject.getString("email")

            if (userJSON == login && passJSON == pass) {
                Toast.makeText(this, "$fioJSON вошел успешно", Toast.LENGTH_SHORT).show()
                isAuth = true

                val sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("login", userJSON)
                editor.putString("pass", passJSON)
                editor.putString("fio", fioJSON)
                editor.putString("email", emailJson)
                editor.apply()

                val newPage = Intent(this, Glavn::class.java)
                startActivity(newPage)
                break
            }
        }
        if (!isAuth) {
            Toast.makeText(this, "Скорее всего, данные неверны", Toast.LENGTH_SHORT).show()
        }
    }
}
