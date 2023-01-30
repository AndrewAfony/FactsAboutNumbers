package andrewafony.factsaboutnumbers.com.numbers.presentation

import andrewafony.factsaboutnumbers.com.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class NumbersAdapter(private val clickListener: ClickListener) :
    RecyclerView.Adapter<NumbersViewHolder>(), Mapper.Unit<List<NumberUi>> {

    private val list = mutableListOf<NumberUi>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumbersViewHolder {
        return NumbersViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.number_layout, parent, false), clickListener)
    }

    override fun onBindViewHolder(holder: NumbersViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    override fun map(source: List<NumberUi>) {
        val diff = DiffUtilCallback(list, source)
        val result = DiffUtil.calculateDiff(diff)
        list.clear()
        list.addAll(source)
        result.dispatchUpdatesTo(this)
    }
}

class NumbersViewHolder(view: View, private val clickListener: ClickListener) : RecyclerView.ViewHolder(view) {

    private val title = itemView.findViewById<TextView>(R.id.titleTextView)
    private val subtitle = itemView.findViewById<TextView>(R.id.subtitleTextView)

    fun bind(model: NumberUi) {
        model.map(title, subtitle)
        itemView.setOnClickListener {
            clickListener.click(model)
        }
    }
}

interface ClickListener {
    fun click(item: NumberUi)
}

class DiffUtilCallback(
    private val oldList: List<NumberUi>,
    private val newList: List<NumberUi>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].map(newList[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}