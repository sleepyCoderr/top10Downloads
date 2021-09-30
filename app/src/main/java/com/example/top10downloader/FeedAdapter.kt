package com.example.top10downloader

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.preference.Preference
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.squareup.picasso.Picasso
import org.w3c.dom.Text
import kotlin.coroutines.coroutineContext

class ViewHolder(v:View){
    val titles:TextView = v.findViewById(R.id.headline)
    val descriptions:TextView=v.findViewById(R.id.info)
    val picture:ImageView=v.findViewById(R.id.pic)

    init{
        v.setOnClickListener{
            val openURL = Intent(v.context,MainActivity::class.java)
            openURL.data = Uri.parse("https://www.google.com/")
            //context.startActivity(openURL)
            v.context.startActivity(openURL)
        }
    }

    }




class FeedAdapter(context: Context, private val resource:Int, private val applications:List<FeedEntry>):
    ArrayAdapter<FeedEntry>(context,resource){

    private val TAG = "FeedAdapter"
    private val inflater = LayoutInflater.from(context)




    override fun getCount(): Int {

        Log.d(TAG,"getCount() Called")
        return applications.size
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        Log.d(TAG,"getView() Called")

        val view:View
        val viewHolder:ViewHolder

        if(convertView==null){
            view = inflater.inflate(resource,parent,false)
            viewHolder = ViewHolder(view)
            view.tag=viewHolder
        }else{
            view = convertView
            viewHolder=view.tag as ViewHolder
        }

//        val titles:TextView = view.findViewById(R.id.headline)
//        val descriptions:TextView=view.findViewById(R.id.info)

        val currentApp=applications[position]

        viewHolder.titles.text=currentApp.title
        viewHolder.descriptions.text=currentApp.description

        Picasso.get().load(currentApp.image).fit().into(viewHolder.picture)
        return view
    }

}