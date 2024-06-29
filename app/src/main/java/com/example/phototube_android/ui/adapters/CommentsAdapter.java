package com.example.phototube_android.ui.adapters;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phototube_android.R;
import com.example.phototube_android.classes.Comment;
import com.example.phototube_android.entities.UserManager;
import com.example.phototube_android.viewmodels.CommentInViewModel;
import com.example.phototube_android.viewmodels.VideoInViewModel;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {
    private static List<Comment> comments;
    private LayoutInflater mInflater;
    private Context context;

    private String loggedInUsername;

    private CommentInViewModel commentInViewModel;

    public CommentsAdapter(Context context, List<Comment> comments,CommentInViewModel civm ) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.comments = comments;
        this.loggedInUsername = UserManager.getInstance().getUserId();
        this.commentInViewModel = civm;
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
        holder.commentTextView.setText(currentComment.getText());

        if (loggedInUsername != null && !loggedInUsername.isEmpty() && currentComment.getCreatedBy().equals(loggedInUsername)) {
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
            deleteComment(position);
        });

        holder.commentTextView.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Full Comment");

            final TextView fullCommentTextView = new TextView(context);
            fullCommentTextView.setText(currentComment.getText());
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

        editCommentEditText.setText(comments.get(position).getText());

        AlertDialog dialog = builder.create();

        saveEditButton.setOnClickListener(v -> {
            String editedCommentText = editCommentEditText.getText().toString();
            if (!editedCommentText.isEmpty()) {
                commentInViewModel.updateComment(comments.get(position).get_id(), editedCommentText);
                commentInViewModel.getUpdateCommentData().observe((LifecycleOwner) context, commentResponse -> {
                    if (commentResponse.isSuccess() && commentResponse.getData() != null) {
                        Toast.makeText(context, commentResponse.getMessage(), Toast.LENGTH_LONG).show();
                        comments.get(position).setText(editedCommentText);
                        notifyItemChanged(position);

                        // Hide the keyboard
                        hideKeyboard(editCommentEditText);

                        dialog.dismiss();
                    } else {
                        Toast.makeText(context, "Failed to load user data: " + commentResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        dialog.show();
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }



    public void deleteComment(int position) {
        Comment currentComment = comments.get(position);
        commentInViewModel.deleteComment(currentComment.get_id());
        commentInViewModel.getDeleteCommentData().observe((LifecycleOwner) context, commentResponse -> {
            if (commentResponse.isSuccess() && commentResponse.getData() != null) {

                Toast.makeText(context,  commentResponse.getData().getMessage(), Toast.LENGTH_LONG).show();
                comments.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, comments.size());
            } else {
                Toast.makeText(context, "Failed to load user data: " + commentResponse.getData().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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
