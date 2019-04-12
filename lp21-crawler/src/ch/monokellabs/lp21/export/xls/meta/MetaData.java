package ch.monokellabs.lp21.export.xls.meta;

import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MetaData 
{
	private final POIXMLProperties props;
	
	public MetaData(XSSFWorkbook wb)
	{
		props = wb.getProperties();
	}
	
	public void write()
	{
		writeMain();
		writeCustom();
	}
	
	private void writeMain()
	{
		POIXMLProperties.CoreProperties coreProp = props.getCoreProperties();
		coreProp.setTitle("Lehrplan 21");
		coreProp.setSubjectProperty("Kompetenzplaner für Lehrpersonen");
		coreProp.setKeywords("LU Lehrplan21");
		coreProp.setDescription("LP21 konforme Planung und Bewertung des Bildungsstandes.\n"
				+ "Ohne teuere Tools und aufwändiges produzieren von Dokumenten.\n\n"
				+ "Carpe diem: Gewinne Zeit für die Detailplanung und den Unterricht durch das effiziente erfassen des Lernstandes.\n\n"
				+ "Die Lehrplan Kompetenzen wurden automatisiert von der offiziellen Homepage (https://lu.lehrplan.ch) geladen.\n"
				+ "Trotzdem übernehme ich keine Haftung für die Vollständigkeit und Inhalte dieses Dokuments.\n\n"
				+ "Bedenke, dass die Entwicklung dieses Plans wertvolle Familienzeit gekostet hat...");
		coreProp.setCreator("Reguel Wermelinger"); 
		coreProp.setCategory("Bildung");
		coreProp.setLastModifiedByUser("rew");
		coreProp.setContentStatus("alpha");
	}

	private void writeCustom() 
	{
		POIXMLProperties.CustomProperties custProp = props.getCustomProperties();
		custProp.addProperty("Author", "ivy-rew");
		custProp.addProperty("Year", 2019);
		custProp.addProperty("Published", true);
		custProp.addProperty("License", "Apache License 2.0");
		custProp.addProperty("Website", "https://github.com/ivy-rew/lp21");
		custProp.addProperty("Honor", "Soli deo gloria");
	}
}