package com.dynmaite.framework.test;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Test;

import com.dynamite.framework.FrameworkException;
import com.dynamite.framework.FrameworkParameters;
import com.dynamite.framework.UserProperties;
import com.dynamite.framework.Utility;

public class Utility_Test {

	@Test
	public void test() throws FrameworkException {
		
		FrameworkParameters parameters =FrameworkParameters.getInstance();
		parameters.setRelativePath();
		parameters.setRunConfiguration("Regression");
		
		String pattern = UserProperties.getGlobalPropeties().getProperty("DateFormatString").replace(" ", "_").replace(":","-");
		String StartTimeStamp = Utility.getCurrentFormattedTimeStamp(pattern) ;
		System.out.println(StartTimeStamp);
		Assert.assertNotNull(StartTimeStamp);
		LocalDateTime startTime = LocalDateTime.now();
		String differenceInTime = Utility.getTimeDifferenc(startTime, startTime.plusHours(2).plusMinutes(30).plusSeconds(30));
		System.out.println(differenceInTime);
		Assert.assertNotNull(differenceInTime);		
	}
}
