package com.backtest;

import java.io.IOException;

// fixme
public interface BaseBackTestInterface {
    void setParameter(); // set current back test parameter

    void listParameter(); // list current back test parameter

    void getResult() throws IOException; // execute back test

}
