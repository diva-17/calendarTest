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
}
