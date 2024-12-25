package com.test.ProjectTask.googlesheet;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.test.ProjectTask.model.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

@Service
public class GoogleSheetWriter {

    public  void writeDataIntoSheet(User user) throws GeneralSecurityException, IOException{

            Sheets sheetsService = GoogleSheetsService.getSheetsService();

        String spreadsheetId = "13auQyxXKo2SHL9mVhVJkAYY1L4WFsamkTDe6BKgm-z8";

        ValueRange response = sheetsService.spreadsheets().values()
                .get(spreadsheetId, "Sheet1!A:A") // Check column A for existing data
                .execute();

        // Determine the next empty row
        int nextRow = response.getValues() == null ? 2 : response.getValues().size() + 1; // Start from row 2 if empty

        // Create the dynamic range
        String range= "Sheet1!A" + nextRow + ":B" + nextRow;

        ValueRange body = new ValueRange()
                .setValues(Arrays.asList(
                        Arrays.asList(user.getName(),user.getEmail())
                ));

        sheetsService.spreadsheets().values()
                .update(spreadsheetId, range, body)
                .setValueInputOption("RAW")
                .execute();

        System.out.println("Data written successfully!");
    }
}

