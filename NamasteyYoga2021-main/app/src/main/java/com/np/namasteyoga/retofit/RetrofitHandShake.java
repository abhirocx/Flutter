package com.np.namasteyoga.retofit;


import okhttp3.OkHttpClient;
import com.np.namasteyoga.retofit.client.HandShakeClient;
import com.np.namasteyoga.retofit.exceptions.HandShakeError;
import com.np.namasteyoga.retofit.exceptions.HandShakeException;
import com.np.namasteyoga.retofit.mode.HandShakeMode;

public class RetrofitHandShake {

    private HandShakeMode mode;


    private RetrofitHandShake(HandShakeMode mode) {
        this.mode = mode;
    }


    private static RetrofitHandShake handShake = null;


    public static RetrofitHandShake mode(HandShakeMode mode) {
        if (handShake == null) {
            synchronized (RetrofitHandShake.class) {
                if (handShake == null) {
                    handShake = new RetrofitHandShake(mode);
                }
            }
        }

        return handShake;
    }


    public HandShakeMode getHandShakeMode() {
        return mode;
    }


    public OkHttpClient handshake(OkHttpClient client) throws HandShakeException {

        if (getHandShakeMode().getMode() == HandShakeMode.PIN_MODE
                && getHandShakeMode().getPinBuilder().getPinUrl().isEmpty()) {
            throw new HandShakeException(HandShakeError.NO_URL_FOUND);
        } else if (getHandShakeMode().getMode() != HandShakeMode.PIN_MODE
                && getHandShakeMode().getMode() != HandShakeMode.UN_PIN_MODE
        ) {
            throw new HandShakeException(HandShakeError.NO_MODE_FOUND);
        } else if (getHandShakeMode().getPinBuilder().getPinKey().isEmpty()
                && getHandShakeMode().getMode() == HandShakeMode.PIN_MODE) {
            throw new HandShakeException(HandShakeError.NO_PUBLIC_KEY_FOUND);
        } else {
            return new HandShakeClient(client, this).handshake();
        }


    }


}
