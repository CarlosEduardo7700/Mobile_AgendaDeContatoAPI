package com.fiap.agendadecontatoapi

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

class AgendaContatoActivity : Activity() {

    val FIREBASE_URL = "URL_DA_API"

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)

        setContentView(R.layout.agenda_contato_form)

        val cliente = OkHttpClient()

        val nome = findViewById<EditText>(R.id.edt_nome)
        val email = findViewById<EditText>(R.id.edt_email)
        val telefone = findViewById<EditText>(R.id.edt_telefone)
        val botaoGravar = findViewById<Button>(R.id.btn_gravar)
        val botaoPesquisar = findViewById<Button>(R.id.btn_pesquisar)

        botaoGravar.setOnClickListener {
            val contatoJson = """
                {
                    "nome": "${nome.text}",
                    "email": "${email.text}",
                    "telefone": "${telefone.text}"
                }
            """.trimIndent()

            val request = Request.Builder()
                .url("$FIREBASE_URL/contatos.json")
                .post(contatoJson.toRequestBody(
                    "application/json".toMediaType()
                )).build()

            val response = object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        Toast.makeText(
                            this@AgendaContatoActivity,
                            "Contato não gravado!",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    runOnUiThread {
                        Toast.makeText(
                            this@AgendaContatoActivity,
                            "Contato gravado!",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }

            }

            cliente.newCall(request).enqueue(response)

        }

        botaoPesquisar.setOnClickListener {
            val request = Request.Builder()
                .url("$FIREBASE_URL/contatos.json")
                .get()
                .build()

            val response = object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("AGENDA", "ERRO: ${e.message}")
                }
                override fun onResponse(call: Call, response: Response) {
                    Log.i("AGENDA", "Resposta: $response")
                }

            }

            cliente.newCall(request).enqueue(response)
        }

    }

}