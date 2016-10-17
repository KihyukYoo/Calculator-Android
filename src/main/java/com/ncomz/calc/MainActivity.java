package com.ncomz.calc;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity {
    //결과값
    private BigDecimal mResult = null;
    //연산자
    private String mOperator = "";
    //피연산자
    private String mOperand = "";
    //comma 체크 활성화 여부
    private boolean mIsComma = false;
    // DecimalFormat 관리자
    private BigDecimalFormatManager mBigDecimalFormatManager = new BigDecimalFormatManager();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //피연산자 버튼 리스너 등록
        for (int i = 0; i <= 9; i++) {
            int iBtnNumberId = getResources().getIdentifier("btn_" + i, "id", getPackageName());

            findViewById(iBtnNumberId).setOnClickListener(btnOperandListener);
        }
        int iBtnPlusMinusId = getResources().getIdentifier("btn_plusminus", "id", getPackageName());
        int iBtnDotId = getResources().getIdentifier("btn_dot", "id", getPackageName());

        findViewById(iBtnPlusMinusId).setOnClickListener(btnOperandListener);
        findViewById(iBtnDotId).setOnClickListener(btnOperandListener);

        //연산자 버튼 리스너 등록
        int iBtnAddId = getResources().getIdentifier("btn_add", "id", getPackageName());
        int iBtnSubtractId = getResources().getIdentifier("btn_subtract", "id", getPackageName());
        int iBtnMultiplyId = getResources().getIdentifier("btn_multiply", "id", getPackageName());
        int iBtnDivideId = getResources().getIdentifier("btn_divide", "id", getPackageName());

        findViewById(iBtnAddId).setOnClickListener(btnOperatorListener);
        findViewById(iBtnSubtractId).setOnClickListener(btnOperatorListener);
        findViewById(iBtnMultiplyId).setOnClickListener(btnOperatorListener);
        findViewById(iBtnDivideId).setOnClickListener(btnOperatorListener);

        //기타 기능 버튼 리스너 등록
        int iBtnCancelId = getResources().getIdentifier("btn_cancel", "id", getPackageName());
        int iBtnDeleteId = getResources().getIdentifier("btn_delete", "id", getPackageName());
        int iBtnCalculateId = getResources().getIdentifier("btn_calculate", "id", getPackageName());

        findViewById(iBtnCancelId).setOnClickListener(btnCancelListener);
        findViewById(iBtnDeleteId).setOnClickListener(btnDeleteListener);
        findViewById(iBtnCalculateId).setOnClickListener(btnCalculateListener);

        //콤마 체크박스 리스너 등록
        CheckBox chkComma = (CheckBox) findViewById(R.id.chk_comma);
        chkComma.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView tvxCalculate = (TextView) findViewById(R.id.txv_calcuate);
                String sCalculate = tvxCalculate.getText().toString();
                if (isChecked)
                    mIsComma = true;
                else
                    mIsComma = false;

                tvxCalculate.setText(mBigDecimalFormatManager.convertStrComma(sCalculate, mIsComma));
            }
        });
        //텍스트 뷰 스크롤 작동
        TextView tvxCalculate = (TextView) findViewById(R.id.txv_calcuate);
        TextView tvxHistory = (TextView) findViewById(R.id.txv_history);
        tvxCalculate.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvxHistory.setMovementMethod(ScrollingMovementMethod.getInstance());
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    //bdResult와 operand를 입력한 operator로 계산
    protected String calculate() {
        TextView tvxCalculate = (TextView) findViewById(R.id.txv_calcuate);
        TextView tvxHistory = (TextView) findViewById(R.id.txv_calcuate);

        int mOperandLength = mOperand.length();
        if (mOperand.contains("."))
            mOperand.replace(".", "");
        try {
            switch (mOperator) {
                case "+":
                    mResult = mResult.add(new BigDecimal(mOperand));
                    break;
                case "-":
                    mResult = mResult.subtract(new BigDecimal(mOperand));
                    break;
                case "*":
                    mResult = mResult.multiply(new BigDecimal(mOperand));
                    break;
                case "/":
                    //mResult = mResult.divide(new BigDecimal(mOperand), BigDecimal.ROUND_CEILING);
                    mResult = mResult.divide(new BigDecimal(mOperand), 7, RoundingMode.HALF_UP);
                    while (mResult.toString().charAt(mResult.toString().length() - 1) == '0')
                        mResult = new BigDecimal(mResult.toString().substring(0, mResult.toString().length() - 1));
                    if (mResult.toString().equals("0E-7"))
                        mResult = new BigDecimal("0");
                    break;
            }
            tvxCalculate.setText(mBigDecimalFormatManager.convertStrComma(mResult.toString(), mIsComma));

            mOperator = "";
            mOperand = "";

            return mResult.toString();
        } catch (NumberFormatException e) {
            //Toast.makeText(getApplicationContext(),"숫자가 아닙니다", Toast.LENGTH_LONG).show();
            Log.d("NumberFormatException", "NumberFormatException");
            mOperator = "";
            mOperand = "";
            mResult = new BigDecimal(tvxCalculate.getText().toString());
            Log.d("mResult", tvxCalculate.getText().toString());
            tvxCalculate.setText(mBigDecimalFormatManager.convertStrComma(mResult.toString(), mIsComma));
            tvxHistory.setText("");
            return mResult.toString();
        } catch (ArithmeticException e) {
            Toast.makeText(getApplicationContext(), "계산할 수 없는 식입니다", Toast.LENGTH_LONG).show();
            ;
            Log.d("ArithmeticException", "ArithmeticException");
            mOperator = "";
            mOperand = "";
            return "";
        } finally {
            tvxCalculate.scrollTo(0, 0);
            tvxHistory.scrollTo(0, 0);
        }
    }

    View.OnClickListener btnOperandListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView tvxCalculate = (TextView) findViewById(R.id.txv_calcuate);
            String sCalculate = tvxCalculate.getText().toString();  //계산창에 있는 텍스트
            sCalculate = sCalculate.replaceAll(",", "");
            Log.d("sCalculate", sCalculate);

            Button btnClicked = (Button) v;
            String sClicked = btnClicked.getText().toString(); //클릭된 버튼의 텍스트

            //"." 클릭시
            if (sClicked.equals(".")) {
                if (!sCalculate.contains(".")) {
                    tvxCalculate.setText(mBigDecimalFormatManager.convertStrComma(sCalculate, mIsComma) + ".");
                    Log.d("...sCalculate", sCalculate);
                    return;
                }
            }

            //"±" 클릭시 입력창의 값이 "0"이 아니라면
            else if (sClicked.equals("±")) {
                if (!sCalculate.equals("0")) {
                    if (sCalculate.contains("-"))
                        sCalculate = sCalculate.split("-")[1];
                    else
                        sCalculate = "-" + sCalculate;

                    if (mOperator.equals(""))
                        mResult = new BigDecimal((sCalculate));
                    else
                        mOperand = sCalculate;
                }
            }

            //숫자 버튼 클릭시
            else {
                if(mResult == null)
                    Log.d("mResult", "null");
                else
                    Log.d("mResult", mResult.toString());
                Log.d("mOperator", mOperator);
                Log.d("mOperand", mOperand);
                if (mOperator.equals("")) {
                    Log.d("1", "1");
                    Log.d("has dot", "" + sCalculate.contains(".") + ":" + sCalculate);
                    if ((sCalculate.equals("0") || mResult == null) && !sCalculate.substring(sCalculate.length() - 1).equals(".")) {
                        //if ((sCalculate.equals("0") || mResult == null) && !sCalculate.substring(sCalculate.length() - 1).equals(".")) {
                        sCalculate = sClicked;
                    }
                    else
                        sCalculate = sCalculate + sClicked;

                    mResult = new BigDecimal(sCalculate);
                }
                else {
                    Log.d("2", "2");
                    Log.d("has dot", "" + sCalculate.contains("\\.") + ":" + sCalculate);
                    Log.d("boolean1", sCalculate.equals("0") + "");
                    Log.d("boolean2", mOperand.equals("")+ "");
                    Log.d("boolean3", !sCalculate.substring(sCalculate.length() - 1).equals(".") + "");
                    if ((sCalculate.equals("0") || mOperand.equals("")) && !sCalculate.substring(sCalculate.length() - 1).equals(".")) {
                        //if ((sCalculate.equals("0") || mOperand.equals("")) && !sCalculate.substring(sCalculate.length() - 1).equals(".")) {
                        sCalculate = sClicked;
                    }
                    else
                        sCalculate = sCalculate + sClicked;

                    mOperand = sCalculate;
                }
            }
            tvxCalculate.setText(mBigDecimalFormatManager.convertStrComma(sCalculate, mIsComma));
        }
    };
    View.OnClickListener btnOperatorListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView tvxCalculate = (TextView) findViewById(R.id.txv_calcuate);
            String sCalculate = tvxCalculate.getText().toString();  //계산창에 있는 텍스트

            //sCalcuate 맨뒤에 .이 있으면 빼기
            Log.d("boolean!!", sCalculate.substring(sCalculate.length() - 1).equals(".") + "");
            if (sCalculate.substring(sCalculate.length() - 1).equals(".")) {
                sCalculate = sCalculate.substring(0, sCalculate.length() - 1);
                Log.d("sCalculate without dot", sCalculate.toString());
                tvxCalculate.setText(sCalculate);
            }

            TextView tvxHistory = (TextView) findViewById(R.id.txv_history);
            String sHistory = tvxHistory.getText().toString();  //히스토리창에 있는 텍스트
            int iHistoryLength = sHistory.length();

            Button btnClicked = (Button) v;
            String sClicked = btnClicked.getText().toString(); //클릭된 버튼의 텍스트

            if (sCalculate.equals("0") && mResult == null)
                mResult = new BigDecimal("0");

            if (mOperand.equals("")) {
                mOperator = sClicked;
                //mResult = new BigDecimal(sCalculate);

                String sHistoryResult = "";
                String sHistoryLast = "";   //history창의 마지막 글자

                if (iHistoryLength != 0)
                    sHistoryLast = sHistory.substring(iHistoryLength - 1);
                if (sHistoryLast.matches("[+-/*]"))
                    sHistoryResult = sHistory.substring(0, iHistoryLength - 1) + sClicked;

                if (sHistory.equals(""))
                    sHistoryResult = sCalculate + sClicked;

                tvxHistory.setText(sHistoryResult);
            } else {
                String result = calculate();
                Log.d("result", result);
                if (result.equals("")) {
                    tvxCalculate.setText("0");
                    tvxHistory.setText("");
                    tvxCalculate.scrollTo(0, 0);
                    tvxHistory.scrollTo(0, 0);
                    return;
                }

                tvxHistory.setText(sHistory + sCalculate + mOperand + sClicked);
                mOperator = sClicked;
                tvxCalculate.scrollTo(0, 0);
                tvxHistory.scrollTo(0, 0);
            }
        }
    };
    View.OnClickListener btnCalculateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView tvxCalculate = (TextView) findViewById(R.id.txv_calcuate);
            String sCalculate = tvxCalculate.getText().toString();  //계산창에 있는 텍스트

            TextView tvxHistory = (TextView) findViewById(R.id.txv_history);
            String sHistory = tvxHistory.getText().toString();  //히스토리창에 있는 텍스트
            int iHistoryLength = sHistory.length();

            Button btnClicked = (Button) v;
            String sClicked = btnClicked.getText().toString(); //클릭된 버튼의 텍스트

            Log.d("mResult", "" + mResult);
            Log.d("mOperator", "" + mOperator);
            Log.d("mOperand", "" + mOperand);

            String result = calculate();
            Log.d("result", result);
            if (result.equals("")) {
                tvxCalculate.setText("0");
                tvxHistory.setText("");
                return;
            }

            tvxCalculate.setText(mBigDecimalFormatManager.convertStrComma(result, mIsComma));
            tvxHistory.setText("");
            tvxCalculate.scrollTo(0, 0);
            tvxHistory.scrollTo(0, 0);
        }
    };
    View.OnClickListener btnDeleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            TextView tvxCalculate = (TextView) findViewById(R.id.txv_calcuate);
            String sCalculate = tvxCalculate.getText().toString();  //입력창에 있는 텍스트
            int iCalculateLength = sCalculate.length();

            //계산창에 내용이 1글자라면
            if (iCalculateLength == 1)
                tvxCalculate.setText("0");
                //계산창의 내용이 2글자인데 앞에 "-"가 붙어 있다면
            else if (iCalculateLength == 2 && sCalculate.contains("-"))
                tvxCalculate.setText("0");
                //계산창에 숫자가 2글자가 이상이고 음수가 아니라면
            else
                //마지막 자 삭제
                tvxCalculate.setText(mBigDecimalFormatManager.convertStrComma(sCalculate.substring(0, iCalculateLength - 1), mIsComma));
        }
    };
    View.OnClickListener btnCancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            TextView txvInput = (TextView) findViewById(R.id.txv_calcuate);
            txvInput.setText("0");


            TextView tvxHistory = (TextView) findViewById(R.id.txv_history);
            tvxHistory.setText("");

            mOperand = "";
            mOperator = "";
            mResult = null;
        }
    };

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}