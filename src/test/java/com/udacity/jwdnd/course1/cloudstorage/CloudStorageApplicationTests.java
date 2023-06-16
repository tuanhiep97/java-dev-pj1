package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
import java.util.List;
import java.util.NoSuchElementException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private HomePage home;
	private LoginPage login;
	private SignupPage signup;


	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
		
		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}

	
	
	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling redirecting users 
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric: 
	 * https://review.udacity.com/#!/rubrics/2724/view 
	 */
	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");
		
		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling bad URLs 
	 * gracefully, for example with a custom error page.
	 * 
	 * Read more about custom error pages at: 
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");
		
		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code. 
	 * 
	 * Read more about file size limits here: 
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}

	@Test
	public void testAccessPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertNotEquals("Home", driver.getTitle());
	}

	@Test
	public void testSignup() throws InterruptedException {
		String firstName = "a", lastName = "b", username = "c", password = "d";

		WebDriverWait wait = new WebDriverWait(driver, 10);
		signup = new SignupPage(driver);
		login = new LoginPage(driver);
		home = new HomePage(driver);

		driver.get("http://localhost:" + this.port + "/signup");
		signup.signup(firstName, lastName, username, password);
		driver.get("http://localhost:" + this.port + "/login");
		WebElement marker = wait.until(webDriver -> webDriver.findElement(By.name("username")));
		login.login(username, password);
		marker = wait.until(webDriver -> webDriver.findElement(By.id("nav-files-tab")));
		home.logout();
		marker = wait.until(webDriver -> webDriver.findElement(By.name("username")));
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertNotEquals("Home", driver.getTitle());
	}

	@Test
	public void testNoteFunction() {
		String firstName = "a", lastName = "b", username = "c", password = "d";
		String noteTitle = "created!", noteDescription = "created!";
		String editedTitle = "changed!", editedDescription = "changed!";
		WebDriverWait wait = new WebDriverWait(driver, 10);
		signup = new SignupPage(driver);
		login = new LoginPage(driver);
		home = new HomePage(driver);

		driver.get("http://localhost:" + this.port + "/signup");
		signup.signup(firstName, lastName, username, password);
		driver.get("http://localhost:" + this.port + "/login");
		WebElement marker = wait.until(ExpectedConditions.elementToBeClickable(By.name("username")));
		login.login(username, password);
		driver.get("http://localhost:" + this.port + "/home");
		marker = wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab")));
		driver.findElement(By.id("nav-notes-tab")).click();
		marker = wait.until(ExpectedConditions.elementToBeClickable(By.id("upload-note-btn")));
		driver.findElement(By.id("upload-note-btn")).click();
		marker = wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title")));
		home.createNote(noteTitle, noteDescription);
		driver.get("http://localhost:" + this.port + "/home");
		marker = wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab")));
		driver.findElement(By.id("nav-notes-tab")).click();
		marker = wait.until(ExpectedConditions.elementToBeClickable(By.id("note-row-title")));
		String result = driver.findElement(By.id("note-row-title")).getText();
		Assertions.assertEquals(result, noteTitle);

		driver.findElement(By.id("note-edit")).click();
		marker = wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title")));
		home.editNote(editedTitle, editedDescription);
		driver.get("http://localhost:" + this.port + "/home");
		marker = wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab")));
		driver.findElement(By.id("nav-notes-tab")).click();
		marker = wait.until(ExpectedConditions.elementToBeClickable(By.id("note-row-title")));
		result = driver.findElement(By.id("note-row-title")).getText();
		Assertions.assertEquals(result, editedTitle);

		driver.findElement(By.id("note-delete")).click();
		driver.get("http://localhost:" + this.port + "/home");
		marker = wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab")));
		driver.findElement(By.id("nav-notes-tab")).click();
		boolean isExist;
		try {
			driver.findElement(By.id("note-row-title"));
			isExist = true;
		}catch(NoSuchElementException e){
			isExist = false;
		}
		Assertions.assertEquals(isExist, false);
	}

	@Test
	public void testCredentialFunction() {
		String firstName = "a", lastName = "b", username = "c", password = "d";
		String credUrl = "google.com", credUsername = "created!", credPassword = "created!";
		String editedUrl = "yahoo.com", editedUsername = "changed!", editedPassword = "changed!";
		int size = 3;
		WebDriverWait wait = new WebDriverWait(driver, 10);
		signup = new SignupPage(driver);
		login = new LoginPage(driver);
		home = new HomePage(driver);

		driver.get("http://localhost:" + this.port + "/signup");
		signup.signup(firstName, lastName, username, password);
		driver.get("http://localhost:" + this.port + "/login");
		WebElement marker = wait.until(ExpectedConditions.elementToBeClickable(By.name("username")));
		login.login(username, password);
		for(int i=0; i<size; i++){
			driver.get("http://localhost:" + this.port + "/home");
			marker = wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab")));
			driver.findElement(By.id("nav-credentials-tab")).click();
			marker = wait.until(ExpectedConditions.elementToBeClickable(By.id("upload-cred-btn")));
			driver.findElement(By.id("upload-cred-btn")).click();
			marker = wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-url")));
			home.createCred(credUrl, credUsername, credPassword);
		}
		driver.get("http://localhost:" + this.port + "/home");
		marker = wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab")));
		driver.findElement(By.id("nav-credentials-tab")).click();
		marker = wait.until(ExpectedConditions.elementToBeClickable(By.id("cred-row-url")));
		List<WebElement> resultList = driver.findElements(By.id("cred-row-url"));
		Assertions.assertEquals(resultList.size(), size);
		WebElement result = driver.findElement(By.id("cred-row-password"));
		Assertions.assertNotEquals(result.getText(), credPassword);

		resultList = driver.findElements(By.id("cred-edit"));
		resultList.get(0).click();
		marker = wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-password")));
		String viewPassword = driver.findElement(By.id("credential-password")).getText();
		Assertions.assertNotEquals(viewPassword, credPassword);

		for(int i=0; i<size; i++){
			driver.get("http://localhost:" + this.port + "/home");
			marker = wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab")));
			driver.findElement(By.id("nav-credentials-tab")).click();
			marker = wait.until(ExpectedConditions.elementToBeClickable(By.id("cred-edit")));
			resultList = driver.findElements(By.id("cred-edit"));
			resultList.get(i).click();
			marker = wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-url")));
			home.editCred(editedUrl, editedUsername, editedPassword);
		}
		driver.get("http://localhost:" + this.port + "/home");
		marker = wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab")));
		driver.findElement(By.id("nav-credentials-tab")).click();
		marker = wait.until(ExpectedConditions.elementToBeClickable(By.id("cred-row-url")));
		resultList = driver.findElements(By.id("cred-row-url"));
		for(int i=0; i<size; i++){
			Assertions.assertEquals(resultList.get(i).getText(), editedUrl);
		}

		for(int i=0; i<size; i++){
			driver.get("http://localhost:" + this.port + "/home");
			marker = wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab")));
			driver.findElement(By.id("nav-credentials-tab")).click();
			marker = wait.until(ExpectedConditions.elementToBeClickable(By.id("cred-delete")));
			result = driver.findElement(By.id("cred-delete"));
			result.click();
		}
		driver.get("http://localhost:" + this.port + "/home");
		marker = wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab")));
		driver.findElement(By.id("nav-credentials-tab")).click();
		marker = wait.until(ExpectedConditions.elementToBeClickable(By.id("upload-cred-btn")));
		boolean isExist;
		try {
			driver.findElement(By.id("cred-delete"));
			isExist = true;
		}catch(NoSuchElementException e){
			isExist = false;
		}
		Assertions.assertEquals(isExist, false);
	}
}
