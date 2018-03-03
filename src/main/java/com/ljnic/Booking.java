package com.ljnic;

public abstract class Booking implements Comparable<Booking>{
    private int memberID;
    private String firstName;
    private String lastName;
    private int deckLevel;
    private int excursions;
    private int occupants;
    private int points;
    final double costPerOccupant = 1200;
    final double costPerExcursion = 97;

    public Booking(int memberId, String first, String last,
                   int occupants, int excursion, int deckLevel){
        this.memberID = memberId;
        this.firstName = first;
        this.lastName = last;
        this.occupants = occupants;
        this.excursions = excursion;
        this.deckLevel = deckLevel;
        earnPoints(100);
    }

    public String getFirst(){
        return firstName;
    }

    public String getLast(){
        return lastName;
    }

    public int getMemberId(){
        return memberID;
    }
    public int getOccupants(){
        return occupants;
    }

    public int getPoints(){
        return points;
    }

    public void earnPoints(int pts){
        points += pts;
    }

    public double calcCost(){
        return (occupants * costPerOccupant) + (excursions * costPerExcursion);
    }
    public double getDeckLevel(){
        return deckLevel;
    }

    public int compareTo(Booking other){
        String thisCompareString = this.lastName + this.firstName + this.memberID;
        String otherCompareString = other.lastName + other.firstName + other.memberID;
        if(thisCompareString.compareTo(otherCompareString) > 0){
            return 1;
        }else if(thisCompareString.compareTo(otherCompareString) < 0){
            return -1;
        }else{
            return 0;
        }
    }

    public String toString(){
        String toString;
        toString = "Member ID: " + memberID + "\n" + "First Name: " + firstName + "\n"
                + "Last Name: " + lastName + "\n" + "Cost: " + calcCost() + "\n"
                + "Points: " + points +"\n\n";
        return toString;
    }


}
