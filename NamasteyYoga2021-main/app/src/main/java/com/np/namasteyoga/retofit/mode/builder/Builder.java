package com.np.namasteyoga.retofit.mode.builder;

import com.np.namasteyoga.retofit.exceptions.HandShakeException;
import com.np.namasteyoga.retofit.mode.HandShakeMode;

public abstract class Builder {

    private String pinUrl;

    Builder(String pinUrl) {
        this.pinUrl = pinUrl;
    }

    public String getPinUrl() {
        return this.pinUrl;
    }

    abstract HandShakeMode build() throws HandShakeException;


}
