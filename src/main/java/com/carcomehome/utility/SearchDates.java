package com.carcomehome.utility;

import java.util.Date;

public class SearchDates  {
	
    private static Date pickUpDate;
	
	private static Date returnBackDate;

	
	public static Date getPickUpDate() {
		return pickUpDate;
	}

	public static void setPickUpDate(Date pickUpDate) {
		SearchDates.pickUpDate = pickUpDate;
	}

	
	public static Date getReturnBackDate() {
		return returnBackDate;
	}

	public static void setReturnBackDate(Date returnBackDate) {
		SearchDates.returnBackDate = returnBackDate;
	}
	
     
}
