# Drohnen App

Diese App dient als verbindung zwischen einem Smartphone und einer darauf entwickelten Drohne. Der Nutzer kann in der App Inputs geben, wie z.B. Vorwärts, Rotieren, etc... Diese Inputs werden schließlich über das UDP Protokoll an die angegebene IP Adresse und Port gesendet mit einem unten
definierten Format

## App Aufbau und Funktion

![Drohnen App Screenshot 1](https://github.com/user-attachments/assets/f68ecb1d-dc52-4954-959f-b8f67743dd49)

So sieht der Startbildschirm der App aus. Das Aussehen der App variiert je nach Gerät. So wird z.B. das MaterialYou Theme beachtet und der Light / Dark Mode

Die App verwendet ein Speziefisches Format zum Senden von Paketen. Dieses Format sieht wie folgt aus:

(String): "0,0,0,0"

Der String ist wie folgt unterteilt:

- Button *1*: "1,0,0,0"
- Button *2*: "0,1,0,0"
- Button *3*: "0,-1,0,0"
- Button *4*: "-1,0,0,0"
- Button *5*: "0,0,1,0"
- Button *6*: "0,0,0,1"
- Button *7*: "0,0,0,-1"
- Button *8*: "0,0,-1,0"

Werden zwei Buttons gedrückt, die an unterschiedlichen Stellen im String gespeichert sind, werden beide gleichzeitig gesendet. So kann z.B. folgender String übertragen werden:

"1,0,1,0"

In diesem Fall hätte der User daher Button *1* und Button *5* gedrückt. Sollten sich wiedersprechende Buttons, wie z.B. Button *2* und *3* gleichzeitig gedrückt werden, wird der zuletzt gedrückte Button übertragen.

Ein Paket wird alle 100 ms gesendet. Wenn der Nutzer einen Knopf gedrückt hält, wird der selbe Befehl durgehend gesendet. Sollte der Nutzer jedoch keinen Button drücken, wird nur einmal "0,0,0,0" gesendet, um das Netzwerk zu schonen.

## Ip und Port einstellen

Wenn auf Button *9* gedrückt wird, öffnet sich das Einstellungsmenü:

![image](https://github.com/user-attachments/assets/c1eac1f5-85af-4061-ac92-2cc2e6b60092)

In den Input Fields können nun IP Adresse und Port eingestellt werden. Wenn nun auf dem Startbildschirm ein Button gedrückt wird, werden die Pakete an diese IP Adresse und Port gesendet. Auch nach vollständigem schließen werden diese Daten beibehalten.

## Authors

- [@MaeschDEV](https://github.com/MaeschDEV)

## License

[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](https://choosealicense.com/licenses/mit/)
