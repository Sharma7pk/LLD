package util;

import java.text.DecimalFormat;

public class PrintOrders {
    private static final DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public void printOrders(String buyOrderId, Double sellPrice, int quantity, String sellerOrderId) {
        System.out.println(buyOrderId + " " + decimalFormat.format(sellPrice) + " " + quantity + " " + sellerOrderId);
    }
}
