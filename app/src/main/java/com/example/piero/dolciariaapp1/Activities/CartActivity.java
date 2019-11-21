package com.example.piero.dolciariaapp1.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.piero.dolciariaapp1.Model.Cart;
import com.example.piero.dolciariaapp1.Prevalent.Prevalent;
import com.example.piero.dolciariaapp1.R;
import com.example.piero.dolciariaapp1.Utilies.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//CARRELLO USER

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ImageView imageBackButton;
    private Button nextProcessBtn;
    private TextView txtTotalAmount, txtMessage, txtMessage2, txtMessage3;
    private String categoryName;
    private DatabaseReference productsRef;

    private double overTotalPrice = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        imageBackButton = findViewById(R.id.back_input_arrow);
        nextProcessBtn = findViewById(R.id.cart_next_button);
        txtTotalAmount = findViewById(R.id.cart_total_price);

        txtMessage = findViewById(R.id.message1);
        txtMessage2 = findViewById(R.id.message2);
        txtMessage3 = findViewById(R.id.message3);


        backBtn();
        nextProcessBtn();

    }

    @Override
    protected void onStart() {
        super.onStart();

        checkOrderButton();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");


        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View")
                .child(Prevalent.currentOnlineUser.getUsername())
                        .child("Products"), Cart.class)
                        .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {
                holder.txtProductQuantity.setText("Quantità = " + model.getQuantity());
                holder.txtProductPrice.setText("Prezzo " + model.getPrice() + "€");
                holder.txtProductName.setText(model.getPname());

                int oneTypeProductTotalPrice = (Integer.valueOf(model.getPrice())) * Integer.valueOf(model.getQuantity());
                overTotalPrice = overTotalPrice + oneTypeProductTotalPrice;
                txtTotalAmount.setText("Totale: " + overTotalPrice + " €");

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[] = new CharSequence[] {
                                "Modifica",
                                "Rimuovi"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Modifica Carrello:");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, final int i) {
                                 if(i == 0) {

                                     categoryName = getIntent().getExtras().get("Category").toString();

                                     Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
                                     intent.putExtra("pid", model.getPid());
                                     intent.putExtra("Category", categoryName);
                                     startActivity(intent);
                                     finish();

                                 } if (i == 1) {
                                     cartListRef.child("User View")
                                             .child(Prevalent.currentOnlineUser.getUsername())
                                             .child("Products")
                                             .child(model.getPid())
                                             .removeValue()
                                             .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                 @Override
                                                 public void onComplete(@NonNull Task<Void> task) {
                                                     if(task.isSuccessful()) {
                                                         Toast.makeText(CartActivity.this, "Prodotto eliminato dal carrello", Toast.LENGTH_SHORT).show();
                                                         finish();
                                                         startActivity(getIntent());
                                                     }
                                                 }
                                             });
                                }
                            }
                        });
                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);


                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void checkOrderButton() {
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getUsername());

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String shippingState = dataSnapshot.child("state").getValue().toString();
                    String userName = dataSnapshot.child("name").getValue().toString();

                    if (shippingState.equals("spedito")) {
                        recyclerView.setVisibility(View.GONE);

                        txtMessage.setVisibility(View.GONE);

                        txtMessage2.setVisibility(View.VISIBLE);
                        txtMessage2.setText("Il tuo ordine è stato spedito");
                        nextProcessBtn.setVisibility(View.GONE);

                        txtMessage3.setText("Puoi effettuare un ordine alla volta");
                        txtMessage3.setVisibility(View.VISIBLE);

                        Toast.makeText(CartActivity.this, "Puoi effettuare un ordine alla volta", Toast.LENGTH_SHORT).show();
                    }
                    else if (shippingState.equals("non spedito")) {
                        txtMessage2.setText("Il tuo ordine non è ancora stato spedito");
                        recyclerView.setVisibility(View.GONE);
                        txtMessage2.setVisibility(View.VISIBLE);


                        txtMessage.setVisibility(View.VISIBLE);
                        nextProcessBtn.setVisibility(View.GONE);

                        txtMessage3.setText("Puoi effettuare un ordine alla volta");
                        txtMessage3.setVisibility(View.VISIBLE);
                        txtTotalAmount.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "Puoi effettuare un ordine alla volta", Toast.LENGTH_SHORT).show();

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public void backBtn() {

        imageBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartActivity.super.onBackPressed();
            }
        });

    }



    public void nextProcessBtn() {
        nextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(overTotalPrice == 0) {
                    Toast.makeText(CartActivity.this, "Il carrello è vuoto", Toast.LENGTH_SHORT).show();
                } else {

                    Intent intent = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                    intent.putExtra("Total Price", String.valueOf(overTotalPrice));
                    startActivity(intent);
                    finish();
                }

            }
        });
    }


}
