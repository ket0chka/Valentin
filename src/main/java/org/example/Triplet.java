package org.example;

public class Triplet<T> {
    private T next;
    private T previous;
    private int firstElem;
    private int lastElem;
    private int TripletLenght = 5;
    private Object[] objects;

    public Triplet(T next, T previous) {
        this.firstElem = 0;
        this.lastElem = 0;
        this.next = next;
        this.previous = previous;
        this.objects = new Object[TripletLenght];
    }

    public T getNext() {
        return next;
    }

    public void setNext(T next) {
        this.next = next;
    }

    public T getPrevious() {
        return previous;
    }

    public void setPrevious(T previous) {
        this.previous = previous;
    }

    public int getFirstElem() {
        return firstElem;
    }

    public void setFirstElem(int firstElem) {
        this.firstElem = firstElem;
    }

    public int getLastElem() {
        return lastElem;
    }

    public void setLastElem(int lastElem) {
        this.lastElem = lastElem;
    }

    public int getTripletLenght() {
        return TripletLenght;
    }

    public void setTripletLenght(int tripletLenght) {
        TripletLenght = tripletLenght;
    }

    public Object[] getObjects() {
        return objects;
    }


    public Object[] setobject(int index, T el) {
        objects[index] = el;
        return objects;
    }

    public Triplet<T> getNextTriplet() {
        Triplet<T> NextTriplet = null;
        return NextTriplet;
    }
}
