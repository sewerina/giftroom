package com.github.sewerina.giftroom;

interface Room {
    String name();
    String id();
    Iterable<Gift> gifts();
    Gift addGift(String name);
}
