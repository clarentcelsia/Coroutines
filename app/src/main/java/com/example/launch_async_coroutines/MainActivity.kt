package com.example.launch_async_coroutines

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var btnDownload: Button
    private lateinit var btnAsyncDownload: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnDownload = findViewById(R.id.btnDownload)
        btnDownload.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.progress)
            dialog.show()
            CoroutineScope(Dispatchers.Main).launch {
                val currentMillis = System.currentTimeMillis()
                val task1 = downloadTask1()
                val task2 = downloadTask2()
                Toast.makeText(
                    this@MainActivity.applicationContext,
                    "Completed! $task1, $task2 in ${(System.currentTimeMillis() - currentMillis) / 1000} seconds",
                    Toast.LENGTH_SHORT
                ).show()
                dialog.dismiss()
            }

        }

        btnAsyncDownload = findViewById(R.id.btnAsyncDownload)
        btnAsyncDownload.setOnClickListener {
            val mDialog = Dialog(this)
            mDialog.setContentView(R.layout.progress)
            mDialog.show()
            CoroutineScope(Dispatchers.Main).launch {
                val currentMillis = System.currentTimeMillis()
                val task1 = async { downloadTask1() }
                val task2 = async { downloadTask2() }
                Toast.makeText(
                    this@MainActivity.applicationContext,
                    "Completed! ${task1.await()}, ${task2.await()} in ${(System.currentTimeMillis() - currentMillis) / 1000} seconds",
                    Toast.LENGTH_SHORT
                ).show()
                mDialog.dismiss()
            }
        }

    }

    //this is a sample code
    private suspend fun tryStartCoroutineByAsyncWithinSuspendFunct(){
        coroutineScope {
            val task1 = async { downloadTask1() }
            val task2 = async { downloadTask2() }
        }
    }

    // all coroutines should be called only from a coroutines or another suspend function
    private suspend fun downloadTask1(): String{
        delay(2000)             // delay is a part of coroutines
        return "task1 completed"        // will be printed after delay
    }

    private suspend fun downloadTask2(): String{
        delay(3000)
        return "task2 completed"
    }


}

/* Conclusion
    without async:
        launch akan menjalankannya secara berurutan (sequentially)

    with async/await:
        berjalan bersamaan (concurrently)

 */