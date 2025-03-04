package com.maeschdev.drohnenapp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

var isRunning = true
var messageByteArray = byteArrayOf(0, 0, 0, 0)
var alreadySendEmptyMessage = false

var IP_ADDRESS = "0"
var PORT = "0"

var isSending = false

fun startSendingCommands(){
    CoroutineScope(Dispatchers.IO).launch {
        while (isRunning){
            if (messageByteArray.any { it != 0.toByte() } || !alreadySendEmptyMessage) {
                sendCommand(messageByteArray)
                alreadySendEmptyMessage = messageByteArray.all { it == 0.toByte() }
            }

            delay(100)
        }
    }
}

fun onButtonPressed(dataType: Int, value: Int){
    messageByteArray[dataType] = value.toByte()
}

fun onButtonReleased(dataType: Int, valueToCompare: Int){
    if (messageByteArray[dataType] == valueToCompare.toByte()){
        messageByteArray[dataType] = 0
    }
}

fun sendCommand(message: ByteArray){
    sendData(message, IP_ADDRESS, PORT.toInt())
}

private val socket = DatagramSocket()

fun sendData(message: ByteArray, ip: String, port: Int){
    if (isSending) return

    CoroutineScope(Dispatchers.IO).launch {
        try {
            isSending = true
            val address = InetAddress.getByName(ip)
            val packet = DatagramPacket(message, message.size, address, port)
            socket.send(packet)
        } catch (e: Exception){
            e.printStackTrace()
        } finally {
            isSending = false
        }
    }
}