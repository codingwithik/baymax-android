package com.francisumeanozie.baymax.utils.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.francisumeanozie.baymax.R
import com.francisumeanozie.baymax.models.Quote

class RateAdapter: RecyclerView.Adapter<RateAdapter.RateViewHolder>() {

    private var quoteList = listOf<Quote>()
    private var context: Context? = null

    class RateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val quote = itemView.findViewById(R.id.txt_quote) as TextView
        val amount = itemView.findViewById(R.id.txt_amount) as TextView
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.exchange_list, parent, false)
        context = view.context
        return RateViewHolder(view)
    }

    override fun getItemCount(): Int = quoteList.size

    fun updateQuote(quoteList: List<Quote>) {
        this.quoteList = quoteList
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {

        val item: Quote = quoteList[position]

        holder.quote.text = item.quote
        holder.amount.text = item.amount.toString()

    }
}