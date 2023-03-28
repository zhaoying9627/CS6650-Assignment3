package org.example;

public class Match {
    private String swiper;
    private String swipee;

    public Match(String swiper, String swipee) {
        this.swiper = swiper;
        this.swipee = swipee;
    }

    public String getSwiper() {
        return swiper;
    }

    public void setSwiper(String swiper) {
        this.swiper = swiper;
    }

    public String getSwipee() {
        return swipee;
    }

    public void setSwipee(String swipee) {
        this.swipee = swipee;
    }
}
