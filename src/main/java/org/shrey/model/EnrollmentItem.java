package org.shrey.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBean;
import lombok.Data;

@Data
public class EnrollmentItem extends CsvToBean {
    @CsvBindByName(column = "userId")
    private String userId;

    @CsvBindByName(column = "firstName")
    private String firstName;

    @CsvBindByName(column = "lastName")
    private String lastName;

    @CsvBindByName(column = "version")
    private int version;

    @CsvBindByName(column = "insuranceCompany")
    private String insuranceCompany;
}
