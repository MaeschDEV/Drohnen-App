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
var messageArray: Array<Int> = arrayOf(0, 0, 0, 0)
var sendEmptyMessage = false

const val ip_address = "192.168.178.145"
const val port = 5000

fun startSendingCommands(){
    GlobalScope.launch {
        while (isRunning){
            var isEmpty = true

            for (item in messageArray){
                if (item != 0){
                    sendCommand(arrayToString(messageArray))
                    isEmpty = false
                    sendEmptyMessage = false
                }
            }

            if (isEmpty && !sendEmptyMessage){
                sendCommand(arrayToString(messageArray))
                sendEmptyMessage = true
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

fun arrayToString(array: Array<Int>): String{
    var outputString = ""

    for (item in array){
        outputString += item
        outputString += ","
    }

    outputString = outputString.removeRange(outputString.length - 1, outputString.length)

    return outputString
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