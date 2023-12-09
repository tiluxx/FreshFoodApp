package com.application.freshfoodapp.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.application.freshfoodapp.databinding.SharingItemBinding;
import com.google.android.material.imageview.ShapeableImageView;

public class SharingViewHolder extends RecyclerView.ViewHolder {
    private Button moreOptionsBtn;
    private ShapeableImageView kitchenThumbnailView, ownerAvatarView;
    private TextView
            sharingKitchenTitleTextView,
            numOfMemberTextView,
            ownerNameTextView;
    private View itemView;

    public SharingViewHolder(SharingItemBinding binding) {
        super(binding.getRoot());
        itemView = binding.getRoot();
        moreOptionsBtn = binding.moreOptionsBtn;
        kitchenThumbnailView = binding.kitchenThumbnailView;
        ownerAvatarView = binding.ownerAvatarView;
        sharingKitchenTitleTextView = binding.sharingKitchenTitleTextView;
        numOfMemberTextView = binding.numOfMemberTextView;
        ownerNameTextView = binding.ownerNameTextView;
    }

    public Button getMoreOptionsBtn() {
        return moreOptionsBtn;
    }

    public ShapeableImageView getKitchenThumbnailView() {
        return kitchenThumbnailView;
    }

    public ShapeableImageView getOwnerAvatarView() {
        return ownerAvatarView;
    }

    public TextView getSharingKitchenTitleTextView() {
        return sharingKitchenTitleTextView;
    }

    public TextView getNumOfMemberTextView() {
        return numOfMemberTextView;
    }

    public TextView getOwnerNameTextView() {
        return ownerNameTextView;
    }

    public View getItemView() {
        return itemView;
    }
}
