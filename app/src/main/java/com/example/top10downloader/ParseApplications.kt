package com.example.top10downloader

import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.lang.Exception

class ParseApplications {
    private val TAG = "ParseApplications"

    val applications = ArrayList<FeedEntry>()

    fun parse(xmlData:String): Boolean{
        Log.d(TAG,"Parse called with $xmlData")
        var status=true
        var inEntry=false
        var textValue=""


        try{
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware=true
            val xpp=factory.newPullParser()
            xpp.setInput(xmlData.reader())
            var eventType=xpp.eventType
            var currentRecord=FeedEntry()
            while(eventType!=XmlPullParser.END_DOCUMENT){
                val tagName=xpp.name?.toLowerCase()
                when (eventType){
                    XmlPullParser.START_TAG->{
                        Log.d(TAG,"parse: Starting tag for "+tagName)
                        if(tagName=="item"){
                            inEntry=true
                        }
                    }

                    XmlPullParser.TEXT->textValue=xpp.text
                    XmlPullParser.END_TAG->{
                        Log.d(TAG,"parse:Ending tag for "+tagName)
                        if (inEntry){
                            when (tagName){
                                "item"->{
                                    applications.add(currentRecord)
                                    inEntry=false
                                    currentRecord= FeedEntry()
                                }


                                "title"->currentRecord.title=textValue
                                "description"->currentRecord.description=textValue
                                "link"->currentRecord.link=textValue
                                "enclosure"->currentRecord.image= xpp.getAttributeValue(null, "url")
                            }
                        }
                    }
                }
                //nothing else to do
                eventType=xpp.next()
            }

            for(app in applications){
                Log.d(TAG,"*****************")
                Log.d(TAG,app.toString())
            }

    }catch(e:Exception){
        e.printStackTrace()
        status=false

        }


        return status
    }


}