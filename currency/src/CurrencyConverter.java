import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.*;
import org.json.JSONObject;
// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class CurrencyConverter {
    public static void main(String[] args) throws IOException {

        boolean running = true;
        do {
            Scanner sc = new Scanner(System.in);

            HashMap<Integer, String> favCurr = new HashMap<>();

            System.out.println("Welcome to the Currency Converter");
            System.out.println("Choose the number of the task you want to perform");
            System.out.println("1) Add your favorite currency to list.");
            System.out.println("2) View your favorite currency list.");
            System.out.println("3) Update your favorite currency list.");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> AddCurrency(favCurr);
                case 2 -> ViewCurrency(favCurr);
                case 3 -> UpdateCurrency(favCurr);
                default -> System.out.println("Invalid choice");
            }

            for (HashMap.Entry<Integer, String> entry : favCurr.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }

            int i, j;
            String from, to;

            System.out.println("Currency converting FROM : ");
            i = sc.nextInt();
            while (i < 0 || i > favCurr.size()) {
                System.out.println("Please enter a valid Integer");
                System.out.println("Currency converting FROM : ");
                i = sc.nextInt();
            }
            from = favCurr.get(i);


            System.out.println("Currency converting TO : ");
            j = sc.nextInt();
            while (j < 0 || j > favCurr.size()) {
                System.out.println("Please enter a valid Integer");
                System.out.println("Currency converting TO : ");
                j = sc.nextInt();
            }
            to = favCurr.get(j);

            double amount;
            System.out.println("Amount you wish to convert : ");
            amount = sc.nextFloat();

            try {
                HttpGetReq(from, to, amount);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        System.out.println("""
                Would you like to make another conversion?
                 Press (1) if YES
                 Press (2) if NO""");

        if(sc.nextInt() != 1)
            running = false;

        } while (running);

        System.out.println("Thanks for using Currency Converter!!!");
    }
        private static void HttpGetReq(String from, String to, double amount) throws IOException {
            String API_KEY = "d13681abddda258c5631b893dc08ef8e";
            String GetURL = "http://api.exchangeratesapi.io/v1/latest?access_key="+API_KEY;
            //System.out.println(GetURL);
            URL url = new URL(GetURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            int responseCode = httpURLConnection.getResponseCode();


            if(responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }in.close();

                JSONObject obj = new JSONObject(response.toString());
                Double fromRate = obj.getJSONObject("rates").getDouble(from);
                Double toRate = obj.getJSONObject("rates").getDouble(to);
                double actualRate = ((toRate/fromRate)*amount);
                System.out.println(amount + from + " = " + actualRate + to);
            }
            else {
                System.out.println("Get request FAILED!!!");
            }
        }
        public static void AddCurrency(HashMap<Integer, String> favCurr) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Add number of favorite currency you want to add to convert. (NOTE :- Add atleast 2)");
            int n = sc.nextInt();
            System.out.println("Add currency code : ");
            for(int i=1; i<=n; i++)
            {
                String currency = sc.next();
                favCurr.put(i, currency);
            }
        }
        public static void ViewCurrency(HashMap<Integer,String> favCurr) {
            System.out.println("View your favorite currency");
            for(HashMap.Entry<Integer, String> entry : favCurr.entrySet()) {
                System.out.println(entry.getKey()+ " : " + entry.getValue());
            }
        }
        public static void UpdateCurrency(HashMap<Integer,String> favCurr) {
            System.out.println("Enter the number to Update your favorite currency : ");
            Scanner sc = new Scanner(System.in);
            int key = sc.nextInt();
            System.out.println("Enter the new currency : ");
            String value = sc.next();
            favCurr.replace(key,value);
        }
}
