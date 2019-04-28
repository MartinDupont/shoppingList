package sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.View
import android.widget.EditText

actual class Sample {
    actual fun checkMe() = 44
}

actual object Platform {
    actual val name: String = "Android"
}

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    // private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var editText: EditText

    private val shoppingList = mutableListOf("an item", "another item")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hello()
        Sample().checkMe()
        setContentView(R.layout.activity_main)

        val viewManager = LinearLayoutManager(this)
        viewAdapter = MyAdapter(shoppingList)

        viewManager.stackFromEnd = true     // items gravity sticks to bottom
        viewManager.reverseLayout = false   // item list sorting(new messages at bottom)

        recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify a viewAdapter (see also next example)
            adapter = viewAdapter


        }

        editText = findViewById(R.id.editText)

        editText.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            val text = editText.text.toString()
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP && text != "") {

                shoppingList.add(text)
                editText.text.clear()
                viewAdapter = MyAdapter(shoppingList)
                recyclerView.adapter = viewAdapter


                return@OnKeyListener true
            }
            false
        })
    }
}
