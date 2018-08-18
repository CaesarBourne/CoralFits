package com.caesar.ken.coralfits.CorralPayment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;


import com.caesar.ken.coralfits.HomeFragment;
import com.caesar.ken.coralfits.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

public class TestPayStack extends AppCompatActivity  {


    private Card card;
    private Charge charge;

    private EditText emailField;
    private EditText cardNumberField;
    private EditText expiryMonthField;
    private EditText expiryYearField;
    private EditText cvvField;
    private ImageView payImageView;
    private TextView textPrice;
    private int amount;

    private String email, cardNumber, cvv;
    private int expiryMonth, expiryYear;
    public static final String EXTRA_PAY_ORDER_image = "image";
    public static final String EXTRA_PAY__ORDER_title = "title";
    private boolean isDelete;

    public static final int NONE = 0;

    public static final int VISA = 1;
    public static final int MASTERCARD = 2;
    public static final int Verve = 3;

    public static final String VISA_PREFIX = "4";
    public static final String MASTERCARD_PREFIX = "51,52,53,54,55,";
    public static final String VERVE_PREFIX = "";
    StatusListener statusListenerChild;
//    ArrayList<Integer> vervelist = new ArrayList<>();
    private  static final ArrayList<String> vervelistConfirm = new ArrayList<>();
    public interface StatusListener{
        void status(String pay, String message);
    }

    public void setStatusListener(StatusListener statusListenerChild){
        this.statusListenerChild = statusListenerChild;
    }
    public ArrayList returnVerve(){
        ArrayList<Integer> vervelist = new ArrayList<>();

        for (int i = 506099; i <= 506198; i++ ){
            vervelist.add(i);
        }
        for (int j = 650002; j <= 650027; j++ ){
            vervelist.add(j);
        }
        return vervelist;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        //init paystack sdk
        PaystackSdk.initialize(getApplicationContext());
     setContentView(R.layout.activity_test_pay_stack);
        Toolbar mytioolbar = findViewById(R.id.payToolbar);
        setSupportActionBar(mytioolbar);
        mytioolbar.setTitle("Payment Corral");
        //init view
        Button payBtn = (Button) findViewById(R.id.pay_button);

        emailField = (EditText) findViewById(R.id.edit_email_address);
        cardNumberField = (EditText) findViewById(R.id.edit_card_number);
        expiryMonthField = (EditText) findViewById(R.id.edit_expiry_month);
        expiryYearField = (EditText) findViewById(R.id.edit_expiry_year);
        cvvField = (EditText) findViewById(R.id.edit_cvv);
        payImageView = findViewById(R.id.payImageView);
        textPrice = findViewById(R.id.textPrice);

        if (getIntent().getExtras() != null) {
            final String image = getIntent().getExtras().getString(EXTRA_PAY_ORDER_image);
            final String title = getIntent().getExtras().getString(EXTRA_PAY__ORDER_title);
            Picasso.get().load(image).into(payImageView);
            textPrice.setText(title);

        }else {
            payImageView.setImageDrawable(getResources().getDrawable(R.drawable.corrallogo));
            textPrice.setText("");
        }


        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateForm()) {
                    return;
                }
                try {
                    email = emailField.getText().toString().trim();
                    cardNumber = cardNumberField.getText().toString().trim();
                    expiryMonth = Integer.parseInt(expiryMonthField.getText().toString().trim());
                    expiryYear = Integer.parseInt(expiryYearField.getText().toString().trim());
                    cvv = cvvField.getText().toString().trim();
                    amount = Integer.parseInt(textPrice.getText().toString().trim());

                    //String cardNumber = "4084084084084081";
                    //int expiryMonth = 11; //any month in the future
                    //int expiryYear = 18; // any year in the future
                    //String cvv = "408";
                    card = new Card(cardNumber, expiryMonth, expiryYear, cvv);

                    if (card.isValid()) {
                        Toast.makeText(TestPayStack.this, "Card is Valid", Toast.LENGTH_LONG).show();
                        performCharge();
                    } else {
                        Toast.makeText(TestPayStack.this, "Card not Valid", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        cardNumberField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (before == 0){
                    isDelete = false;
                }
                else {
                    isDelete = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String source = s.toString();
                int length=source.length();

//                StringBuilder stringBuilder = new StringBuilder();
//                stringBuilder.append(source);
//
//                if(length>0 && length%5==0)mdmj
//                {
//                    if(isDelete)
//                        stringBuilder.deleteCharAt(length-1);
//                    else {
//                        stringBuilder.insert(length - 1, " ");
//                        cardNumberField.setText(stringBuilder);
//
//                    }
//                }

                if(length >= 6){
                   if (cardtype(source) == VISA){
                       cardNumberField.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_visa, 0);
                    }
                    else if (cardtype(source) == MASTERCARD) {
                       cardNumberField.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_mastercard, 0);
                    }
                    else if (cardtype(source) == Verve){
                       cardNumberField.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.vervec, 0);
                   }
                   else {
                       cardNumberField.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                   }
                }

            }
        });


    }
    public int cardtype(String cardprefix){

        if (cardprefix.substring(0, 1).equals(VISA_PREFIX))
            return VISA;
        else if (MASTERCARD_PREFIX.contains(cardprefix.substring(0, 2) + ","))
            return MASTERCARD;
        else if (returnVerve().contains(cardprefix.substring(0, 6))){
            return Verve;
        }
        else{
            return  0;
        }
    }
