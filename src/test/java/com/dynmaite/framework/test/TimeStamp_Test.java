package com.dynmaite.framework.test;

import  org.junit.Assert;

import org.junit.Test;

import com.dynamite.framework.FrameworkException;
import com.dynamite.framework.FrameworkParameters;
import com.dynamite.framework.TimeStamp;



public class TimeStamp_Test {

	@Test
	public void test() throws FrameworkException {
		FrameworkParameters parameters= FrameworkParameters.getInstance();
		parameters.setRelativePath();
		parameters.setRunConfiguration("Junit");
		Assert.assertNotNull(TimeStamp.getInstance());
		Assert.assertNotNull(TimeStamp.getOldReportInstance());
	}

}
