package com.gee5.aws.testharness;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

public class ConfigRegion {

	public static Region fetchAWSRegion(String configRegion) throws Exception {
		Region result = null;

		if (configRegion.equalsIgnoreCase("AP_NORTHEAST_1")) {
			result = Region.getRegion(Regions.AP_NORTHEAST_1);
		} else if (configRegion.equalsIgnoreCase("AP_SOUTHEAST_1")) {
			result = Region.getRegion(Regions.AP_SOUTHEAST_1);
		} else if (configRegion.equalsIgnoreCase("AP_SOUTHEAST_2")) {
			result = Region.getRegion(Regions.AP_SOUTHEAST_2);
		} else if (configRegion.equalsIgnoreCase("CN_NORTH_1")) {
			result = Region.getRegion(Regions.CN_NORTH_1);
		} else if (configRegion.equalsIgnoreCase("EU_WEST_1")) {
			result = Region.getRegion(Regions.EU_WEST_1);
		} else if (configRegion.equalsIgnoreCase("GovCloud")) {
			result = Region.getRegion(Regions.GovCloud);
		} else if (configRegion.equalsIgnoreCase("SA_EAST_1")) {
			result = Region.getRegion(Regions.SA_EAST_1);
		} else if (configRegion.equalsIgnoreCase("US_EAST_1")) {
			result = Region.getRegion(Regions.US_EAST_1);
		} else if (configRegion.equalsIgnoreCase("US_WEST_1")) {
			result = Region.getRegion(Regions.US_WEST_1);
		} else if (configRegion.equalsIgnoreCase("US_WEST_2")) {
			result = Region.getRegion(Regions.US_WEST_2);
		} else {
			throw new Exception("Region provided [" + configRegion + "] is not valid");
		}

		return result;
	}

}
