package com.petarpetrevski.mobilnidomasna2

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerRecnik : RecyclerView
    private lateinit var inputPrebaruvaj : EditText
    private lateinit var inputMakedonski : EditText
    private lateinit var inputAngliski : EditText
    private lateinit var btnDodajZbor : Button
    private lateinit var btnIzbrisiFiltri : Button
    private lateinit var recnik : ArrayList<Zbor>
    private lateinit var recnikAdapter : RecnikAdapter

    fun popolniPodatociEdit(mkZbor: String, enZbor: String) {
        this.inputMakedonski.setText(mkZbor)
        this.inputAngliski.setText(enZbor)
        this.btnDodajZbor.setText("Зачувај Промени")
        this.btnDodajZbor.setOnClickListener{
            this.izmeniZbor(mkZbor, enZbor)
        }
    }

    fun izmeniZbor(mkZbor: String, enZbor: String) {
        if (mkZbor.isEmpty()) {
            this.inputMakedonski.setError("Полето е празно!")
        } else if (enZbor.isEmpty()) {
            this.inputAngliski.setError("Полето е празно!")
        } else {
            val path = filesDir
            val letDirectory = File(path, "LET")
            letDirectory.mkdirs()
            val file = File(letDirectory, "recnik.txt")
            val tempFile = File(letDirectory, "tempfile.txt")
            val regex = Regex(mkZbor + "\t" + enZbor)

            tempFile.printWriter().use { writer ->
                file.forEachLine { line ->
                    writer.println(
                        when {
                            regex.matches(line) -> this.inputMakedonski.text.toString() + "\t" + this.inputAngliski.text.toString()
                            else -> line
                        }
                    )
                }
            }

            check(file.delete() && tempFile.renameTo(file)) { "failed to replace file" }
            this.resetInputs()
            this.inputPrebaruvaj.setText("")
            this.citajNanovoRecnik()

            Toast.makeText(applicationContext, "Зборот е променет успешно!", Toast.LENGTH_SHORT).show()
        }
    }

    fun resetInputs() {
        this.inputMakedonski.setText("")
        this.inputAngliski.setText("")
        this.btnDodajZbor.setText("Додај нов збор")
        this.btnDodajZbor.setOnClickListener{
            dodajNovZbor()
        }
    }

    fun dodajNovZbor() {
        val mkZbor = this.inputMakedonski.text.toString()
        val enZbor = this.inputAngliski.text.toString()

        if (mkZbor.isEmpty()) {
            this.inputMakedonski.setError("Полето е празно!")
        } else if (enZbor.isEmpty()) {
            this.inputAngliski.setError("Полето е празно!")
        } else {
            val path = filesDir
            val letDirectory = File(path, "LET")
            letDirectory.mkdirs()
            val file = File(letDirectory, "recnik.txt")

            file.appendText(mkZbor + "\t" + enZbor + "\n")
            val zborNov = Zbor(mkZbor, enZbor)
            recnik.add(zborNov)
            this.inputMakedonski.setText("")
            this.inputAngliski.setText("")
            this.inputMakedonski.clearFocus()
            this.inputAngliski.clearFocus()
            this.inputPrebaruvaj.setText("")
            this.recnikAdapter.notifyDataSetChanged()

            Toast.makeText(applicationContext, "Зборот е додаден успешно!", Toast.LENGTH_SHORT).show()
        }
    }

    fun citajNanovoRecnik() {
        val path = filesDir
        val letDirectory = File(path, "LET")
        val file = File(letDirectory, "recnik.txt")

        recnik.clear()

        file.forEachLine {
            val line = it.split("\t")
            val zborLinija = Zbor(line[0], line[1])
            recnik.add(zborLinija)
        }

        recnikAdapter.notifyDataSetChanged()
    }

    fun barajZbor(barajVrednost : CharSequence) {
        val path = filesDir
        val letDirectory = File(path, "LET")
        val file = File(letDirectory, "recnik.txt")

        recnik.clear()

        file.forEachLine {
            val line = it.split("\t")
            val zborLinija = Zbor(line[0], line[1])

            if (line[0].contains(barajVrednost)) {
                recnik.add(zborLinija)
            } else {
            }
        }

        this.recnikAdapter.notifyDataSetChanged()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputMakedonski = findViewById(R.id.inputMakedonski)
        inputAngliski = findViewById(R.id.inputAngliski)
        btnDodajZbor = findViewById(R.id.btnDodajZbor)
        btnIzbrisiFiltri = findViewById(R.id.btnIzbrisiFiltri)
        inputPrebaruvaj = findViewById(R.id.inputPrebaruvaj)
        recyclerRecnik = findViewById(R.id.recyclerRecnik)
        recnik = ArrayList()
        recnikAdapter = RecnikAdapter(this, recnik)
        recyclerRecnik.layoutManager = LinearLayoutManager(this)
        recyclerRecnik.adapter = recnikAdapter
        val path = filesDir
        val letDirectory = File(path, "LET")
        letDirectory.mkdirs()
        val file = File(letDirectory, "recnik.txt")

        file.createNewFile()
        this.citajNanovoRecnik()

        btnDodajZbor.setOnClickListener {
            this.dodajNovZbor()
        }

        btnIzbrisiFiltri.setOnClickListener {
            this.inputPrebaruvaj.setText("")
        }

        inputPrebaruvaj.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                barajZbor(s)
            }
        })
    }
}
