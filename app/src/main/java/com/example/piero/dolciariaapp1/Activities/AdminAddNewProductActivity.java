package com.example.piero.dolciariaapp1.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.piero.dolciariaapp1.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {

    private String categoryName, pDescription, pPrice, pName, saveCurrentDate, saveCurrentTime;
    private Button addNewProductButton;
    private ImageView inputProductImage, imageBackButton;
    private EditText inputProductName, inputProductDescription, inputProductPrice;
    private static final int GALLERY_PICK = 1;
    private Uri imageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference productImagesRef;
    private DatabaseReference productsRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        imageBackButton = findViewById(R.id.back_input_arrow);

        addNewProductButton = findViewById(R.id.add_new_product);
        inputProductImage = findViewById(R.id.select_product_image);
        inputProductName = findViewById(R.id.product_name);
        inputProductDescription = findViewById(R.id.product_description);
        inputProductPrice = findViewById(R.id.product_price);



        categoryName = getIntent().getExtras().get("category").toString();
        productImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child("Category");


        loadingBar = new ProgressDialog(this);
        addNewProduct();
        setNewProductImage();
        backBtn();
    }

    public void addNewProduct() {
        addNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateProductData();
            }
        });
    }


    public void setNewProductImage() {
        inputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            openGallery();
            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            inputProductImage.setImageURI(imageUri);
        }

    }
    private void validateProductData() {
        pDescription = inputProductDescription.getText().toString().trim();
        pName = inputProductName.getText().toString().trim();
        pPrice = inputProductPrice.getText().toString().trim();


        if(imageUri == null) {
            Toast.makeText(this, "Scegli un'immagine per il prodotto", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(pName)) {
            Toast.makeText(this, "Inserisci il nome", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(pDescription)) {
            Toast.makeText(this, "Inserisci una descrizione", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(pPrice)) {
            Toast.makeText(this, "Inserisci il prezzo", Toast.LENGTH_SHORT).show();
        } else {
            storeProductInformation();
        }
    }

    private void storeProductInformation() {

        loadingBar.setTitle("Nuovo prodotto");
        loadingBar.setMessage("Inserimento nuovo prodotto... attendere");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();


        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;


        final StorageReference filePath = productImagesRef.child(imageUri.getLastPathSegment() + productRandomKey + ".jpg");
        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminAddNewProductActivity.this, "Errore " + message , Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               Toast.makeText(AdminAddNewProductActivity.this, "Immagine caricata con successo", Toast.LENGTH_SHORT).show();

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                    if(!task.isSuccessful()) {
                        throw task.getException();

                    }
                    downloadImageUrl = filePath.getDownloadUrl().toString();
                    return  filePath.getDownloadUrl();

                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()) {

                        downloadImageUrl = task.getResult().toString();
                        Toast.makeText(AdminAddNewProductActivity.this, "Immagine del prodotto salvata nel database", Toast.LENGTH_SHORT).show();
                        saveProductInfoToDatabase();
                    }
                }
            });
            }
        });



    }

    private void saveProductInfoToDatabase() {
        HashMap<String, Object> productMap = new HashMap<>();

        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", pDescription);
        productMap.put("image", downloadImageUrl);
        productMap.put("category", categoryName);
        productMap.put("price", pPrice);
        productMap.put("pname", pName);

        productsRef.child(categoryName).child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {

                            Intent intent = new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);
                            startActivity(intent);
                            finish();

                            loadingBar.dismiss();
                            Toast.makeText(AdminAddNewProductActivity.this, "Prodotto aggiunto", Toast.LENGTH_SHORT).show();
                        } else {

                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminAddNewProductActivity.this, "Prodotto non aggiunto" + message, Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }
    public void backBtn() {
        imageBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
