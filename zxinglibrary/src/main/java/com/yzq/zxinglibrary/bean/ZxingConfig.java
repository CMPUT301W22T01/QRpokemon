package com.yzq.zxinglibrary.bean;

import androidx.annotation.ColorRes;

import com.yzq.zxinglibrary.R;

import java.io.Serializable;

/**
 * @author: yzq
 * @date: 2017/10/27 14:48
 * @declare :zxing
 */


public class ZxingConfig implements Serializable {


    //Whether to play sound
    private boolean isPlayBeep = true;
    //Whether it vibrates
    private boolean isShake = true;
    //Whether to display other function layouts below
    private boolean isShowbottomLayout = true;
    //Whether to display the flash button
    private boolean isShowFlashLight = true;
    //Whether to display the album button
    private boolean isShowAlbum = true;
    //Whether to parse the bar code
    private boolean isDecodeBarCode = true;
    //Whether to scan in full screen
    private boolean isFullScreenScan = true;

    //Color of the four corners
    @ColorRes
    private int reactColor = R.color.react;
    //Scan box color
    @ColorRes
    private int frameLineColor = -1;


    //Scan line color
    @ColorRes
    private int scanLineColor = R.color.scanLineColor;

    public int getFrameLineColor() {
        return frameLineColor;
    }

    public void setFrameLineColor(@ColorRes int frameLineColor) {
        this.frameLineColor = frameLineColor;
    }

    public int getScanLineColor() {
        return scanLineColor;
    }

    public void setScanLineColor(@ColorRes int scanLineColor) {
        this.scanLineColor = scanLineColor;
    }

    public int getReactColor() {
        return reactColor;
    }

    public void setReactColor(@ColorRes int reactColor) {
        this.reactColor = reactColor;
    }

    public boolean isFullScreenScan() {
        return isFullScreenScan;
    }

    public void setFullScreenScan(boolean fullScreenScan) {
        isFullScreenScan = fullScreenScan;
    }

    public boolean isDecodeBarCode() {
        return isDecodeBarCode;
    }

    public void setDecodeBarCode(boolean decodeBarCode) {
        isDecodeBarCode = decodeBarCode;
    }

    public boolean isPlayBeep() {
        return isPlayBeep;
    }

    public void setPlayBeep(boolean playBeep) {
        isPlayBeep = playBeep;
    }

    public boolean isShake() {
        return isShake;
    }

    public void setShake(boolean shake) {
        isShake = shake;
    }

    public boolean isShowbottomLayout() {
        return isShowbottomLayout;
    }

    public void setShowbottomLayout(boolean showbottomLayout) {
        isShowbottomLayout = showbottomLayout;
    }

    public boolean isShowFlashLight() {
        return isShowFlashLight;
    }

    public void setShowFlashLight(boolean showFlashLight) {
        isShowFlashLight = showFlashLight;
    }

    public boolean isShowAlbum() {
        return isShowAlbum;
    }

    public void setShowAlbum(boolean showAlbum) {
        isShowAlbum = showAlbum;
    }
}
