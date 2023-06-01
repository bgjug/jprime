package site.facade;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import site.model.Speaker;
import site.model.Submission;

@Service(CSVService.NAME)
public class CSVService {
	private static final Logger logger = LoggerFactory.getLogger(CSVService.class);
	public static final String NAME = "csvFacade";
	private static final String[]
        SUBMISSION_HEADER = new String[] { "Title", "Abstract", "Session level", "Session type", "Speaker Name", "Speaker Bio",
			"Co-Speaker Name", "Co-Speaker Bio" };

	public File exportSubmissions(List<Submission> submissions) throws IOException{
		File submissionsCSVFile = File.createTempFile("submissions.", ".csv");
		logger.info("Created submissions file with path: {}", submissionsCSVFile.getAbsolutePath());
        try(ICsvMapWriter mapWriter = new CsvMapWriter(new FileWriter(submissionsCSVFile.getAbsolutePath()), CsvPreference.STANDARD_PREFERENCE)) {
        	writeSubmissions(submissions, mapWriter);
		}
		return submissionsCSVFile;
	}

	private void writeSubmissions(List<Submission> submissions, ICsvMapWriter mapWriter) throws IOException{
		CellProcessor[] processors = new CellProcessor[] {null, null, null, null, null, null, null, null};
   	 	Map<String, Object> submissionRow;
   	 	mapWriter.writeHeader(SUBMISSION_HEADER);

		for(Submission submission: submissions){
			submissionRow = new HashMap<>();
			submissionRow.put(SUBMISSION_HEADER[0], submission.getTitle());
			submissionRow.put(SUBMISSION_HEADER[1], submission.getDescription());
			submissionRow.put(SUBMISSION_HEADER[2], submission.getLevel());
			submissionRow.put(SUBMISSION_HEADER[3], submission.getType());
			Speaker speaker = submission.getSpeaker();
			submissionRow.put(SUBMISSION_HEADER[4], speaker.getFirstName() + " " + speaker.getLastName());
			submissionRow.put(SUBMISSION_HEADER[5], speaker.getBio());
			Speaker coSpeaker = submission.getCoSpeaker();
			if(coSpeaker != null){
				submissionRow.put(SUBMISSION_HEADER[6], coSpeaker.getFirstName() + " " + coSpeaker.getLastName());
				submissionRow.put(SUBMISSION_HEADER[7], coSpeaker.getBio());
			}
			mapWriter.write(submissionRow, SUBMISSION_HEADER, processors);
		}
	}
}
