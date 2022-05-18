import model.SellOrder;
import service.StockExchangeService;
import service.StockExchangeServiceImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class StockExchangeApplication {

    private static final String BUY = "buy";
    private static final String SELL = "sell";

    public static void main(String[] args) throws FileNotFoundException {

        //File location should come from input
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input File Location : ");
        String fileLocation = scanner.nextLine();
        StockExchangeService service = new StockExchangeServiceImpl();
        ArrayList<SellOrder> list = new ArrayList<>();
        getSellOrderFromFile(fileLocation);
    }

    private static void getSellOrderFromFile(String fileLocation) throws FileNotFoundException {
        StockExchangeService service = new StockExchangeServiceImpl();
        File ordersFile = new File(fileLocation);
        Scanner scanner = new Scanner(ordersFile);

        while (scanner.hasNextLine()) {
            String[] data = scanner.nextLine().split(" ");
            String orderId = data[0];
            LocalTime time = LocalTime.parse(data[1]);
            String stock = data[2];
            String type = data[3];
            Double price = Double.parseDouble(data[4]);
            int quantity = Integer.parseInt(data[5]);
            if (type.equals(BUY)) {
                service.matchAndExecuteBuyOrder(orderId, stock, price, quantity, time);
            }
            if (type.equals(SELL)) {
                service.matchAndExecuteSellOrder(orderId, stock, price, quantity, time);
            }
        }
    }
}
