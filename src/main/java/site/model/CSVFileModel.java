package site.model;

import org.springframework.web.multipart.MultipartFile;

public class CSVFileModel {

    private MultipartFile csvFile;

    private VisitorType visitorType;

    private VisitorStatus visitorStatus;

    private boolean emptyVisitorsBeforeUpload;

    public VisitorStatus getVisitorStatus() {
        return visitorStatus;
    }

    public void setVisitorStatus(VisitorStatus visitorStatus) {
        this.visitorStatus = visitorStatus;
    }

    public boolean isEmptyVisitorsBeforeUpload() {
        return emptyVisitorsBeforeUpload;
    }

    public void setEmptyVisitorsBeforeUpload(boolean emptyVisitorsBeforeUpload) {
        this.emptyVisitorsBeforeUpload = emptyVisitorsBeforeUpload;
    }

    public VisitorType getVisitorType() {
        return visitorType;
    }

    public void setVisitorType(VisitorType visitorType) {
        this.visitorType = visitorType;
    }

    public MultipartFile getCsvFile() {
        return csvFile;
    }

    public void setCsvFile(MultipartFile csvFile) {
        this.csvFile = csvFile;
    }
}
