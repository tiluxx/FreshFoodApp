package com.application.freshfoodapp.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.application.freshfoodapp.MainActivity;
import com.application.freshfoodapp.R;
import com.application.freshfoodapp.databinding.FragmentProfileBinding;
import com.application.freshfoodapp.ui.auth.AuthActivity;
import com.application.freshfoodapp.ui.profile.restriction.RestrictionFragment;
import com.application.freshfoodapp.ui.recipes.RecipesFragment;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel mViewModel;
    private GoogleSignInClient mGoogleSignInClient;
    ShapeableImageView profileAvatar;
    Button setReminderBtn, setRestrictionBtn, logoutBtn;
    TextView displayNameTextView, emailTextView;
    public static FirebaseUser curUser;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        curUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileAvatar = binding.profileAvatar;
        setReminderBtn = binding.setReminderBtn;
        setRestrictionBtn = binding.setRestrictionBtn;
        logoutBtn = binding.logoutBtn;
        displayNameTextView = binding.displayNameTextView;
        emailTextView = binding.emailTextView;

        Picasso.get()
                .load(MainActivity.getCurUser().getPhotoUrl())
                .resize(60, 60)
                .centerCrop()
                .into(profileAvatar);
        displayNameTextView.setText(MainActivity.getCurUser().getDisplayName());
        emailTextView.setText(MainActivity.getCurUser().getEmail());

        MaterialAlertDialogBuilder logoutConfirmDialog = new MaterialAlertDialogBuilder(view.getContext())
                .setTitle("Log out your account?")
                .setMessage("Are you sure you want to log out?")
                .setNegativeButton("Cancel", (dialog, which) -> {
                })
                .setPositiveButton("Log out", (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(view.getContext(), AuthActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                });


        logoutBtn.setOnClickListener(v -> {
            logoutConfirmDialog.show();
        });

        setRestrictionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString(RestrictionFragment.ARG_RESTRICTION, curUser.getUid());
                MainActivity.getNavController().navigate(R.id.restrictionFragment, args);
            }
        });
    }
}