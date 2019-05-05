package sample

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView

class MyAdapter(private val context: Context, private val myDataset: MutableList<String>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(val cardView: RelativeLayout) : RecyclerView.ViewHolder(cardView) {
        //private lateinit var deleteButton: Button
        val deleteButton = cardView.findViewById<Button>(R.id.button)

    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyAdapter.MyViewHolder {
        // create a new view
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_text_view, parent, false) as RelativeLayout
        return MyViewHolder(textView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.cardView.findViewById<TextView>(R.id.info_text).text = myDataset[position]


        holder.deleteButton.setOnClickListener {
            val currentPosition = holder.layoutPosition
            myDataset.removeAt(currentPosition)
            notifyItemRemoved(currentPosition)
            val output = context.openFileOutput("file.txt", Activity.MODE_PRIVATE)
            saveList(output, myDataset)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}
