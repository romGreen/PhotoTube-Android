package com.example.phototube_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phototube_android.R;
import com.example.phototube_android.entities.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {
    private static List<Comment> comments;
    private LayoutInflater mInflater;
    private Context context;

    public CommentsAdapter(Context context, List<Comment> comments) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.comments = comments;
    }

    public CommentsAdapter(Context context) {
        this.context = context;
        this.comments = new ArrayList<>();  // Initialize with an empty list
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        Comment currentComment = comments.get(position);
        holder.commentTextView.setText(currentComment.getCommentText());

        holder.editButton.setOnClickListener(v -> {
            // Trigger edit comment logic, possibly through a dialog
            editComment(position);
        });

        holder.deleteButton.setOnClickListener(v -> {
            // Remove comment from the list
            removeComment(position);
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

    public void editComment(int position) {
        // Assuming you get new text from a dialog, you update the comment
        comments.get(position).setCommentText("Edited Comment Text");
        notifyItemChanged(position);
    }

    public void removeComment(int position) {
        comments.remove(position);
        notifyItemRemoved(position);
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentTextView;
        Button editButton, deleteButton;

        CommentViewHolder(View itemView) {
            super(itemView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
