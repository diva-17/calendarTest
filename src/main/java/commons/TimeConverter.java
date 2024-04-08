package commons;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeConverter {

    static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MMM dd yyyy hh:mm a");
    static DateTimeFormatter outputDateFormat = DateTimeFormatter.ofPattern("MMM dd, yyyy,hh:mm a");

    public static String convertTime(String fromTimeStamp, String toTimeStamp, String timeToConvert) {
        try {

            // Parse the timeToConvert string to LocalDateTime
            LocalDateTime dateTime = LocalDateTime.parse(timeToConvert.replace(",","").replace("am","AM").replace("pm","PM"), dateFormat);

            // Convert LocalDateTime to ZonedDateTime with 'from' timezone
            ZonedDateTime fromZonedDateTime = dateTime.atZone(ZoneId.of(fromTimeStamp.split(" ")[1]));

            // Convert ZonedDateTime to 'to' timezone
            ZonedDateTime toZonedDateTime = fromZonedDateTime.withZoneSameInstant(ZoneId.of(toTimeStamp.split(" ")[1]));

            // Format the final ZonedDateTime according to the desired output format
            return toZonedDateTime.format(outputDateFormat);
        } catch (Exception e) {
            // Handle parsing errors
            throw new RuntimeException("Error parsing date-time string: " + timeToConvert, e);
        }
    }

    public static void main(String[] args) {
        String timeToConvert = "Apr 20, 2024, 12:00 pm"; // Replace this with an actual date-time string
        String fromTimeStamp = "GMT-10:00 Pacific/Honolulu (HST)";
        String toTimeStamp = "GMT+05:30 Asia/Calcutta (IST)";
        String convertedTime = convertTime(fromTimeStamp, toTimeStamp, timeToConvert);
        System.out.println("Converted Time: " + convertedTime);
    }
}
