package com.maeschdev.drohnenapp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

var messageByteArray = byteArrayOf(0, 0, 0, 0, 0)

var IP_ADDRESS = "0"
var PORT = "0"

var stopSignal = false
var amountOfStopSignal = 0

var isSending = false

fun startSendingCommands(){
    CoroutineScope(Dispatchers.IO).launch {
        while (true) {
            if (!stopSignal) {
                sendCommand(messageByteArray)
                println("Normale Daten")

                amountOfStopSignal = 0
            }
            else if (amountOfStopSignal <= 50) {
                sendCommand(byteArrayOf(0, 0, 0, 0, 1))
                println("Stop Daten")

                amountOfStopSignal++
            }
            else {
                println("Keine Daten")
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

fun onToggleStopButtonPressed(stopped: Boolean) {
    stopSignal = !stopped
}

fun sendCommand(message: ByteArray){
    sendData(message, IP_ADDRESS, PORT.toInt())
}

private val socket = DatagramSocket()

fun sendData(message: ByteArray, ip: String, port: Int){
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