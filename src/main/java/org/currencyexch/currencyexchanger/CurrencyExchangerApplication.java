package org.currencyexch.currencyexchanger;

import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.yaml.snakeyaml.reader.StreamReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Scanner;

@SpringBootApplication
public class CurrencyExchangerApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(CurrencyExchangerApplication.class, args);

        HashMap <Integer, String> currencyCodes = new HashMap<Integer, String>();
        int fromCode, toCode;
        double amount;
        boolean proceeding = true;

        currencyCodes.put(1, "USD");
        currencyCodes.put(2, "EUR");
        currencyCodes.put(3, "AUD");
        currencyCodes.put(4, "PLN");

        Scanner sc = new Scanner(System.in);

        System.out.println("Welcome to the best currency converter in the world.");
        System.out.println("Currently we offer the following currencies: ");
        System.out.println("1: USD, 2: EUR, 3: AUD, 4: PLN");
        System.out.println("Please use the provided numbers to choose a currency.");

        do {
            System.out.println("Which currency do you want to convert from?");
            System.out.println("1: USD, 2: EUR, 3: AUD, 4: PLN");

            int fromCodeAttempt = sc.nextInt();
            while(fromCodeAttempt < 1 || fromCodeAttempt > 4){
                System.out.println("The input provided by you was not correct. Please try again.");
                System.out.println("Choose a number from 1 to 4");
                fromCodeAttempt = sc.nextInt();
            }
            fromCode = fromCodeAttempt;
            System.out.println("Chosen: " + currencyCodes.get(fromCode));


            System.out.println("Which currency do you want to convert to?");
            System.out.println("1: USD, 2: EUR, 3: AUD, 4: PLN");

            int toCodeAttempt = sc.nextInt();
            while(toCodeAttempt < 1 || toCodeAttempt > 5){
                System.out.println("The input provided by you was not correct. Please try again.");
                System.out.println("Choose a number from 1 to 4");
                toCodeAttempt = sc.nextInt();
            }
            toCode = toCodeAttempt;
            System.out.println("Chosen: "+currencyCodes.get(toCode));

            System.out.println("What is your wished amount?");
            amount = sc.nextDouble();
            while (amount < 0){
                System.out.println("Please put in a positive amount.");
                amount = sc.nextDouble();
            }
            System.out.println("Chosen amount: " + amount + currencyCodes.get(fromCode));

            getCurrentExchangeRate(amount, currencyCodes.get(fromCode), currencyCodes.get(toCode));

            System.out.println("Do you want to make more conversions?");
            System.out.println("1: Yes; 2: No");
            int proceedingAnswer = sc.nextInt();
            if(proceedingAnswer != 1){
                proceeding = false;
            }
        }while (proceeding);
        System.out.println("Thank you for cooperation.");
    }

    public static void getCurrentExchangeRate(double amount, String fromCode, String toCode) throws IOException {
        String GET_URL = "https://api.frankfurter.app/latest?from=" + fromCode + "&to=" + toCode; //https://api.frankfurter.app/latest?from=USD&to=EUR
        URL url = new URL(GET_URL);

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        DecimalFormat f = new DecimalFormat("00.00");


        int httpResponse = httpURLConnection.getResponseCode();
        if(httpResponse == HttpURLConnection.HTTP_OK){
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }in.close();

            JSONObject obj = new JSONObject(response.toString());
            System.out.println("check: " + obj);
            double exchangeRate = obj.getJSONObject("rates").getDouble(toCode);

            System.out.println(amount + fromCode + " successfully converted to " + f.format(amount * exchangeRate) + toCode);
        }else{
            System.out.println("API connection failed, please contact some nerd.");
        }
    }
}
