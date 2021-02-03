package colloquio;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CreaColloqui {
	public ArrayList<Colloquio> colloqui;
	public String inputFile = "";
	public int durataMinuti;
	public String calendarId;
	
	public CreaColloqui() {
		this.inputFile = "src/main/resources/{NOMEFILE}";
		this.durataMinuti = 9;
		this.colloqui = new ArrayList<Colloquio>();
		this.calendarId = "{MAIL}";
	}

	/*
	 args[0] = data del colloquio in formato dd/mm/yyyy
	*/
	public static void main(String[] args) throws Exception {
		CreaColloqui creaColloqui = new CreaColloqui();
		EventoUtils eventoUtils = new EventoUtils();
		
		//popolo l'array dei colloqui
		creaColloqui.parseColloqui(args[0]);

        if(creaColloqui.colloqui.isEmpty()) {
        	System.out.println("Non ci sono colloqui per la data " + args[0]);
        }
        else {
        	if(creaColloqui.getSN("Vuoi creare i colloqui trovati? S/N")) {
        		for(Colloquio colloquio : creaColloqui.colloqui) {
        			//Creo il colloquio con data inizio/fine calcolate con la durata configurata
        			String confirm = eventoUtils.creaEvento("Colloquio " + colloquio.getAlunno(),
        												    creaColloqui.parseDate(colloquio.getDataOra()),
        												    creaColloqui.parseDate(DateUtils.addMinutes(colloquio.getDataOra(), creaColloqui.durataMinuti)),
        												    creaColloqui.calendarId);
        			System.out.println(confirm);
        		}
        	}
        }
		
        System.out.println("**FINE PROGRAMMA**");
	}
	
	//Trasforma un oggetto Date in una stringa in formato ISO
	public String parseDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		sdf.setTimeZone(TimeZone.getTimeZone("CET"));
		
		return sdf.format(date);
	}

	//Popola l'array dei colloqui
	public void parseColloqui(String dataColloquioStr) throws Exception {
		File input = new File(this.inputFile);
		Document doc = Jsoup.parse(input, "UTF-8");
	    Element table = doc.select("table").get(0); //prima tabella della pagina
	    Elements rows = table.select("tr");
	    Date dataColloquio = new SimpleDateFormat("dd/MM/yyyy").parse(dataColloquioStr);

	    //Scorro le righe a partire dalla seconda
	    for (int i=1; i<rows.size(); i++) {
	        Elements cols = rows.get(i).select("td");

	        if(DateUtils.isSameDay(dataColloquio, getDataOraColloquio(cols.get(1).text(),
					 												  cols.get(2).text()))) {
		        Colloquio colloquio = new Colloquio();
		        colloquio.setAlunno(cols.get(3).text());
		        colloquio.setDataOra(getDataOraColloquio(cols.get(1).text(),
									 cols.get(2).text()));
		        this.colloqui.add(colloquio);
		        System.out.println(colloquio);
	        }
	    }
	}
	
	//Ritorna la data e l'ora di inzio del colloquio
	public Date getDataOraColloquio(String s1, String s2) throws ParseException {
		String data = getDataColloquio(s1);
		String ora = getOraInizioColloquio(s2);
		
		return new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(data + " " + ora);
	}

    //Ritorna una stringa data a partire dalla cella della tabella
	public String getDataColloquio(String dataora) throws ParseException {
		return dataora.split("\\s+")[0];
	}

    //Ritorna una stringa ora compresa tra 'asintotico a' e ')' dalla cella della tabella
	public String getOraInizioColloquio(String oraCollquio) {
		return StringUtils.substringBetween(oraCollquio, "\u2243", ")");
	}
	
	public boolean getSN(String s) throws IOException {
		String risposta;
		
		do {
			risposta = getString(s).toUpperCase();
		}
		while(!risposta.equals("S") && 
		      !risposta.equals("N"));
		
		if(risposta.equals("S")) {
			return true;
		}
		else {
			return false;
		}
	}

	public String getString(String s) throws IOException {
		InputStreamReader input = new InputStreamReader(System.in);
	    BufferedReader tastiera = new BufferedReader(input);
		
	    System.out.println(s);

		return tastiera.readLine();
	}

}
