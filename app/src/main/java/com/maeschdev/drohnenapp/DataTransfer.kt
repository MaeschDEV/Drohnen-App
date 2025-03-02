package com.maeschdev.drohnenapp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

fun sendData(message: String, ip: String, port: Int){
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val socket = DatagramSocket()
            val address = InetAddress.getByName(ip)
            val buffer = message.toByteArray()
            val packet = DatagramPacket(buffer, buffer.size, address, port)
            socket.send(packet)
            socket.close()
        } catch (e: Exception){
            e.printStackTrace()
        }
    }
}