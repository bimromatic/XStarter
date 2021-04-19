package com.wang.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wang.android.launch.XStarter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testButton.setOnClickListener {
            XStarter.isDebug = true
            XStarter.emit(DemoApplication.instance)
        }
    }
}
