package colloquio;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.ConferenceData;
import com.google.api.services.calendar.model.ConferenceSolutionKey;
import com.google.api.services.calendar.model.CreateConferenceRequest;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class EventoUtils {
    private final String APPLICATION_NAME = "Crea Colloqui in Calendar";
    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    private final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = EventoUtils.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public String creaEvento(String summary,
    						 String dateStartEvent,
    						 String dateEndEvent,
    						 String calendarId) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        Event event = new Event()
        	    .setSummary(summary);

        //Inizio Evento
    	DateTime startDateTime = new DateTime(dateStartEvent);
    	EventDateTime start = new EventDateTime()
    			.setDateTime(startDateTime);
    	event.setStart(start);

    	//Fine Evento
    	DateTime endDateTime = new DateTime(dateEndEvent);
    	EventDateTime end = new EventDateTime()
    			.setDateTime(endDateTime);
    	event.setEnd(end);

    	//Link di Meet
    	ConferenceSolutionKey key = new ConferenceSolutionKey()
    			.setType("hangoutsMeet");
    	CreateConferenceRequest request = new CreateConferenceRequest()
    			.setRequestId(String.valueOf(new Random().nextInt(999999)))
    			.setConferenceSolutionKey(key);
    	ConferenceData conferenceData = new ConferenceData()
    			.setCreateRequest(request);
    	event.setConferenceData(conferenceData);
    	
    	//Notifiche
    	EventReminder[] reminderOverrides = new EventReminder[] {
    	    new EventReminder().setMethod("popup").setMinutes(5),
    	};
    	Event.Reminders reminders = new Event.Reminders()
    	    .setUseDefault(false)
    	    .setOverrides(Arrays.asList(reminderOverrides));
    	event.setReminders(reminders);

    	//Creazione Evento
    	event = service.events().insert(calendarId, event).setConferenceDataVersion(1).execute();
    	
    	return "Event created: " + event.getHtmlLink();
    }
}