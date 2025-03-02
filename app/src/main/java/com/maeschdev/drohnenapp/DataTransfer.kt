package com.maeschdev.drohnenapp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

var isRunning = true
var lastMessage = "STOP"

const val ip_address = "192.168.178.145"
const val port = 5000

fun startSendingCommands(){
    GlobalScope.launch {
        while (isRunning){
            sendCommand(lastMessage)
            delay(100)
        }
    }
}

fun onButtonPressed(message: String){
    lastMessage = message
    isRunning = true
    startSendingCommands()
}

fun onButtonReleased(){
    lastMessage = "STOP"
    isRunning = false
    sendCommand(lastMessage)
}

fun sendCommand(message: String){
    println("Send: $message")
    sendData(message, ip_address, port)
}

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