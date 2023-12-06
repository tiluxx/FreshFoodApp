package com.application.freshfoodapp.ui.sendinginvitation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.application.freshfoodapp.MainActivity;
import com.application.freshfoodapp.databinding.ActivitySharingKitchenBinding;
import com.application.freshfoodapp.model.Invitation;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SendingInvitationActivity extends AppCompatActivity {
    public static final String RES_SHARING_KITCHEN = "SHARING_KITCHEN";
    private ActivitySharingKitchenBinding binding;
    private String curOwnerId;

    Button cancelSharingBtn, confirmSharingBtn;
    Toolbar toolbar;
    TextInputEditText sharingInfoTextInput;
    TextInputLayout sharingInfoTextInputLayout;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySharingKitchenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();

        toolbar = binding.sharingKitchenTopAppBar;

        cancelSharingBtn = binding.contentSharingKitchen.cancelSharingBtn;
        confirmSharingBtn = binding.contentSharingKitchen.confirmSharingBtn;
        sharingInfoTextInput = binding.contentSharingKitchen.viewSharingKitchen.sharingInfoTextInput;
        sharingInfoTextInputLayout = binding.contentSharingKitchen.viewSharingKitchen.sharingInfoTextInputLayout;

        Intent receivedIntent = getIntent();

        cancelSharingBtn.setOnClickListener(v -> sendResultActivity(false));

        confirmSharingBtn.setOnClickListener(v -> {
            if (validateAllInput()) {
                FirebaseUser sender = MainActivity.getCurUser();
                Invitation sharingInvitation =
                        new Invitation(
                                sender.getUid(),
                                sender.getDisplayName(),
                                sender.getEmail(),
                                sender.getPhotoUrl(),
                                sharingInfoTextInput.getText().toString(),
                                MainActivity.getCurKitchenId());
                db.collection("invitations").add(sharingInvitation);
                sendResultActivity(true);
            }
        });
    }

    private void sendResultActivity(boolean isSuccess) {
        if (isSuccess) {
            Intent resIntent = new Intent();
            resIntent.putExtra(RES_SHARING_KITCHEN, sharingInfoTextInput.getText().toString());
            setResult(RESULT_OK, resIntent);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    private boolean validateAllInput() {
        boolean isAllFullFilled = true;
        if (sharingInfoTextInput.getText().toString().isEmpty()) {
            sharingInfoTextInputLayout.setErrorEnabled(true);
            sharingInfoTextInputLayout.setError("Please enter an email");
            isAllFullFilled = false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS
                .matcher(sharingInfoTextInput.getText())
                .matches()
        ) {
            sharingInfoTextInputLayout.setErrorEnabled(true);
            sharingInfoTextInputLayout.setError("Please enter a valid email");
            isAllFullFilled = false;
        }

        return isAllFullFilled;
    }
}