package com.opensooq.search;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.github.bonigarcia.wdm.WebDriverManager;

public class SearchCar {
	public WebDriver driver;
	SoftAssert softassertProcess = new SoftAssert();

	@BeforeTest
	public void LoginToWebSite() {

		driver = runBrowser("chrome", driver);
		driver.manage().window().maximize();
		driver.get("https://jo.opensooq.com/en/cars/cars-for-sale");
	}

	@Test
	public void serachForExactCar() {

		/* this code that parsing "bmw" from other static class , then search about the
		first item that match "BMW x5"*/
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		Select carType = new Select(driver.findElement(By.id("PostDynamicFieldModel[Brand][]")));
		PassParameters.convertToUpper();
		carType.selectByValue(PassParameters.theItem);
		driver.findElement(By.xpath("//*[@id=\"landingPostDynamicField\"]/div/button")).click();

		List<WebElement> allResults = driver.findElements(By.className("noEmojiText"));
		String actualItem;
		String expectedItem = "";
		for (int i = 0; i < allResults.size(); i++) {
			actualItem = allResults.get(i).getText();
			System.out.println(actualItem);
			if (actualItem.contains(PassParameters.theItem + " X5")) {
				expectedItem = actualItem;
				break;
			}
		}
		System.out.println("The searched one is :" + expectedItem);

		System.out.println("====================================");
		// This code that verify if the searched item that matches the results in the
		// search list
		driver.findElement(By.xpath("//*[@id=\"searchBox\"]")).sendKeys(expectedItem.trim());
		driver.findElement(By.xpath("//*[@id=\"landingPostDynamicField\"]/div/button")).click();

		List<WebElement> newItemsAfterSearch = driver.findElements(By.className("noEmojiText"));
		for (int i = 0; i < newItemsAfterSearch.size(); i++) {

			actualItem = newItemsAfterSearch.get(i).getText();

			softassertProcess.assertEquals(actualItem, expectedItem);

		} // end of for

		softassertProcess.assertAll();
	}

	public WebDriver runBrowser(String browser, WebDriver driver) {
		if (browser.equalsIgnoreCase("firefox")) {

			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();

		} else if (browser.equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();

		} else if (browser.equalsIgnoreCase("edge")) {
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
		}
		return driver;

	}

}
