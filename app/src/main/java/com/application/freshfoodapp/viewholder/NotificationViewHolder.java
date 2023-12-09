package com.application.freshfoodapp.viewholder;

import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.application.freshfoodapp.databinding.NotificationItemBinding;
import com.google.android.material.imageview.ShapeableImageView;

public class NotificationViewHolder extends RecyclerView.ViewHolder {
    private ShapeableImageView senderAvatarView;
    private TextView senderNameTextView, messageTextView;
    private Button acceptBtn, declineBtn;

    public NotificationViewHolder(NotificationItemBinding binding) {
        super(binding.getRoot());
        senderAvatarView = binding.senderAvatarView;
        senderNameTextView = binding.senderNameTextView;
        messageTextView = binding.messageTextView;
        acceptBtn = binding.acceptBtn;
        declineBtn = binding.declineBtn;
    }

    public ShapeableImageView getSenderAvatarView() {
        return senderAvatarView;
    }

    public TextView getSenderNameTextView() {
        return senderNameTextView;
    }

    public TextView getMessageTextView() {
        return messageTextView;
    }

    public Button getAcceptBtn() {
        return acceptBtn;
    }

    public Button getDeclineBtn() {
        return declineBtn;
    }
}
