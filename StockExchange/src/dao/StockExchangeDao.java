package dao;

import java.util.Objects;
import java.util.PriorityQueue;
import model.BuyOrder;
import model.SellOrder;

/*
This Dao maintains the Sell and Buy order in priority queues
 */
public class StockExchangeDao {

    private final PriorityQueue<SellOrder> sellOrderPriorityQueue = new PriorityQueue<>(
        (o1, o2) -> {
            // if same price return the more quantity
            if (Objects.equals(o1.getPrice(), o2.getPrice())) {
                return o2.getQuantity() - o1.getQuantity();
            }
            return Double.compare(o1.getPrice(), o2.getPrice());
        });

    private final PriorityQueue<BuyOrder> buyOrderPriorityQueue = new PriorityQueue<>(
        (o1, o2) -> {
            // if same price return the more quantity
            if (Objects.equals(o1.getPrice(), o2.getPrice())) {
                return o2.getQuantity() - o1.getQuantity();
            }
            return Double.compare(o1.getPrice(), o2.getPrice());
        });

    public PriorityQueue<SellOrder> getSellOrderPriorityQueue() {
        return sellOrderPriorityQueue;
    }

    public PriorityQueue<BuyOrder> getBuyOrderPriorityQueue() {
        return buyOrderPriorityQueue;
    }

    public void addSellOrder(SellOrder order) {
        sellOrderPriorityQueue.add(order);
    }

    public void addBuyOrder(BuyOrder order) {
        buyOrderPriorityQueue.add(order);
    }

}
