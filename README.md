### !!! ALPHA STADIUM !!!
### Die hier entstehende Anwendung funktioniert noch nicht.

---

# Stefans Poker Game

Was erreicht werden soll:
- Der Spieler soll einen schnellen Einstieg in das Spiel haben ohne Registrierungsprozess und trotzdem die Möglichkeit haben, seinen Account/Fortschritt bei einem Gerätewechsel mitzunehmen.
- Minimaler Datenaustausch, um Traffik zu sparen. Damit soll auch mobil gespielt werden können.
- Klare Objekt-Struktur, die den Code nachvollziehbar macht
- Einfache Installation ohne großartige Abhängigkeiten zu MySQL, Tomcat usw.
  Das Vorhandensein einer Java-Runtime soll ausreichen.
- Geringe Hardware-Anforderungen. Der Server soll auf einem RaspberryPi laufen können.
- Client und Server sollen gegenseitig die Spielregeln prüfen, falls ein modifzierter
  Client bzw. Server unterwegs ist. Sowohl Server als auch Client müssen also falsche
  Bet-Größen oder Spieler, die out of turn agieren, ablehnen.
- Spätere Portierung auf Android mittels JavaFXPorts

Eingesetzte Frameworks:
- JavaFX als GUI Framework
- Java WebSockets für Datenaustausch
- Google GSON für Serialisierung der Nachrichten zwischen Client und Server
- MapDB als Persistenz
