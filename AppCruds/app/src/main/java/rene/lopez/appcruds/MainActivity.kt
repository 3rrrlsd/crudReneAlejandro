package rene.lopez.appcruds

import Modelo.claseConexion
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        //Mando a llamar a todos los elementos en pantalla
        val txtUser =findViewById<TextView>(R.id.txtUser)
        val txtPassword = findViewById<TextView>(R.id.txtPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)

        btnLogin.setOnClickListener {
            val Datos = Intent(this, Datos::class.java)
            GlobalScope.launch (Dispatchers.IO){
                val objConexion = claseConexion().cadenaConexion()
                val comprobarUsuario =objConexion?.prepareStatement("SELECT * FROM usuario WHERE usuario = ? AND  contrasena = ?")!!
                comprobarUsuario.setString(1, txtUser.text.toString())
                comprobarUsuario.setString(2, txtPassword.text.toString())
                val resultado = comprobarUsuario.executeQuery()
                if (resultado.next()){
                    startActivity(Datos)
                } else{
                    println("Usuario no encontrado, verifique sus credenciales")
                }
            }
        }
        btnRegistrar.setOnClickListener {
            val Registro = Intent(this@MainActivity, Registro::class.java)
            startActivity(Registro)
        }
    }
}