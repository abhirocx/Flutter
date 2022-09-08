package com.np.namasteyoga.retofit.mode;

import com.np.namasteyoga.retofit.exceptions.HandShakeError;
import com.np.namasteyoga.retofit.exceptions.HandShakeException;
import com.np.namasteyoga.retofit.mode.builder.Builder;
import com.np.namasteyoga.retofit.mode.builder.PinBuilder;

public class PinMode extends HandShakeMode {


    private PinMode(PinBuilder pinBuilder) {
        super.setPinBuilder(pinBuilder);
        super.setMode(PIN_MODE);
    }

    public PinMode(){}


    @Override
    public HandShakeMode getHandShakeMode(Builder modeBuilder) throws HandShakeException {
        if (modeBuilder instanceof PinBuilder) {
            return new PinMode((PinBuilder) modeBuilder);
        } else {
            throw new HandShakeException(HandShakeError.NO_MODE_FOUND);
        }
    }
}
