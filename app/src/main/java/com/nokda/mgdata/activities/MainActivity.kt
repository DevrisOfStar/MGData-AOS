package com.nokda.mgdata.activities

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Gravity
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.nokda.mgdata.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    lateinit var layout:LinearLayout
    lateinit var chatLayout:LinearLayout
    lateinit var userInputEditText:EditText
    lateinit var sendButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL

        chatLayout = LinearLayout(this)
        chatLayout.orientation = LinearLayout.VERTICAL

        val chatLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0,
            1f
        )

        layout.addView(chatLayout, chatLayoutParams)

        userInputEditText = EditText(this)
        userInputEditText.hint = "여기에 메시지를 입력하세요..."

        userInputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Do nothing
            }

            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty() && s[s.length - 1] == '\n') {
                    s.delete(s.length - 1, s.length) // Remove the newline character

                    val userMessage = s.toString()
                    addChatMessage(chatLayout, userMessage, true)

                    CoroutineScope(Dispatchers.IO).launch {
                        val botResponse = getBotResponse(userMessage)
                        runOnUiThread {
                            addChatMessage(chatLayout, botResponse, false)
                        }
                    }

                    userInputEditText.text.clear()
                }
            }
        })

        layout.addView(userInputEditText)

        sendButton = Button(this)
        sendButton.text = "전송"
        layout.addView(sendButton)

        sendButton.setOnClickListener {
            val userMessage = userInputEditText.text.toString()
            addChatMessage(chatLayout, userMessage, true)

            CoroutineScope(Dispatchers.IO).launch {
                val botResponse = getBotResponse(userMessage)
                runOnUiThread {
                    addChatMessage(chatLayout, botResponse, false)
                }
            }

            userInputEditText.text.clear()
        }

        setContentView(layout)
    }

    private fun addChatMessage(layout: LinearLayout, message: String, isUser: Boolean) {
        val chatBubble = TextView(this)
        chatBubble.setPadding(20, 10, 20, 10)

        val drawable = getTailShape(isUser)
        drawable.setColor(if (isUser) ContextCompat.getColor(this, android.R.color.holo_orange_light) else ContextCompat.getColor(this, android.R.color.holo_blue_light))

        chatBubble.background = drawable

        if (isUser) {
            chatBubble.setTextColor(ContextCompat.getColor(this, android.R.color.black))
            chatBubble.text = message
        } else {
            userInputEditText.isEnabled = false
            sendButton.isEnabled = false
            CoroutineScope(Dispatchers.Main).launch {
                for (char in message) {
                    chatBubble.append(char.toString())
                    delay(25) // 0.1-second delay
                }

                userInputEditText.isEnabled = true
                sendButton.isEnabled = true
            }
        }

        val displayMetrics = resources.displayMetrics
        val width = displayMetrics.widthPixels
        val isTablet = resources.getBoolean(R.bool.isTablet) // Define this in your resources
        val bubbleWidth = if (isTablet) (width * 0.25).toInt() else (width * 0.55).toInt()

        val params = LinearLayout.LayoutParams(bubbleWidth, LinearLayout.LayoutParams.WRAP_CONTENT)
        params.gravity = if (isUser) Gravity.END else Gravity.START
        params.setMargins(10, 10, 10, 10)
        chatBubble.layoutParams = params

        layout.addView(chatBubble)
    }

    private fun getTailShape(isUser: Boolean): GradientDrawable {
        val shape = GradientDrawable()
        shape.cornerRadius = 20f
        if (isUser) {
            shape.setCornerRadii(floatArrayOf(20f, 20f, 0f, 0f, 20f, 20f, 20f, 20f))
        } else {
            shape.setCornerRadii(floatArrayOf(0f, 0f, 20f, 20f, 20f, 20f, 20f, 20f))
        }
        return shape
    }

    private fun getBotResponse(userMessage: String): String {
        //  val urlString = "https://your-chatbot-api.com/respond?message=$userMessage"
        //  val url = URL(urlString)
        //  val connection = url.openConnection() as HttpURLConnection
        //
        //  return connection.inputStream.bufferedReader().use { it.readText() }
        return  "Hello, World Hello, World " +
                "Hello, World Hello, World " +
                "Hello, World Hello, World " +
                "Hello, World Hello, World " +
                "Hello, World Hello, World " +
                "Hello, World Hello, World"
    }
}
