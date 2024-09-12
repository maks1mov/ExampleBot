package Maks1mov.utm.objects;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UtmTag {

	private int id;
	private String trafficName;
	
	private int joinsForToday;
	private int joinsForYesterday;
	private int joinsForWeek;
	private int joinsForMonth;
	private int joinsForTotal;
	private int payments;
}