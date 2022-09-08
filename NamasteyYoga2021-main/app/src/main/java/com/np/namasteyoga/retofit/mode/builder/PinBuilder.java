package com.np.namasteyoga.retofit.mode.builder;

import com.np.namasteyoga.retofit.exceptions.HandShakeException;
import com.np.namasteyoga.retofit.mode.HandShakeMode;
import com.np.namasteyoga.retofit.mode.PinMode;
import com.np.namasteyoga.retofit.pin_extractor.PinExtract;

public class PinBuilder extends Builder {


    private String pinKey = "";

    private PinBuilder(String pinUrl) {
        super(pinUrl);
    }

    public static PinBuilder newBuilder(String pinUrl) {
        return new PinBuilder(pinUrl);
    }


    public String getPinKey() {
        return pinKey;
    }

    public PinBuilder pinKey(String pinKey) {
        this.pinKey = pinKey;
    return this;
    }


    public PinBuilder pinKey(PinExtract pinExtract) throws HandShakeException {
        this.pinKey = pinExtract.getPublicKey();
    return this;
    }

    @Override
    public HandShakeMode build() throws HandShakeException {
        return new PinMode().getHandShakeMode(this);
    }



}
