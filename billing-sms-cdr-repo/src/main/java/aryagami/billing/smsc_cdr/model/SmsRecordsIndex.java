package aryagami.billing.smsc_cdr.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;

@Entity
@Table(name = "sms_records_index")
public class SmsRecordsIndex {

	@Id
	@Column(name = "record_index")
	private String recordIndex;

	@Column(name = "status")
	private String status;
	
	@Column(name = "is_records_commited")
	private Boolean isRecordsCommited;

	@Column(name = "success_files", columnDefinition = "TEXT")
	private String successFiles;

	@Column(name = "failed_files", columnDefinition = "TEXT")
	private String failedFiles;


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getIsRecordsCommited() {
		return isRecordsCommited;
	}

	public void setIsRecordsCommited(Boolean isRecordsCommited) {
		this.isRecordsCommited = isRecordsCommited;
	}

	public String getSuccessFiles() {
		return successFiles;
	}

	public void setSuccessFiles(String successFiles) {
		this.successFiles = successFiles;
	}

	public String getFailedFiles() {
		return failedFiles;
	}

	public void setFailedFiles(String failedFiles) {
		this.failedFiles = failedFiles;
	}

	public String getRecordIndex() {
		return recordIndex;
	}

	public void setRecordIndex(String recordIndex) {
		this.recordIndex = recordIndex;
	}

	@Override
	public String toString() {
		return "SmsRecordsIndex [recordIndex=" + recordIndex + ", status=" + status + ", isRecordsCommited="
				+ isRecordsCommited + ", successFiles=" + successFiles + ", failedFiles=" + failedFiles + "]";
	}

}
