package com.example.pillcare_capstone.adapter

import android.text.Editable
import android.text.TextWatcher
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
    private var inflater: LayoutInflater,
    private val contextType: ContextType = ContextType.DEFAULT
) : RecyclerView.Adapter<RecyclerView.ViewHolder> (){


    enum class ContextType {
        DEFAULT, MY_INFO
    }

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

        when (contextType) {
            ContextType.MY_INFO -> {
                viewHolder.guardianMemoItemLayout.setBackgroundResource(R.drawable.gray_inputbox)
            }
            ContextType.DEFAULT -> {
                viewHolder.guardianMemoItemLayout.background = null
            }
        }
        viewHolder.guardianMemoItemEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val currentPosition = holder.adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    guardianMemoList[currentPosition].content = s.toString()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
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

