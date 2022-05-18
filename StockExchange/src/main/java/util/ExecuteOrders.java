package util;

public class ExecuteOrders {
    public void printOrders(String buyOrderId, Double sellPrice, int quantity, String sellerOrderId) {
        System.out.println(buyOrderId + " " + sellPrice + " " + quantity + " " + sellerOrderId);
    }
}
