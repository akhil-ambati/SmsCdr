package aryagami.billing.smsc_cdr.bs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.time.DateUtils;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import aryagami.billing.smsc_cdr.Utils.CDRStatus;
import aryagami.billing.smsc_cdr.Utils.DateFormatUtils;
import aryagami.billing.smsc_cdr.Utils.SmsRecordTypes;
import aryagami.billing.smsc_cdr.model.SmsRecords;
import aryagami.billing.smsc_cdr.model.SmsRecordsIndex;
import aryagami.billing.smsc_cdr.repository.SmsRecordIndexRepository;
import aryagami.billing.smsc_cdr.repository.SmsRecordsRepository;

@EnableScheduling
@EnableAsync
@Controller
public class SmsBillingStation {

	Mapper mapper = new DozerBeanMapper();

	@Autowired
	SmsRecordsRepository smsRecordsRepository;

	@Autowired
	SmsRecordIndexRepository smsRecordIndexRepository;

	static InputStream inputStream;
	static Logger logger = LoggerFactory.getLogger(SmsBillingStation.class);
	static String cdrFilesDir;
	static String destinationBaseDir;
	static String taskRunningTime;
	static Boolean processCdrs = false;

	@PostConstruct
	public static void startSmsCdrProcesss() throws InterruptedException {
		logger.info("Initializing configuration.......");
		initConfiguration();
	}

	@Scheduled(cron = "* * * ? * * ")
	public void ScheduleTask() {
		if (!processCdrs) {
			processCdrs = true;
			processCDRFiles();
			processCdrs = false;
		}
	}

	static class RandomString {
		static String alpahbets = "abcdefghijklmnopqrstuvwxyz";

		public static String generateToken(int numDigits) {
			String uuid = UUID.randomUUID().toString();
			String token = "";
			Random rn = new Random();
			int uuidlen = uuid.length();

			for (int i = 0; i < numDigits; i++) {
				int randPos = rn.nextInt(uuidlen);

				String dString = uuid.substring(randPos, randPos + 1);
				if (dString.equals("-")) {
					int randPos2 = rn.nextInt(26);
					token = token + alpahbets.substring(randPos2, randPos2 + 1);
				} else {
					token = token + dString;
				}
			}

			return String.valueOf(token);
		}
	}

	public boolean processCDRFiles() {
		String srcRecordsPath = cdrFilesDir;

		File path = new File(srcRecordsPath);
		File[] files = path.listFiles();
		String recIndex = null;
		if (files != null) {
			if (files.length > 0) {
				recIndex = SmsBillingStation.RandomString.generateToken(16);
				moveCDRFilesForProcessing(recIndex);
			}
		}

		if (recIndex == null) {
			// log here
			logger.info("No cdr files in source directory.");
			return false;
		}
		doProcessCDRFiles(recIndex);
		return true;
	}

	void moveCDRFilesForProcessing(String recIndex) {
		File file = new File(cdrFilesDir);
		if (!file.canRead()) {
			logger.info("directory doesn't have read permissions");
		}
		File[] files = file.listFiles();
		if (files.length > 0) {
			String newBatchRecordsPath = destinationBaseDir + "/" + recIndex;
			File dir = new File(newBatchRecordsPath);
			if (!dir.exists()) {
				try {
					dir.mkdirs();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			for (int i = 0; i < files.length; i++) {
				Path newPath = Paths.get(dir + "/" + files[i].getName());
				try {
					Files.move(files[i].toPath(), newPath, StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				if (i > 100) {
					break;
				}
			}

		}
	}

	void doProcessCDRFiles(String recIndex) {

		String successFiles = "";
		String failedFiles = "";
		String newBatchRecordsPath = destinationBaseDir + "/" + recIndex;
		File path = new File(newBatchRecordsPath);
		File[] files = path.listFiles();

		FileReader fStream = null;
		String smsCdr = null;
		for (File file : files) {
			String fileName = file.getName();
			System.out.println(fileName);
			try {
				fStream = new FileReader(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				failedFiles += fileName + ";";
				e.printStackTrace();
				continue;
			}
			// TODO Auto-generated catch block
			BufferedReader bufferedReader = new BufferedReader(fStream);
			if (bufferedReader != null) {
				String tmp = null;
				try {
					while ((tmp = bufferedReader.readLine()) != null && !tmp.isEmpty()) {
						String[] tokens = tmp.split(",");
						SmsRecords smsRecords = new SmsRecords();
						smsRecords.setRecordsIndex(recIndex);
						smsRecords.setMessageId(tokens[1]);
						smsRecords.setFileName(fileName);
						if (tokens.length == 6) {
							smsRecords.setMtNumber(tokens[3]);
							smsRecords.setEventDate(
									DateFormatUtils.convertStringToDate(tokens[6], "dd-MMM-yyyy HH:mm:ss:SSS", 0));
							smsRecords.setMoNumber(tokens[2]);
							smsRecords.setResponseCode(tokens[4]);
							smsRecords.setRecordType(SmsRecordTypes.MORecord.toString());
						} else {
							smsRecords.setMessageType(tokens[1]);
							smsRecords.setMoNumber(tokens[3]);
							smsRecords.setSmscCentreNumber(tokens[4]);
							smsRecords.setResponseCode(tokens[5]);
							smsRecords.setEventDate(
									DateFormatUtils.convertStringToDate(tokens[6], "dd-MMM-yyyy HH:mm:ss:SSS", 0));
							smsRecords.setRecordType(SmsRecordTypes.MTRecord.toString());
							smsRecords.setMtNumber(tokens[2]);
						}
						smsRecordsRepository.save(smsRecords);
						if (!successFiles.contains(fileName)) {
							successFiles += fileName + ";";
						}
					}
				} catch (IOException | ArrayIndexOutOfBoundsException | NullPointerException e) {
					// TODO Auto-generated catch block
					if (!failedFiles.contains(fileName))
						failedFiles += fileName + ";";
					e.printStackTrace();
					continue;
				}
			}
		}

		if (successFiles != null && (successFiles.split(";").length == files.length)) {
			commitRecordIndexEntriesToDB(recIndex, successFiles, failedFiles, CDRStatus.Success.toString());
		} else if (failedFiles != null && (failedFiles.split(";").length == files.length)) {
			commitRecordIndexEntriesToDB(recIndex, successFiles, failedFiles, CDRStatus.Failed.toString());
		} else {
			commitRecordIndexEntriesToDB(recIndex, successFiles, failedFiles, CDRStatus.Partial.toString());
		}
	}

	private void commitRecordIndexEntriesToDB(String recIndex, String successFiles, String failedFiles, String status) {

		SmsRecordsIndex smsRecordsIndex = new SmsRecordsIndex();
		
		if (smsRecordsIndex != null) {
			smsRecordsIndex.setRecordIndex(recIndex);
			smsRecordsIndex.setSuccessFiles(successFiles);
			smsRecordsIndex.setFailedFiles(failedFiles);
			smsRecordsIndex.setStatus(status);
			smsRecordsIndex.setIsRecordsCommited(true);
			smsRecordIndexRepository.save(smsRecordsIndex);
		}
	}

	static public void initConfiguration() {
		Properties prop = new Properties();
		try {
			inputStream = new FileInputStream("/home/aryagami/Desktop/sms/config.properties");
			try {
				prop.load(inputStream);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e2) {
			logger.info("Config file not found");
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		cdrFilesDir = prop.getProperty("fileDirectory");
		destinationBaseDir = prop.getProperty("destinationDirectory");
	}

}
