// IMyInterface.aidl
package com.lei.learningnote;

import com.lei.learningnote.Room;
// Declare any non-default types here with import statements

interface IMyInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    String getInfo(String s);

    Room getRoom();

}
