package com.maeschdev.drohnenapp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

var isSendingFlightData = true
var messageByteArray = byteArrayOf(0, 0, 0, 0, 0)

var IP_ADDRESS = "0"
var PORT = "0"

var amountOfStopSignal = 0

var isSending = false

fun startSendingCommands(){
    CoroutineScope(Dispatchers.IO).launch {
        while (isSendingFlightData) {
            sendCommand(messageByteArray)

            amountOfStopSignal = 0

            delay(100)
        }

        while (!isSendingFlightData && amountOfStopSignal <= 50) {
            sendCommand(byteArrayOf(0, 0, 0, 0, 1))

            amountOfStopSignal++
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
    if (stopped) {
        isSendingFlightData = true
        startSendingCommands()
    }
    else {
        isSendingFlightData = false
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