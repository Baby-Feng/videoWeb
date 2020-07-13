package com.example.hello.entity;

public class Keyword {
    private String label;
    private int sum;

    public Keyword(String label, int sum) {
        this.label = label;
        this.sum = sum;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "Keyword{" +
                "label='" + label + '\'' +
                ", sum=" + sum +
                '}';
    }
}
