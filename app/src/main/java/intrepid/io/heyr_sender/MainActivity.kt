package intrepid.io.heyr_sender

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.triggertrap.seekarc.SeekArc
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class MainActivity : AppCompatActivity() {

    lateinit var circle: SeekArc
    lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        circle = findViewById(R.id.circle)
        button = findViewById(R.id.button)
        button.setOnClickListener {
            sendHeyR()
        }
    }

    fun sendHeyR() {
        Log.d("Progress", circle.progress.toString())
        val angle = circle.progress.toDouble() / 100.0 * 360.0
        var adjustedAngle = angle + 90
        if (adjustedAngle > 360) {
            adjustedAngle -= 360
        }
        Log.d("Angle", adjustedAngle.toInt().toString())
        sendUdpMessage(adjustedAngle.toInt())
    }

    fun sendUdpMessage(angle: Int) {
        Thread(SendUdp(angle)).start()
        Toast.makeText(this, "HEY ARNOLD!!!", Toast.LENGTH_SHORT).show()
    }

    class SendUdp(val angle: Int) : Runnable {

        override fun run() {
            val socket = DatagramSocket()
            val ipAddress = InetAddress.getByName("192.168.8.104")
            val port = 8051
            val message = angle.toString().toByteArray()
            val packet = DatagramPacket(message, message.size, ipAddress, port)
            socket.send(packet)
        }
    }
}
