package com.recheng.superandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.recheng.superpay.callback.OnPayResultListener;
import com.recheng.superpay.enums.PayWay;
import com.recheng.superpay.pay.ChengPay;
import com.recheng.superpay.pay.PayParams;
import com.recheng.superpay.utils.LogUtil;

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private RadioGroup rg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        rg = findViewById(R.id.rg);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PayParams.Builder payBuilder = new PayParams.Builder(MainActivity.this);
                switch (rg.getCheckedRadioButtonId()) {
                    case R.id.rbWechat:
                        payBuilder.payWay(PayWay.WechatPay);
                        //微信支付包名签名必须和官网一致  请注意!!!
                        payBuilder.wechatAppID("填入你的微信appid");
                        break;
                    case R.id.rbAliPay:
                        payBuilder.payWay(PayWay.AliPay);
                        break;
                }
                PayParams payParams = payBuilder.payInfo(editText.getText().toString()).build();
                ChengPay.newInstance(payParams).doPay(new OnPayResultListener() {
                    @Override
                    public void onPaySuccess(PayWay payWay) {
                        LogUtil.i("支付成功 " + payWay.toString());
                        Toast.makeText(MainActivity.this, "支付成功 " + payWay.toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onPayCancel(PayWay payWay) {
                        LogUtil.i("支付取消 " + payWay.toString());
                        Toast.makeText(MainActivity.this, "支付取消 " + payWay.toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onPayFailure(PayWay payWay, int errCode) {
                        LogUtil.i("支付失败 " + payWay.toString() + errCode);
                        Toast.makeText(MainActivity.this, "支付失败 " + payWay.toString() + errCode, Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }
}
