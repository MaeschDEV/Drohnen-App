package com.maeschdev.drohnenapp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

var isRunning = true
var messageArray: Array<Int> = arrayOf(0, 0, 0, 0)
var sendEmptyMessage = false

var IP_ADDRESS = "192.168.178.145"
var PORT = "5000"

fun startSendingCommands(){
    CoroutineScope(Dispatchers.IO).launch {
        while (isRunning){
            val message = arrayToString(messageArray)

            if (messageArray.any { it != 0 } || !sendEmptyMessage) {
                sendCommand(message)
                sendEmptyMessage = messageArray.all { it == 0 }
            }

            delay(100)
        }
    }
}

fun onButtonPressed(dataType: Int, value: Int){
    messageArray[dataType] = value
}

fun onButtonReleased(dataType: Int, valueToCompare: Int){
    if (messageArray[dataType] == valueToCompare){
        messageArray[dataType] = 0
    }
}

fun arrayToString(array: Array<Int>): String = array.joinToString(",")

fun sendCommand(message: String){
    println("Ip Adresse: $IP_ADDRESS; Port: $PORT")
    println("Send: $message")
    sendData(message, IP_ADDRESS, PORT.toInt())
}

private val socket = DatagramSocket()

fun sendData(message: String, ip: String, port: Int){
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val address = InetAddress.getByName(ip)
            val packet = DatagramPacket(message.toByteArray(), message.length, address, port)
            socket.send(packet)
        } catch (e: Exception){
            e.printStackTrace()
        }
    }
}