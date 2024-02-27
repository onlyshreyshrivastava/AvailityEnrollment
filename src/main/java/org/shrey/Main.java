package org.shrey;


import com.opencsv.CSVWriter;
import com.opencsv.bean.*;
import org.shrey.model.EnrollmentItem;

import java.io.*;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

//Coding exercise: Availity receives enrollment files from various benefits management and enrollment solutions
//(I.e. HR platforms, payroll platforms).  Most of these files are typically in EDI format.  However, there are some files
//in CSV format.  For the files in CSV format, write a program in a language that makes sense to you that will read the
//content of the file and separate enrollees by insurance company in its own file. Additionally, sort the contents of
//each file by last and first name (ascending).  Lastly, if there are duplicate User Ids for the same Insurance Company,
//then only the record with the highest version should be included. The following data points are included in the file:
// User ID (string)
// First Name (string)
// Last Name (string)
// Version (integer)
// Insurance Company (string)
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) throws Exception {

        Reader reader = new BufferedReader(new FileReader("src/main/resources/input/enrollment.csv"));
        CsvToBean<EnrollmentItem> csvReader = new CsvToBeanBuilder(reader)
                .withType(EnrollmentItem.class)
                .withSeparator(',')
                .withIgnoreLeadingWhiteSpace(true)
                .withIgnoreEmptyLine(true)
                .build();

        List<EnrollmentItem> results = csvReader.parse();
        Map<String, List<EnrollmentItem>> recordsByInsurance = results.stream()
                .collect(groupingBy(EnrollmentItem::getInsuranceCompany));

        recordsByInsurance.forEach((key, value) -> {
            sortByLastNameAndFirstName(value);

            value.stream()
                    .collect(groupingBy(EnrollmentItem::getUserId))
                    .values()
                    .stream()
                    .filter(v -> v.size() > 1)
                    .flatMap(List::stream)
                    .max(Comparator.comparingInt(EnrollmentItem::getVersion))
                    .ifPresent(i -> value.removeIf(v -> v != i));

            //System.out.println(key + " : " + value);
            String outputPath = "src/main/resources/output/" + key.replaceAll(" ", "_") + ".csv";
            try {
                writeCsvFromBean(outputPath, value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });

    }

    private static void sortByLastNameAndFirstName(List<EnrollmentItem> value) {
        value.sort((Comparator) (item1, item2) -> {

            // Sort by last name

            String ln1 = ((EnrollmentItem) item1).getLastName();
            String ln2 = ((EnrollmentItem) item2).getLastName();
            int lnComp = ln1.compareTo(ln2);

            if (lnComp != 0) {
                return lnComp;
            }

            // If last name is same, sort by first name

            String fn1 = ((EnrollmentItem) item1).getFirstName();
            String fn2 = ((EnrollmentItem) item2).getFirstName();

            return fn1.compareTo(fn2);
        });
    }

    public static void writeCsvFromBean(String path, List<EnrollmentItem> data) throws Exception {

        try (Writer writer  = new FileWriter(path)) {
            StatefulBeanToCsv<EnrollmentItem> sbc = new StatefulBeanToCsvBuilder<EnrollmentItem>(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .build();
            sbc.write(data);
        }
    }

}