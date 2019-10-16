package aryagami.billing.smsc_cdr.repository;

import org.springframework.data.repository.CrudRepository;

import aryagami.billing.smsc_cdr.model.SmsRecordsIndex;

public interface SmsRecordIndexRepository extends CrudRepository<SmsRecordsIndex, String>{

}
