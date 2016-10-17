package com.ncomz.calc;

import android.util.Log;

import java.text.DecimalFormat;

/**
 * Created by NCOMZ on 2016-10-14.
 */

public class BigDecimalFormatManager {
    public String convertStrComma(String input, boolean isComma){
        input = input.replaceAll(",", "");
        double dCalculate = Double.parseDouble(input);
        boolean hasDot = false;

        if(isComma) {
            String[] inputArray;
            if(input.contains(".")) {
                inputArray = input.split("\\.");
                //input.sp
                hasDot = true;
            }
            else {
                inputArray = new String[2];
                inputArray[0] = input;
                inputArray[1] = "";
            }
            StringBuffer stringBuffer = new StringBuffer(inputArray[0]);
            inputArray[0] = stringBuffer.reverse().toString();
            int inputLength = stringBuffer.length();
            Log.d("input length", inputLength + "");
            for(int i = 0, j = 0; i < inputLength; i++){
                if(i % 3 == 0 && i != 0) {
                    stringBuffer.insert(i + j, ",");
                    j++;
                    Log.d("string buffer result [" + i + "]", stringBuffer.toString());
                }
            }
            stringBuffer.reverse();
            if(hasDot) {
                stringBuffer.append(".");
                if(inputArray.length == 2)
                    stringBuffer.append(inputArray[1]);
            }
            input = stringBuffer.toString();
        }
        /*
        else {
            input = input.replaceAll(",", "");
        }
        */
        Log.d("Covert result", input + " ::isComma:" + isComma + " ::hasDot:" + hasDot);

        return input;
    }
}
