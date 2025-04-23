package com.example.pillcare_capstone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.pillcare_capstone.R
import com.example.pillcare_capstone.data_class.GuardianMemo
import com.google.android.material.textfield.TextInputLayout


//SignUpActivityTwo.kt에서 사용하는 리사이클러뷰 어댑터
class GuardianMemoAdapter(
    var guardianMemoList: MutableList<GuardianMemo>,
    private var inflater: LayoutInflater
) : RecyclerView.Adapter<RecyclerView.ViewHolder> (){

    private inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val guardianMemoItemLayout : TextInputLayout
        val guardianMemoItemEditText : EditText

        init {
            guardianMemoItemLayout = itemView.findViewById(R.id.guardianMemoItemLayout)
            guardianMemoItemEditText = itemView.findViewById(R.id.guardianMemoItemEditText)

            itemView.setOnClickListener {
                //item을 클릭햇다는걸 캐치는 되는데  몇번째 아이템인지 알 수 없다.
                // 이럴때 RecyclerView에서 가지고 있는 adapterPosition변수를 사용한다.
                val position: Int = adapterPosition
                val guardianMemo = guardianMemoList.get(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = inflater.inflate(R.layout.guardian_memo_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ViewHolder
        val memo = guardianMemoList[position]
        viewHolder.guardianMemoItemEditText.setText(memo.content)

    }

    override fun getItemCount(): Int {
        return guardianMemoList.size

    }

    fun addItem() {
        guardianMemoList.add(GuardianMemo())
        notifyItemInserted(guardianMemoList.size - 1)
    }

    fun getMemoList(): List<GuardianMemo> {
        return guardianMemoList
    }
}

