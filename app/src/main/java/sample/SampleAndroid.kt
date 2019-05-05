package sample

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import java.io.*

actual class Sample {
    actual fun checkMe() = 44
}

actual object Platform {
    actual val name: String = "Android"
}

fun getInitialList(inputStream: FileInputStream): MutableList<String> {
    try {
        val file = InputStreamReader(inputStream)
        val br = BufferedReader(file)
        var line = br.readLine()
        val all = StringBuilder()
        while (line != null) {
            all.append(line + "\n")
            line = br.readLine()
        }
        br.close()
        file.close()

        val text = all.toString()
        return text.split(",").toMutableList()
    } catch (e: IOException) {
        return mutableListOf()
    }
}

fun saveList(outputStream: FileOutputStream, shoppingList: MutableList<String>) {
    try {
        val file = OutputStreamWriter(outputStream)

        file.write(shoppingList.toString().replace("[", "").replace("]", ""))
        file.flush()
        file.close()
    } catch (e: IOException) {
        println(e.message)
    }
}


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    // private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var editText: EditText

    val filename = "file.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hello()
        Sample().checkMe()
        setContentView(R.layout.activity_main)

        val shoppingList = if (fileList().contains(filename)) {
            getInitialList(openFileInput(filename)!!)
        } else {
            mutableListOf<String>()
        }

        val viewManager = LinearLayoutManager(this)
        val context = this
        viewAdapter = MyAdapter(context, shoppingList)

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
                viewAdapter = MyAdapter(context, shoppingList)
                recyclerView.adapter = viewAdapter
                recyclerView.scrollToPosition(viewAdapter.itemCount-1)

                val output = openFileOutput(filename, Activity.MODE_PRIVATE)
                saveList(output, shoppingList)


                return@OnKeyListener true
            }
            false
        })
    }
}
