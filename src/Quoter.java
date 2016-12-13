import javafx.application.Application;
import org.openqa.selenium.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Quoter {
    private static boolean isGUI = true;
    private static QuoterGUI quoterGUI;

    public static void main(String[] args) throws MalformedURLException{
        if(args.length != 0){
            if(args[0].equalsIgnoreCase("-c")){
                isGUI = false;
                commandLineQuoter();
            } else {
                System.err.println("Invalid argument");
                System.err.println("Usage: java -jar Quoter [-c]");
                System.exit(1);
            }
        } else {
            quoterGUI = new QuoterGUI();
            Application.launch(quoterGUI.getClass(), args);
        }
    }

    private static void commandLineQuoter(){
        Scanner in = new Scanner(System.in);
        System.out.print("Enter webpage: ");
        String webpage = in.next();
        System.out.print("Enter quote to generate: ");
        in = new Scanner(System.in);
        String searchQuote = in.nextLine();

        beginSearch(webpage, searchQuote);
    }

    public static void beginSearch(String webpage, String searchQuote){
        System.setProperty("webdriver.chrome.driver", "Driver/chromedriver.exe");
        WebDriver driver = new ChromeDriver(DesiredCapabilities.chrome());
        try{
            if(!webpage.contains("www.")){
                webpage = "http://www." + webpage;
            } else if(!webpage.contains("http://") && !webpage.contains("https://")){
                webpage = "http://" + webpage;
            }
            String text = openPage(driver, webpage);
            search(text, searchQuote, webpage);
        } catch (WebDriverException e){
            String warning = "Bad URL. Make sure URL's have http://www. at the beginning";
            System.err.println(warning);
            driver.quit();
            if(isGUI){
                quoterGUI.setWarning(warning);
            } else {
                System.exit(1);
            }
        } finally {
            driver.quit();
        }
    }

    private static void search(String text, String searchQuote, String webpage){
        String[] quoteSplit = searchQuote.split("\\s+");
        String[] textSplit = text.split("\\s+");

        int cur = 0;
        boolean found = false;
        boolean begin = false;
        Integer[] spaces = new Integer[quoteSplit.length];
        for(String s: textSplit){
            if(s.equalsIgnoreCase(quoteSplit[cur])){
                quoteSplit[cur] = s;
                begin = true;
                spaces[cur] = 0;
                cur++;
                if(quoteSplit.length == cur){
                    found = true;
                    break;
                }
            } else {
                if(begin){
                    spaces[cur-1]++;
                }
            }
        }

        if(found){
            String finalQuote = "\""+quoteSplit[0];
            int amount = 0;
            for(int i = 1; i < quoteSplit.length; i++){
                if(spaces[i-1] > 0){
                    finalQuote += "..." + quoteSplit[i];
                    amount += spaces[i-1];
                } else {
                    finalQuote += " " + quoteSplit[i];
                }
            }
            finalQuote += "\" - " + webpage;
            if(isGUI){
                quoterGUI.setQuote(finalQuote);
                quoterGUI.setVariance(amount + " words.");
            } else {
                System.out.println(finalQuote);
                System.out.println("Total Variance: " + amount + " words.");
            }
        } else {
            if(isGUI){
                quoterGUI.setQuote(webpage + " never said that.");
            } else {
                System.out.println(webpage + " never said that.");
            }
        }
    }

    private static String openPage(WebDriver driver, String webpage) throws WebDriverException{
        driver.get(webpage);
        String text = driver.findElement(By.tagName("body")).getText();
        text = text.replaceAll("\\.", " ");
        text = text.replaceAll(",", " ");
        return text.replaceAll("\\s+"," ");
    }

}
