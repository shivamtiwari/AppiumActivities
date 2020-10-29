package Appium_Activities;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class Activity_5 {
	AppiumDriver<MobileElement> driver = null;
	WebDriverWait wait;

	@BeforeClass
	public void beforeClass() throws MalformedURLException {
		// Set the Desired Capabilities
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("deviceName", "samsung SM-G935F");
		caps.setCapability("platformName", "Android");
		caps.setCapability("appPackage", "com.google.android.contacts");
		caps.setCapability("appActivity", "com.android.contacts.activities.PeopleActivity");
		caps.setCapability("noReset", true);

		// Instantiate Appium Driver
		URL appServer = new URL("http://0.0.0.0:4723/wd/hub");
		driver = new AndroidDriver<MobileElement>(appServer, caps);
		wait = new WebDriverWait(driver, 5);
	}

	@Test
	public void addContact() {
		// Click on add new contact floating button
		driver.findElementByAccessibilityId("Create contact").click();
		
		wait = new WebDriverWait(driver,30);
		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.xpath("//android.widget.ImageButton[@content-desc='Cancel']")));

		// Find the first name, last name, and phone number fields
		MobileElement firstName = driver.findElementByXPath("//android.widget.EditText[@text='First name']");
		MobileElement lastName = driver.findElementByXPath("//android.widget.EditText[@text='Surname']");
		MobileElement phoneNumber = driver.findElementByXPath("//android.widget.EditText[@text='Phone']");

		// Enter the text in the fields
		firstName.sendKeys("Aaditya");
		lastName.sendKeys("Varma");
		phoneNumber.sendKeys("9991284782");

		// Save the contact
		driver.findElementById("menu_save").click();
		
		// Wait for contact card to appear
		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.id("toolbar")));

		// Assertion
		MobileElement mobileCard = driver.findElementById("toolbar");
		Assert.assertTrue(mobileCard.isDisplayed());

		String contactName = driver.findElementById("large_title").getText();
		Assert.assertEquals(contactName, "Aaditya Varma");
	}
	
	@Test
	public void deleteContact() {
		// Click on More options button		
		driver.findElementByAccessibilityId("More options").click();
		
		// Wait for More options menu to appear
		wait = new WebDriverWait(driver,30);
		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.xpath("//android.widget.TextView[@text='Delete']")));
		
		// Click on the Delete option
		driver.findElementByXPath("//android.widget.TextView[@text='Delete']").click();
		
		// Wait for Delete confirmation pop up to appear 
		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.xpath("//android.widget.Button[@text='Delete']")));
		
		// Click on the Delete button
		driver.findElementByXPath("//android.widget.Button[@text='Delete']").click();
		
		// Wait for the Contact list to appear		
		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.xpath("//android.widget.TextView[@text='No contacts in this account']")));
		
		// Assertion
		String emptyContactList = driver.findElementByXPath("//android.widget.TextView[@text='No contacts in this account']").getText();
		Assert.assertEquals(emptyContactList, "No contacts in this account");
	}

	@AfterClass
	public void afterClass() {
		driver.quit();
	}
}