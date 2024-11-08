package com.example.phototube_android.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phototube_android.R;
import com.example.phototube_android.entities.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {
    private static List<Comment> comments;
    private LayoutInflater mInflater;
    private Context context;

    private String loggedInUsername;

    public CommentsAdapter(Context context, List<Comment> comments, String loggedInUsername) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.comments = comments;
        this.loggedInUsername = loggedInUsername;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        Comment currentComment = comments.get(position);

        holder.usernameTextView.setText(currentComment.getUsername() + ": ");
        holder.commentTextView.setText(currentComment.getCommentText());

        if (loggedInUsername != null && !loggedInUsername.isEmpty() && currentComment.getUsername().equals(loggedInUsername)) {
            holder.editButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setVisibility(View.VISIBLE);
        } else {
            holder.editButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
        }

        holder.editButton.setOnClickListener(v -> {
            // Trigger edit comment logic, possibly through a dialog
            showEditCommentDialog(position);
        });

        holder.deleteButton.setOnClickListener(v -> {
            // Remove comment from the list
            removeComment(position);
        });

        holder.commentTextView.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Full Comment");

            final TextView fullCommentTextView = new TextView(context);
            fullCommentTextView.setText(currentComment.getCommentText());
            fullCommentTextView.setPadding(16, 16, 16, 16);
            builder.setView(fullCommentTextView);

            builder.setPositiveButton("Close", null);
            builder.show();
        });
    }



    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        notifyItemInserted(comments.size() - 1);
    }

    public void showEditCommentDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = mInflater.inflate(R.layout.dialog_edit_comment, null);
        builder.setView(dialogView);


        EditText editCommentEditText = dialogView.findViewById(R.id.edit_comment_edit_text);
        ImageButton saveEditButton = dialogView.findViewById(R.id.save_edit_button);

        editCommentEditText.setText(comments.get(position).getCommentText());

        AlertDialog dialog = builder.create();

        saveEditButton.setOnClickListener(v -> {
            String editedCommentText = editCommentEditText.getText().toString();
            if (!editedCommentText.isEmpty()) {
                comments.get(position).setCommentText(editedCommentText);
                notifyItemChanged(position);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void removeComment(int position) {
        comments.remove(position);
        notifyItemRemoved(position);
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentTextView, usernameTextView;
        ImageButton editButton, deleteButton;

        CommentViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            editButton = itemView.findViewById(R.id.edit_Comment_Button);
            deleteButton = itemView.findViewById(R.id.delete_Comment_Button);
        }
    }
}
