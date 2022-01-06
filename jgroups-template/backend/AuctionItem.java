package backend;

import client.Buyer;
import client.Client;
import client.Seller;

import java.io.Serializable;

public class AuctionItem implements Serializable {

    private static final long serialVersionUID=1234567L;
    private Buyer lastBidders;
    private double reserve_price;
    private String description;
    private double start_price;
    private int ID;
    private double highest_bid;
    private Seller seller;

    public AuctionItem(double reserve_price, String description, double start_price, Seller seller) {
        this.reserve_price = reserve_price;
        this.description = description;
        this.start_price = start_price;
        this.highest_bid=start_price;
        this.seller=seller;
    }


    public void setLastBidders(Buyer lastBidders) {
        this.lastBidders = lastBidders;
    }

    public double getHighest_bid() {
        return highest_bid;
    }

    public double getStart_price() {
        return start_price;
    }

    public void setHighest_bid(double highest_bid) {
        this.highest_bid = highest_bid;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Client getLastBidders() {
        return lastBidders;
    }



    public double getReserve_price() {
        return reserve_price;
    }

    public Seller getSeller() {
        return seller;
    }



    @Override
    public String toString() {
        return "AuctionItem{" +
                "lastBidders=" + lastBidders +
                ", reserve_price=" + reserve_price +
                ", description='" + description + '\'' +
                ", start_price=" + start_price +
                ", ID=" + ID +
                ", highest_bid=" + highest_bid +
                ", seller=" + seller +
                '}';
    }



}
