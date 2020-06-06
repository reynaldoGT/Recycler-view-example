package com.example.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class MainActivity : AppCompatActivity() {
    var lista: RecyclerView? = null
    var adaptador: AdaptadorCustom? = null
    var layout_Manager: RecyclerView.LayoutManager? = null

    //Para el action mode
    var isActionMode = false
    var actionMode:ActionMode?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val platillos = ArrayList<Platillo>()

        platillos.add(Platillo("Platillo", 250.0, 3.5F, R.drawable.img1))
        platillos.add(Platillo("Otro platillo", 250.0, 3.5F, R.drawable.img2))
        platillos.add(Platillo("Platillo 3", 250.0, 3.5F, R.drawable.img3))
        platillos.add(Platillo("Platillo", 250.0, 3.5F, R.drawable.img4))
        platillos.add(Platillo("Platillo", 250.0, 3.5F, R.drawable.img5))
        platillos.add(Platillo("Platillo", 250.0, 3.5F, R.drawable.img6))


        lista = findViewById(R.id.lista)
        lista?.setHasFixedSize(true)

        // para poder ponder las celdas una debajo de la otra
        layout_Manager = LinearLayoutManager(this)
        lista?.layoutManager = layout_Manager

        val callback = object : ActionMode.Callback {
            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {

                when(item?.itemId){
                    R.id.i_eliminar ->{
                        Toast.makeText(applicationContext,"Elementos eliminados",Toast.LENGTH_SHORT).show()
                        adaptador?.eliminarSeccionados()
                    }
                    else ->{
                        return true
                    }
                }

                adaptador?.terminarActionMode()
                mode?.finish()
                isActionMode = false

                return true
            }

            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                //Inializar action mode
                adaptador?.iniciarActionMode()
                actionMode = mode
                // Inflater menu
                // para que aparesca el menu que creamos
                menuInflater.inflate(R.menu.menu_contextual,menu!!)
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
                adaptador?.destruirActionMode()
                isActionMode = false
            }

        }

        adaptador = AdaptadorCustom(platillos, object : ClickListener {
            override fun onclick(vista: View, index: Int) {
                Toast.makeText(applicationContext, platillos[index].nombre, Toast.LENGTH_SHORT)
                    .show()
            }
        }, object : LongClickListener {
            override fun longClik(vista: View, index: Int) {

                if (!isActionMode) {
                    startActionMode(callback)
                    isActionMode = true
                } else {
                    //HAcer las seleccion o deseleciones
                    adaptador?.seleccionarItem(index)
                }
                actionMode?.title = adaptador?.obtenerItemsSeleccionados().toString() +" items seleccionados"
            }

        })
        lista?.adapter = adaptador


        val swipeToRefresh = findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)
        swipeToRefresh.setOnRefreshListener {
            for (i in 1..100000) {

            }
            swipeToRefresh.isRefreshing = false


        }
    }

}