package rene.lopez.appcruds

import Modelo.claseConexion
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Text

class Registro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Mando a llamar a todos los elementos en pantalla
        val txtUsuario = findViewById<EditText>(R.id.txtUsuario)
        val txtContrasena = findViewById<EditText>(R.id.txtContrasena)
        val btnRegistro= findViewById<Button>(R.id.btnRegistro)

        //Creo la funcion de registro
        btnRegistro.setOnClickListener {
            val Login = Intent (this, MainActivity::class.java)
            GlobalScope.launch(Dispatchers.IO) {
                val objConexion= claseConexion().cadenaConexion()

                val RegistrarUsuario = objConexion?.prepareStatement("Insert into usuario (usuario, contrasena) VALUES (?,?)")!!
                RegistrarUsuario.setString(1, txtUsuario.text.toString())
                RegistrarUsuario.setString(2, txtContrasena.text.toString())
                RegistrarUsuario.executeUpdate()
                withContext(Dispatchers.Main){
                    Toast.makeText(this@Registro, "Usuario creado", Toast.LENGTH_SHORT).show()
                    startActivity(Login)
                }

            }


        }
    }
}

