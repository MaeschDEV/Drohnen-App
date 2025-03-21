# Drohnen App

Diese App dient als verbindung zwischen einem Smartphone und einer darauf entwickelten Drohne. Der Nutzer kann in der App Inputs geben, wie z.B. Vorwärts, Rotieren, etc... Diese Inputs werden schließlich über das UDP Protokoll an die angegebene IP Adresse und Port gesendet mit einem unten
definierten Format

## App Aufbau und Funktion

![Drohnen App Screenshot](https://github.com/user-attachments/assets/64de2aac-b910-412f-96f3-0373bf2c0f69)

So sieht der Startbildschirm der App aus. Das Aussehen der App variiert je nach Gerät. So wird z.B. das MaterialYou Theme beachtet und der Light / Dark Mode

Die App verwendet ein Speziefisches Format zum Senden von Paketen. Dieses Format sieht wie folgt aus:

(ByteArray): 0,0,0,0,0

Das ByteArray ist wie folgt unterteilt:

- Button *1*: "1,0,0,0"
- Button *3*: "0,1,0,0"
- Button *4*: "0,2,0,0"
- Button *5*: "2,0,0,0"
- Button *6*: "0,0,1,0"
- Button *7*: "0,0,0,1"
- Button *8*: "0,0,0,2"
- Button *9*: "0,0,2,0"

Werden zwei Buttons gedrückt, die an unterschiedlichen Stellen im String gespeichert sind, werden beide gleichzeitig gesendet. So kann z.B. folgender String übertragen werden:

"1,0,1,0"

In diesem Fall hätte der User daher Button *1* und Button *6* gedrückt. Sollten sich wiedersprechende Buttons, wie z.B. Button *3* und *4* gleichzeitig gedrückt werden, wird der zuletzt gedrückte Button übertragen.

Ein Paket wird alle 100 ms gesendet. Wenn der Nutzer einen Knopf gedrückt hält, wird der selbe Befehl durgehend gesendet.

Sollte der 5. Byte auf 1 sein, bedeutet das, die Drohne soll alle Rotoren ausschalten und einen "Not-Stop" einleiten.
Der 5. Byte wird auf 1 gesetzt, wenn Button *2* gedrückt wird. Dieser Button disabled alle Buttons. Es ist togglebar

## Ip und Port einstellen

Wenn auf Button *10* gedrückt wird, öffnet sich das Einstellungsmenü:

![image](https://github.com/user-attachments/assets/c1eac1f5-85af-4061-ac92-2cc2e6b60092)

In den Input Fields können nun IP Adresse und Port eingestellt werden. Wenn nun auf dem Startbildschirm ein Button gedrückt wird, werden die Pakete an diese IP Adresse und Port gesendet. Auch nach vollständigem schließen werden diese Daten beibehalten.

## Lokal testen

- Lade dir die neuste Version der App für Android als .apk von [hier](https://github.com/MaeschDEV/Drohnen-App/releases) herunter
- Zum Testen kannst du auf deinem lokalen PC ein Socket abhören auf dem von dir in der App definierten Port. Du kannst dafür [dieses Python Programm](#python-testprogramm) verwenden
- Gebe die IPV4 Adresse deines Computers in der App ein
- Wenn du nun das Python Programm startest und einen Button in der App drückst, solltest du "Received message: (...)" in dem Output sehen. Wenn ja, hat alles geklappt

## Python Testprogramm
```
import socket

UDP_IP = "0.0.0.0"
UDP_PORT = 5000

sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
sock.bind((UDP_IP, UDP_PORT))

print(f"Listening on port {UDP_PORT}...")

while True:
    data, addr = sock.recvfrom(1024)
    print(f"Received message: {data} from {addr}")
```

## Autor

- [@MaeschDEV](https://github.com/MaeschDEV)

## Special Thanks
- [@FalkAurel](https://github.com/FalkAurel) - Ersteller der Drohne, die angesteuert wird

## License

[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](https://choosealicense.com/licenses/mit/)
