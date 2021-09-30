package com.example.top10downloader

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.AsyncTask.execute
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import java.net.URL
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.properties.Delegates


class FeedEntry{
    var title: String=""
    var description:String=""
    var link:String=""
    var image:String=""

    override fun toString(): String {
        return """
           title=$title
            description=$description
            link = $link
            enclosure=$image
        """.trimIndent()
    }
}

class MainActivity : AppCompatActivity() {

    private val TAG ="MainActivity"

private var downloadData:DownloadData?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG,"onCreate called")
        //val downloadData=DownloadData(this,xmlListView)
        downloadUrl("https://www.nasa.gov/rss/dyn/breaking_news.rss")

    }

    private fun downloadUrl(feedUrl:String){
        Log.d(TAG,"downloadingURL starting Async")
        downloadData=DownloadData(this,xmlListView)
        downloadData?.execute(feedUrl)
        Log.d(TAG,"downloadingURL done")


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.feeds_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       val feedUrl:String
       when(item?.itemId){
           R.id.nasa->
               feedUrl="https://www.nasa.gov/rss/dyn/breaking_news.rss"
           R.id.imageoftheday->
               feedUrl="https://www.nasa.gov/rss/dyn/lg_image_of_the_day.rss"
           R.id.sol->
               feedUrl="https://www.nasa.gov/rss/dyn/solar_system.rss"
           else->
               return super.onOptionsItemSelected(item)

       }
        downloadUrl(feedUrl)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        downloadData?.cancel(true)
    }


    companion object{
        //first parameter is type of data in this case its the URL
        //second parameter is progress bar
        //third parameter is output
        private class DownloadData(context: Context,listView: ListView):AsyncTask<String,Void,String>() {
            private val TAG = "DownloadData"

            var propContext: Context by Delegates.notNull()
            var propListView:ListView by Delegates.notNull()

            init {
                propContext = context
                propListView=listView
            }

            override fun onPostExecute(result: String) {
            super.onPostExecute(result)

               // Log.d(TAG,"onPostExecute:parameter is $result")
                val parseApplications=ParseApplications()
                parseApplications.parse(result)

//                val arrayAdapter= ArrayAdapter<FeedEntry>(propContext,R.layout.list_item,parseApplications.applications)
//                propListView.adapter=arrayAdapter

                val feedAdapter= FeedAdapter(propContext,R.layout.list_record,parseApplications.applications)
                propListView.adapter=feedAdapter

            }


            override fun doInBackground(vararg url: String?): String {
                Log.d(TAG,"doInBackground:starts with  ${url[0]}")
                val rssFeed=downloadXML(url[0])
                if(rssFeed.isEmpty())
                {
                    Log.e(TAG,"doInbackground:Error Downloading")
                }
                return rssFeed
            }

            private fun downloadXML(urlPath:String?):String{

                return URL(urlPath).readText()
            }
        }
    }
}
//                val xmlResult = StringBuilder()
//
//                try {
//                    val url = URL(urlPath)
//                    val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
//                    val response = connection.responseCode
//                    Log.d(TAG, "downloadXML: The response code was $response")
//                    Log.d(TAG, "xmlResult: $xmlResult")
//
//
////                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
////                    val inputBuffer = CharArray(500)
////                    var charsRead = 0
////                    while (charsRead >= 0) {
////                        charsRead = reader.read(inputBuffer)
////                        if (charsRead > 0) {
////                            xmlResult.append(String(inputBuffer, 0, charsRead))
////                        }
////                    }
////                    reader.close()
//
//                    val stream = connection.inputStream
//                    stream.buffered().reader().use{ xmlResult.append(it.readText())}
//
//
//                    Log.d(TAG, "Received ${xmlResult.length} bytes")
//                    return xmlResult.toString()
//
//
//
////--------------------replacing Catch block with Kotlin Code---------------------------------------------/////////////
////                } catch(e:MalformedURLException){//the order of the catch block is important
////                    Log.e(TAG,"downloadXML: Invalid URL ${e.message}")
////                } catch (e:IOException){
////                    Log.e(TAG,"downloadXML: IO Exeption reading Data: ${e.message}")
////                } catch(e:SecurityException){
////                    e.printStackTrace()
////                    Log.e(TAG,"Need Internet Permission")
////                } catch (e:Exception){
////                   Log.e(TAG,"unknown:${e.message}")
////                }
//
//                }catch (e:Exception){
//                    val errorMessage:String = when(e) {
//                        is MalformedURLException -> "downloadXML:Invalid URL ${e.message}"
//                        is IOException -> "downloadXML:IOException reading data: ${e.message}"
//                        is SecurityException -> {
//                            e.printStackTrace()
//                            "downloadXML: Security Exception. Needs permission? ${e.message}"
//                        }
//
//                        else -> "Unknown error: ${e.message}"
//                    }
//                }
//                return "" //if it gets to here, theres a proble, return empty string
