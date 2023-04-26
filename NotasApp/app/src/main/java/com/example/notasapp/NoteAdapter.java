package com.example.notasapp;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter  extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {

    private List<Note> notes = new ArrayList<>();

    private OnItemClickListener listener;

    @NonNull
    @Override
    public NoteAdapter.NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notas_items,parent,false);
        return new NoteHolder(view);
    }

    public void onBindViewHolder(@NonNull NoteHolder holder, int position){

        Note currentNote = notes.get(position);

        holder.textViewTitulo.setText(currentNote.getTitle());
        holder.textViewDescripcion.setText(currentNote.getDescription());

    }

    @Override
    public int getItemCount(){
        return notes.size();
    }

    public void setNotes(List<Note> notes){

        this.notes = notes;
        notifyDataSetChanged();
    }

    public Note getNote(int position){

        return notes.get(position);
    }

    class NoteHolder extends RecyclerView.ViewHolder{

        TextView textViewTitulo;
        TextView textViewDescripcion;

        public NoteHolder(@NonNull View itemView){
            super(itemView);
            textViewTitulo = itemView.findViewById(R.id.textViewTitulo);
            textViewDescripcion = itemView.findViewById(R.id.textViewDescripcion);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION){

                        listener.onItemClick(notes.get(position));
                    }

                }
            });
        }
    }

    public interface OnItemClickListener{

        void onItemClick(Note note);
    }

    public void setOnItemClickListener(OnItemClickListener listener){

        this.listener = listener;
    }
}
