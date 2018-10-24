package br.com.fiap.firegames;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import br.com.fiap.firegames.model.Game;

public class GameRecyclerViewAdapter extends RecyclerView.Adapter<GameRecyclerViewAdapter.ViewHolder> {

    private List<Game> gamesList;
    private Context context;
    private FirebaseFirestore firestoreDB;

    public GameRecyclerViewAdapter(List<Game> gamesList, Context context, FirebaseFirestore firestoreDB) {
        this.gamesList = gamesList;
        this.context = context;
        this.firestoreDB = firestoreDB;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int itemPosition = position;
        final Game game = gamesList.get(itemPosition);

        // Set TextView on Item
        holder.name.setText(game.getName());
        holder.developer.setText(game.getDeveloper());
        holder.releaseDate.setText(game.getReleaseDate());

        // Events
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateGame(game);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteGame(game.getId(), itemPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return gamesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, developer, releaseDate;
        ImageView edit;
        ImageView delete;

        ViewHolder(View view) {
            super(view);

            // TextView
            name = view.findViewById(R.id.tvName);
            developer = view.findViewById(R.id.tvDeveloper);
            releaseDate = view.findViewById(R.id.tvReleaseDate);

            // Actions
            edit = view.findViewById(R.id.ivEdit);
            delete = view.findViewById(R.id.ivDelete);
        }
    }

    private void updateGame(Game game) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.putExtra("UpdateGameId", game.getId());
        intent.putExtra("UpdateGameName", game.getName());
        intent.putExtra("UpdateGameDeveloper", game.getDeveloper());
        intent.putExtra("UpdateGameReleaseDate", game.getReleaseDate());

        context.startActivity(intent);
    }

    private void deleteGame(String id, final int position) {
        firestoreDB.collection("games")
                .document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        gamesList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, gamesList.size());
                        Toast.makeText(context, "Jogo removido!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
