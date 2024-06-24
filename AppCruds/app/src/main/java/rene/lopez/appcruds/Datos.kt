package rene.lopez.appcruds

import Modelo.claseConexion
import Modelo.dataclassTickets
import RecyclerViewHelpers.Adaptador
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class Datos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_datos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Mando a llamar todos los elementos en pantalla
        val txtTitulo = findViewById<EditText>(R.id.txtTitulo)
        val txtDescripcion = findViewById<EditText>(R.id.txtDescripcion)
        val txtResponsable = findViewById<EditText>(R.id.txtResponsable)
        val txtEmail = findViewById<EditText>(R.id.txtEmail)
        val txtTelefono = findViewById<EditText>(R.id.txtTelefono)
        val txtUbicacion = findViewById<EditText>(R.id.txtUbicacion)
        val btnCrearTicket = findViewById<Button>(R.id.btnCrearTicket)
        val rcvTicket = findViewById<RecyclerView>(R.id.rcvTicket)

        //Programo el boton
        btnCrearTicket.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val objConexion = claseConexion().cadenaConexion()

                val CrearTicket = objConexion?.prepareStatement("insert into tablaticket (ticketnum, tituloticket, descripcion, responsable, emailautor, telefonoautor,  ubicacion, estado) Values (?,?,?,?,?,?,?,?)")!!
                CrearTicket.setString(1, UUID.randomUUID().toString())
                CrearTicket.setString(2, txtTitulo.text.toString())
                CrearTicket.setString(3, txtDescripcion.text.toString())
                CrearTicket.setString(4, txtResponsable.text.toString())
                CrearTicket.setString(5, txtEmail.text.toString())
                CrearTicket.setString(6, txtTelefono.text.toString())
                CrearTicket.setString(7, txtUbicacion.text.toString())
                CrearTicket.setString(8, "Activo")
                CrearTicket.executeUpdate()
            }
            rcvTicket.layoutManager = LinearLayoutManager(this)

            fun obtenerDatos():List<dataclassTickets>{
            val objConexion = claseConexion().cadenaConexion()

                val statement= objConexion?.createStatement()
                val resultSet= statement?.executeQuery("select * from tablaticket")!!
                val tickets = mutableListOf<dataclassTickets>()

                while (resultSet.next()) {
                    val numeroTicket = resultSet.getString("NumeroTicket")
                    val tituloTicket = resultSet.getString("TituloTicket")
                    val descripcion = resultSet.getString("Descripcion")
                    val responsable = resultSet.getString("Responsable")
                    val emailAutor = resultSet.getString("EmailAutor")
                    val telefonoAutor = resultSet.getString("TelefonoAutor")
                    val ubicacion = resultSet.getString("Ubicacion")
                    val estado = resultSet.getString("Estado")

                    val ticket = dataclassTickets(
                        numeroTicket,
                        tituloTicket,
                        descripcion,
                        responsable,
                        emailAutor,
                        telefonoAutor,
                        ubicacion,
                        estado
                    )
                    tickets.add(ticket)
                }
                    return tickets
                }
            CoroutineScope(Dispatchers.IO).launch {
                val bdTickets = obtenerDatos()
                withContext(Dispatchers.Main){
                    val adapter = Adaptador(bdTickets)
                    rcvTicket.adapter = adapter
                }
            }
            }
        }

    }
