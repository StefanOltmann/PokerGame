### !!! ALPHA STADIUM !!!
### Die hier entstehende Anwendung funktioniert noch nicht.

---

# Stefans Poker Game

Was erreicht werden soll:
- Minimaler Datenaustausch, um auch für mobiles Internet freundlich zu sein.
- Klare Objekt-Struktur, die den Code nachvollziehbar macht
- Einfache Installation ohne großartige Abhängigkeiten zu MySQL, Tomcat usw.
  Das Vorhandensein einer Java-Runtime muss reichen.
- Der Server soll auf einem RaspberryPi laufen können.
- Client und Server sollen gegenseitig die Spielregeln prüfen, falls ein modifzierter
  Client bzw. Server unterwegs ist. Sowohl Server als auch Client müssen also falsche
  Bet-Größen oder Spieler, die out of turn agieren, ablehnen.

Eingesetzte Frameworks:
- Java WebSockets für Datenaustausch
- Google GSON für Serialisierung der Nachrichten zwischen Client und Server
- MapDB als Persistenz
