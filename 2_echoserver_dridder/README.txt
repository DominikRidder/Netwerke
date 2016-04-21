Name: Ridder
Vorname: Dominik
Matrikelnummer: 4018222

Bemerkungen:
Ich hab keine bessere Loesung fuer das Schliessen den Clients gefunden.
Der Client beendet sich, wenn der Benutzer "exit" schreib, statt wenn
der Server dies tut.
Zudem habe ich nicht den fall abgefangen, das andere mittels broadcast
den ServerListener von anderen Clients beendet.

Zu showstat:
Ich zeige bei showstat die Statistiken an, bevor showstat selber dazu
gezaehlt wird. Ich fand das so besser, weil dies im allgemeinen das ist,
was man (meiner Meinung nach) sehen moechte.
Dennoch zaehle ich showstat danach in den Statistiken mit, weil ich
insgesamt wirklich jede Nachricht mit einbeziehen moechte.