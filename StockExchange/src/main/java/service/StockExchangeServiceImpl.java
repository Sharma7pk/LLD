package service;

import constants.OrderType;
import dao.StockExchangeDao;
import model.BuyOrder;
import model.OrderFactory;
import model.SellOrder;
import util.PrintOrders;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class StockExchangeServiceImpl implements StockExchangeService {

    StockExchangeDao stockExchangeDao = new StockExchangeDao();
    PrintOrders ui = new PrintOrders();

    @Override
    public void matchAndExecuteSellOrder(String orderId, String stockName, Double price, int quantity, LocalTime time) {
        SellOrder order = (SellOrder) OrderFactory
            .getOrder(OrderType.SELL_ORDER, orderId, stockName, price, quantity, time);
        stockExchangeDao.addSellOrder(order);
        executeOrder(order);
    }

    @Override
    public void matchAndExecuteBuyOrder(String orderId, String stockName, Double price, int quantity, LocalTime time) {
        BuyOrder order = (BuyOrder) OrderFactory
            .getOrder(OrderType.BUY_ORDER, orderId, stockName, price, quantity, time);
        stockExchangeDao.addBuyOrder(order);
        executeOrder(order);
    }

    private void executeOrder(BuyOrder buy) {
        PriorityQueue<SellOrder> sellQueue = stockExchangeDao.getSellOrderPriorityQueue();
        PriorityQueue<BuyOrder> buyQueue = stockExchangeDao.getBuyOrderPriorityQueue();
        List<SellOrder> sellOrdersList = new ArrayList<>();
        while (!sellQueue.isEmpty()) {
            SellOrder sell = sellQueue.peek();
            if (sell.getPrice() > buy.getPrice()) {
                break;
            } else {
                sellOrdersList.add(0, sellQueue.poll());
            }
        }

        int buyQuantity = buy.getQuantity();
        for (SellOrder sell : sellOrdersList) {
            if (sell.getQuantity() > buyQuantity) {
                int sellQuantity = sell.getQuantity() - buyQuantity;
                sell.setQuantity(sellQuantity);
                buyQuantity = 0;
                ui.printOrders(buy.getOrderId(), sell.getPrice(), buyQuantity, sell.getOrderId());
                break;
            } else {
                if (sell.getQuantity() <= buyQuantity) {
                    buyQuantity -= sell.getQuantity();
                    ui.printOrders(buy.getOrderId(), sell.getPrice(), sell.getQuantity(), sell.getOrderId());
                    sell.setQuantity(-1);
                }
            }
        }

        if(buyQuantity <= 0){
            buyQueue.remove(buy);
        }else {
            buy.setQuantity(buyQuantity);
        }

        // add back the list to queue for future
        for (SellOrder sell : sellOrdersList) {
            if (sell.getQuantity() != -1) {
                stockExchangeDao.addSellOrder(sell);
            }
        }
    }

    private void executeOrder(SellOrder sell) {
        PriorityQueue<SellOrder> sellQueue = stockExchangeDao.getSellOrderPriorityQueue();
        PriorityQueue<BuyOrder> buyQueue = stockExchangeDao.getBuyOrderPriorityQueue();
        List<BuyOrder> buyOrdersList = new ArrayList<>();
        while (!buyQueue.isEmpty()) {
            BuyOrder buy = buyQueue.peek();
            if (sell.getPrice() > buy.getPrice()) {
                break;
            } else {
                buyOrdersList.add(0, buyQueue.poll());
            }
        }

        int sellQuantity = sell.getQuantity();
        for (BuyOrder buy : buyOrdersList) {
            if (buy.getQuantity() > sellQuantity) {
                int buyQuantity = buy.getQuantity() - sellQuantity;
                buy.setQuantity(buyQuantity);
                ui.printOrders(buy.getOrderId(), sell.getPrice(), sellQuantity, sell.getOrderId());
                sellQuantity = 0;
                break;
            } else {
                if (buy.getQuantity() <= sellQuantity) {
                    sellQuantity -= buy.getQuantity();
                    ui.printOrders(buy.getOrderId(), sell.getPrice(), buy.getQuantity(), sell.getOrderId());
                    buy.setQuantity(-1);
                }
            }
        }

        if(sellQuantity <= 0){
            sellQueue.remove(sell);
        }else {
            sell.setQuantity(sellQuantity);
        }

        for (BuyOrder buy : buyOrdersList) {
            if (buy.getQuantity() != -1) {
                stockExchangeDao.addBuyOrder(buy);
            }
        }
    }
}
