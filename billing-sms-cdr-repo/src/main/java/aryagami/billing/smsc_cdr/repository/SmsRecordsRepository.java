package aryagami.billing.smsc_cdr.repository;

import org.springframework.data.repository.CrudRepository;

import aryagami.billing.smsc_cdr.model.SmsRecords;

public interface SmsRecordsRepository extends CrudRepository<SmsRecords, Integer> {

}
