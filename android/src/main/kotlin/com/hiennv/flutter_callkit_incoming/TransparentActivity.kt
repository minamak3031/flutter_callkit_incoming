package com.hiennv.flutter_callkit_incoming

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.content.SharedPreferences

class TransparentActivity : Activity() {

    companion object {

        fun getIntentAccept(context: Context, data: Bundle?): Intent {
            val intent = Intent(context, TransparentActivity::class.java)
            intent.putExtra("data", data)
            intent.putExtra("type", "ACCEPT")
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            return intent
        }

        fun getIntentCallback(context: Context, data: Bundle?): Intent {
            val intent = Intent(context, TransparentActivity::class.java)
            intent.putExtra("data", data)
            intent.putExtra("type", "CALLBACK")
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            return intent
        }

    }


    override fun onStart() {
        super.onStart()
        setVisible(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when (intent.getStringExtra("type")) {
            "ACCEPT" -> {
                val data = intent.getBundleExtra("data")
                val map: HashMap<String, Any?> = data?.getSerializable("EXTRA_CALLKIT_EXTRA") as HashMap<String, Any?>
                val prefs: SharedPreferences = getSharedPreferences("FlutterSharedPreferences", Context.MODE_PRIVATE)
                prefs.edit().putString("flutter.case_id", map["case_id"] as String).commit()
                val acceptIntent = CallkitIncomingBroadcastReceiver.getIntentAccept(this@TransparentActivity, data)
                sendBroadcast(acceptIntent)
            }
            "CALLBACK" -> {
                val data = intent.getBundleExtra("data")
                val acceptIntent = CallkitIncomingBroadcastReceiver.getIntentCallback(this@TransparentActivity, data)
                sendBroadcast(acceptIntent)
            }
            else -> { // Note the block
                val data = intent.getBundleExtra("data")
                val acceptIntent = CallkitIncomingBroadcastReceiver.getIntentAccept(this@TransparentActivity, data)
                sendBroadcast(acceptIntent)
            }
        }
        finish()
        overridePendingTransition(0, 0)
    }
}