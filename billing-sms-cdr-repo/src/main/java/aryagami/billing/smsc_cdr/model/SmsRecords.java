package aryagami.billing.smsc_cdr.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sms_records")
public class SmsRecords {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	private String recordsIndex;
	private String messageId;
	private String moNumber;
	private String mtNumber;
	private String responseCode;
	private Date recordDate;
	private String fileName;
	private String recordType;
	private String smscCentreNumber;
	private String status;
	private Date eventDate;
	private Boolean isRecordsCommited;
	private String successFiles;
	private String failedFiles;
	private String messageType;

	public String getSmscCentreNumber() {
		return smscCentreNumber;
	}

	public void setSmscCentreNumber(String smscCentreNumber) {
		this.smscCentreNumber = smscCentreNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
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

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRecordsIndex() {
		return recordsIndex;
	}

	public void setRecordsIndex(String recordsIndex) {
		this.recordsIndex = recordsIndex;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getMoNumber() {
		return moNumber;
	}

	public void setMoNumber(String moNumber) {
		this.moNumber = moNumber;
	}

	public String getMtNumber() {
		return mtNumber;
	}

	public void setMtNumber(String mtNumber) {
		this.mtNumber = mtNumber;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public Date getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		return "SmsRecords [id=" + id + ", recordsIndex=" + recordsIndex + ", messageId=" + messageId + ", moNumber="
				+ moNumber + ", mtNumber=" + mtNumber + ", responseCode=" + responseCode + ", recordDate=" + recordDate
				+ ", fileName=" + fileName + ", recordType=" + recordType + ", smscCentreNumber=" + smscCentreNumber
				+ ", status=" + status + ", eventDate=" + eventDate + ", isRecordsCommited=" + isRecordsCommited
				+ ", successFiles=" + successFiles + ", failedFiles=" + failedFiles + ", messageType=" + messageType
				+ "]";
	}

}
