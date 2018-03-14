package site.facade;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import site.model.Submission;

@Service(CSVService.NAME)
public class CSVService {
	private static final Logger logger = LogManager.getLogger(CSVService.class);
	public static final String NAME = "csvFacade";
	final String[] submissionHeader = new String[] { "Title", "Abstract", "Session level", "Session type", "Speaker Name", "Speaker Bio",
			"Co-Speaker Name", "Co-Speaker Bio" };
	
	public File exportSubmissions(List<Submission> submissions) throws IOException{
		File submissionsCSVFile = File.createTempFile("submissions.", ".csv");
		logger.info("Created submissions file with path: " + submissionsCSVFile.getAbsolutePath());
        try(ICsvMapWriter mapWriter = new CsvMapWriter(new FileWriter(submissionsCSVFile.getAbsolutePath()), CsvPreference.STANDARD_PREFERENCE)) {
        	writeSubmissions(submissions, mapWriter);
		}
		return submissionsCSVFile;
	}
	
	private void writeSubmissions(List<Submission> submissions, ICsvMapWriter mapWriter) throws IOException{
		CellProcessor[] processors = new CellProcessor[] {null, null, null, null, null, null, null, null};
   	 	Map<String, Object> submissionRow;
   	 	mapWriter.writeHeader(submissionHeader);
   	 	
		for(Submission submission: submissions){
			submissionRow = new HashMap<String, Object>();
			submissionRow.put(submissionHeader[0], submission.getTitle());
			submissionRow.put(submissionHeader[1], submission.getDescription());
			submissionRow.put(submissionHeader[2], submission.getLevel());
			submissionRow.put(submissionHeader[3], submission.getType());
			submissionRow.put(submissionHeader[4], submission.getSpeaker().getFirstName());
			submissionRow.put(submissionHeader[5], submission.getSpeaker().getBio());
			if(submission.getCoSpeaker() != null){
				submissionRow.put(submissionHeader[6], submission.getCoSpeaker().getFirstName());
				submissionRow.put(submissionHeader[7], submission.getCoSpeaker().getBio());
			}
			mapWriter.write(submissionRow, submissionHeader, processors);
		}
	}
}