//    public static boolean isValid(String cardNumber) {
//        if (!TextUtils.isEmpty(cardNumber) && cardNumber.length() >= 4)
//            if (getCardType(cardNumber) == VISA && ((cardNumber.length() == 13 || cardNumber.length() == 16)))
//                return true;
//            else if (getCardType(cardNumber) == MASTERCARD && cardNumber.length() == 16)
//                return true;
//            else if (getCardType(cardNumber) == AMEX && cardNumber.length() == 15)
//                return true;
//            else if (getCardType(cardNumber) == DISCOVER && cardNumber.length() == 16)
//                return true;
//        return false;
//    }

    /**
     * Method to perform the charging of the card
     */
    private void performCharge() {
        //create a Charge object
        charge = new Charge();

        //set the card to charge
        charge.setCard(card);

        //call this method if you set a plan
        //charge.setPlan("PLN_yourplan");

        charge.setEmail(email); //dummy email address

        charge.setAmount(amount); //test amount

        PaystackSdk.chargeCard(TestPayStack.this, charge, new Paystack.TransactionCallback() {
            @Override
            public void onSuccess(Transaction transaction) {
                // This is called only after transaction is deemed successful.
                // Retrieve the transaction, and send its reference to your server
                // for verification.
                String paymentReference = transaction.getReference();
                Toast.makeText(TestPayStack.this, "Transaction Successful! payment reference: "
                        + paymentReference, Toast.LENGTH_LONG).show();
                if (statusListenerChild != null){
                    statusListenerChild.status(paymentReference, "your order is been processed...");
                    HomeFragment.startHomefragment(TestPayStack.this);
                }

            }

            @Override
            public void beforeValidate(Transaction transaction) {
                // This is called only before requesting OTP.
                // Save reference so you may send to server. If
                // error occurs with OTP, you should still verify on server.
            }

            @Override
            public void onError(Throwable error, Transaction transaction) {
                //handle error here
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = emailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailField.setError("You must input email");
            valid = false;
        } else {
            emailField.setError(null);
        }

        String cardNumber = cardNumberField.getText().toString();
        if (TextUtils.isEmpty(cardNumber)) {
            cardNumberField.setError("card number required");
            valid = false;
        } else {
            cardNumberField.setError(null);
        }


        String expiryMonth = expiryMonthField.getText().toString();
        if (TextUtils.isEmpty(expiryMonth)) {
            expiryMonthField.setError("field required");
            valid = false;
        } else {
            expiryMonthField.setError(null);
        }

        String expiryYear = expiryYearField.getText().toString();
        if (TextUtils.isEmpty(expiryYear)) {
            expiryYearField.setError("Required.");
            valid = false;
        } else {
            expiryYearField.setError(null);
        }

        String cvv = cvvField.getText().toString();
        if (TextUtils.isEmpty(cvv)) {
            cvvField.setError("cvv is required");
            valid = false;
        } else {
            cvvField.setError(null);
        }

        return valid;
    }

}


