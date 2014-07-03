package com.brotherlogic.beer;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;

/**
 * Hello world!
 * 
 */
public class CheckCellar
{
   private final SpreadsheetService service = new SpreadsheetService("MySpreadsheetIntegration-v1");
   private final DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

   public void printActions() throws Exception
   {
      SpreadsheetEntry spreadsheet = getSpreadSheet();

      // Make a request to the API to fetch information about all
      // worksheets in the spreadsheet.
      List<WorksheetEntry> worksheets = spreadsheet.getWorksheets();

      // Iterate through each worksheet in the spreadsheet.
      for (WorksheetEntry worksheet : worksheets)
      {
         URL cellFeedUrl = worksheet.getCellFeedUrl();
         CellFeed cellFeed = service.getFeed(cellFeedUrl, CellFeed.class);
         for (CellEntry entry : cellFeed.getEntries())
         {
            if (entry.getCell().getRow() == 24 && entry.getCell().getCol() == 3)
            {
               Date d = df.parse(entry.getCell().getValue());
               Calendar today = Calendar.getInstance();
               Calendar out = Calendar.getInstance();
               out.setTime(d);
               if (out.before(today))
                  System.out.println("Beers out");
            }

            if (entry.getCell().getRow() == 24 && entry.getCell().getCol() == 5)
            {
               Date d = df.parse(entry.getCell().getValue());
               Calendar today = Calendar.getInstance();
               Calendar out = Calendar.getInstance();
               out.setTime(d);
               if (out.before(today))
                  System.out.println("Beers in");
            }
         }
      }
   }

   private SpreadsheetEntry getSpreadSheet() throws Exception
   {

      String USERNAME = Config.getValue("USERNAME");
      String PASSWORD = Config.getValue("PASSWORD");
      service.setUserCredentials(USERNAME, PASSWORD);

      // Define the URL to request. This should never change.
      URL SPREADSHEET_FEED_URL = new URL(
            "https://spreadsheets.google.com/feeds/spreadsheets/private/full");

      // Make a request to the API and get all spreadsheets.
      SpreadsheetFeed feed = service.getFeed(SPREADSHEET_FEED_URL, SpreadsheetFeed.class);
      List<SpreadsheetEntry> spreadsheets = feed.getEntries();

      for (SpreadsheetEntry entry : spreadsheets)
         if (entry.getTitle().getPlainText().equals("Beer Cellar"))
            return entry;

      return null;
   }

   public static void main(String[] args) throws Exception
   {
      CheckCellar mine = new CheckCellar();
      mine.printActions();
   }
}
