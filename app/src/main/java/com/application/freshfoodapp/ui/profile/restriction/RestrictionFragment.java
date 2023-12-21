package com.application.freshfoodapp.ui.profile.restriction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.application.freshfoodapp.R;
import com.application.freshfoodapp.databinding.FragmentRestrictionBinding;
import com.application.freshfoodapp.model.Restriction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RestrictionFragment extends Fragment {
    private FragmentRestrictionBinding binding;
    private List<Integer> checkedChipIds;
    private List<String> restrictions;
    private String ownerUid = "";
    private FirebaseFirestore db;
    private boolean checkBtnClick = false;
    private Restriction restriction;
    Bundle args;
    public static final String ARG_RESTRICTION = "restriction_fragment";
    public RestrictionFragment() {
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRestrictionBinding.inflate(inflater, container, false);
        args = getArguments();
        if (args != null) {
            if (args.getString(ARG_RESTRICTION) != null) {
                ownerUid = args.getString(ARG_RESTRICTION);
            }
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
    }

    private void loadData() {
        restrictions = new ArrayList<>();
        db.collection("restrictions")
                .whereEqualTo("ownerId", ownerUid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Restriction restriction;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            restriction = document.toObject(Restriction.class);
                            restriction.setOwnerId(document.getId());
                            restrictions = restriction.getRestrictions();
                        }
                        prepareData(restrictions);
                    }
                });
    }

    @SuppressLint("ResourceType")
    private void prepareData(List<String> prevRestriction) {
        ChipGroup chipGroup;
        Button btnRestriction;

        btnRestriction = binding.btnRestriction;
        chipGroup = binding.chipHealthyLabelsGroup;
        List<String> chips = Arrays.asList(
                "alcohol-free",
                "celery-free",
                "crustacean-free",
                "dairy-free",
                "fish-free",
                "gluten-free",
                "wheat-free",
                "lupine-free",
                "mollusk-free",
                "mustard-free",
                "peanut-free",
                "pork-free",
                "red-meat-free",
                "sesame-free",
                "shellfish-free",
                "soy-free",
                "tree-nut-free",
                "vegan",
                "vegetarian",
                "low-fat-abs",
                "low-potassium",
                "low-sugar",
                "no-oil-added",
                "sugar-conscious"
        );

        Context context = chipGroup.getContext();
        for(String ch: chips) {
            Chip chip = new Chip(context);
            int drawableResourceId = R.drawable.baseline_check_24;
            Drawable checkedIcon = ContextCompat.getDrawable(context, drawableResourceId);

            chip.setText(ch);
            chip.setCheckedIcon(checkedIcon);
            chip.setCheckedIconVisible(true);
            chip.setCheckable(true);
            chip.setEnsureMinTouchTargetSize(true);
            chipGroup.addView(chip);

            if(!prevRestriction.isEmpty()) {
                if(prevRestriction.contains(ch)) {
                    chip.setChecked(true);
                }
            }
        }

        btnRestriction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chipGroup.getCheckedChipIds().size() < 1) {
                    Toast.makeText(context, "Please select some restrictions", Toast.LENGTH_SHORT).show();
                } else {
                    checkBtnClick = true;
                    cleanPrevData();
                    checkedChipIds = chipGroup.getCheckedChipIds();
                    restrictions = new ArrayList<>();
                    Chip selectedChip;
                    for (Integer id : checkedChipIds) {
                        selectedChip = chipGroup.findViewById(id);
                        restrictions.add(selectedChip.getText().toString());
                    }

                    if(!restrictions.contains("none")) {
                        restriction = new Restriction();
                        restriction.setOwnerId(ownerUid);
                        restriction.setRestrictions(restrictions);
                        Toast.makeText(context, "Update restrictions successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        checkBtnClick = false;
                        Toast.makeText(context, "Your restriction is none!", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if(!restrictions.isEmpty() && checkBtnClick) {
            db.collection("restrictions").add(restriction);
        }
    }

    private void cleanPrevData() {
        db.collection("restrictions")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Iterate through the retrieved documents and delete each one
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                deleteDocument(db.collection("restrictions").document(document.getId()));
                            }
                        }
                    }
                });

    }

    private void deleteDocument(DocumentReference documentRef) {
        // Delete the document
        documentRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Document deleted successfully
                } else {
                    // Handle errors
                }
            }
        });
    }
}