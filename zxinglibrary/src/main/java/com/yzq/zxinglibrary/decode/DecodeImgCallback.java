package com.yzq.zxinglibrary.decode;

import com.google.zxing.Result;

/**
 * The callback to parse the picture
 */

public interface DecodeImgCallback {
    void onImageDecodeSuccess(Result result);

    void onImageDecodeFailed();
}
