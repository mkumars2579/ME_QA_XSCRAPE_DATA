package demo;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;
import net.bytebuddy.build.Plugin.Engine.Source.Element;


import org.testng.annotations.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.google.gson.*;




public class TestCases {
    ChromeDriver driver;

    /*
     * TODO: Write your tests here with testng @Test annotation. 
     * Follow `testCase01` `testCase02`... format or what is provided in instructions
     */

     
    /*
     * Do not change the provided methods unless necessary, they will help in automation and assessment
     */
    @BeforeTest
    public void startBrowser()
    {
        System.setProperty("java.util.logging.config.file", "logging.properties");

        // NOT NEEDED FOR SELENIUM MANAGER
        // WebDriverManager.chromedriver().timeout(30).setup();

        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        options.addArguments("--remote-allow-origins=*");

        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log"); 

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();
    }

    String name;
    String year;
    double winPercentage;
  
   
    @Test
    public void testCase01() throws Exception{
        System.out.println("Test case 01 Execution Start");
        WebDriverWait wait1 = new WebDriverWait(driver,Duration.ofMillis(2000));
     List<HashMap<String, Object>> teamsData;
     teamsData = new ArrayList<>();
    driver.get("https://www.scrapethissite.com/pages/");
    Thread.sleep(4000);
    wait1.until(ExpectedConditions.urlToBe("https://www.scrapethissite.com/pages/"));
    driver.findElement(By.xpath("//a[@href='/pages/forms/']")).click();
    for (int page = 1; page <= 4; page++) {
            // Locate the table rows for each page
            List<WebElement> rows = driver.findElements(By.xpath("//table[@id='teamsTable']/tbody/tr"));

        
            for (WebElement row : rows) {
                String teamName = row.findElement(By.xpath("./td[1]")).getText();
                String year = row.findElement(By.xpath("./td[2]")).getText();
                double winPercent = Double.parseDouble(row.findElement(By.xpath("./td[3]")).getText());

               
                if (winPercent < 0.40) {
                    HashMap<String, Object> teamInfo = new HashMap<>();
                    teamInfo.put("Epoch Time of Scrape", Instant.now().getEpochSecond());
                    teamInfo.put("Team Name", teamName);
                    teamInfo.put("Year", year);
                    teamInfo.put("Win %", winPercent);
                    teamsData.add(teamInfo);
                }
            }

           
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File("hockey-team-data.json"), teamsData);
        } catch (IOException e) {
            e.printStackTrace();
        }
     
        System.out.println("Test case 01 Execution Completed");
}


@Test
public void testCase02() throws Exception{
    System.out.println("Test case 02 Execution Started");
     WebDriverWait wait2 = new WebDriverWait(driver,Duration.ofMillis(2000));
    
    driver.get("https://www.scrapethissite.com/pages/");
    
    Thread.sleep(4000);   
    wait2.until(ExpectedConditions.urlToBe("https://www.scrapethissite.com/pages/"));
    driver.findElement(By.xpath("//a[@href='/pages/ajax-javascript/']")).click();
    List<WebElement> yearlist=driver.findElements(By.xpath("//a[@class='year-link' ]"));
    boolean isWinner=false;
     String year="";

     
    ArrayList<String> topfivemovies=new ArrayList<>();
    for(WebElement wb1:yearlist){
        wb1.click();
       Thread.sleep(4000);
       int count=0;
       List<WebElement> moviename=driver.findElements(By.xpath("//td [@class='film-title']"));
         for(WebElement wb2:moviename){
            topfivemovies.add(wb2.getText());
            if(count==4){
                break;
            }
            count++;
         }
      
    }
    Gson gson = new Gson();
    String json = gson.toJson(topfivemovies);

    // Save JSON to a file
    String outputDir = "src\\test\\resources\\directoryfile";
    File outputFile = new File(outputDir + File.separator + "oscar-winner-data.json");
    try (FileWriter writer = new FileWriter(outputFile)) {
        writer.write(json);
    }

    // Assertion to check file existence and non-empty
    assert outputFile.exists();
    assert outputFile.length() > 0;

    System.out.println("Test case 02 Execution Completed");
}

    @AfterTest
    public void endTest()
    {

        
        driver.close();
        driver.quit();

    }
}