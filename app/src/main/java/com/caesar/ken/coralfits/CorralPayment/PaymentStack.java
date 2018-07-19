package com.caesar.ken.coralfits.CorralPayment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.caesar.ken.coralfits.R;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

public class PaymentStack extends AppCompatActivity {

    Button payButton;
    int expiryyear;
    String cardnumber;
    String cww;
    Card card;
    int expirymonth ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_stack);

        payButton = (Button) findViewById(R.id.sendPay);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {


                    expiryyear = 18;
                    cardnumber = "1234567887654321";
                    cww = "123";
                    expirymonth = 8;
                    card = new Card(cardnumber, expirymonth, expiryyear, cww);
                    if (card.isValid()) {
                        Toast.makeText(PaymentStack.this, "This motherfucking card is valid", Toast.LENGTH_LONG).show();



                    } else {
                        Toast.makeText(PaymentStack.this, "YourCard isninvalid", Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e){

                }

            }
        });

    }

    public void chargeCard(){
        Charge charge = new Charge();
        charge.setCard(card);
        charge.setAmount(100);
        charge.setEmail("caesaradek@gmail.com");

        PaystackSdk.chargeCard(PaymentStack.this, charge, new Paystack.TransactionCallback() {
            @Override
            public void onSuccess(Transaction transaction) {

            }

            @Override
            public void beforeValidate(Transaction transaction) {

            }

            @Override
            public void onError(Throwable error, Transaction transaction) {

            }
        });
    }

    public boolean ValidateForm(){
        return true;
    }
}
