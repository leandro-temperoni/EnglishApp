package com.temperoni.english.utils;

/**
 * Created by leandro.temperoni on 3/15/2016.
 */
import com.squareup.otto.Bus;

    public class AppBus {

        private static final Bus BUS = new Bus();

        private AppBus(){
        }

        public static Bus getInstance() {
            return BUS;
        }
}
