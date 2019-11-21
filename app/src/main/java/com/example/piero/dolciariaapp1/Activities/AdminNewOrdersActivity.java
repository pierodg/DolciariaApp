package com.example.piero.dolciariaapp1.Activities;

import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.piero.dolciariaapp1.Model.AdminOrders;
import com.example.piero.dolciariaapp1.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



import java.util.HashMap;

//L'ADMIN VISUALIZZA GLI ORDINI RICEVUTI DAGLI USERS

public class AdminNewOrdersActivity extends AppCompatActivity {

    private RecyclerView ordersList;
    private DatabaseReference ordersRef, cartRef;
    private ImageView imageBackButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        cartRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        ordersList = findViewById(R.id.orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));

        imageBackButton = findViewById(R.id.back_input_arrow);

        backBtn();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options =
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(ordersRef, AdminOrders.class)
                .build();

        FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, final int position, @NonNull final AdminOrders model) {
                        holder.userName.setText("Nome: " + model.getName());
                        holder.userPhoneNumber.setText("Numero di telefono: " + model.getPhone());
                        holder.userDateTime.setText("Data: " + model.getDate() + " " + model.getTime());
                        holder.userShipAddress.setText("Indirizzo: " + model.getAddress() + ", " + model.getCity());
                        holder.userTotalPrice.setText("Totale ordine: " + model.getTotalAmount() + " €");

                        holder.showOrders.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String uID = getRef(position).getKey();

                                Intent intent = new Intent(AdminNewOrdersActivity.this, AdminUserProductsActivity.class);
                                intent.putExtra("uid", uID);
                                startActivity(intent);
                                finish();
                            }
                        });

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
                                builder.setCancelable(false);
                                builder.setTitle(Html.fromHtml("<font color='#3E2723'>L'ordine è stato spedito?</font>"));

                                builder.setPositiveButton(Html.fromHtml("<font color='#e08e00'>SI</font>"), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        final String uID = getRef(position).getKey();

                                                        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(uID);
                                                        HashMap<String, Object> orderMap = new HashMap<>();
                                                        orderMap.put("state", "spedito");

                                                        ordersRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                                                                            .child("User View")
                                                                            .child(uID)
                                                                            .removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if( task.isSuccessful()) {
                                                                                        Toast.makeText(AdminNewOrdersActivity.this, "Ordine spedito", Toast.LENGTH_SHORT).show();


                                                                                    }
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });


                                                    }






                                }).setNegativeButton(Html.fromHtml("<font color='#e08e00'>NO</font>"), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                builder.create().show();

                            }
                        });

                        holder.deleteOrder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
                                builder.setCancelable(false);
                                builder.setTitle(Html.fromHtml("<font color='#3E2723'>Eliminare quest'ordine?</font>"));


                                builder.setPositiveButton(Html.fromHtml("<font color='#e08e00'>Si</font>"), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String uID = getRef(position).getKey();
                                        removeOrder(uID);
                                    }
                                }).setNegativeButton(Html.fromHtml("<font color='#e08e00'>No</font>"), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                builder.create().show();
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.orders_layout, viewGroup, false);
                        return new AdminOrdersViewHolder(view);
                    }
                };
        ordersList.setAdapter(adapter);
        adapter.startListening();
    }


    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder{

        public TextView userName, userPhoneNumber, userTotalPrice, userDateTime, userShipAddress;
        public Button showOrders, deleteOrder;


        public AdminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.order_username);
            userShipAddress = itemView.findViewById(R.id.order_address);
            userDateTime = itemView.findViewById(R.id.order_date_time);
            userPhoneNumber = itemView.findViewById(R.id.order_phone_number);
            userTotalPrice = itemView.findViewById(R.id.order_total_price);

            showOrders = itemView.findViewById(R.id.order_show_products);
            deleteOrder = itemView.findViewById(R.id.delete_order);

        }
    }

    public void backBtn() {
        imageBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminNewOrdersActivity.super.onBackPressed();
            }
        });
    }


    private void removeOrder(String uID) {
        ordersRef.child(uID).removeValue();
        cartRef.removeValue(); //Rimuove la cart list utente da Admin View da utilizzare per "Ordini precedenti"
    }

}
