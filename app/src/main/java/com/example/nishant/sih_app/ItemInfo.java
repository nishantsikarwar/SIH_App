package com.example.nishant.sih_app;

/**
 * Created by nishant on 29/11/17.
 */

public class ItemInfo {

 private String qrcode;

 public ItemInfo(){

 }

 public ItemInfo(String qrcode){
     this.qrcode=qrcode;
 }
 public void getQrcode(String qrcode){
     this.qrcode=qrcode;
 }
 public String setQrcode(){return qrcode;}

}
