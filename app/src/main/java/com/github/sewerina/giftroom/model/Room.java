package com.github.sewerina.giftroom.model;

public interface Room {
    String name();
    String id();
    Iterable<Gift> gifts();
    Gift addGift(String name);
}
