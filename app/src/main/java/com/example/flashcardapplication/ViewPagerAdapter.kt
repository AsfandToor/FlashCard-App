package com.example.flashcardapplication

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class ViewPagerAdapter(val list:MutableList<FlashCards>) : RecyclerView.Adapter<ViewPagerAdapter.ViewPagerHolder>() {
    var db = FirebaseDatabase.getInstance()

    class ViewPagerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val question = itemView.findViewById<TextView>(R.id.front_text)
        val answer = itemView.findViewById<TextView>(R.id.back_text)
        val deleteBtnFront = itemView.findViewById<ImageButton>(R.id.front_btn)
        val deleteBtnBack = itemView.findViewById<ImageButton>(R.id.back_btn)
        val front_card = itemView.findViewById<CardView>(R.id.front_card)
        val back_card = itemView.findViewById<CardView>(R.id.back_card)
        val front_anim = AnimatorInflater.loadAnimator(itemView.context, R.animator.front_animation) as AnimatorSet
        val back_anim = AnimatorInflater.loadAnimator(itemView.context, R.animator.back_animation) as AnimatorSet
        val scale:Float = itemView.context.resources.displayMetrics.density
        var isFront = true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return ViewPagerHolder(view)
    }
    var holderprev:ViewPagerAdapter.ViewPagerHolder?=null
    override fun onBindViewHolder(holder: ViewPagerHolder, position: Int) {
        // Setting Data
        holder.question.setText(list[position].question)
        holder.answer.setText(list[position].answer)

        // Creating Rotating Effect
        holder.front_card.cameraDistance = 8000 * holder.scale
        holder.back_card.cameraDistance = 8000 * holder.scale

        if(holderprev!=null && !holderprev!!.isFront) {
            holderprev!!.front_anim.setTarget(holderprev!!.back_card)
            holderprev!!.back_anim.setTarget(holderprev!!.front_card)
            holderprev!!.front_anim.start()
            holderprev!!.back_anim.start()
            holderprev!!.isFront = true
        }

        holder.itemView.setOnClickListener {
            if (holder.isFront) {
                holder.front_anim.setTarget(holder.front_card)
                holder.back_anim.setTarget(holder.back_card)
                holder.front_anim.start()
                holder.back_anim.start()
                holder.isFront = false
                holderprev=holder
            }
            else {
                holder.front_anim.setTarget(holder.back_card)
                holder.back_anim.setTarget(holder.front_card)
                holder.front_anim.start()
                holder.back_anim.start()
                holder.isFront = true
            }
        }

        // Deleting Flash Cards
        val ref = db.getReference("FlashCards")

        holder.deleteBtnBack.setOnClickListener {
            ref.child(list[position].id).removeValue()
        }

        holder.deleteBtnFront.setOnClickListener {
            ref.child(list[position].id).removeValue()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}