package org.shrey.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBean;
import lombok.Data;

@Data
public class EnrollmentItem extends CsvToBean {
    @CsvBindByName(column = "USERID")
    private String userId;

    @CsvBindByName(column = "FIRSTNAME")
    private String firstName;

    @CsvBindByName(column = "LASTNAME")
    private String lastName;

    @CsvBindByName(column = "VERSION")
    private int version;

    @CsvBindByName(column = "INSURANCECOMPANY")
    private String insuranceCompany;
}
