# creaEventiColloqui
Crea tutti gli eventi Google Calendar di un determinato giorno, relativamente alle prenotazioni che i genitori effettuano sul registro Axios (Vallauri)
Come procedere:

	-Creare un progetto Java Maven utilizzando il file pom.xml ed installare tutte le libreri
	
	-Creare un progetto sulla console del servizio Google Cloud Platform relativo all'account a cui si vogliono aggiungere gli eventi
	https://console.cloud.google.com
	La cosa è più immediata cliccando 'Enable the Google Calendar API" nella guida
	https://developers.google.com/calendar/quickstart/java
	
	-Scaricare il file 'credentials.json' e metterlo in src\main\resources (Si può anche scaricare in un secondo momento dalla console cloud)
	
	-Salvare l'HTML della pagina prenotazioni colloqui del registro in src\main\resources
	
	-Nella classe principale 'CreaColloqui' inserire il nome del file HTML, la propria mail ed eventualmente la durata in minuti del colloquio (di default è messa a 9 minuti)
	
	-Far partire l'applicazione passando al main() la data che si desidera in formato dd/mm/yyyy
	
	-La prima volta che l'applicazione gira si viene mandati sul browser predefinito e si deve dare il consenso ai privilegi dell'applicazione
	
	-Dopo la prima chiamata, si crea una cartella tokens nella root del progetto in cui viene salvato di volta in volta il token che viene scambiato con le API

**************INFO UTILI**************

Console & Dashboard per gestire le proprie applicazioni e le relative API abilitate
	https://console.cloud.google.com

QuickStart per Java:
	https://developers.google.com/calendar/quickstart/java

Guida alle API Calendar:
	https://developers.google.com/calendar

NB Se si cambia lo scope dell'applicazione (CalendarScopes.CALENDAR o CalendarScopes.CALENDAR_READONLY) bisogna cancellare il file 'StoredCredential' nella cartella 'tokens' che viene ricreato alla chiamata successiva.
